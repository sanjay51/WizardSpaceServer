package wizardspace;

import wizardspace.app.UpdateAppActivity;
import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;

import java.util.List;

import static wizardspace.app.AppConstants.*;

public class Test {
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
    public void testAppEntityUpdate() throws Exception {
        final AppEntity appEntity = new AppEntity("926bf4a9-1d67-4b0a-9d78-e23836e12d07");
        //appEntity.read();

        appEntity.setNumberAttributeValue(DRAFT_VERSION, 234);
        //appEntity.update();
        System.out.println("hello");
    }
}
