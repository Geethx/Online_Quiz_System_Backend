package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.AuthResponseDTO;
import com.onlinequiz.online_quiz.dto.LoginDTO;
import com.onlinequiz.online_quiz.dto.RegisterDTO;
import com.onlinequiz.online_quiz.entity.Role;
import com.onlinequiz.online_quiz.entity.User;
import com.onlinequiz.online_quiz.repository.UserRepository;
import com.onlinequiz.online_quiz.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // Check if username exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Determine role - Hardcoded to STUDENT for security
        // Admin accounts must be created manually in the database
        Role role = Role.STUDENT;

        // Create user
        User user = new User(
                registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getFullName(),
                registerDTO.getEmail(),
                role
        );

        User savedUser = userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(savedUser, savedUser.getRole().name(), savedUser.getId());

        return new AuthResponseDTO(
                token,
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getRole().name(),
                savedUser.getId()
        );
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user, user.getRole().name(), user.getId());

        return new AuthResponseDTO(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRole().name(),
                user.getId()
        );
    }
}
