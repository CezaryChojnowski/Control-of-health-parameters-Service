package pb.wi.cohp.config.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PhoneNumberConstraintValidator.class)
@Target({FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPhoneNumber {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

