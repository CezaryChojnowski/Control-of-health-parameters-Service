package pb.wi.cohp.domain.test;

import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends CrudRepository<Test, Long> {
    List<Test> findAllByUserAndHiddenIsFalse(User user);

    Optional<Test> findByIdAndUser(Long id, User user);
}
