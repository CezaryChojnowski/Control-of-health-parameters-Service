package pb.wi.cohp.domain.parameter;

import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestRepository;

import java.util.List;

@Service
public class ParameterService {

    final ParameterRepository parameterRepository;

    final TestRepository testRepository;

    public ParameterService(ParameterRepository parameterRepository, TestRepository testRepository){
        this.parameterRepository = parameterRepository;
        this.testRepository = testRepository;
    }

    public void createParameter(List<Parameter> parameterList, Long testId){
        Test test = testRepository.findById(testId).get();
        for(Parameter parameter: parameterList){
            parameterRepository
                    .save(
                            new Parameter
                                    .ParameterBuilder()
                                    .name(parameter.getName())
                                    .test(test)
                                    .build()
                    );
        }
    }
}
