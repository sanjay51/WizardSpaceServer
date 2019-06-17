package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.activity.LoginActivity;

public class Service extends LambdaRestService {

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
