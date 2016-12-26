package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import co.minesweepers.birthday.services.DataUploaderService;

public class Memory {
    private static final String TAG = "Memory";
    private final String mId;
    private static Memory sInstance;
    private Map<String, Person> mPersons;

    private static final String KEY_MEMORY_ID = "id";
    private static final String KEY_PEOPLE = "people";

    private Memory() {
        mId = UUID.randomUUID().toString();
        mPersons = new HashMap<>();
    }

    private Memory(String id) {
        mId = id;
        mPersons = new LinkedHashMap<>();
    }

    public static Memory getInstance() {
        if (sInstance == null) {
            sInstance = new Memory();
        }

        return sInstance;
    }

    public void addPerson(@NonNull Person person) {
        mPersons.put(person.getId(), person);
    }

    public Person getPerson(@NonNull String id) {
        return mPersons.get(id);
    }

    public String getId() {
        return mId;
    }

    private JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_MEMORY_ID, mId);
            JSONArray personArray = new JSONArray();
            for (String personId : mPersons.keySet()) {
                Person person = getPerson(personId);
                personArray.put(person.serialize());
            }
            jsonObject.put(KEY_PEOPLE, personArray);
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR: " + e.getLocalizedMessage());
        }

        return jsonObject;
    }

    public static @NonNull Memory fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String memoryId = jsonObject.getString(KEY_MEMORY_ID);
        Memory memory = new Memory(memoryId);
        JSONArray peopleArray = jsonObject.getJSONArray(KEY_PEOPLE);
        for (int i = 0; i < peopleArray.length(); i++) {
            JSONObject personJson = peopleArray.getJSONObject(i);
            Person person = Person.fromJsonObject(personJson);
            memory.addPerson(person);
        }

        return memory;
    }

    public void upload() {
        final String json = serialize().toString();
        DataUploaderService.uploadJson(json, new DatabaseReference.CompletionListener() {
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
