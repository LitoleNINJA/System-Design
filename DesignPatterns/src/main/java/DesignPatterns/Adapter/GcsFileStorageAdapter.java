package DesignPatterns.Adapter;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GcsFileStorageAdapter implements FileStorage {
    private final GcsBlobStorage gcsBlobStorage;

    public GcsFileStorageAdapter(GcsBlobStorage gcsBlobStorage) {
        this.gcsBlobStorage = gcsBlobStorage;
    }

    @Override
    public void upload(String bucket, String key, byte[] data) {
        try {
            this.gcsBlobStorage.uploadBlob(uri(bucket, key), ByteBuffer.wrap(data));
        } catch (GcsBlobStorage.GcsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] download(String bucket, String key) {
        try {
            ByteBuffer data = this.gcsBlobStorage.downloadBlob(uri(bucket, key));
            byte[] result = new byte[data.remaining()];
            data.get(result);
            return result;
        } catch (GcsBlobStorage.GcsException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listKeys(String bucket, String prefix) {
        Iterable<URI> uris = this.gcsBlobStorage.findBlobs(uri(bucket, prefix));
        List<String> keys = new ArrayList<>();
        for (URI uri : uris) {
            keys.add(uri.getPath().substring(1));   // "/vacation.jpg" -> "vacation.jpg"
        }
        return keys;
    }

    @Override
    public void delete(String bucket, String key) {
        try {
            this.gcsBlobStorage.removeBlob(uri(bucket, key));
        } catch (GcsBlobStorage.GcsException e) {
            throw new RuntimeException(e);
        }
    }

    private URI uri(String bucket, String key) {
        return URI.create("gs://" + bucket + "/" + key);
    }
}
