package pb.wi.cohp.domain.user;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.PasswordValidationFailedException;
import pb.wi.cohp.config.error.exception.UserExistsException;
import pb.wi.cohp.domain.role.ERole;
import pb.wi.cohp.domain.role.Role;
import pb.wi.cohp.domain.role.RoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@PropertySource("classpath:PL.exception.messages.properties")
@PropertySource("classpath:PL.validation.messages.properties")
public class UserService {
    final UserRepository userRepository;

    final Environment env;

    final PasswordEncoder encoder;

    final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, Environment env, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.env = env;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    public boolean checkIfExistsUserWithGivenUsername(String username){
        if(userRepository.findUserByUsername(username).isPresent()){
            throw new UserExistsException(env.getProperty("usernameIsAlreadyTaken"));
        }
        return false;
    }

    public boolean checkIfExistsUserWithGivenEmail(String email){
        if(userRepository.findUserByEmail(email).isPresent()){
            throw new UserExistsException(env.getProperty("emailIsAlreadyTaken"));
        }
        return false;
    }

    public boolean checkIfExistsUserWithGivenPersonalIdNumber(String personalIdNumber){
        if(userRepository.findUserByPersonalIdNumber(personalIdNumber).isPresent()){
            throw new UserExistsException(env.getProperty("personalIdNumberIsAlreadyTaken"));
        }
        return false;
    }

    public boolean checkIfPasswordIsValid(String password)
    {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()){
            throw new PasswordValidationFailedException(env.getProperty("user.password.valid"));
        }
        return true;
    }

    public User createUser(String username,
                           String firstName,
                           String lastName,
                           String personalIdNumber,
                           String email,
                           String password,
                           boolean active){
        checkIfExistsUserWithGivenUsername(username);
        checkIfExistsUserWithGivenEmail(email);
        checkIfExistsUserWithGivenPersonalIdNumber(personalIdNumber);
        checkIfPasswordIsValid(password);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER.name());
        roles.add(userRole);
        User user = new User.UserBuilder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .personalIdNumber(personalIdNumber)
                .roles(roles)
                .active(active)
                .build();
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }
}
