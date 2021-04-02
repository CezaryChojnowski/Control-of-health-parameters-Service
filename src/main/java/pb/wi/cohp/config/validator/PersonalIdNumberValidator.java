package pb.wi.cohp.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import pb.wi.cohp.domain.user.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonalIdNumberValidator implements ConstraintValidator<UniquePersonalIdNumber,String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniquePersonalIdNumber unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String personalIdNumber, ConstraintValidatorContext context) {
        return !userRepository.existsByPersonalIdNumber(personalIdNumber);
    }
}
