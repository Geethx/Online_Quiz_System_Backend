package com.onlinequiz.online_quiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinequiz.online_quiz.dto.CreateQuestionDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        // Increase buffer size to 16MB for large API responses
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .exchangeStrategies(strategies)
                .build();
        this.objectMapper = objectMapper;
    }

    public List<CreateQuestionDTO> extractQuestionsFromFile(MultipartFile file, Long subjectId, Integer grade) throws Exception {
        String contentType = file.getContentType();
        String prompt = "You are a teacher extracting Multiple Choice Questions from a document. " +
                "Extract all the questions, the 4 options (A, B, C, D), and identify the correct option number (1 for A, 2 for B, 3 for C, 4 for D). " +
                "If the correct answer is not explicitly marked in the document, use your intelligence to determine the correct answer. " +
                "Also assign a difficulty (EASY, MEDIUM, or HARD). " +
                "Format your response ONLY as a raw JSON array of objects. Do not include markdown tags like ```json. " +
                "Each object must have exactly these keys: text (String), optionA (String), optionB (String), optionC (String), optionD (String), correctOption (Integer), difficulty (String), points (Integer - default to 10), subjectId (Long - set to " + subjectId + "), grade (Integer - set to " + grade + ").";

        Map<String, Object> requestBody = new HashMap<>();

        if (contentType != null && contentType.equals("application/pdf")) {
            String extractedText = extractTextFromPdf(file);
            requestBody = buildTextRequestBody(prompt + "\n\nDocument Text:\n" + extractedText);
        } else if (contentType != null && contentType.startsWith("image/")) {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            requestBody = buildImageRequestBody(prompt, base64Image, contentType);
        } else {
            throw new IllegalArgumentException("Unsupported file type. Only PDF and Images are allowed.");
        }

        String geminiResponse = callGeminiApi(requestBody);
        return parseGeminiResponse(geminiResponse);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private Map<String, Object> buildTextRequestBody(String text) {
        return Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", text)
                ))
            )
        );
    }

    private Map<String, Object> buildImageRequestBody(String prompt, String base64Image, String mimeType) {
        return Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt),
                    Map.of("inlineData", Map.of(
                        "mimeType", mimeType,
                        "data", base64Image
                    ))
                ))
            )
        );
    }

    private String callGeminiApi(Map<String, Object> requestBody) {
        if (geminiApiKey == null || geminiApiKey.contains("your_api_key_here") || geminiApiKey.trim().isEmpty()) {
            throw new RuntimeException("Gemini API Key is not configured. Please add a valid key in application.properties.");
        }

        System.out.println("[AIService] Using API key: " + geminiApiKey.substring(0, Math.min(10, geminiApiKey.length())) + "...");
        System.out.println("[AIService] Calling Gemini API with model: gemini-3.5-flash");

        JsonNode response = webClient.post()
                .uri("/v1beta/models/gemini-3.5-flash:generateContent?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.err.println("[AIService] Gemini API Error: HTTP " + clientResponse.statusCode() + " - " + errorBody);
                                return Mono.error(new RuntimeException(
                                        "Gemini API Error (HTTP " + clientResponse.statusCode() + "): " + errorBody));
                            });
                })
                .bodyToMono(JsonNode.class)
                .block();

        System.out.println("[AIService] Gemini API response received successfully");

        if (response != null && response.has("candidates") && response.get("candidates").size() > 0) {
            return response.get("candidates").get(0)
                    .get("content").get("parts").get(0)
                    .get("text").asText();
        }
        throw new RuntimeException("Failed to get a valid response from Gemini API. Response: " + response);
    }

    private List<CreateQuestionDTO> parseGeminiResponse(String geminiText) throws Exception {
        // Clean up response if the AI still included markdown block
        String cleanJson = geminiText.trim();
        if (cleanJson.startsWith("```json")) {
            cleanJson = cleanJson.substring(7);
        }
        if (cleanJson.startsWith("```")) {
            cleanJson = cleanJson.substring(3);
        }
        if (cleanJson.endsWith("```")) {
            cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
        }
        cleanJson = cleanJson.trim();

        return objectMapper.readValue(cleanJson, new TypeReference<List<CreateQuestionDTO>>() {});
    }
}
