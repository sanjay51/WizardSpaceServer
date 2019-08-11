package wizardspace;

import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;

import java.util.List;

import static wizardspace.app.AppConstants.*;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        final AppVersionEntity appVersion = new AppVersionEntity("asdf", 1.0);
        appVersion.setAttributeValue(NAME, "asdf");
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
}
