package pb.wi.cohp.domain.test

import pb.wi.cohp.domain.parameter.ParameterService
import pb.wi.cohp.domain.user.UserService
import pb.wi.cohp.typeOfTest.UnitTest
import spock.lang.Specification
import org.springframework.core.env.Environment;
import org.junit.experimental.categories.Category

@Category(UnitTest.class)
class TestServiceSpec extends Specification{

    TestRepository testRepository

    ParameterService parameterService

    Environment environment

    UserService userService

    TestService testService

    def setup(){
        testRepository = Stub(TestRepository)
        parameterService = Stub(ParameterService)
        environment = Stub(Environment)
        userService = Stub(UserService)
        testService = new TestService(
                testRepository,
                parameterService,
                environment,
                userService,
        )
    }
}
