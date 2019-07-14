package wizardspace.activity.users;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.AllowSelfPolicy;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import wizardspace.user.Auth;
import wizardspace.user.UserEntity;

import java.util.Arrays;
import java.util.List;

import static wizardspace.user.Constants.*;

public class GetUserByIdActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String userId = getParameterByName(USER_ID).getStringValue();

        final UserEntity user = UserEntity.newInstanceFromGSI(userId, GSI_USER_ID);
        user.read();

        return new Response(user.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter(USER_ID, validators),
                new Parameter(REQUESTER_ID, validators),
                new Parameter(AUTH_ID, validators)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return Arrays.asList(
                new Authentication(REQUESTER_ID, AUTH_ID, Auth.getAuthenticationContext()),
                new Authorization(Arrays.asList(new AllowSelfPolicy(USER_ID, REQUESTER_ID)))
        );
    }
}
