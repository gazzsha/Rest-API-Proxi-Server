package gazzsha.sprint.security.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JwtRequest(@NotBlank
                         @Size(max = 50)
                         String username,
                         @NotBlank
                         @Size(min = 3,max = 50)
                         String password) {}