package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.Entity;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.value.StringValue;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import wizardspace.app.common.AppGroup;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static wizardspace.Constants.AUTH_ID;
import static wizardspace.Constants.USER_ID;
import static wizardspace.app.common.AppConstants.*;

public class GetAppsByGroupAndCategoryActivity extends Activity {
    @Override
    public Response enact() throws Exception {
        final String appGroupId = getStringParameterByName(APP_GROUP_ID);
        final String category = getStringParameterByName(CATEGORY);

        final AppGroupEntity appGroupEntity = new AppGroupEntity();

        final List<DDBEntity> entities = appGroupEntity.readAllByGSI(new Attribute(APP_GROUP_ID, new StringValue(appGroupId)),
                new Attribute(CATEGORY, new StringValue(category)), "appGroupId-category-index");

        final List<Map<String, Object>> responseEntities = new ArrayList<>();
        for (final Entity entity: entities) {
            responseEntities.add(((DDBEntity) entity).getAsKeyValueObject());
        }

        return new Response(responseEntities);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_GROUP_ID, validators),
                new Parameter(CATEGORY, validators),
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
