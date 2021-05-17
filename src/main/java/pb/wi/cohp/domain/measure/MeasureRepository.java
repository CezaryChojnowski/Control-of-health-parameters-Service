package pb.wi.cohp.domain.measure;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeasureRepository extends CrudRepository<Measure, Long> {
    List<Measure> findAllByUser_Username(String username);
}
