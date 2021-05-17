package pb.wi.cohp.domain.measureParameter;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeasureParameterRepository extends CrudRepository<MeasureParameter, Long> {
    List<MeasureParameter> findAllByMeasureId(Long id);
}
