package wizardspace.user;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.AccountStatus;
import wizardspace.user.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static wizardspace.Constants.*;

public class CreateUserActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String email = getParameterByName(EMAIL).getStringValue();
        final String password = getParameterByName(PASSWORD).getStringValue();
        final String fname = getParameterByName(FNAME).getStringValue();
        final String lname = getParameterByName(FNAME).getStringValue();

        final long epochMillis = System.currentTimeMillis();

        final UserEntity user = new UserEntity(email);
        final String userId = UUID.randomUUID().toString();
        user.setAttributeValue(USER_ID, userId);
        user.setAttributeValue(PASSWORD, password);
        user.setAttributeValue(FNAME, fname);
        user.setAttributeValue(LNAME, lname);
        user.setAttributeValue(ACCESS_LEVEL, AccessLevel.FOUR.toString());
        user.setAttributeValue(ACCOUNT_STATUS, AccountStatus.ACTIVE.toString());
        user.setNumberAttributeValue(CREATION_EPOCH, epochMillis);
        user.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        user.setAttributeValue(LAST_UPDATED_BY, userId);

        user.create();

        return new Response(user.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter<String>(EMAIL, validators),
                new Parameter<String>(PASSWORD, validators),
                new Parameter<String>(FNAME, validators),
                new Parameter<String>(LNAME, validators)
        );

    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        // add captcha validation
        return null;
    }
}
