package DesignPatterns.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * PROVIDED — DO NOT MODIFY.
 *
 * Stand-in for the AWS S3 SDK. Looks like real S3 client code: bucket+key
 * addressing, byte[] payloads, unchecked NoSuchElementException on missing
 * keys. The println lines exist so the demo can prove the adapter actually
 * delegated to the right backend — leave them in.
 */
public class LegacyS3Client {

    private final Map<String, Map<String, byte[]>> store = new HashMap<>();

    public void putObject(String bucket, String key, byte[] data) {
        System.out.printf("[S3] putObject(bucket=%s, key=%s, %d bytes)%n", bucket, key, data.length);
        store.computeIfAbsent(bucket, b -> new HashMap<>()).put(key, data);
    }

    public byte[] getObject(String bucket, String key) {
        System.out.printf("[S3] getObject(bucket=%s, key=%s)%n", bucket, key);
        Map<String, byte[]> b = store.get(bucket);
        if (b == null || !b.containsKey(key)) {
            throw new NoSuchElementException("S3 object not found: " + bucket + "/" + key);
        }
        return b.get(key);
    }

    public List<String> listKeys(String bucket, String prefix) {
        System.out.printf("[S3] listKeys(bucket=%s, prefix=%s)%n", bucket, prefix);
        Map<String, byte[]> b = store.get(bucket);
        if (b == null) return List.of();
        return b.keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .sorted()
                .toList();
    }

    public void deleteObject(String bucket, String key) {
        System.out.printf("[S3] deleteObject(bucket=%s, key=%s)%n", bucket, key);
        Map<String, byte[]> b = store.get(bucket);
        if (b != null) b.remove(key);
    }
}
