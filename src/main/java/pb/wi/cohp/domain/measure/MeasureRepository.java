package pb.wi.cohp.domain.measure;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findAllByUser_UsernameAndHiddenFalse(String username);

    Optional<Measure> findByIdAndHiddenFalse(Long id);
}
