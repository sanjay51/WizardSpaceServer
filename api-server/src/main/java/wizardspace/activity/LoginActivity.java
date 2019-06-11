package wizardspace.activity;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.codec.binary.StringUtils;
import wizardspace.user.User;

import java.util.Arrays;
import java.util.List;

import static wizardspace.user.Constants.EMAIL;
import static wizardspace.user.Constants.PASSWORD;

public class LoginActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String email = getParameterByName(EMAIL).getStringValue();
        final String password = getParameterByName(PASSWORD).getStringValue();

        final User user = new User(email);

        try {
            user.read();

            if (StringUtils.equals(password, user.getAttribute(PASSWORD).toDynamoDBAttributeValue().getS()))
                throw new NotAuthorizedException("Incorrect email or password");

        } catch (EntityNotFoundException e) {
            throw new NotAuthorizedException("No user found");
        }

        return new Response(user.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter(EMAIL, validators),
                new Parameter(PASSWORD, validators)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return null;
    }
}
