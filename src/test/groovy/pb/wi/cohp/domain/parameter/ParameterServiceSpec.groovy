package pb.wi.cohp.domain.parameter

import org.junit.experimental.categories.Category
import org.springframework.core.env.Environment
import pb.wi.cohp.config.error.exception.ObjectNotFoundException
import pb.wi.cohp.domain.test.TestRepository
import pb.wi.cohp.typeOfTest.UnitTest
import spock.lang.Specification

@Category(UnitTest.class)

class ParameterServiceSpec extends Specification{

    ParameterRepository parameterRepository

    TestRepository testRepository

    Environment environment

    ParameterService parameterService

    def setup(){
        parameterRepository = Stub(ParameterRepository)
        testRepository = Stub(TestRepository)
        environment = Stub(Environment)
        parameterService = new ParameterService(parameterRepository, testRepository, environment)
    }

    def id = 1L
    def parameterName = "param"

    def "Should thrown ObjectNotFoundException when user with given id doesnt exist"(){
        given:
        parameterRepository.findById(id) >> Optional.empty()
        when:
        parameterService.findParameterById(id)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should return parameter"(){
        given:
        def parameter = new Parameter()
        parameter.setName(parameterName)
        and:
        parameterRepository.findById(id) >> Optional.of(parameter)
        when:
        def result = parameterService.findParameterById(id)
        then:
        result.equals(parameter)
    }


}
