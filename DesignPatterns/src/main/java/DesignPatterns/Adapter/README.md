# Exercise — Multi-Cloud File Storage

> Adapter pattern · LLD practice
> Real-world version of Adapter. Every backend that runs on multi-cloud (S3 + GCS + sometimes Azure) needs this. Forget the cliché "shape adapter" or "power adapter" — this is what Adapter actually looks like in production.

---

## Problem Statement

Your app stores user-uploaded files. It currently uses **AWS S3**, but the company is going multi-cloud and you need to support **Google Cloud Storage** too. New code shouldn't have to know which provider is underneath — it should just call `storage.upload(...)` and `storage.download(...)` and let the right thing happen.

You can't modify the cloud SDKs (they're third-party JARs). You only control:
- the `FileStorage` interface — your unified application API
- the **adapter** classes that wrap each SDK

That's the **Adapter** pattern: foreign API → your API, via composition.

---

## What's Already Provided (in this folder)

- **`LegacyS3Client.java`** — fake S3-style SDK. Methods: `putObject`, `getObject`, `listKeys`, `deleteObject`. Throws `NoSuchElementException` on missing keys. Uses `byte[]` and `String bucket, String key`.
- **`GcsBlobStorage.java`** — fake GCS-style SDK. Methods: `uploadBlob`, `downloadBlob`, `findBlobs`, `removeBlob`. Throws **checked** `GcsException`. Uses `URI` (e.g. `gs://photos/vacation.jpg`) and `ByteBuffer`. Returns `Iterable<URI>` from list.
- **`FileStorageDemo.java`** — the test driver. Uses only your unified `FileStorage` interface. Runs the *exact same test code* against both adapters.

You **must not modify** the three files above — they represent third-party code outside your control. That constraint is the whole reason Adapter exists.

---

## Your Job

Implement three files:

1. **`FileStorage.java`** — the unified Target interface:
   ```java
   public interface FileStorage {
       void          upload   (String bucket, String key, byte[] data);
       byte[]        download (String bucket, String key);
       List<String>  listKeys (String bucket, String prefix);
       void          delete   (String bucket, String key);
   }
   ```

2. **`S3FileStorageAdapter.java`** — implements `FileStorage`. In its constructor, takes a `LegacyS3Client` and stores it as a field. Each method translates the call to the underlying S3 client. This adapter is *easy* — the SDK is similar in shape; you're mostly forwarding.

3. **`GcsFileStorageAdapter.java`** — implements `FileStorage`. In its constructor, takes a `GcsBlobStorage` and stores it as a field. This adapter is *the interesting one* — every method has real translation work:
   - `(bucket, key)` → `URI.create("gs://" + bucket + "/" + key)`
   - `byte[]` → `ByteBuffer.wrap(bytes)`
   - `ByteBuffer` returned → copy bytes back into a `byte[]`
   - `Iterable<URI>` → `List<String>` (extract just the key portion of each URI)
   - **Catch the checked `GcsException`** and rethrow as an unchecked `RuntimeException`. The application interface uses unchecked exceptions; the foreign SDK uses checked. The adapter absorbs that difference.

---

## Class Hints

```
// PROVIDED — do not modify:
class LegacyS3Client { putObject, getObject, listKeys, deleteObject }    // byte[], String bucket/key
class GcsBlobStorage { uploadBlob, downloadBlob, findBlobs, removeBlob } // URI, ByteBuffer, throws GcsException

// YOUR JOB:
interface FileStorage { upload, download, listKeys, delete }            // the Target

class S3FileStorageAdapter  implements FileStorage {
    private final LegacyS3Client client;
    // ctor stores the client; each method delegates with mostly the same params
}

class GcsFileStorageAdapter implements FileStorage {
    private final GcsBlobStorage storage;
    // ctor stores the storage; each method translates params, return types,
    // and exceptions before/after delegating
}
```

> 💡 **The structural fingerprint of Adapter:** the adapter `implements` your `Target` and **holds** an instance of the `Adaptee` as a field. Composition, not inheritance — same fingerprint as Decorator, but the *wrapped type is foreign* (a different interface than the one you implement).

---

## Expected Output

```
=== Testing S3 backend ===
[S3] putObject(bucket=photos, key=vacation.jpg, 11 bytes)
[S3] putObject(bucket=photos, key=selfie.jpg, 11 bytes)
Uploaded 2 files
[S3] getObject(bucket=photos, key=vacation.jpg)
Downloaded vacation.jpg: PHOTO_BYTES
[S3] listKeys(bucket=photos, prefix=v)
Keys with prefix 'v': [vacation.jpg]
[S3] deleteObject(bucket=photos, key=vacation.jpg)
Deleted vacation.jpg
[S3] listKeys(bucket=photos, prefix=)
Remaining: [selfie.jpg]

=== Testing GCS backend ===
[GCS] uploadBlob(uri=gs://photos/vacation.jpg, 11 bytes)
[GCS] uploadBlob(uri=gs://photos/selfie.jpg, 11 bytes)
Uploaded 2 files
[GCS] downloadBlob(uri=gs://photos/vacation.jpg)
Downloaded vacation.jpg: PHOTO_BYTES
[GCS] findBlobs(prefix=gs://photos/v)
Keys with prefix 'v': [vacation.jpg]
[GCS] removeBlob(uri=gs://photos/vacation.jpg)
Deleted vacation.jpg
[GCS] findBlobs(prefix=gs://photos/)
Remaining: [selfie.jpg]
```

