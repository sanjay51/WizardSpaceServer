package wizardspace.kv;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.common.KVDomain;
import wizardspace.common.KVDomainValidator;
import wizardspace.common.MinUserAccessLevelAuthorizationPolicy;
import wizardspace.user.Auth;
import wizardspace.user.entity.AccessLevel;

import java.util.*;

import static wizardspace.Constants.*;

public class GetAllKvByDomainActivity extends Activity {
    @Override
    protected Response enact() {
        final String domain = getParameterByName(DOMAIN).getStringValue();
        final KvEntity kv = new KvEntity(domain);
        final List<KvEntity> kvEntities = kv.getAll();

        final List<Map<String, Object>> responseEntities = new ArrayList<>();
        for (final KvEntity entity: kvEntities) {
            responseEntities.add(entity.getAsKeyValueObject());
        }

        return new Response(responseEntities);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter<String>(DOMAIN, Arrays.asList(new StringNotBlankValidator(), new KVDomainValidator())),
                new Parameter<String>(USER_ID, validators),
                new Parameter<String>(AUTH_ID, validators)
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
