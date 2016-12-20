package co.minesweepers.birthday.services;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.UUID;

import co.minesweepers.birthday.model.Memory;

public class DataUploaderService {

    private static final String TAG = "DataUploaderService";
    private static final String FIREBASE_MEMORIES_DIRECTORY = "memories";

    public interface ResponseHandler {
        void onSuccess(String objectPath);

        void onProgress(long totalBytes, long bytesSent);

        void onFailure();
    }


    public static void uploadData(@NonNull final Uri uri, @Nullable final ResponseHandler handler) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final String randomisedFilename = UUID.randomUUID().toString() + uri.getLastPathSegment();
        final String objectPath = Memory.getInstance().getId() + "/" + randomisedFilename;
        StorageReference objectRef = storageRef.child(objectPath);


        objectRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (handler != null) {
                            handler.onSuccess(objectPath);
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        if (handler != null) {
                            handler.onProgress(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        if (handler != null) {
                            handler.onFailure();
                        }
                    }
                });
    }

    public static void uploadJson(@NonNull final String json, @Nullable final DatabaseReference.CompletionListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FIREBASE_MEMORIES_DIRECTORY)
                .child(Memory.getInstance().getId())
                .setValue(json, listener);
    }
}