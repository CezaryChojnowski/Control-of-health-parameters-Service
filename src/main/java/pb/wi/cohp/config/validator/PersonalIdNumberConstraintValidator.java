package pb.wi.cohp.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalIdNumberConstraintValidator implements ConstraintValidator<ValidPersonalIdNumber, String> {
    private int[] personalIdNumber = new int[11];
    private String personalIdNumberString;

    @Override
    public void initialize(ValidPersonalIdNumber contractNumber){

    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        this.personalIdNumberString = contactField;
        if(isPeselFormatValid())
            convertStringToArray();
        else
            return false;

        return isValid();
    }

    private void convertStringToArray() {
        for(int i=0; i<personalIdNumberString.length(); i++) {
            personalIdNumber[i] = Integer.parseInt(String.valueOf(personalIdNumberString.charAt(i)));
        }
    }

    public boolean isPeselFormatValid() {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(personalIdNumberString);

        if(matcher.find() && personalIdNumberString.length()==11)
            return true;
        return false;
    }

    private boolean isValid() {

        int sum = 0;
        for(int i=0; i<10; i++) {
            int multiplier;

            switch((i+1)%4) {
                case 1:
                    multiplier=9;
                    break;
                case 2:
                    multiplier=7;
                    break;
                case 3:
                    multiplier=3;
                    break;
                default:
                    multiplier=1;
            }
            sum+=multiplier*personalIdNumber[i];
        }

        return sum % 10 == personalIdNumber[personalIdNumber.length - 1];
    }
}
