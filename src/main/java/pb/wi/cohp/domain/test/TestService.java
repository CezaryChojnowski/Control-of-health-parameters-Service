package pb.wi.cohp.domain.test;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.reminder.Reminder;
import pb.wi.cohp.domain.reminder.ReminderService;
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
                                .hidden(false)
                                .owner(false)
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
                                .hidden(false)
                                .user(user)
                                .owner(true)
                                .build()
                );
    }

    public Test findTestById(Long id){
        if(testRepository.findById(id).isPresent()){
            return testRepository.findById(id).get();
        }
        throw new ObjectNotFoundException(env.getProperty("testNotFound"));
    }

    public Test findTestByIdAndUser(Long id, String username){
        User user = userService.getUserByUsername(username);
        if(testRepository.findByIdAndUser(id, user).isPresent()){
            return testRepository.findByIdAndUser(id, user).get();
        }
        throw new ObjectNotFoundException(env.getProperty("testNotFound"));
    }

    public void deleteTestById(Long id){
        Test test = findTestById(id);
        test.setHidden(true);
        testRepository.save(test);
        List<Parameter> parameters = test.getParameters();
        for(Parameter parameter: parameters) {
            parameterService.deleteParameter(parameter.getId());
        }
    }



    public Test editTest(TestDTO test, boolean isAdmin){
        Test result = findTestById(test.getId());
        result.setName(test.getName());
        parameterService.createParameter(test.getParameters(), test, isAdmin);
        return testRepository.save(result);
    }

    public List<Test> getTests(){
        List<Test> result = testRepository.findAllByUserAndHiddenIsFalse(null);
        result.forEach(
                e -> {
                    e.getParameters().removeIf(Parameter::getHidden);
                }
        );
        return result;
    }

    public List<Test> getTests(String username){
        User user = userService.getUserByUsername(username);
        List<Test> userTests = testRepository.findAllByUserAndHiddenIsFalse(user);
        List<Test> publiclyAvailableTests = testRepository.findAllByUserAndHiddenIsFalse(null);
        userTests.addAll(publiclyAvailableTests);
        userTests.forEach(
                e -> {
                    e.getParameters().removeIf(Parameter::getHidden);
                }
        );
        return userTests;
    }

}
