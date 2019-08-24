package wizardspace.kv;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.common.S3KVDomainValidator;
import wizardspace.user.Auth;

import java.util.*;

import static wizardspace.Constants.*;

public class SetS3KvActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String domain = getParameterByName(DOMAIN).getStringValue();
        final String key = getParameterByName(KEY).getStringValue();
        final String value = getParameterByName(VALUE).getStringValue();

        final S3KvEntity kv = new S3KvEntity(domain + "/" + key, value);

        kv.create();

        final Map<String, Object> entity = new HashMap<>();

        entity.put("domain", domain);
        entity.put("key", key);
        entity.put("value", value);

        return new Response(entity);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter<String>(DOMAIN, Arrays.asList(new StringNotBlankValidator(), new S3KVDomainValidator())),
                new Parameter<String>(KEY, validators),
                new Parameter<String>(VALUE, validators),
                new Parameter<String>(USER_ID, null),
                new Parameter<String>(AUTH_ID, null)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        if (StringUtils.isNotBlank(getParameterByName(USER_ID).getStringValue())) {
            return Arrays.asList(
                    new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext())
                    // add captcha validation
            );
        }

        return null;
    }
}
