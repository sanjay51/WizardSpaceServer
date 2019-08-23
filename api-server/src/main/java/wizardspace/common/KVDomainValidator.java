package wizardspace.common;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.ValidationResponse;

public class KVDomainValidator  implements ParamValidator {
    @Override
    public ValidationResponse isValid(Parameter parameter) {
        try {
            KVDomain.valueOf(parameter.getStringValue());
            return new ValidationResponse(true, null);
        } catch (IllegalArgumentException e) {
            return new ValidationResponse(true, parameter.getStringValue() + "is invalid");
        }
    }
}