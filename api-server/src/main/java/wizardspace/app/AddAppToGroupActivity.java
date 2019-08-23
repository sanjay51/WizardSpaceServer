package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.Policy;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.app.entity.AppVersionEntity;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import IxLambdaBackend.storage.attribute.Attribute;
import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class AddAppToGroupActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String groupId = getStringParameterByName(APP_GROUP_ID);
        final String appId = getStringParameterByName(APP_ID);
        final String userId = getStringParameterByName(USER_ID);

        final long epochMillis = System.currentTimeMillis();

        // Read App
        final AppEntity app = new AppEntity(appId);
        app.read();

        // Create App group row
        final AppGroupEntity appGroupEntity = new AppGroupEntity(groupId, epochMillis);
        appGroupEntity.setAttributeValue(APP_ID, (String) app.getAttribute(APP_ID).get());
        final Map<String, Attribute> payload = app.getPayload();

        for (final Map.Entry<String, Attribute> entry: payload.entrySet()) {
            appGroupEntity.setAttribute(entry.getKey(), entry.getValue());
        }

        appGroupEntity.setNumberAttributeValue(CREATION_EPOCH, epochMillis);
        appGroupEntity.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        appGroupEntity.setAttributeValue(LAST_UPDATED_BY, userId);
        appGroupEntity.create();

        return new Response(appGroupEntity.getAsKeyValueObject());
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
                new Parameter(APP_ID, validators),
                new Parameter(APP_GROUP_ID, validators),
                new Parameter(USER_ID, validators),
                new Parameter(AUTH_ID, validators)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext()),
                new Authorization(Arrays.asList(new MinUserAccessLevelAuthorizationPolicy(AccessLevel.TWELVE)))
        );
    }
}
