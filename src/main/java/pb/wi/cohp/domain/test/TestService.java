package pb.wi.cohp.domain.test;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.parameter.ParameterService;

import java.util.List;

@Service
@PropertySource("classpath:en.exception.messages.properties")
public class TestService {

    final TestRepository testRepository;

    final ParameterService parameterService;

    final Environment env;

    public TestService(TestRepository testRepository, ParameterService parameterService, Environment env) {
        this.testRepository = testRepository;
        this.parameterService = parameterService;
        this.env = env;
    }

    public Test createTest(String name){
        return testRepository
                .save(
                        new Test
                                .TestBuilder()
                                .name(name)
                                .build()
                );
    }

    public Test createTest(String name, String username){
        User user = userService.getUserByUsername(username);
        return testRepository
                .save(
                        new Test
                                .TestBuilder()
                                .name(name)
                                .build()
                );
    }

    public Test findTestById(Long id){
        if(testRepository.findById(id).isPresent()){
            return testRepository.findById(id).get();
        }
        throw new ObjectNotFoundException(env.getProperty("testNotFound"));
    }

    public void deleteTestById(Long id){
        Test test = findTestById(id);
        testRepository.delete(test);
    }

    public Test editTest(TestDTO test){
        Test result = findTestById(test.getId());
        result.setName(test.getName());
        parameterService.createParameter(test.getParameters(), test);
        return testRepository.save(result);
    }

    public List<Test> getTests(){
        return (List<Test>) testRepository.findAllByUser(null);
    }

}
