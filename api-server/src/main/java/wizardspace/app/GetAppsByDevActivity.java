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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class GetAppsByDevActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String devId = getParameterByName(USER_ID).getStringValue();

        final AppEntity appEntity = AppEntity.newInstanceFromGSI(devId, null);
        List<AppEntity> entities = appEntity.getAllByGSI(devId, GSI_DEV_APP_ID);

        List<Map<String, Object>> responseEntities =
                entities.stream().map(e -> e.getAsKeyValueObject()).collect(Collectors.toList());

        return new Response(responseEntities);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(USER_ID, validators),
                new Parameter(AUTH_ID, validators)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext())
        );
    }
}
