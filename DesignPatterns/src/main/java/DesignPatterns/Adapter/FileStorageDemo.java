package DesignPatterns.Adapter;

import java.util.List;

/**
 * Test driver / demo for the Multi-Cloud File Storage exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting classes in this same
 * folder/package so this file compiles and runs:
 *
 *   1. FileStorage.java             — interface with upload, download,
 *                                     listKeys, delete (signatures match
 *                                     how testStorage() below calls them)
 *   2. S3FileStorageAdapter.java    — implements FileStorage,
 *                                     ctor(LegacyS3Client client)
 *   3. GcsFileStorageAdapter.java   — implements FileStorage,
 *                                     ctor(GcsBlobStorage storage)
 *
 * Until all three exist you'll get "cannot find symbol" errors — each error
 * tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of Adapter: the SAME `testStorage(...)` method
 * runs against TWO completely different SDKs (S3-style vs GCS-style) and
 * produces the SAME application-level output. The adapters absorb every
 * difference — bucket+key vs URI, byte[] vs ByteBuffer, List vs Iterable,
 * unchecked vs checked exceptions.
 *
 * Note: FileStorageDemo never imports LegacyS3Client or GcsBlobStorage
 * directly inside testStorage(). The references appear only at the point
 * where each adapter is constructed in main(). Once wrapped, the foreign
 * SDK is invisible to the rest of the code.
 */
public class FileStorageDemo {

    public static void main(String[] args) {
        FileStorage s3  = new S3FileStorageAdapter(new LegacyS3Client());
        FileStorage gcs = new GcsFileStorageAdapter(new GcsBlobStorage());

        System.out.println("=== Testing S3 backend ===");
        testStorage(s3);

        System.out.println("\n=== Testing GCS backend ===");
        testStorage(gcs);
    }

    /** Note: this method only ever sees `FileStorage`. It has no idea what's behind it. */
    private static void testStorage(FileStorage storage) {
        storage.upload("photos", "vacation.jpg", "PHOTO_BYTES".getBytes());
        storage.upload("photos", "selfie.jpg",   "SELFIE_BYTES".getBytes());
        System.out.println("Uploaded 2 files");

        byte[] data = storage.download("photos", "vacation.jpg");
        System.out.println("Downloaded vacation.jpg: " + new String(data));

        List<String> keys = storage.listKeys("photos", "v");
        System.out.println("Keys with prefix 'v': " + keys);

        storage.delete("photos", "vacation.jpg");
        System.out.println("Deleted vacation.jpg");

        keys = storage.listKeys("photos", "");
        System.out.println("Remaining: " + keys);
    }
}
