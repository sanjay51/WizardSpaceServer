package wizardspace.activity.users;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.AllowSelfPolicy;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.validator.param.NotNullValidator;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.google.common.collect.ImmutableMap;
import wizardspace.user.AttributesParamValidator;
import wizardspace.user.Auth;
import wizardspace.user.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wizardspace.user.Constants.*;

public class UpdateUserActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final Map<String, String> attributes = (Map<String, String>) getParameterByName(ATTRIBUTES).getValue();
        final String userId = getParameterByName(USER_ID).getStringValue();

        final User user = User.newInstanceFromGSI(userId, GSI_USER_ID);
        user.read();

        final Map<String, String> failedUpdates = new HashMap<>();
        final Schema schema = user.getSchema();

        // only update attributes which have write access.
        attributes.entrySet().stream().forEach(attribute -> {
            final String name = attribute.getKey();
            final String value = attribute.getValue();

            if (! schema.hasWriteAccess(name)) {
                failedUpdates.put(name, "Update not allowed");
            } else {
                user.setAttributeValue(name, value);
            }
        });

        user.setAttributeValue(LAST_UPDATED_BY, USER_ID);
        user.setNumberAttributeValue(LAST_UPDATED_EPOCH, System.currentTimeMillis());

        user.update();

        final Map<String, Object> response = ImmutableMap.of("user", user.getAsKeyValueObject(), "failedUpdates", failedUpdates);
        return new Response(response);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        final ParamValidator attributesValidator =
                new AttributesParamValidator(new User("").getSchema());

        return Arrays.asList(
                new Parameter(REQUESTER_ID, validators),
                new Parameter(AUTH_ID, validators),
                new Parameter(USER_ID, validators),
                new Parameter(ATTRIBUTES, Arrays.asList(new NotNullValidator(), attributesValidator))
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
