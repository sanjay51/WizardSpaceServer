package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.activity.LoginActivity;
import wizardspace.activity.users.CreateUserActivity;

public class Service extends LambdaRestService {

    @POST(path="/users")
    public Activity createUserActivity() {
        return new CreateUserActivity();
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
