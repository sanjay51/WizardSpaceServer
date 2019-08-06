package wizardspace.client;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Client {
    private static AmazonS3 s3;

    public static AmazonS3 get() {
        if (s3 == null) {
            s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();;
        }

        return s3;

    }
}
