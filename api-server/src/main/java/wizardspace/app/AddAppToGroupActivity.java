package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.exception.EntityAlreadyExistsException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import IxLambdaBackend.storage.attribute.Attribute;

import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

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
        final AppGroupEntity appGroupEntity = addAppToGroup(groupId, epochMillis, app, userId, epochMillis);

        return new Response(appGroupEntity.getAsKeyValueObject());
    }

    public static AppGroupEntity addAppToGroup(final String groupId, final long rank,
                                               final AppEntity app,
                                               final String requesterId,
                                               final long epochMillis) throws InternalException, EntityAlreadyExistsException {
        final AppGroupEntity appGroupEntity = new AppGroupEntity(groupId, rank);
        appGroupEntity.setAttributeValue(APP_ID, (String) app.getAttribute(APP_ID).get());
        final Map<String, Attribute> payload = app.getPayload();

        for (final Map.Entry<String, Attribute> entry: payload.entrySet()) {
            appGroupEntity.setAttribute(entry.getKey(), entry.getValue());
        }

        appGroupEntity.setNumberAttributeValue(CREATION_EPOCH, epochMillis);
        appGroupEntity.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        appGroupEntity.setAttributeValue(LAST_UPDATED_BY, requesterId);
        appGroupEntity.create();

        return appGroupEntity;
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
                new Authorization(new MinUserAccessLevelAuthorizationPolicy(AccessLevel.TWELVE))
        );
    }
}
