package wizardspace;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.response.Response;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.junit.Before;
import wizardspace.app.GetAppByGroupAndAppIdActivity;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;
import wizardspace.client.DynamoDBClient;
import wizardspace.kv.KvEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static wizardspace.app.common.AppConstants.*;

public class Test {
    final static AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("wizardspace")).withRegion(Regions.US_EAST_1).build();

    @Before
    public void init() {
        DynamoDBClient.setClient(ddb);
    }
    @org.junit.Test
    public void test() throws Exception {
        final AppVersionEntity appVersion = new AppVersionEntity("asdf", 1.0);
        appVersion.setAttributeValue(APP_NAME, "asdf");
    }

    @org.junit.Test
    public void testGSIQuery() throws Exception {
        final AppEntity appEntity = new AppEntity("asdf");
        //List<AppEntity> entityList = appEntity.getAllByGSI("sanjay", GSI_DEV_APP_ID);
        System.out.print("asdf");
    }

    @org.junit.Test
    public void testAppEntityRead() throws Exception {
        final AppEntity appEntity = new AppEntity("cf95ae03-7774-4a22-a4d5-f90615e76a4a");
        //appEntity.read();
        System.out.println("hello");
    }

    @org.junit.Test
    public void testAppGroupEntityRead() throws Exception {
        GetAppByGroupAndAppIdActivity activity = new GetAppByGroupAndAppIdActivity();
        activity.addParameter(new Parameter(APP_GROUP_ID, null).withValue("LIVE_APPS"));
        activity.addParameter(new Parameter(APP_ID, null).withValue("fc792379-f37c-4cd7-8789-c323bd0629aa"));

        //Response response = activity.enact();
        //appEntity.read();
        System.out.println("hello");
    }

    @org.junit.Test
    public void testAppGroupEntityReadAll() throws Exception {

        final AppEntity app = new AppEntity("8af805ef-0667-4a3c-b7a9-5505e17a9b17");
        //app.read();

        //AddAppToGroupActivity.addAppToGroup("LIVE_APPS", 1567919237004L, app, "bd52e962-c5ee-4494-b052-d091e7456194", System.currentTimeMillis());
        //final List<Entity> entities = appGroupEntity.getAll();

        System.out.println("hello");
    }


    @org.junit.Test
    public void testAnother() throws Exception {
        final String appId = "5c54ac66-379c-4826-9073-7f71c2efa656";
        final String userId = "bd52e962-c5ee-4494-b052-d091e7456194";

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put(IMAGES, Arrays.asList("https://d3frsattnbx5l6.cloudfront.net/1534061655472-1800flowers-1800flowers1.png",
                "https://d3frsattnbx5l6.cloudfront.net/1534061656156-1800flowers-1800flowers2.png",
                "https://d3frsattnbx5l6.cloudfront.net/1534061656285-1800flowers-1800flowers3.png"));
        attributes.put(DEV_ID, userId);
        attributes.put(IS_EXTERNAL, true);
        attributes.put(APP_NAME, "1800Flowers");
        attributes.put("description", "Send flowers and send a smile! Discover fresh flowers online, gift baskets, and florist-designed arrangements. Flower delivery is easy at 1-800-Flowers.com.");
        attributes.put("isOfflineSupported", false);
        attributes.put("isIOSInstallable", true);
        attributes.put("appLink", "https,//pwa.www.1800flowers.com");
        attributes.put("isAndroidInstallable", false);
        attributes.put("creationEpoch", 1567901461354L);
        attributes.put("lighthouseScore", 69);
        attributes.put("appId", "5c54ac66-379c-4826-9073-7f71c2efa656");
        attributes.put("logo", "https,//d3frsattnbx5l6.cloudfront.net/1532688944141-1800flowers-flowers-icon-192.png");
        attributes.put("isHTTPSEnabled", true);
        attributes.put("lastUpdatedEpoch", 1567901467888L);
        attributes.put("category", "Shopping");
        attributes.put("draftVersion", 1567901467888L);

        final AppEntity app = new AppEntity(appId);
        //app.read();

        //final Map<String, String> failedUpdates = UpdateAppActivity.populate(appId, app, attributes, userId);
        //app.update();

        System.out.println("hello");
    }

    @org.junit.Test
    public void testGetAll() throws Exception {
        final KvEntity kvEntity = new KvEntity("FEATURE_REQUESTS");
        //List<Entity> entityList = kvEntity.getAll();
        //DDBEntity entity = (DDBEntity) entityList.get(0);
        //System.out.print("FEATURE_REQUESTS" + entity.getAsKeyValueObject());
    }
}
