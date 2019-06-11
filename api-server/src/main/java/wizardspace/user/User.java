package wizardspace.user;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.AttributeType;
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

    public User(String primaryKeyValue) {
        super(primaryKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributeTypesMap = new HashMap<String, Types>() {{
            put(EMAIL, new Types(AttributeType.PRIMARY_KEY, ValueType.STRING));
            put(USER_ID, new Types(AttributeType.REGULAR, ValueType.STRING));
            put(PASSWORD, new Types(AttributeType.REGULAR, ValueType.STRING, true));
            put(FNAME, new Types(AttributeType.REGULAR, ValueType.STRING));
            put(LNAME, new Types(AttributeType.REGULAR, ValueType.STRING));
            put(ACCESS_LEVEL, new Types(AttributeType.REGULAR, ValueType.STRING));
            put(ACCOUNT_STATUS, new Types(AttributeType.REGULAR, ValueType.STRING));
            put(CREATION_EPOCH, new Types(AttributeType.REGULAR, ValueType.NUMBER));
            put(LAST_UPDATED_EPOCH, new Types(AttributeType.REGULAR, ValueType.NUMBER));
            put(LAST_UPDATED_BY, new Types(AttributeType.REGULAR, ValueType.STRING, true));
        }};

        return new Schema(TABLE_USER, attributeTypesMap);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        if (this.ddb == null) this.ddb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

        return this.ddb;
    }
}
