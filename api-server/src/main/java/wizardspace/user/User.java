package wizardspace.user;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.IndexType;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import java.util.HashMap;
import java.util.Map;

import static wizardspace.user.Constants.*;

public class User extends DDBEntity<User> {
    private AmazonDynamoDB ddb;

    public User(final String primaryKeyValue) {
        super(primaryKeyValue);
    }

    private User(final String primaryKeyValue, final String gsiIndexName) {
        super(primaryKeyValue, null, gsiIndexName);
    }

    public static User newInstanceFromGSI(final String primaryKeyValue, final String gsiIndexName) {
        return new User(primaryKeyValue, gsiIndexName);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
            put(EMAIL, new Types(ValueType.STRING, IndexType.PRIMARY_KEY));
            put(USER_ID, new Types(ValueType.STRING, IndexType.GSI_PRIMARY_KEY));
            put(PASSWORD, new Types(ValueType.STRING).markConfidential());
            put(FNAME, new Types(ValueType.STRING));
            put(LNAME, new Types(ValueType.STRING));
            put(ACCESS_LEVEL, new Types(ValueType.STRING));
            put(ACCOUNT_STATUS, new Types(ValueType.STRING));
            put(CREATION_EPOCH, new Types(ValueType.NUMBER));
            put(LAST_UPDATED_EPOCH, new Types(ValueType.NUMBER));
            put(LAST_UPDATED_BY, new Types(ValueType.STRING).markConfidential());
        }};

        return new Schema(TABLE_USER, attributeTypesMap);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        if (this.ddb == null) this.ddb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        return this.ddb;
    }
}
