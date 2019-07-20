package wizardspace.user.entity;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.IndexType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import wizardspace.client.DynamoDBClient;

import java.util.HashMap;
import java.util.Map;

import static IxLambdaBackend.storage.schema.AccessType.READ_ONLY;
import static IxLambdaBackend.storage.schema.AccessType.WRITE_ONLY;
import static wizardspace.Constants.*;

public class UserEntity extends DDBEntity<UserEntity> {
    private AmazonDynamoDB ddb;

    public UserEntity(final String primaryKeyValue) {
        super(primaryKeyValue);
    }

    private UserEntity(final String primaryKeyValue, final String gsiIndexName) {
        super(primaryKeyValue, null, gsiIndexName);
    }

    public static UserEntity newInstanceFromGSI(final String primaryKeyValue, final String gsiIndexName) {
        return new UserEntity(primaryKeyValue, gsiIndexName);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
            put(EMAIL, new Types(ValueType.STRING, IndexType.PRIMARY_KEY).withAccess(READ_ONLY));
            put(USER_ID, new Types(ValueType.STRING, IndexType.GSI_PRIMARY_KEY).withAccess(READ_ONLY));
            put(PASSWORD, new Types(ValueType.STRING).withAccess(WRITE_ONLY));
            put(FNAME, new Types(ValueType.STRING));
            put(LNAME, new Types(ValueType.STRING));
            put(ACCESS_LEVEL, new Types(ValueType.STRING).withAccess(READ_ONLY));
            put(ACCOUNT_STATUS, new Types(ValueType.STRING).withAccess(READ_ONLY));
            put(CREATION_EPOCH, new Types(ValueType.NUMBER).withAccess(READ_ONLY));
            put(LAST_UPDATED_EPOCH, new Types(ValueType.NUMBER).withAccess(READ_ONLY));
            put(LAST_UPDATED_BY, new Types(ValueType.STRING).withAccess(READ_ONLY));
        }};

        return new Schema(TABLE_USER, attributeTypesMap);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
