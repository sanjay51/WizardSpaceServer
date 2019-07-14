package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.PATCH;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.activity.sessions.CreateSessionActivity;
import wizardspace.activity.users.CreateUserActivity;
import wizardspace.activity.users.GetUserByIdActivity;
import wizardspace.activity.users.UpdateUserActivity;

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
