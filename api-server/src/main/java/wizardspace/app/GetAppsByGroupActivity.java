package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.Entity;
import IxLambdaBackend.storage.Page;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import wizardspace.app.common.AppGroup;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.common.KVDomain;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.common.S3KVDomain;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.*;

import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.APP_GROUP_ID;

public class GetAppsByGroupActivity extends Activity {
    @Override
    public Response enact() throws Exception {
        final String groupId = getStringParameterByName(APP_GROUP_ID);
        final String paginationHandle = getStringParameterByName(PAGINATION_HANDLE);
        final int pageSize = Integer.parseInt(getStringParameterByName(PAGE_SIZE));

        final AppGroupEntity appGroupEntity = new AppGroupEntity(groupId);
        final Page page = appGroupEntity.getAllWithPagination(paginationHandle, pageSize);

        List<Map<String, Object>> responseEntities = new ArrayList<>();
        for (final Entity entity: page.getEntities()) {
            responseEntities.add(((DDBEntity) entity).getAsKeyValueObject());
        }

        final Map<String, Object> response = new HashMap<>();

        response.put("apps", responseEntities);
        response.put("paginationHandle", page.getPaginationHandle());

        return new Response(response);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_GROUP_ID, validators),
                new Parameter(USER_ID, null),
                new Parameter(AUTH_ID, null),
                new Parameter(PAGINATION_HANDLE, null),
                new Parameter(PAGE_SIZE, null)
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
