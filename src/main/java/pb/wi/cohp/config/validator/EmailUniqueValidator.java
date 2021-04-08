package pb.wi.cohp.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import pb.wi.cohp.domain.user.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail unique) {
        unique.message();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try{
            boolean exists = userRepository.existsByEmail(email);
            return !exists;
        }catch (NullPointerException nullPointerException){
            return true;
        }
    }
}
