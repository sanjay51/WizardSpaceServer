package wizardspace.kv;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.AccountStatus;
import wizardspace.user.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static wizardspace.Constants.*;

public class CreateKvActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String domain = getParameterByName(DOMAIN).getStringValue();
        final String value = getParameterByName(VALUE).getStringValue();
        final String id = getParameterByName(ID).getStringValue();
        final String userId = getParameterByName(USER_ID).getStringValue();

        final long epochMillis = System.currentTimeMillis();
        final String sortKey = this.constructSortKey(id, userId);

        final KvEntity kv = new KvEntity(domain, sortKey);
        kv.setAttributeValue(VALUE, value);

        if (StringUtils.isNotBlank(userId)) kv.setAttributeValue(USER_ID, userId);
        
        kv.setNumberAttributeValue(CREATION_EPOCH, epochMillis);
        kv.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);

        kv.create();

        return new Response(kv.getAsKeyValueObject());
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());
        return Arrays.asList(
                new Parameter<String>(DOMAIN, validators),
                new Parameter<String>(VALUE, validators),
                new Parameter<String>(ID, null),
                new Parameter<String>(USER_ID, null)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        // add captcha validation
        return null;
    }

    private String constructSortKey(final String id, final String userId) {
        String sortKey = "";

        if (StringUtils.isNotBlank(id))
            sortKey += id + "_";

        if (StringUtils.isNotBlank(userId))
            sortKey += userId + "_";

        sortKey += UUID.randomUUID();

        return sortKey;
    }
}
