package wizardspace.user;

public class Constants {
    public static String AUTH_ID = "authId";
    public static String REQUESTER_ID = "requesterId";
    public static String ATTRIBUTES = "attributes";

    // USER related
    public static String TABLE_USER = "user";
    public static String GSI_USER_ID = "userId-index";

    public static String USER = "user";
    public static String EMAIL = "email";
    public static String USER_ID = "userId";
    public static String PASSWORD = "password";
    public static String FNAME = "fname";
    public static String LNAME = "lname";
    public static String ACCESS_LEVEL = "accessLevel";
    public static String ACCOUNT_STATUS = "accountStatus";
    public static String CREATION_EPOCH = "creationEpoch";
    public static String LAST_UPDATED_EPOCH = "lastUpdatedEpoch";
    public static String LAST_UPDATED_BY = "lastUpdatedBy";

    // APP related
    public static String TABLE_APPS = "wz-apps";
    public static String GSI_DEV_APP_ID = "devId-appId-index";

    public static String APP_ID = "appId";
    public static String DEV_ID = "devId";
    public static String NAME = "name";
    public static String DESCRIPTION = "description";
    public static String IMAGE_URL = "imageUrl";
    public static String LIVE_VERSION = "liveVersion";
    public static String CATEGORY = "category";
    public static String SCREENSHOT_URLS = "screenshotUrls";
    public static String RATING = "rating";
    public static String REVIEW_COUNT = "reviewCount";
    // public static String LAST_UPDATED_EPOCH = "lastUpdatedEpoch";
    // public static String CREATION_EPOCH = "creationEpoch";

    // APP-VERSION related
    public static String TABLE_APP_VERSIONS = "wz-app-versions";
    // public static String APP_ID = "appId";
    public static String VERSION_ID = "versionId";
    // public static String DEV_ID = "devId";
    // public static String NAME = "name";
    // public static String DESCRIPTION = "description";

    // APP-NAME related
    public static String TABLE_APP_NAMES = "wz-app-names";
    // public static String NAME = "name";
    // public static String APP_ID = "appId";
}
