package pb.wi.cohp.domain.user;

import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.domain.role.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPersonalIdNumber(String personalIdNumber);

    boolean existsByUsername(String username);

    List<User> findUserByRoles(Role role);
}
