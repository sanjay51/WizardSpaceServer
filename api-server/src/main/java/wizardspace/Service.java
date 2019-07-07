package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.PATCH;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.activity.LoginActivity;
import wizardspace.activity.sessions.CreateSessionActivity;
import wizardspace.activity.users.CreateUserActivity;
import wizardspace.activity.users.GetUserByIdActivity;
import wizardspace.activity.users.UpdateUserActivity;

public class Service extends LambdaRestService {

    /* USER ROUTES */
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

    /* SESSION ROUTES */
    @POST(path="/sessions")
    public Activity createSessionActivity() {
        return new CreateSessionActivity();
    }

    @GET(path="/login")
    public Activity getLoginActivity() {
        return new LoginActivity();
    }

    @POST(path="/login-post")
    public Activity getLogin() {
        return new LoginActivity();
    }

    @GET(path="/hello")
    public String hello() {
        return "world";
    }

    @GET(path="/good")
    public String good(final Request request) {
        return "good " + request.getQueryStringParameters().get("when");
    }
}
