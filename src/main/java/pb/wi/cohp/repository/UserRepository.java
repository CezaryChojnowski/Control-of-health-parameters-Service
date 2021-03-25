package pb.wi.cohp.repository;

import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
