package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.AllowSelfPolicy;
import IxLambdaBackend.auth.authorization.policy.OR;
import IxLambdaBackend.auth.authorization.policy.Policy;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.storage.exception.InternalException;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.common.AllowUnownedAppAccessPolicy;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

public class GetAppByIdActivity extends Activity {
    private AppEntity appEntity;

    @Override
    protected Response enact() {
        return new Response(this.appEntity.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(APP_ID, validators),
                new Parameter(USER_ID, null),
                new Parameter(AUTH_ID, null)
        );
    }

    @Override
    protected void preprocess() throws EntityNotFoundException, InternalException {
        final String appId = getStringParameterByName(APP_ID);
        this.appEntity = new AppEntity(appId);
        this.appEntity.read();

        final String appDevId = (String) this.appEntity.getAttribute(DEV_ID).get();
        this.addParameter(new Parameter<String>(OWNER_DEV_ID, null).withValue(appDevId));
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        final AllowSelfPolicy allowSelf = new AllowSelfPolicy(OWNER_DEV_ID, USER_ID);
        final AllowUnownedAppAccessPolicy allowUnownedAppAccess = new AllowUnownedAppAccessPolicy(this.getStringParameterByName(OWNER_DEV_ID));
        final MinUserAccessLevelAuthorizationPolicy allowAdmin = new MinUserAccessLevelAuthorizationPolicy(AccessLevel.TWELVE);

        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext()),
                new Authorization(OR.of(allowSelf, allowUnownedAppAccess, allowAdmin))
        );
    }
}
