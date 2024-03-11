package gazzsha.sprint.security.application.service;

import gazzsha.sprint.security.application.dto.RegistrationUserDto;
import gazzsha.sprint.security.application.dto.UserDto;
import gazzsha.sprint.security.application.entity.User;
import gazzsha.sprint.security.application.repository.RoleRepository;
import gazzsha.sprint.security.application.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with username %s not found", username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList()
        );
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public User createNewUser(final RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.username());
        user.setPassword(passwordEncoder.encode(registrationUserDto.password()));
        user.setRoles(List.of(roleRepository.findByRoleName("ROLE_USER").get()));
        userRepository.saveAndFlush(user);
        return user;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
