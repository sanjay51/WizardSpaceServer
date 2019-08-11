package wizardspace.app.entity;

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

import static wizardspace.Constants.*;
import static wizardspace.app.AppConstants.*;

public class AppVersionEntity extends DDBEntity<AppVersionEntity> {

    public AppVersionEntity(final String primaryKeyValue, final String sortKeyValue) {
        super(primaryKeyValue, sortKeyValue);
    }

    public
    AppVersionEntity(final String primaryKeyValue, final double sortKeyValue) {
        super(primaryKeyValue, sortKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributes = new HashMap<String, Types>() {{
            put(APP_ID, new Types(ValueType.STRING, IndexType.PRIMARY_KEY).withAccess(AccessType.READ_ONLY));
            put(VERSION_ID, new Types(ValueType.STRING, IndexType.SORT_KEY).withAccess(AccessType.READ_ONLY));
            put(DEV_ID, new Types(ValueType.STRING).withAccess(AccessType.READ_ONLY));
            put(NAME, new Types(ValueType.STRING));
            put(DESCRIPTION, new Types(ValueType.STRING));
            put(PUBLIC_VERSION, new Types(ValueType.STRING));
        }};

        return new Schema(TABLE_APP_VERSIONS, attributes);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
