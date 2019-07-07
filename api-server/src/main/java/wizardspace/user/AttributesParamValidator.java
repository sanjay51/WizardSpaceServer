package wizardspace.user;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.ValidationResponse;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class AttributesParamValidator implements ParamValidator {
    final Schema schema;

    @Override
    public ValidationResponse isValid(Parameter param) {
        Map<String, String> attributes = (Map<String, String>) param.getValue();

        for (Map.Entry<String, String> entry: attributes.entrySet()) {
            final String name = entry.getKey();

            if (!this.schema.isValidAttribute(name)) {
                return new ValidationResponse(false, "Invalid attribute " + name);
            }
        }

        return new ValidationResponse(true);
    }
}