package pb.wi.cohp.domain.range;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface RangeRepository extends JpaRepository<Range, Long> {
    Optional<Range> findByParameter(Parameter parameter);
    Range findByUser(User user);
    Optional<Range> findByUserAndParameter(User user, Parameter parameter);
    List<Range> findAllByUser_Username(String username, Pageable page);
}
