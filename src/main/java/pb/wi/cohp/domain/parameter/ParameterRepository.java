package pb.wi.cohp.domain.parameter;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ParameterRepository extends CrudRepository<Parameter, Long> {
    Optional<Parameter> findParameterByIdAndHiddenFalse(Long id);
}
