package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Register.RegisterRequest;
import com.formula1.capbank.dtos.Register.RegisterResponse;

public interface IAuthService {
    RegisterResponse register(RegisterRequest registerRequest);
}
