package gazzsha.sprint.security.application.dto;

public record UserDto(Integer id, String name, String email, AddressDto address,
                      String phone, String website, CompanyDto company) {}
