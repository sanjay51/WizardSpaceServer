package wizardspace.app;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.AccessType;
import IxLambdaBackend.storage.schema.IndexType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import wizardspace.client.DynamoDBClient;

import java.util.HashMap;
import java.util.Map;

import static wizardspace.user.Constants.*;

public class App extends DDBEntity<App> {

    public App(String primaryKeyValue) {
        super(primaryKeyValue);
    }

    private App(String gsiPrimaryKeyValue, String gsiSortKeyValue) {
        super(gsiPrimaryKeyValue, gsiSortKeyValue, GSI_DEV_APP_ID);
    }

    public App newInstanceFromGSI(String gsiPrimaryKeyValue, String gsiSortKeyValue) {
        return new App(gsiPrimaryKeyValue, gsiSortKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributes = new HashMap<String, Types>() {{
            put(APP_ID, new Types(ValueType.STRING, IndexType.PRIMARY_KEY).withAccess(AccessType.READ_ONLY));
            put(DEV_ID, new Types(ValueType.STRING, IndexType.GSI_PRIMARY_KEY)).withAccess(AccessType.READ_ONLY);
            put(NAME, new Types(ValueType.STRING).withAccess(AccessType.READ_ONLY));
            put(DESCRIPTION, new Types(ValueType.STRING));
            put(IMAGE_URL, new Types(ValueType.STRING));
            put(LIVE_VERSION, new Types(ValueType.NUMBER));
            put(LAST_UPDATED_EPOCH, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
            put(LAST_UPDATED_BY, new Types(ValueType.STRING).withAccess(AccessType.READ_ONLY));
            put(CREATION_EPOCH, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
            put(CATEGORY, new Types(ValueType.STRING));
            put(SCREENSHOT_URLS, new Types(ValueType.STRING_SET));
            put(RATING, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
            put(REVIEW_COUNT, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
        }};

        return new Schema(TABLE_APPS, attributes);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
