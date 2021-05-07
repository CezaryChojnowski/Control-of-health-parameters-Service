package pb.wi.cohp.domain.user

import org.junit.experimental.categories.Category
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import pb.wi.cohp.config.error.exception.IncorrectTokenException
import pb.wi.cohp.config.error.exception.InvalidDataException
import pb.wi.cohp.config.error.exception.ObjectNotFoundException
import pb.wi.cohp.domain.email.EmailService
import pb.wi.cohp.domain.role.Role
import pb.wi.cohp.domain.role.RoleRepository
import pb.wi.cohp.typeOfTest.UnitTest
import spock.lang.Specification

@Category(UnitTest.class)
class UserServiceSpec extends Specification{

    UserRepository userRepository

    Environment environment

    PasswordEncoder passwordEncoder

    RoleRepository roleRepository

    EmailService emailService

    UserService userService

    def setup(){
        userRepository = Stub(UserRepository)
        environment = Stub(Environment)
        passwordEncoder = Stub(PasswordEncoder)
        roleRepository = Stub(RoleRepository)
        emailService = Stub(EmailService)
        userService = new UserService(userRepository,
        environment,
        passwordEncoder,
        roleRepository,
        emailService)
    }

    def testUsername = "username"
    def testFirstName = "firstname"
    def testLastName = "lastname"
    def testPersonalIdNumber = "00000000000"
    def testEmail = "email"
    def testPassword = "testPassword"
    def testToken = "token"
    def testTokenToResetPassword = "tokenToResetPassword"
    def roleUserString = "ROLE_USER"
    def roleAdminString = "ROLE_ADMIN"
    def encodePassword ="2a10WZ9hlTxzolxidM0jSUwXyuctYiuL6hscikSvbTpJQ1ttrNZQYfGu"
    def active = false

    def "Should return user with role_user"(){
        given:"Defined data to call method"
        def role = new Role()
        role.setName(roleUserString)
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        def user = new User.UserBuilder()
                .username(testUsername)
                .firstName(testFirstName)
                .lastName(testLastName)
                .password(testPassword)
                .email(testEmail)
                .personalIdNumber(testPersonalIdNumber)
                .roles(roles)
                .active(active)
                .token(testToken)
                .build()
        and:"findByName method return role, encode method return encodePassword, save method return user"
        roleRepository.findByName(_ as String) >> role
        passwordEncoder.encode(_ as String) >> encodePassword
        userRepository.save(_ as User) >> user
        when:"Try create user"
        def result = userService.createUser(testUsername,
                testFirstName,
                testLastName,
                testPersonalIdNumber,
                testEmail,
                testPassword,
                testToken,
                active)
        then:
        result.equals(user)
    }

    def "Should return true when checking token length"(){
        given:"Defined data to call method"
        def length = 21
        when:"Generate token"
        def result = userService.generateSomeToken()
        then:
        result.length().equals(length)
    }

    def "Should return true when checking last character in token"(){
        given:"Defined data to call method"
        def lastChar = "@"
        when:"Generate token"
        def result = userService.generateSomeToken()
        then:
        result.charAt(result.length() - 1).toString().equals(lastChar)
    }

    def "Should thrown IncorrectTokenException"(){
        given:"Defined data to call method"
        def user = new User.UserBuilder()
                .token(testToken)
                .email(testEmail)
                .active(false)
                .build()
        def token ="2a10nFelobpxakX08pHDi2CiuMUhTz4VSgCX5wcWk8jidpY7vAqINO"
        and: "findUserByEmail return optional user and save method return user"
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        userRepository.save(_ as User) >> user
        when:"Try activate account"
        userService.activateAccount(testEmail, token)
        then:
        thrown(IncorrectTokenException)
    }

    def "Should thrown ObjectNotFoundException"(){
        given:"Defined data to call method"
        def token ="2a10nFelobpxakX08pHDi2CiuMUhTz4VSgCX5wcWk8jidpY7vAqINO"
        and: "findUserByEmail return empty optional and save method return user"
        userRepository.findUserByEmail(_ as String) >> Optional.empty()
        userRepository.save(_ as User) >> new User()
        when:"Try activate account"
        userService.activateAccount(testEmail, token)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should return 200 status code value"(){
        given:"Defined data to call method"
        def user = new User.UserBuilder()
                .token(testToken)
                .email(testEmail)
                .active(false)
                .build()
        def resultUser = new User.UserBuilder()
                .email(testEmail)
                .active(true)
                .build()
        and: "findUserByEmail return optional user and save method return user"
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        userRepository.save(_ as User) >> resultUser
        when:"Try activate account"
        def result = userService.activateAccount(testEmail, testToken)
        then:
        result.getStatusCodeValue().equals(200)
    }

