package com.smartexpense.api.application.port.out;

import com.smartexpense.api.domain.model.User;

public interface JwtTokenPort {
    String generateToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
}
