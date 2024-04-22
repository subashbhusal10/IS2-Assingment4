package com.hidemessage.dao;

import com.hidemessage.dtos.LoginRequest;
import com.hidemessage.dtos.LoginResponse;
import com.hidemessage.dtos.RegisterRequest;
import com.hidemessage.errors.BadRequestException;
import com.hidemessage.errors.UnauthorizedException;
import com.hidemessage.jpa.UserRepository;
import com.hidemessage.models.User;
import com.hidemessage.security.JwtService;
import com.hidemessage.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserDao {


    @Value("${app.jwt.access-expiry-minutes}")
    private long jwtAccessExpiryMinutes;

    @Value("${app.jwt.refresh-expiry-years}")
    private long jwtRefreshExpiryYears;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserDao(
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse register(RegisterRequest registerRequest) {

        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        User user = new User(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                hashedPassword);
        userRepository.save(user);
        String accessToken = jwtService.createToken(user, jwtAccessExpiryMinutes, ChronoUnit.MINUTES);
        String refreshToken = jwtService.createToken(user, jwtRefreshExpiryYears * 365, ChronoUnit.DAYS);
        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse login(LoginRequest loginRequest) throws UnauthorizedException {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.email());
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("Invalid Credentials.");
        }
        User user = userOpt.get();
        if (!BCrypt.checkpw(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        String accessToken = jwtService.createToken(user, jwtAccessExpiryMinutes, ChronoUnit.MINUTES);
        String refreshToken = jwtService.createToken(user, jwtRefreshExpiryYears * 365, ChronoUnit.DAYS);
        return new LoginResponse(accessToken, refreshToken);
    }

    public User getLoggedUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}