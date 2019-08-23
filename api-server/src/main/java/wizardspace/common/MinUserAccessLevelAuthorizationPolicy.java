package wizardspace.common;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.authorization.policy.Policy;
import org.apache.commons.lang3.StringUtils;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.UserEntity;

import java.util.Map;

import static wizardspace.Constants.*;

public class MinUserAccessLevelAuthorizationPolicy implements Policy {
    final AccessLevel minAccessLevel;

    public MinUserAccessLevelAuthorizationPolicy(final AccessLevel minAccessLevel) {
        this.minAccessLevel = minAccessLevel;
    }

    @Override
    public boolean verify(final Map<String, Parameter> map) {
        final String userId = map.get(USER_ID).getStringValue();
        if (StringUtils.isBlank(userId)) return false;

        final UserEntity user = UserEntity.newInstanceFromGSI(userId, GSI_USER_ID);

        try {
            user.read();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        final String al = (String) user.getAttribute(ACCESS_LEVEL).get();
        final AccessLevel accessLevel = AccessLevel.valueOf(al);

        return (accessLevel.getValue() >= minAccessLevel.getValue());
    }
}
