package DesignPatterns.Adapter;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * PROVIDED — DO NOT MODIFY.
 *
 * Stand-in for the Google Cloud Storage SDK. Deliberately styled differently
 * from the S3 SDK so the adapter actually has translation work:
 *
 *   - Uses URIs (gs://bucket/key) instead of (bucket, key) string pairs.
 *   - Uses ByteBuffer instead of byte[].
 *   - Returns Iterable<URI> from findBlobs (not a List).
 *   - Throws CHECKED GcsException — your unified API uses unchecked exceptions,
 *     so the adapter must catch+rethrow.
 *
 * Leave the println lines as-is — the demo uses them to verify the adapter
 * actually called this backend.
 */
public class GcsBlobStorage {

    private final Map<String, ByteBuffer> blobs = new HashMap<>();

    public void uploadBlob(URI gcsUri, ByteBuffer data) throws GcsException {
        if (gcsUri == null || data == null) {
            throw new GcsException("uploadBlob: null argument");
        }
        System.out.printf("[GCS] uploadBlob(uri=%s, %d bytes)%n", gcsUri, data.remaining());
        // store a defensive copy so caller mutations don't affect us
        ByteBuffer copy = ByteBuffer.allocate(data.remaining()).put(data.duplicate()).flip();
        blobs.put(gcsUri.toString(), copy);
    }

    public ByteBuffer downloadBlob(URI gcsUri) throws GcsException {
        System.out.printf("[GCS] downloadBlob(uri=%s)%n", gcsUri);
        ByteBuffer b = blobs.get(gcsUri.toString());
        if (b == null) {
            throw new GcsException("blob not found: " + gcsUri);
        }
        return b.duplicate();   // duplicate so caller's reads don't move our position
    }

    public Iterable<URI> findBlobs(URI prefix) {
        System.out.printf("[GCS] findBlobs(prefix=%s)%n", prefix);
        return blobs.keySet().stream()
                .filter(s -> s.startsWith(prefix.toString()))
                .sorted()
                .map(URI::create)
                .toList();
    }

    public void removeBlob(URI gcsUri) throws GcsException {
        System.out.printf("[GCS] removeBlob(uri=%s)%n", gcsUri);
        if (!blobs.containsKey(gcsUri.toString())) {
            throw new GcsException("removeBlob: blob not found: " + gcsUri);
        }
        blobs.remove(gcsUri.toString());
    }

    /** Checked exception type — adapter must catch and rethrow as unchecked. */
    public static class GcsException extends Exception {
        public GcsException(String message) { super(message); }
    }
}
