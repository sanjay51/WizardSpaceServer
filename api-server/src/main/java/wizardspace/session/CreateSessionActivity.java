package wizardspace.session;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.util.TokenUtils;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import wizardspace.user.Auth;
import wizardspace.user.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static wizardspace.Constants.*;

public class CreateSessionActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String email = getParameterByName(EMAIL).getStringValue();
        final String password = getParameterByName(PASSWORD).getStringValue();

        final UserEntity user = new UserEntity(email);
        user.read();

        if (! password.equals((String) user.getAttribute(PASSWORD).get())) {
            throw new NotAuthorizedException("Invalid credentials.");
        }
        System.out.println("email:" + email + "; password:" + password + "; actual_password:" + (String) user.getAttribute(PASSWORD).get());

        final Map<String, Object> response = user.getAsKeyValueObject();

        final String userId = (String) user.getAttribute(USER_ID).get();
        final String authId = TokenUtils.generateToken(userId, Auth.getAuthenticationContext());
        response.put(AUTH_ID, authId);

        return new Response(response);
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
        // TODO: Add captcha
        return null;
    }
}
