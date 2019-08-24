package wizardspace;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.PATCH;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.service.LambdaRestService;
import wizardspace.app.*;
import wizardspace.kv.CreateKvActivity;
import wizardspace.kv.GetAllKvByDomainActivity;
import wizardspace.kv.GetS3KvActivity;
import wizardspace.kv.SetS3KvActivity;
import wizardspace.session.CreateSessionActivity;
import wizardspace.user.CreateUserActivity;
import wizardspace.user.GetUserByIdActivity;
import wizardspace.user.UpdateUserActivity;

public class Service extends LambdaRestService {

    /******************** USER ROUTES *********************/

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

    /******************** SESSION ROUTES *********************/

    @POST(path="/sessions")
    public Activity createSessionActivity() {
        return new CreateSessionActivity();
    }

    /******************** APP ROUTES *********************/

    @POST(path="/apps")
    public Activity createAppActivity() {
        return new CreateAppActivity();
    }

    @PATCH(path="/apps/{appId}")
    public Activity updateAppActivity() { return new UpdateAppActivity(); }

    @GET(path="/apps")
    public Activity getAppsByDevActivity() { return new GetAppsByDevActivity(); }

    @GET(path="/apps/{appId}")
    public Activity getAppByIdActivity() { return new GetAppByIdActivity(); }


    /******************** APP GROUP ROUTES *********************/

    @GET(path="/app-group/{groupId}")
    public Activity getAppsByGroupActivity() { return new GetAppsByGroupActivity(); }

    @POST(path="/app-group-entities")
    public Activity addAppToGroupActivity() {
        return new AddAppToGroupActivity();
    }

    @POST(path="/live-app")
    public Activity publishAppActivity() {
        return new PublishAppActivity();
    }

    /******************** KEY-VALUE ROUTES *********************/

    @GET(path="/kv")
    public Activity getAllKvByDomain() {
        return new GetAllKvByDomainActivity();
    }

    @POST(path="/kv")
    public Activity createKeyValue() {
        return new CreateKvActivity();
    }

    /******************** BIG KEY-VALUE (S3 based) ROUTES *********************/

    @POST(path="/big-kv")
    public Activity setBigKeyValue() {
        return new SetS3KvActivity();
    }

    @GET(path="/big-kv")
    public Activity getBigKeyValue() {
        return new GetS3KvActivity();
    }

    /******************** OTHER TEST ROUTES *********************/

    @GET(path="/hello")
    public String hello() {
        return "world";
    }

    @GET(path="/good")
    public String good(final Request request) {
        return "good " + request.getQueryStringParameters().get("when");
    }
}
