package wizardspace.kv;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.IndexType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import wizardspace.client.DynamoDBClient;

import java.util.HashMap;
import java.util.Map;

import static IxLambdaBackend.storage.schema.AccessType.*;
import static wizardspace.Constants.*;

public class KvEntity extends DDBEntity<KvEntity> {
    private AmazonDynamoDB ddb;

    public KvEntity(final String primaryKeyValue, final String sortKeyValue) {
        super(primaryKeyValue, sortKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
            put(WZ_DOMAIN, new Types(ValueType.STRING, IndexType.PRIMARY_KEY).withAccess(READ_ONLY));
            put(WZ_KEY, new Types(ValueType.STRING, IndexType.SORT_KEY).withAccess(READ_ONLY));
            put(VALUE, new Types(ValueType.STRING).withAccess(READ_WRITE));
            put(USER_ID, new Types(ValueType.STRING));
            put(CREATION_EPOCH, new Types(ValueType.NUMBER).withAccess(READ_ONLY));
            put(LAST_UPDATED_EPOCH, new Types(ValueType.NUMBER).withAccess(READ_ONLY));
            put(LAST_UPDATED_BY, new Types(ValueType.STRING).withAccess(READ_ONLY));
        }};

        return new Schema(TABLE_KV, attributeTypesMap);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
