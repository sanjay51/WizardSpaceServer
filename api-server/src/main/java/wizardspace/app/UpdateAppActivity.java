package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;
import wizardspace.user.Auth;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class UpdateAppActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String userId = getParameterByName(USER_ID).getStringValue();
        final String appId = getParameterByName(APP_ID).getStringValue();
        final String description = getParameterByName(DESCRIPTION).getStringValue();

        final long epochMillis = System.currentTimeMillis();

        // Read App
        final AppEntity app = new AppEntity(appId);
        app.read();

        final String currentDevId = (String) app.getAttribute(DEV_ID).get();

        if (StringUtils.isNotBlank(currentDevId) && !StringUtils.equals(currentDevId, userId)) {
            throw new NotAuthorizedException("Not authorized to perform this action");
        }

        app.setAttributeValue(DEV_ID, userId);
        app.setAttributeValue(DESCRIPTION, description);
        app.setNumberAttributeValue(DRAFT_VERSION, epochMillis);
        app.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        app.setAttributeValue(LAST_UPDATED_BY, userId);
        app.update();

        return new Response(app.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(USER_ID, EMPTY_LIST),
                new Parameter(AUTH_ID, EMPTY_LIST),
                new Parameter(APP_ID, validators),
                new Parameter(DESCRIPTION, EMPTY_LIST)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        final String userId = this.getParameterByName(USER_ID).getStringValue();
        final String authId = this.getParameterByName(AUTH_ID).getStringValue();

        // No auth needed if anonymous user
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(authId)) {
            return Arrays.asList();
        }

        // Else, authenticate!
        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext())
        );
    }
}
