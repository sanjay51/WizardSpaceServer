package wizardspace.client;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDBClient {
    private static AmazonDynamoDB ddb;

    public static AmazonDynamoDB get() {
        if (ddb == null) {
            ddb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        }

        return ddb;

    }
}
