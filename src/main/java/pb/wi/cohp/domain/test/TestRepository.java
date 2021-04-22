package pb.wi.cohp.domain.test;

import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.domain.user.User;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Long> {
    List<Test> findAllByUser(User user);
}
