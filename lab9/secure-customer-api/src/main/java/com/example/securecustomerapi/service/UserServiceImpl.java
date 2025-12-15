package com.example.securecustomerapi.service;

import com.example.securecustomerapi.dto.LoginRequestDTO;
import com.example.securecustomerapi.dto.LoginResponseDTO;
import com.example.securecustomerapi.dto.RegisterRequestDTO;
import com.example.securecustomerapi.dto.UserResponseDTO;
import com.example.securecustomerapi.entity.Role;
import com.example.securecustomerapi.entity.User;
import com.example.securecustomerapi.exception.DuplicateResourceException;
import com.example.securecustomerapi.exception.ResourceNotFoundException;
import com.example.securecustomerapi.repository.UserRepository;
import com.example.securecustomerapi.security.JwtTokenProvider;
import com.example.securecustomerapi.dto.ChangePasswordDTO;
import com.example.securecustomerapi.dto.ForgotPasswordDTO;
import com.example.securecustomerapi.dto.ResetPasswordDTO;
import com.example.securecustomerapi.dto.UpdateProfileDTO;
import com.example.securecustomerapi.dto.UpdateRoleDTO;
import com.example.securecustomerapi.dto.RefreshTokenDTO;
import com.example.securecustomerapi.entity.RefreshToken;
import com.example.securecustomerapi.repository.RefreshTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        String token = jwtTokenProvider.generateToken(authentication);
        String refreshTokenValue = generateAndSaveRefreshToken(user);

        return new LoginResponseDTO(token, refreshTokenValue, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Override
    public UserResponseDTO register(RegisterRequestDTO registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponseDTO getCurrentUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO request) {
        String identifier = request.getIdentifier();
        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        return token;
    }

    @Override
    public void resetPassword(ResetPasswordDTO request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token is expired");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponseDTO updateProfile(String username, UpdateProfileDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.findByEmail(dto.getEmail())
                .filter(other -> !other.getId().equals(user.getId()))
                .ifPresent(other -> { throw new DuplicateResourceException("Email already in use"); });

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        User saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Override
    public void deleteAccount(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserRole(Long id, UpdateRoleDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(dto.getRole());
        User saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Override
    public UserResponseDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(user.getIsActive() == null ? Boolean.FALSE : !user.getIsActive());
        User saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Override
    public LoginResponseDTO refreshToken(RefreshTokenDTO request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (refreshToken.getExpiryDate() == null || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token is expired");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, java.util.Collections.emptyList())
        );

        return new LoginResponseDTO(newAccessToken, refreshToken.getToken(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    private String generateAndSaveRefreshToken(User user) {
        String refreshTokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);
        return refreshTokenValue;
    }

    private UserResponseDTO mapToUserResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
