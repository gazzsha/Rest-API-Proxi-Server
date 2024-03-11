package gazzsha.sprint.security.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegistrationUserDto(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String confirmPassword) {
}
