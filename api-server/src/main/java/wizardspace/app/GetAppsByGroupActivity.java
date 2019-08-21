package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppGroupEntity;
import wizardspace.user.Auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static wizardspace.Constants.AUTH_ID;
import static wizardspace.Constants.USER_ID;
import static wizardspace.app.AppConstants.APP_GROUP_ID;
import static wizardspace.app.AppConstants.GSI_DEV_APP_ID;

public class GetAppsByGroupActivity extends Activity {
    @Override
    protected Response enact() {
        final String groupId = getStringParameterByName(APP_GROUP_ID);

        final AppGroupEntity appGroupEntity = new AppGroupEntity(groupId);
        final List<AppGroupEntity> entities = appGroupEntity.getAll();

        List<Map<String, Object>> responseEntities = new ArrayList<>();
        for (final AppGroupEntity entity: entities) {
            responseEntities.add(entity.getAsKeyValueObject());
        }

        return new Response(responseEntities);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_GROUP_ID, validators)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return null;
    }
}
