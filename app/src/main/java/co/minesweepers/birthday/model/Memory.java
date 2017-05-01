package co.minesweepers.birthday.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import co.minesweepers.birthday.services.FirebaseDBService;
import co.minesweepers.birthday.Utils;

public class Memory {
    private static final String TAG = "Memory";
    private final String mId;
    private static Memory sInstance;
    private Map<String, Person> mPersons;
    private Recipient recipient;

    private static final String KEY_MEMORY_ID = "id";
    private static final String KEY_PEOPLE = "people";
    private static final String KEY_RECIPIENT = "recipient";

    private Memory() {
        this(UUID.randomUUID().toString());
    }

    private Memory(String id) {
        mId = id;
        // When creating a memory the ordering is important so we use a LinkedHashMap
        mPersons = new LinkedHashMap<>();
    }

    public static Memory getInstance() {
        if (sInstance == null) {
            sInstance = new Memory();
        }

        return sInstance;
    }

    public static void reset() {
        sInstance = null;
    }

    public void addPerson(@NonNull Person person) {
        mPersons.put(person.getId(), person);
    }

    public Person getPerson(@NonNull String id) {
        return mPersons.get(id);
    }

    public @NonNull List<Person> getAllPeople() {
        return new ArrayList<>(mPersons.values());
    }

    public String getId() {
        return mId;
    }

    public void setRecipient(final String name, Uri imageUri, @NonNull final Utils.GenericOperationListener listener) {
        FirebaseDBService.uploadData(imageUri, new FirebaseDBService.ResponseHandler() {
            @Override
            public void onSuccess(String objectPath) {
                recipient = new Recipient(name, objectPath);
                listener.onSuccess();
            }

            @Override
            public void onProgress(long totalBytes, long bytesSent) {
                // ignore
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }
        });
    }


    public void setPeopleOrderFromList(List<Person> people) {
        mPersons.clear();
        for (Person person : people) {
            addPerson(person);
        }
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_MEMORY_ID, mId);
            JSONArray personArray = new JSONArray();
            for (String personId : mPersons.keySet()) {
                Person person = getPerson(personId);
                personArray.put(person.serialize());
            }
            jsonObject.put(KEY_PEOPLE, personArray);
            if(recipient!=null) {
                jsonObject.put(KEY_RECIPIENT, recipient.serialize());
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR: " + e.getLocalizedMessage());
        }

        return jsonObject;
    }

    public static @NonNull Memory fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String memoryId = jsonObject.getString(KEY_MEMORY_ID);
        sInstance = new Memory(memoryId);

        JSONArray peopleArray = jsonObject.getJSONArray(KEY_PEOPLE);
        for (int i = 0; i < peopleArray.length(); i++) {
            JSONObject personJson = peopleArray.getJSONObject(i);
            Person person = Person.fromJsonObject(personJson);
            sInstance.addPerson(person);
        }

        sInstance.recipient = Recipient.fromJson(jsonObject.getJSONObject(KEY_RECIPIENT));
        return sInstance;
    }

    public void upload() {
        final String json = serialize().toString();
        FirebaseDBService.uploadJson(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d(TAG, "JSON Object uploaded for message id " + Memory.getInstance().getId());
                } else {
                    Log.e(TAG, "ERROR while uploading json" + json);
                }
            }
        });
    }
}
