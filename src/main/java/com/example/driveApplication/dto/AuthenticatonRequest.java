package com.example.driveApplication.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AuthenticatonRequest {

    private String username;
    private String password;
}
