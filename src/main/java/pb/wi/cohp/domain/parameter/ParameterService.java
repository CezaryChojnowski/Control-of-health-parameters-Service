package pb.wi.cohp.domain.parameter;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestDTO;
import pb.wi.cohp.domain.test.TestRepository;

import java.util.List;

@Service
@PropertySource("classpath:en.exception.messages.properties")
public class ParameterService {

    final ParameterRepository parameterRepository;

    final TestRepository testRepository;

    final Environment env;

    public ParameterService(ParameterRepository parameterRepository, TestRepository testRepository, Environment env){
        this.parameterRepository = parameterRepository;
        this.testRepository = testRepository;
        this.env = env;
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
                                    .hidden(false)
                                    .build()
                    );
        }
    }

    public void createParameter(List<Parameter> parameterList, TestDTO testDTO){
        Test test = testRepository.findById(testDTO.getId()).get();
        for(Parameter parameter: parameterList){
            parameterRepository
                    .save(
                            new Parameter
                                    .ParameterBuilder()
                                    .id(parameter.getId())
                                    .hidden(false)
                                    .name(parameter.getName())
                                    .test(test)
                                    .build()
                    );
        }
    }

    public void deleteParameter(Long parameterId){
        Parameter parameter = findParameterById(parameterId);
        parameter.setHidden(true);
        parameterRepository.save(parameter);
    }

    public Parameter findParameterById(Long id){
        if(parameterRepository.findParameterByIdAndHiddenFalse(id).isPresent()){
            return parameterRepository.findParameterByIdAndHiddenFalse(id).get();
        }
        throw new ObjectNotFoundException(env.getProperty("parameterNotFound"));
    }
}
