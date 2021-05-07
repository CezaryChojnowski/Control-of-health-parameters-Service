package pb.wi.cohp.domain.user

import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import pb.wi.cohp.domain.email.EmailService
import pb.wi.cohp.domain.role.RoleRepository
import pb.wi.cohp.typeOfTest.IntegrationTest
import spock.lang.Specification

import javax.validation.ConstraintViolationException


@ContextConfiguration
@SpringBootTest
@Category(IntegrationTest.class)
class UserServiceTest extends Specification{

    @Autowired
    UserRepository userRepository

    @Autowired
    Environment environment

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    RoleRepository roleRepository

    EmailService emailService

    @Autowired
    UserService userService

    def setup(){
        emailService = Stub(EmailService)
            userService = new UserService(userRepository,
                    environment,
                    passwordEncoder,
                    roleRepository,
                    emailService)
    }

    //correct data
    def correctUsername = "testUsername"
    def correctFirstName = "testFirstName"
    def correctLastName = "testLastName"
    def correctPersonalIdNumber = "96050900414"
    def correctEmail = "testEmail@email.com"
    def correctPassword = "TestTest1@"

    //incorrect data
    def incorrectEmail = "incorrectEmail"
    def incorrectPassword = "incorrectPassword"
    def incorrectPersonalIdNumber = "0"

    //other data
    def token = ""
    def active = true


    @Transactional
    def "Should thrown ConstraintViolationException when trying create user with empty lastName"(){
        given:"Defined data"
        when:"Try create user"
        userService.createUser(correctUsername, correctFirstName, "", correctPersonalIdNumber, correctEmail, correctPassword, token, active)
        then:
        thrown(ConstraintViolationException)
    }

    @Transactional
    def "Should thrown ConstraintViolationException when trying create user with incorrect personal id number"(){
        given:"Defined data"
        when:"Try create user"
        userService.createUser(correctUsername, correctFirstName, correctLastName, incorrectPersonalIdNumber, correctEmail, correctPassword, token, active)
        then:
        thrown(ConstraintViolationException)
    }

    @Transactional
    def "Should thrown ConstraintViolationException when trying create user with incorrect email"(){
        given:"Defined data"
        when:"Try create user"
        userService.createUser(correctUsername, correctFirstName, correctLastName, correctPersonalIdNumber, incorrectEmail, correctPassword, token, active)
        then:
        thrown(ConstraintViolationException)
    }

    @Transactional
    def "Should thrown ConstraintViolationException when trying create user with incorrect password"(){
        given:"Defined data"
        when:"Try create user"
        userService.createUser(correctUsername, correctFirstName, correctLastName, correctPersonalIdNumber, correctEmail, incorrectPassword, token, active)
        then:
        thrown(ConstraintViolationException)
    }

    @Transactional
    def "Should return true if user account is active"(){
        given:
        def username = "user1"
        when:"Check if account is active"
        def active = userService.checkIfUserHasActivatedAccount(username)
        then:
        active
    }

    @Transactional
    def "Should return true if user account is not active"(){
        given:
        def username = "user3"
        when:"Check if account is active"
        def active = userService.checkIfUserHasActivatedAccount(username)
        then:
        !active
    }
}
