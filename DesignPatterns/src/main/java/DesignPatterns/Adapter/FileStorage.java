package DesignPatterns.Adapter;

import java.util.List;

public interface FileStorage {
    void upload(String bucket, String key, byte[] data);

    byte[] download(String bucket, String key);

    List<String> listKeys(String bucket, String prefix);

    void delete(String bucket, String key);
}
