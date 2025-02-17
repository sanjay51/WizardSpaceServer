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
import static wizardspace.app.common.AppConstants.*;

public class AppGroupEntity extends DDBEntity<AppGroupEntity> {

    public AppGroupEntity(final String primaryKeyValue) {
        super(primaryKeyValue);
    }

    public AppGroupEntity(final String primaryKeyValue, final double sortKeyValue) {
        super(primaryKeyValue, sortKeyValue);
    }

    public AppGroupEntity() {
        super();
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributes = new HashMap<String, Types>() {{
            put(APP_GROUP_ID, new Types(ValueType.STRING, IndexType.PRIMARY_KEY).withAccess(AccessType.RESTRICTED));
            put(RANK, new Types(ValueType.NUMBER, IndexType.SORT_KEY).withAccess(AccessType.RESTRICTED));

            put(APP_DATA, new Types(ValueType.MAP).withAccess(AccessType.READ_ONLY));
            put(CATEGORY, new Types(ValueType.STRING, IndexType.GSI_SORT_KEY));
            put(APP_ID, new Types(ValueType.STRING).withAccess(AccessType.READ_ONLY));
            put(CREATION_EPOCH, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
            put(LAST_UPDATED_EPOCH, new Types(ValueType.NUMBER).withAccess(AccessType.READ_ONLY));
            put(LAST_UPDATED_BY, new Types(ValueType.STRING).withAccess(AccessType.RESTRICTED));
        }};

        return new Schema(TABLE_APP_GROUP, attributes);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
