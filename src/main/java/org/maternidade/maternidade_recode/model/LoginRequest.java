package org.maternidade.maternidade_recode.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}