package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.activity.LoginActivity;

public class Service extends LambdaRestService {

    @GET(path="/login")
    public Activity getLoginActivity() {
        return new LoginActivity();
    }

    @GET(path="/hello")
    public String hello() {
        return "world";
    }
}
