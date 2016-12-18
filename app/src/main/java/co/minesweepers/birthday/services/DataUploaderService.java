package co.minesweepers.birthday.services;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.UUID;

public class DataUploaderService {

    public interface ResponseHandler {
        void onSuccess(String objectPath);

        void onFailure();
    }


    public static void uploadData(final Uri uri, final ResponseHandler handler) throws FileNotFoundException {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final String randomisedFilename = UUID.randomUUID().toString() + uri.getLastPathSegment();
        final String objectPath = MemoryIdProvider.getId() + "/" + randomisedFilename;
        StorageReference objectRef = storageRef.child(objectPath);


        objectRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        handler.onSuccess(objectPath);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        handler.onFailure();
                    }
                });
    }
}