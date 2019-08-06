package wizardspace.kv;

import IxLambdaBackend.storage.S3Entity;
import com.amazonaws.services.s3.AmazonS3;
import wizardspace.client.S3Client;

public class S3KvEntity extends S3Entity<S3KvEntity> {
    private static final String bucketName = "wizardspace-data";

    public S3KvEntity(final String key, final String value) {
        super(bucketName, key, value);
    }

    public S3KvEntity(final String key) {
        super(bucketName, key);
    }

    @Override
    public AmazonS3 createS3Client() {
        return S3Client.get();
    }
}
