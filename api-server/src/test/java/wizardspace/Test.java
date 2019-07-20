package wizardspace;

import wizardspace.app.entity.AppEntity;
import wizardspace.app.entity.AppVersionEntity;

import static wizardspace.app.AppConstants.DEV_ID;
import static wizardspace.app.AppConstants.NAME;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        final AppVersionEntity appVersion = new AppVersionEntity("asdf", 1.0);
        appVersion.setAttributeValue(NAME, "asdf");
    }
}
