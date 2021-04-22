package pb.wi.cohp.domain.test;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.util.List;

@Service
@PropertySource("classpath:en.exception.messages.properties")
public class TestService {

    final TestRepository testRepository;

    final ParameterService parameterService;

    final Environment env;

    final UserService userService;

    public TestService(TestRepository testRepository, ParameterService parameterService, Environment env, UserService userService) {
        this.testRepository = testRepository;
        this.parameterService = parameterService;
        this.env = env;
        this.userService = userService;
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
                                .user(user)
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

    public List<Test> getTests(String username){
        User user = userService.getUserByUsername(username);
        List<Test> userTests = testRepository.findAllByUser(user);
        List<Test> publiclyAvailableTests = testRepository.findAllByUser(null);
        userTests.addAll(publiclyAvailableTests);
        return userTests;
    }

}
