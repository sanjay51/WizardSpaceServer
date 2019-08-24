package wizardspace.common;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.authorization.policy.Policy;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import wizardspace.user.entity.AccessLevel;
import wizardspace.user.entity.UserEntity;

import java.util.Map;

import static wizardspace.Constants.*;

@AllArgsConstructor
public class AllowUnownedAppAccessPolicy implements Policy {
    final String appDevId;

    @Override
    public boolean verify(final Map<String, Parameter> map) {
        return StringUtils.isBlank(this.appDevId) || this.appDevId.startsWith("temp-");
    }
}
