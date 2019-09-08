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
import IxLambdaBackend.validator.param.NotNullValidator;
import IxLambdaBackend.validator.param.ParamValidator;
import IxLambdaBackend.validator.param.StringNotBlankValidator;
import com.amazonaws.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppNameEntity;
import wizardspace.user.Auth;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;
import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

public class UpdateAppActivity extends Activity {

    @Override
    protected Response enact() throws Exception {

        final String userId = getStringParameterByName(USER_ID);
        final String appId = getStringParameterByName(APP_ID);
        final Map<String, Object> attributes = (Map<String, Object>) getParameterByName(APP_ATTRIBUTES).getValue();

        // Read App
        final AppEntity app = new AppEntity(appId);
        app.read();

        // Populate new attributes
        final Map<String, String> failedUpdates = this.populate(appId, app, attributes, userId);

        // Update app
        app.update();

        // Send response
        Map<String, Object> response = app.getAsKeyValueObject();
        response.put("Failed updates", failedUpdates);

        return new Response(response);
    }

    public static Map<String, String> populate(final String appId, final AppEntity app,
                                               final Map<String, Object> attributes,
                                               final String userId) throws Exception {
        final Map<String, String> failedUpdates = new HashMap<>();

        String appName = (String) attributes.get(APP_NAME);
        final long epochMillis = System.currentTimeMillis();

        // Obtain DevId
        String currentDevId = (String) app.getAttribute(DEV_ID).get();
        final String newDevId = authorizeAndGetNewDevId(currentDevId, userId);

        // Set name, if it's unique
        boolean isNameTaken = resolveAppName(appName, appId);
        if (isNameTaken) {
            failedUpdates.put("appName", "Name is taken");
        } else {
            if (StringUtils.isNotBlank(appName)) app.setAttributeValue(APP_NAME, appName);
        }

        // Set String attributes and update
        app.setAttributeValue(DEV_ID, newDevId);

        for (final String attributeName: attributes.keySet()) {
            if (attributeName.equals(APP_NAME)) continue;

            if (app.getSchema().hasWriteAccess(attributeName) && attributes.containsKey(attributeName)) {
                app.setAttributeValue(attributeName, String.valueOf(attributes.get(attributeName)));
            } else {
                failedUpdates.put(attributeName, "Invalid attribute or Update not allowed");
            }
        }

        // populate IMAGES
        final List<String> images = (List<String>) attributes.get(IMAGES);
        Set<String> imageSet = null;
        if (images != null)
            imageSet = images.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        if (!CollectionUtils.isNullOrEmpty(imageSet)) app.setAttribute(IMAGES, new Attribute(IMAGES, new StringSetValue(imageSet)));

        // set additional attributes
        app.setNumberAttributeValue(DRAFT_VERSION, epochMillis);
        app.setNumberAttributeValue(LAST_UPDATED_EPOCH, epochMillis);
        if (StringUtils.isNotBlank(userId)) app.setAttributeValue(LAST_UPDATED_BY, userId);

        return failedUpdates;

    }

    private static boolean resolveAppName(final String appName, final String appId) throws Exception {
        boolean isNameTaken = false;
        if (StringUtils.isNotBlank(appName)) {
            AppNameEntity appNameEntity = new AppNameEntity(appName);

            try {
                appNameEntity.read();

                // if name is taken by some other appId, don't set it
                if (! StringUtils.equals((String) appNameEntity.getAttribute(APP_ID).get(), appId)) {
                    // name is taken
                    isNameTaken = true;
                }

                // if name is taken by the same appId, do nothing
            } catch (EntityNotFoundException e) {
                // if the name is available, grab it with both hands
                appNameEntity.setAttributeValue(APP_ID, appId);
                appNameEntity.create();
            }
        }

        return isNameTaken;
    }

    private static String authorizeAndGetNewDevId(final String currentDevId, final String userId) {
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
                new Parameter(APP_ATTRIBUTES, Arrays.asList(new NotNullValidator()))
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
