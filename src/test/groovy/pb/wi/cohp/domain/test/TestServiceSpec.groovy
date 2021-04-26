package pb.wi.cohp.domain.test

import pb.wi.cohp.config.error.exception.ObjectNotFoundException
import pb.wi.cohp.domain.parameter.Parameter
import pb.wi.cohp.domain.parameter.ParameterService
import pb.wi.cohp.domain.user.User
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

    def id = 1
    def email = "testEmail"
    def userName = "testUsername"
    def testName = "testName"
    def newTestName = "newTestName"

    def "Should throw ObjectNoFoundException when try find test by id"(){
        given: "The findById method will always return Optional.empty when called with given ticket id which does not exists"
        testRepository.findById(id) >> Optional.empty()
        when : "Try find test by id"
        testService.findTestById(id)
        then: "Thrown ObjectNoFoundException"
        thrown(ObjectNotFoundException)
    }

    def "Should return test by id"(){
        given: "Defined data to call method"
        def test = new Test.TestBuilder().id(id)
                .name(testName)
                .build()
        and: "The findById method will always return Optiona test when called with given ticked id which exists"
        testRepository.findById(test.getId()) >> Optional.of(test)
        when: "Try find test by id"
        def result = testService.findTestById(test.getId())
        then:
        test.equals(result)
    }

    def "Should return test by id and username"(){
        given: "Defined data to call method"
        def user = new User.UserBuilder()
                .email(email)
                .username(userName)
                .build()
        def test = new Test.TestBuilder()
                .id(id)
                .name(testName)
                .user(user)
                .build()
        and: "GetUserByUsername return user by username and findByIdAndUser method return Optional test by id and user"
        userService.getUserByUsername(userName) >> user
        testRepository.findByIdAndUser(id, user as User) >> Optional.of(test)
        when:"Try find test by id and user"
        Test result = testService.findTestByIdAndUser(id, userName)
        then:
        test.equals(result)
    }

    def "Should throw ObjectNoFoundException when try find test by id and user"(){
        given: "Defined data to call method"
        def user = new User.UserBuilder()
                .email(email)
                .username(userName)
                .build()
        and: "GetUserByUsername return user by username and findByIdAndUser method return empty Optional"
        userService.getUserByUsername(userName) >> user
        testRepository.findByIdAndUser(id, user as User) >> Optional.empty()
        when:"Try find test by id and user"
        testService.findTestByIdAndUser(id, userName)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should create test"(){
        given: "Defined data to call method"
        def test = new Test.TestBuilder()
                .id(id)
                .name(testName)
                .build()
        and: "The save method return created test"
        testRepository.save(_ as Test) >> test
        when: "Try save test"
        Test result = testService.createTest(testName)
        then:
        test.equals(result)
    }

    def "Should return edited test with new name"(){
        given: "Defined data to call method"
        def test = new Test.TestBuilder()
                .id(id)
                .name(testName)
                .build()
        def testDTO = new TestDTO(id, newTestName, null)
        and:"The findById method return optional test, createParameter return null, save method return Test"
        testRepository.findById(test.getId()) >> Optional.of(test)
        parameterService.createParameter(_ as List<Parameter>, _ as TestDTO) >> null
        testRepository.save(_ as Test) >> new Test.TestBuilder()
                .id(id)
                .name(newTestName)
                .build()
        when:"Try edit test"
        Test result = testService.editTest(testDTO)
        then:
        testDTO.getName().equals(result.getName())
    }

    def "should return test list of given user and no user"(){
        given: "Defined data to call method"
        def user = new User.UserBuilder()
                .email(email)
                .username(userName)
                .build()

        def firstTestWithUser = new Test()
        firstTestWithUser.setName(testName)
        firstTestWithUser.setUser(user)
        def secondTestWithUser = new Test()
        secondTestWithUser.setName(testName)
        secondTestWithUser.setUser(user)
        List<Test> testsWithUser = new ArrayList<>()
        testsWithUser.add(firstTestWithUser)
        testsWithUser.add(secondTestWithUser)
        def testWithoutUser = new Test()
        testWithoutUser.setName(testName)
        List<Test> testsWithoutUser = new ArrayList<>()
        testsWithoutUser.add(testWithoutUser)
        def mergedLists = testsWithoutUser + testsWithUser
        and:"GetUserByUsername method return user, findAllByUser return testsWithUser list, findAllByUser with null param return testWIthoutUser list"
        userService.getUserByUsername(_ as String) >> user
        testRepository.findAllByUser(_ as User) >> testsWithUser
        testRepository.findAllByUser(null) >> testsWithoutUser
        when:
        def result = testService.getTests(userName)
        then:
        result.size().equals(mergedLists.size())
    }

    def "Should create a user test"(){
        given: "Defined data to call method"
        def user = new User.UserBuilder()
                .email(email)
                .username(userName)
                .build()
        def test = new Test.TestBuilder()
                .id(id)
                .name(testName)
                .user(user)
                .build()
        and: "The save method return created test and getUserByUsername method return user"
        userService.getUserByUsername(_ as String) >> user
        testRepository.save(_ as Test) >> test
        when: "Try save test"
        Test result = testService.createTest(testName, userName)
        then:
        test.equals(result)
    }



}
