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
import wizardspace.user.Auth;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.AUTH_ID;
import static wizardspace.Constants.USER_ID;
import static wizardspace.app.common.AppConstants.*;

public class GetAppByIdActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String appId = getParameterByName(APP_ID).getStringValue();
        final String requesterId = getParameterByName(USER_ID).getStringValue();

        final AppEntity appEntity = new AppEntity(appId);
        appEntity.read();

        final String appDevId = (String) appEntity.getAttribute(DEV_ID).get();

        if (StringUtils.isBlank(appDevId) || StringUtils.startsWith(appDevId, "temp-") || StringUtils.equals(requesterId, appDevId)) {
            return new Response(appEntity.getAsKeyValueObject());
        }

        throw new NotAuthorizedException("Don't have permission to read app data.");
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_ID, validators),
                new Parameter(USER_ID, EMPTY_LIST),
                new Parameter(AUTH_ID, EMPTY_LIST)
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

        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext())
        );
    }
}
