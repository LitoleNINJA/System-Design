package DesignPatterns.Adapter;

import java.util.List;

public class S3FileStorageAdapter implements FileStorage {
    private final LegacyS3Client legacyS3Client;

    public S3FileStorageAdapter(LegacyS3Client legacyS3Client) {
        this.legacyS3Client = legacyS3Client;
    }

    @Override
    public void upload(String bucket, String key, byte[] data) {
        this.legacyS3Client.putObject(bucket, key, data);
    }

    @Override
    public byte[] download(String bucket, String key) {
        return this.legacyS3Client.getObject(bucket, key);
    }

    @Override
    public List<String> listKeys(String bucket, String prefix) {
        return this.legacyS3Client.listKeys(bucket, prefix);
    }

    @Override
    public void delete(String bucket, String key) {
        this.legacyS3Client.deleteObject(bucket, key);
    }
}
