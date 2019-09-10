package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.AllowSelfPolicy;
import IxLambdaBackend.auth.authorization.policy.OR;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.Entity;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.google.common.annotations.VisibleForTesting;
import wizardspace.app.common.AppGroup;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.common.AllowUnownedAppAccessPolicy;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

public class GetAppByGroupAndAppIdActivity extends Activity {
    @Override
    public Response enact() throws Exception {
        final String appGroupId = getStringParameterByName(APP_GROUP_ID);
        final String appId = getStringParameterByName(APP_ID);

        final AppGroupEntity appGroupEntity = new AppGroupEntity();
        appGroupEntity.readByGSI(new Attribute(APP_GROUP_ID, new StringValue(appGroupId)),
                new Attribute(APP_ID, new StringValue(appId)), "appGroupId-appId-index");

        return new Response(appGroupEntity.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_GROUP_ID, validators),
                new Parameter(APP_ID, validators),
                new Parameter(USER_ID, null),
                new Parameter(AUTH_ID, null)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        final String appGroupId = this.getStringParameterByName(APP_GROUP_ID);

        if (AppGroup.LIVE_APPS.name().equals(appGroupId)) return null;

        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext()),
                new Authorization(new MinUserAccessLevelAuthorizationPolicy(AccessLevel.TWELVE))
        );
    }
}