The **`[S3]`** and **`[GCS]`** lines are printed by the *foreign SDKs themselves* (provided code), proving each adapter is genuinely calling the right backend. The rest of each test block is identical because the application code (`FileStorageDemo.testStorage(...)`) doesn't know or care which backend it has.

---

## What the Interviewer is Looking For

- **Adapter implements the Target, holds the Adaptee as a field.** Composition, never inheritance from a foreign class.
- **Translation lives in the adapter.** All bridging — type conversion, parameter reshaping, exception wrapping — is in the adapter, not in application code or the SDK.
- **Adaptees are unmodified.** You don't reach into the SDK to fix it; you wrap it.
- **Open/Closed:** adding `AzureFileStorageAdapter` tomorrow is one new file; nothing else changes.
- **Adapter vs Decorator:** *both* implement an interface and *both* hold a wrapped object. The difference: a Decorator wraps an object of the **same** interface and adds behavior; an Adapter wraps an object of a **different** interface and bridges to yours. If the wrapped type matches the implemented type → Decorator. If they differ → Adapter.

---

## Adapter vs Things It Gets Confused With

| vs | Distinction |
|---|---|
| **Adapter vs Decorator** | Adapter changes the *interface*. Decorator preserves it. `S3FileStorageAdapter` wraps `LegacyS3Client` (different shape) → Adapter. `LoggingMiddleware` wraps `HttpHandler` (same shape) → Decorator. |
| **Adapter vs Facade** | Facade simplifies a *complex subsystem of multiple classes* behind one entry point. Adapter bridges *one* class with the wrong interface. If you're combining multiple SDKs into one method → Facade. If you're translating one SDK → Adapter. |
| **Adapter vs Bridge** | Bridge is a deliberate two-axis decoupling designed up front (`Shape` × `Renderer`). Adapter is reactive — you didn't design the foreign API; you're stuck with it. |
| **Adapter vs Proxy** | Proxy controls *access* to an object of the same type (lazy load, remote, security). Adapter changes the *type*. |

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. Read `LegacyS3Client.java` and `GcsBlobStorage.java`. Understand each method's signature, params, return type, and exception behavior.
2. Read `FileStorageDemo.java`. Note exactly what `FileStorage` methods it calls — that's your interface contract.
3. Write `FileStorage.java` — the Target interface.
4. Write `S3FileStorageAdapter.java` — the easy one. Mostly forwarding with method renames.
5. ✅ **Run with just S3** — comment out the GCS block in the demo. Verify it works end-to-end first.
6. Write `GcsFileStorageAdapter.java` — the meaty translation work:
   - `bucket + "/" + key` → `URI` and back. A small private helper `private URI uri(String bucket, String key) { return URI.create("gs://" + bucket + "/" + key); }` cleans this up.
   - `ByteBuffer` ↔ `byte[]`: see hints below.
   - Catch `GcsException`, throw `RuntimeException(e)` — the application can't handle a checked exception it never declared.
7. Uncomment the GCS block in the demo. Run again. Both blocks should produce identical application-level output.

### Two ByteBuffer gotchas that cost candidates time

```java
// ByteBuffer ← byte[]:
ByteBuffer.wrap(bytes)                              // simple, works fine

// ByteBuffer → byte[]:
byte[] result = new byte[buffer.remaining()];        // size by REMAINING, not capacity
buffer.get(result);                                  // reads from current position
// Note: this CONSUMES the buffer. If you need to read it twice, use buffer.duplicate() first.
```

### URI parsing for `listKeys`

Each URI in `findBlobs` looks like `gs://photos/vacation.jpg`. To extract `vacation.jpg`:
```java
uri.getPath().substring(1)   // ".getPath()" returns "/vacation.jpg"; strip the leading "/"
```

---

## Files in this Exercise

| File | Status | Role |
|------|--------|------|
| `LegacyS3Client.java`         | ✅ provided | The first Adaptee (foreign SDK, can't modify) |
| `GcsBlobStorage.java`         | ✅ provided | The second Adaptee (foreign SDK, awkwardly different) |
| `FileStorageDemo.java`        | ✅ provided | Client / `main` — the test contract |
| `FileStorage.java`            | 🛠 your job | The Target interface |
| `S3FileStorageAdapter.java`   | 🛠 your job | Adapter for S3 (easy) |
| `GcsFileStorageAdapter.java`  | 🛠 your job | Adapter for GCS (meaty translation) |
