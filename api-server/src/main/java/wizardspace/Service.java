package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.PATCH;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.app.CreateAppActivity;
import wizardspace.kv.CreateKvActivity;
import wizardspace.kv.KvEntity;
import wizardspace.session.CreateSessionActivity;
import wizardspace.user.CreateUserActivity;
import wizardspace.user.GetUserByIdActivity;
import wizardspace.user.UpdateUserActivity;

public class Service extends LambdaRestService {

    /** USER ROUTES */

    @POST(path="/users")
    public Activity createUserActivity() {
        return new CreateUserActivity();
    }

    @GET(path="/users/{userId}")
    public Activity getUserByIdActivity() {
        return new GetUserByIdActivity();
    }

    @PATCH(path="/users/{userId}")
    public Activity updateUserActivity() {
        return new UpdateUserActivity();
    }

    /** SESSION ROUTES */

    @POST(path="/sessions")
    public Activity createSessionActivity() {
        return new CreateSessionActivity();
    }

    /** APP ROUTES */

    @POST(path="/apps")
    public Activity createAppActivity() {
        return new CreateAppActivity();
    }

    /** Key Value ROUTES */
    @POST(path="/kv")
    public Activity createKeyValue() {
        return new CreateKvActivity();
    }

    /** OTHER TEST ROUTES */

    @GET(path="/hello")
    public String hello() {
        return "world";
    }

    @GET(path="/good")
    public String good(final Request request) {
        return "good " + request.getQueryStringParameters().get("when");
    }
}
