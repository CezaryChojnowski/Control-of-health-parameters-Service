package pb.wi.cohp.domain.test;

import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;

import java.util.List;

@Service
public class TestService {

    final TestRepository testRepository;

    final ParameterService parameterService;

    public TestService(TestRepository testRepository, ParameterService parameterService) {
        this.testRepository = testRepository;
        this.parameterService = parameterService;
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

    public Test findTestById(Long id){
        return testRepository.findById(id).get();
    }

    public void deleteTestById(Long id){
        testRepository.deleteById(id);
    }

    public Test editTest(Test test){
        return testRepository.save(test);
    }

    public List<Test> getTests(){
        return (List<Test>) testRepository.findAll();
    }

}
