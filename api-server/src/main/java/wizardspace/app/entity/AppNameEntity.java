package wizardspace.app.entity;

import IxLambdaBackend.storage.DDBEntity;
import IxLambdaBackend.storage.attribute.value.ValueType;
import IxLambdaBackend.storage.schema.IndexType;
import IxLambdaBackend.storage.schema.Schema;
import IxLambdaBackend.storage.schema.Types;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import wizardspace.client.DynamoDBClient;

import java.util.HashMap;
import java.util.Map;

import static wizardspace.app.common.AppConstants.*;

public class AppNameEntity extends DDBEntity<AppNameEntity> {
    public AppNameEntity(String primaryKeyValue) {
        super(primaryKeyValue);
    }

    @Override
    public Schema createSchema() {
        final Map<String, Types> attributes = new HashMap<String, Types>() {{
            put(APP_NAME, new Types(ValueType.STRING, IndexType.PRIMARY_KEY));
            put(APP_ID, new Types(ValueType.STRING));
        }};

        return new Schema(TABLE_APP_NAMES, attributes);
    }

    @Override
    public AmazonDynamoDB createDDBClient() {
        return DynamoDBClient.get();
    }
}
