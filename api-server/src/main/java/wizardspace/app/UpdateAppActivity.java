package wizardspace.app;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.value.StringSetValue;
import IxLambdaBackend.storage.exception.EntityNotFoundException;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.amazonaws.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppNameEntity;
import wizardspace.user.Auth;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class UpdateAppActivity extends Activity {
    @Override
    protected Response enact() throws Exception {
        final String userId = getStringParameterByName(USER_ID);
        final String appId = getStringParameterByName(APP_ID);
        final String description = getStringParameterByName(DESCRIPTION);
        String name = getStringParameterByName(APP_NAME);
        final String category = getStringParameterByName(CATEGORY);
        final String logo = getStringParameterByName(LOGO);

        final Parameter<List<String>> images = getParameterByName(IMAGES);
        Set<String> imageSet = null;
        if (images != null && images.getValue() != null)
            imageSet = images.getValue().stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());

        final long epochMillis = System.currentTimeMillis();

        // Read App
        final AppEntity app = new AppEntity(appId);
        app.read();

        // Obtain DevId
        String currentDevId = (String) app.getAttribute(DEV_ID).get();
        final String newDevId = authorizeAndGetNewDevId(currentDevId, userId);

        // Check if name is unique
        if (StringUtils.isNotBlank(name)) {
            AppNameEntity appNameEntity = new AppNameEntity(name);

            try {
                appNameEntity.read();

                // if name is taken by some other appId, don't set it
                if (! StringUtils.equals(appNameEntity.getAttribute(APP_ID).toString(), appId)) {
                    // name is taken
                    name = null;
                }

                // if name is taken by the same appId, do nothing
            } catch (EntityNotFoundException e) {
                // if the name is available, grab it with both hands
                appNameEntity.setAttributeValue(APP_ID, appId);
                appNameEntity.create();
            }
        }

        // Set attributes and update
        app.setAttributeValue(DEV_ID, newDevId);
        if (StringUtils.isNotBlank(name)) app.setAttributeValue(APP_NAME, name);
        if (StringUtils.isNotBlank(description)) app.setAttributeValue(DESCRIPTION, description);
        if (StringUtils.isNotBlank(category)) app.setAttributeValue(CATEGORY, category);
        if (StringUtils.isNotBlank(logo)) app.setAttributeValue(LOGO, logo);
        if (!CollectionUtils.isNullOrEmpty(imageSet)) app.setAttribute(IMAGES, new Attribute(IMAGES, new StringSetValue(imageSet)));

        app.setNumberAttributeValue(DRAFT_VERSION, epochMillis);
        app.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        if (StringUtils.isNotBlank(userId)) app.setAttributeValue(LAST_UPDATED_BY, userId);
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
                new Parameter(APP_NAME, EMPTY_LIST),
                new Parameter(DESCRIPTION, EMPTY_LIST),
                new Parameter(CATEGORY, EMPTY_LIST),
                new Parameter(LOGO, EMPTY_LIST),
                new Parameter(IMAGES, EMPTY_LIST)
        );
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        final String userId = this.getStringParameterByName(USER_ID);
        final String authId = this.getStringParameterByName(AUTH_ID);

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
