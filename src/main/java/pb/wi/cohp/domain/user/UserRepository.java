package pb.wi.cohp.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByPersonalIdNumber(String personalIdNumber);

    Optional<User> findUserByEmail(String email);
}
