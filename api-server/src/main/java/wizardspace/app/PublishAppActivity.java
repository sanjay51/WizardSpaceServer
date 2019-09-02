package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.google.gson.Gson;
import wizardspace.app.common.AppGroup;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.common.S3KVDomain;
import wizardspace.kv.S3KvEntity;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.Arrays;
import java.util.List;

import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

public class PublishAppActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String appId = this.getStringParameterByName(APP_ID);
        final String userId = this.getStringParameterByName(USER_ID);

        final long epochMillis = System.currentTimeMillis();

        AppEntity appEntity = new AppEntity(appId);
        appEntity.read();

        boolean isExternal = Boolean.valueOf((String) appEntity.getAttribute(IS_EXTERNAL).get());
        if (! isExternal) {
            // Read S3 and App entities
            S3KvEntity s3KvEntity = new S3KvEntity(S3KVDomain.DRAFT_APPS.name() + "/" + appId);
            s3KvEntity.read();

            // Copy S3 entity
            s3KvEntity.setKey(S3KVDomain.LIVE_APPS.name() + "/" + appId);
            s3KvEntity.create();
        }

        // Add app to LIVE_APPS group
        final AppGroupEntity appGroupEntity = AddAppToGroupActivity.addAppToGroup(AppGroup.LIVE_APPS.name(), epochMillis, appEntity, userId, epochMillis);

        return new Response(appGroupEntity.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(USER_ID, validators),
                new Parameter(AUTH_ID, validators),
                new Parameter(APP_ID, validators)
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
