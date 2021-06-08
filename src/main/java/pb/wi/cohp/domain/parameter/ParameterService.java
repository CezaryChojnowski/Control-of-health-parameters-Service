package pb.wi.cohp.domain.parameter;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.range.RangeService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestDTO;
import pb.wi.cohp.domain.test.TestRepository;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:en.exception.messages.properties")
public class ParameterService {

    final ParameterRepository parameterRepository;

    final TestRepository testRepository;

    final Environment env;

    final UserService userService;

    final RangeService rangeService;

    public ParameterService(ParameterRepository parameterRepository, TestRepository testRepository, Environment env, UserService userService, RangeService rangeService){
        this.parameterRepository = parameterRepository;
        this.testRepository = testRepository;
        this.env = env;
        this.userService = userService;
        this.rangeService = rangeService;
    }

    public void createParameter(List<Parameter> parameterList, Long testId, boolean isAdmin, boolean createNew){
        Test test = testRepository.findById(testId).get();
        if(isAdmin) {
            List<User> users = userService.getUsers();
            for (Parameter parameter : parameterList) {
                Parameter param = new Parameter
                        .ParameterBuilder()
                        .name(parameter.getName())
                        .test(test)
                        .hidden(false)
                        .build();
                parameterRepository
                        .save(param);
                for (User user : users) {
                    rangeService.createRange(user, param);
                }
            }
        }
        else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByUsername(authentication.getName());
            for (Parameter parameter : parameterList) {
                Parameter param = new Parameter
                        .ParameterBuilder()
                        .name(parameter.getName())
                        .test(test)
                        .hidden(false)
                        .build();
                parameterRepository
                        .save(param);
                rangeService.createRange(user, param);
            }
        }
    }

    public void createParameter(List<Parameter> newParameters, TestDTO testDTO, boolean isAdmin){
        Test test = testRepository.findById(testDTO.getId()).get();
        if(isAdmin){
            List<User> users = userService.getUsers();
            for (Parameter parameter : newParameters) {
                Parameter param = new Parameter
                        .ParameterBuilder()
                        .id(parameter.getId())
                        .hidden(false)
                        .name(parameter.getName())
                        .test(test)
                        .build();
                Parameter result = parameterRepository.save(param);
                    for(User user: users){
                        if(!rangeService.getByUserAndParameter(user,result).isPresent()){
                            rangeService.createRange(user, result);
                    }
                }
            }
        }
        else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByUsername(authentication.getName());
            for (Parameter parameter : newParameters) {
                Parameter param = new Parameter
                        .ParameterBuilder()
                        .id(parameter.getId())
                        .hidden(false)
                        .name(parameter.getName())
                        .test(test)
                        .build();
                Parameter result = parameterRepository.save(param);
                    if(!rangeService.getByUserAndParameter(user,result).isPresent()){
                        rangeService.createRange(user, result);
                    }
            }
        }

    }

    public void deleteParameter(Long parameterId){
        Parameter parameter = findParameterById(parameterId);
        parameter.setHidden(true);
        parameterRepository.save(parameter);
        List<Range> ranges = rangeService.getRangesByParameterId(parameter.getId());
        for(Range range: ranges){
            rangeService.hideRange(range.getId());
        }
    }

    public Parameter findParameterById(Long id){
        if(parameterRepository.findParameterByIdAndHiddenFalse(id).isPresent()){
            return parameterRepository.findParameterByIdAndHiddenFalse(id).get();
        }
        throw new ObjectNotFoundException(env.getProperty("parameterNotFound"));
    }
}
