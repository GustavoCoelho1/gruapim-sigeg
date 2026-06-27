package com.sigeg.service;

import com.sigeg.dto.request.LoginRequest;
import com.sigeg.dto.request.RegisterRequest;
import com.sigeg.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
