package gazzsha.sprint.security.application.repository;

import gazzsha.sprint.security.application.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(String name);
}
