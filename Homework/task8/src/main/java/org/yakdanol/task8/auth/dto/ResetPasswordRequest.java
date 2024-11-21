package org.yakdanol.task8.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String username;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private String code; // Код подтверждения для сброса пароля
}
