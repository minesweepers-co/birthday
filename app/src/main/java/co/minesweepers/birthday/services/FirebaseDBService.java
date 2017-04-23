package co.minesweepers.birthday.services;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.File;
import java.util.UUID;

import co.minesweepers.birthday.model.Memory;

import static co.minesweepers.birthday.App.APP_STORAGE_DIRECTORY;

public class FirebaseDBService {

    private static final String TAG = "FirebaseDBService";
    private static final String FIREBASE_MEMORIES_DIRECTORY = "memories";

    public interface ResponseHandler {
        void onSuccess(String objectPath);

        void onProgress(long totalBytes, long bytesSent);

        void onFailure();
    }

    public interface MediaUrlListener {
        void onSuccess(Uri uri);

        void onFailure();
    }

    public interface MediaDownloadStatusListener {
        void onSuccess(Uri uri);

        void onProgress(float percentageDone);

        void onFailure();
    }

    public interface MemoryDownloadListener {
        void onSuccess(Memory memory);

        void onFailure();
    }

    public static void uploadData(@NonNull final Uri uri, @NonNull final ResponseHandler handler) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final String randomisedFilename = UUID.randomUUID().toString() + uri.getLastPathSegment();
        final String objectPath = Memory.getInstance().getId() + "/" + randomisedFilename;
        StorageReference objectRef = storageRef.child(objectPath);


        objectRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        handler.onSuccess(objectPath);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        handler.onProgress(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        handler.onFailure();
                    }
                });
    }

    public static void getUrlForMedia(@NonNull final String objectPath, @NonNull final MediaUrlListener listener) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(objectPath).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            listener.onSuccess(uri);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.onFailure();
                                        }
                                    });
    }

    public static void downloadMedia(@NonNull final String objectPath, @NonNull final MediaDownloadStatusListener listener) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child(objectPath);

        final File file = new File(APP_STORAGE_DIRECTORY, fileRef.getName());
        fileRef.getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        listener.onSuccess(Uri.fromFile(file));
                    }
                })
                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        listener.onProgress((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                    }
                });
    }

    public static void getMemoryForId(@NonNull final String memoryId, @NonNull final MemoryDownloadListener listener) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String json = (String) dataSnapshot.getValue();
                    Memory memory = Memory.fromJson(json);
                    listener.onSuccess(memory);
                } catch (JSONException e) {
                    listener.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        };

        DatabaseReference mMemoryReference = FirebaseDatabase.getInstance().getReference().child(FIREBASE_MEMORIES_DIRECTORY).child(memoryId);
        mMemoryReference.addListenerForSingleValueEvent(eventListener);
    }

    public static void uploadJson(@NonNull final String json, @Nullable final DatabaseReference.CompletionListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FIREBASE_MEMORIES_DIRECTORY)
                .child(Memory.getInstance().getId())
                .setValue(json, listener);
    }
}