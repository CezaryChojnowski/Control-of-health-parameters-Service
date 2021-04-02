package pb.wi.cohp.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import pb.wi.cohp.domain.user.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameUniqueValidator implements ConstraintValidator<UniqueUsername,String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String personalIdNumber, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(personalIdNumber);
    }
}