    def"Should return user with active account and emtpy token"(){
        given:"Defined data to call method"
        def user = new User()
        user.setEmail(testEmail)
        user.setActive(false)
        user.setToken(testToken)
        def resultUser = new User()
        resultUser.setEmail(testEmail)
        resultUser.setActive(true)
        resultUser.setToken("")
        and: "findUserByEmail return optional user and save method return user"
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        userRepository.save(_ as User) >> resultUser
        when:"Try activate account"
        def result = userService.activateAccount(testEmail)
        then:
        result.isActive() && result.getToken().isEmpty()
    }

    def "Should return false when password is valid"(){
        given:"Defined data to call method"
        def password = "testPassword"
        when:"Check if password is valid"
        def result = userService.isValid(password)
        then:
        !result
    }

    def "Should return true when password is valid"(){
        given:"Defined data to call method"
        def password = "TestTest1@"
        when:"Check if password is valid"
        def result = userService.isValid(password)
        then:
        result
    }

    def "Should return null when token to reset password is not the same as given token"(){
        given:"Defined data to call method"
        def user = new User.UserBuilder()
                .resetPasswordToken(testTokenToResetPassword)
                .email(testEmail)
                .build()
        def token ="2a10nFelobpxakX08pHDi2CiuMUhTz4VSgCX5wcWk8jidpY7vAqINO"
        and: "findUserByEmail return optional user "
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        when:"Try change password"
        def result = userService.changePassword(testEmail, token, testPassword)
        then:
        result.equals(null)
    }

    def "Should thrown InvalidDataException when password is not valid"(){
        given:"Defined data to call method"
        def user = new User.UserBuilder()
                .resetPasswordToken(testTokenToResetPassword)
                .email(testEmail)
                .build()
        and: "findUserByEmail return optional user "
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        when:"Try change password"
        userService.changePassword(testEmail, testTokenToResetPassword, testPassword)
        then:
        thrown(InvalidDataException)
    }

    def "Should thrown ObjectNotFound when user with given email doesnt exist"(){
        given: "findUserByEmail return empty optional "
        userRepository.findUserByEmail(_ as String) >> Optional.empty()
        when:"Try change password"
        userService.changePassword(testEmail, testTokenToResetPassword, testPassword)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should return user with new password and null token"(){
        given:"Defined data to call method"
        def user = new User.UserBuilder()
                .resetPasswordToken(testTokenToResetPassword)
                .email(testEmail)
                .build()
        def newPassword = "TestTest1@"
        def resultUser = new User.UserBuilder()
                .resetPasswordToken(null)
                .email(testEmail)
                .password(newPassword)
                .build()
        and: "findUserByEmail return optional user and save method return user"
        userRepository.findUserByEmail(_ as String) >> Optional.of(user)
        userRepository.save(_ as User) >> resultUser
        when:"Try change password"
        def result = userService.changePassword(testEmail, testTokenToResetPassword, newPassword)
        then:
        result.getResetPasswordToken().equals(null) && result.getPassword().equals(newPassword)
    }

    def "Should thrown ObjectNotFoundExpcetion when user with given email doesnt exist"(){
        given: "findUserByEmail return empty optional"
        userRepository.findUserByEmail(_ as String) >> Optional.empty()
        when: "Try set token to reset password"
        userService.setTokenToResetPassword(testEmail)
        then:
        thrown(ObjectNotFoundException)
    }

    def "Should return user with role_admin"(){
        given:"Defined data to call method"
        def role = new Role()
        role.setName(roleAdminString)
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        def user = new User.UserBuilder()
                .username(testUsername)
                .firstName(testFirstName)
                .lastName(testLastName)
                .email(testEmail)
                .personalIdNumber(testPersonalIdNumber)
                .roles(roles)
                .active(true)
                .build()
        and:"findByName method return role, encode method return encodePassword, save method return user"
        roleRepository.findByName(_ as String) >> role
        passwordEncoder.encode(_ as String) >> encodePassword
        userRepository.save(_ as User) >> user
        emailService.sendEmailWithPasswordAndLogin(_ as String, _ as String, _ as String) >> null
        when:"Try create user"
        def result = userService.createUser(testUsername,
                testFirstName,
                testLastName,
                testPersonalIdNumber,
                testEmail)
        then:
        result.equals(user)
    }

}
