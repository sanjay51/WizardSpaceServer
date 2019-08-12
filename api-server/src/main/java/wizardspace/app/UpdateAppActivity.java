package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;
import wizardspace.user.Auth;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class UpdateAppActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String userId = getParameterByName(USER_ID).getStringValue();
        final String appId = getParameterByName(APP_ID).getStringValue();
        final String description = getParameterByName(DESCRIPTION).getStringValue();

        final long epochMillis = System.currentTimeMillis();

        // Read App
        final AppEntity app = new AppEntity(appId);
        app.read();

        String currentDevId = (String) app.getAttribute(DEV_ID).get();
        final String newDevId = authorizeAndGetNewDevId(currentDevId, userId);

        app.setAttributeValue(DEV_ID, newDevId);
        if (StringUtils.isNotBlank(description)) app.setAttributeValue(DESCRIPTION, description);
        if (StringUtils.isNotBlank(userId)) app.setAttributeValue(LAST_UPDATED_BY, userId);
        app.setNumberAttributeValue(DRAFT_VERSION, epochMillis);
        app.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        app.update();

        return new Response(app.getAsKeyValueObject());
    }

    private String authorizeAndGetNewDevId(final String currentDevId, final String userId) {
        boolean isCurrentDevIdTemporary = StringUtils.startsWith(currentDevId, "temp-");

        /*
        currentDevId and userId same =>

                           userId
                          T   N   P
                       T  D   x   x
         currentDevId  N  x   CN  x
                       P  x   x   D

        currentDevId and userId different =>

                           userId
                          T   N   P
                       T  U   D   U
         currentDevId  N  U   x   U
                       P  NA  NA  NA

         T = Temporary, N = Null, P = Permanent
         U = userId, D = DevId, NA = NotAuthorized, CN = Create New, x = Impossible case
         */
        if (StringUtils.equals(userId, currentDevId)) {
            if (currentDevId == null) return "temp-" + UUID.randomUUID();

            return currentDevId;

        } else {
            if (!isCurrentDevIdTemporary) throw new NotAuthorizedException(("Not authorized to perform this action"));
            if (userId == null) return currentDevId;

            return userId;
        }
    }

    @Override
    protected List<Parameter> getParameters() {
        final List<ParamValidator> validators = Arrays.asList(new StringNotBlankValidator());

        return Arrays.asList(
                new Parameter(USER_ID, EMPTY_LIST),
                new Parameter(AUTH_ID, EMPTY_LIST),
                new Parameter(APP_ID, validators),
                new Parameter(DESCRIPTION, EMPTY_LIST)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        final String userId = this.getParameterByName(USER_ID).getStringValue();
        final String authId = this.getParameterByName(AUTH_ID).getStringValue();

        // No auth needed if anonymous user
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(authId)) {
            return Arrays.asList();
        }

        // Else, authenticate!
        return Arrays.asList(
                new Authentication(USER_ID, AUTH_ID, Auth.getAuthenticationContext())
        );
    }
}
