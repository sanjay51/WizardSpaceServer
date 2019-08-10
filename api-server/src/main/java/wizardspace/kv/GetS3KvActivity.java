package wizardspace.kv;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.user.Auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wizardspace.Constants.*;

public class GetS3KvActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String domain = getParameterByName(DOMAIN).getStringValue();
        final String key = getParameterByName(KEY).getStringValue();

        final S3KvEntity kv = new S3KvEntity(domain + "_" + key);

        kv.read();

        final Map<String, Object> entity = new HashMap<>();

        entity.put("domain", domain);
        entity.put("key", key);
        entity.put("value", kv.getValue());

        return new Response(entity);
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter<String>(DOMAIN, validators),
                new Parameter<String>(KEY, validators),
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
