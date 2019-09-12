package wizardspace;

import IxLambdaBackend.storage.attribute.Attribute;
import IxLambdaBackend.storage.attribute.value.StringSetValue;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import wizardspace.app.entity.AppEntity;
import wizardspace.client.DynamoDBClient;

import java.io.File;
import java.util.*;

import static wizardspace.Constants.*;
import static wizardspace.app.common.AppConstants.*;

public class BulkUploadApps {
    final String myId = "bd52e962-c5ee-4494-b052-d091e7456194";

    final static AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("wizardspace")).withRegion(Regions.US_EAST_1).build();

    @Before
    public void init() {
        DynamoDBClient.setClient(ddb);
    }
    @Test
    public void bulkUpload() throws Exception {
        final String appGroupId = "LIVE_APPS";

        for (final AppEntity app: getApps()) {
            final long epoch = System.currentTimeMillis();
            //AddAppToGroupActivity.addAppToGroup(appGroupId, epoch, app, myId, epoch);
            //Thread.sleep(40);
        }

    }

    private List<AppEntity> getApps() throws Exception {
        List<AppEntity> apps = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        List<LinkedHashMap> rows = new ArrayList<>();
        rows = objectMapper.readValue(new File("/personal-workspace/WizardSpaceServer/api-server/src/test/java/wizardspace/app-data.json"), rows.getClass());
        final long epoch = System.currentTimeMillis();

        for (LinkedHashMap<String, String> row: rows) {
            final String appId = UUID.randomUUID().toString();
            final String devId = myId;
            final String appName = row.get("title");
            final String appLink = row.get("link");
            final String description = row.get("description");
            final String logo = row.get("logo");
            final String isExternal = "true";
            final int liveVersion = 0;
            final long draftVersion = epoch;
            final long publicVersion = 0;
            final long lastUpdatedEpoch = epoch;
            final String lastUpdatedBy = myId;
            final String category = row.get("category");
            final String video = "";

            final Set<String> imageSet = new HashSet<>();
            if (StringUtils.isNotBlank(row.get("screenshotNeg1"))) imageSet.add(row.get("screenshotNeg1"));
            if (StringUtils.isNotBlank(row.get("screenshot0"))) imageSet.add(row.get("screenshot0"));
            if (StringUtils.isNotBlank(row.get("screenshot1"))) imageSet.add(row.get("screenshot1"));
            if (StringUtils.isNotBlank(row.get("screenshot2"))) imageSet.add(row.get("screenshot2"));
            if (StringUtils.isNotBlank(row.get("screenshot3"))) imageSet.add(row.get("screenshot3"));

            final boolean isHttpsEnabled = StringUtils.equals("true", row.get("isHttpsEnabled"));
            final boolean isOfflineSupported = StringUtils.equals("true", row.get("isOfflineSupported"));
            final boolean isAndroidInstallable = StringUtils.equals("true", row.get("isAndroidInstallable"));
            final boolean isIOSInstallable = StringUtils.equals("true", row.get("isIOSInstallable"));
            final float lighthouseScore = Float.parseFloat(row.get("lighthouseScore"));

            final AppEntity app = new AppEntity(appId);
            app.setAttributeValue(DEV_ID, devId);
            app.setAttributeValue(APP_NAME, appName);
            app.setAttributeValue(APP_LINK, appLink);
            app.setAttributeValue(DESCRIPTION, description);
            app.setAttributeValue(LOGO, logo);
            app.setAttributeValue(IS_EXTERNAL, isExternal);
            app.setNumberAttributeValue(LIVE_VERSION, liveVersion);
            app.setNumberAttributeValue(DRAFT_VERSION, draftVersion);
            app.setNumberAttributeValue(LAST_UPDATED_EPOCH, lastUpdatedEpoch);
            app.setAttributeValue(LAST_UPDATED_BY, lastUpdatedBy);
            app.setNumberAttributeValue(CREATION_EPOCH, epoch);
            app.setAttributeValue(CATEGORY, category);
            app.setAttribute(IMAGES, new Attribute(IMAGES, new StringSetValue(imageSet)));

            app.setBooleanAttributeValue(IS_HTTPS_ENABLED, isHttpsEnabled);
            app.setBooleanAttributeValue(IS_OFFLINE_SUPPORTED, isOfflineSupported);
            app.setBooleanAttributeValue(IS_ANDROID_INSTALLABLE, isAndroidInstallable);
            app.setBooleanAttributeValue(IS_IOS_INSTALLABLE, isIOSInstallable);
            app.setNumberAttributeValue(LIGHTHOUSE_SCORE, lighthouseScore);

            apps.add(app);
        }

        return apps;
    }
}
