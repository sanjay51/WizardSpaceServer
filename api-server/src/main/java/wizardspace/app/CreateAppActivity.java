package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
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
import static wizardspace.app.common.AppConstants.*;

public class CreateAppActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String userId = getParameterByName(USER_ID).getStringValue();
        final String name = getParameterByName(APP_NAME).getStringValue();
        final Boolean isExternal = Boolean.parseBoolean(getStringParameterByName(IS_EXTERNAL));
        final String appId = UUID.randomUUID().toString();
        final long epochMillis = System.currentTimeMillis();
        final long liveVersion = 0;

        final String devId = StringUtils.isBlank(userId) ? "temp-" + UUID.randomUUID() : userId;

        // Create App
        final AppEntity app = constructAppEntity(appId, devId, epochMillis, liveVersion);
        if (isExternal) app.setAttributeValue(IS_EXTERNAL, "true");
        app.create();

        // Create App Version
        final AppVersionEntity appVersion = new AppVersionEntity(appId, liveVersion);
        if (StringUtils.isNotBlank(userId)) appVersion.setAttributeValue(DEV_ID, userId);
        appVersion.setAttributeValue(APP_NAME, name);
        appVersion.create();

        return new Response(app.getAsKeyValueObject());
    }

    private AppEntity constructAppEntity(final String appId, final String devId,
                                         final long epochMillis, final long liveVersion) {
        final AppEntity app = new AppEntity(appId);

        if (StringUtils.isNotBlank(devId)) {
            app.setAttributeValue(DEV_ID, devId);
            app.setAttributeValue(LAST_UPDATED_BY, devId);
        }

        app.setNumberAttributeValue(LIVE_VERSION, liveVersion);
        app.setNumberAttributeValue(DRAFT_VERSION, epochMillis);
        app.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        app.setNumberAttributeValue(CREATION_EPOCH, epochMillis);

        return app;
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(USER_ID, EMPTY_LIST),
                new Parameter(AUTH_ID, EMPTY_LIST),
                new Parameter(IS_EXTERNAL, EMPTY_LIST),
                new Parameter(APP_NAME, validators)
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
