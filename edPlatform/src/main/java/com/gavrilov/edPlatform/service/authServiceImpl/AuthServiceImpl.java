package com.gavrilov.edPlatform.service.authServiceImpl;

import com.gavrilov.edPlatform.exception.InvalidTokenException;
import com.gavrilov.edPlatform.exception.UserNotFoundException;
import com.gavrilov.edPlatform.jwt.JwtAuthentication;
import com.gavrilov.edPlatform.jwt.JwtProvider;
import com.gavrilov.edPlatform.model.JwtRefreshToken;
import com.gavrilov.edPlatform.model.JwtRequest;
import com.gavrilov.edPlatform.model.JwtResponse;
import com.gavrilov.edPlatform.model.PlatformUser;
import com.gavrilov.edPlatform.repository.JwtRefreshTokenRepository;
import com.gavrilov.edPlatform.service.AuthService;
import com.gavrilov.edPlatform.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final JwtRefreshTokenRepository tokenRepository;


    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final PlatformUser user = userService.findByUsername(authRequest.getLogin());
        if (user == null) {
            throw new UserNotFoundException("User with this login doesn't exists");
        }
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            JwtRefreshToken newRefreshToken = new JwtRefreshToken();
            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setLogin(user.getUsername());
            tokenRepository.save(newRefreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new UserNotFoundException("Wrong password");
        }
    }

    @Override
    public PlatformUser register(@NonNull PlatformUser user) {
        return userService.saveUser(user);
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = tokenRepository.findByLogin(login).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final PlatformUser user = userService.findByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }


    @Override
    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = tokenRepository.findByLogin(login).getToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final PlatformUser user = userService.findByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                JwtRefreshToken newRefreshTokenToDB = new JwtRefreshToken();
                newRefreshTokenToDB.setToken(newRefreshToken);
                newRefreshTokenToDB.setLogin(login);
                tokenRepository.save(newRefreshTokenToDB);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
