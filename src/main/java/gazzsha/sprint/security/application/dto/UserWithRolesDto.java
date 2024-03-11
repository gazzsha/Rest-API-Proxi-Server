package gazzsha.sprint.security.application.dto;

import java.util.List;

public record UserWithRolesDto(Long id, String password, List<String> roles) {}
