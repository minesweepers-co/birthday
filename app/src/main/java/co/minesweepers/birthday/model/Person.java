package co.minesweepers.birthday.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.minesweepers.birthday.services.DataUploaderService;

public class Person {

    private static final String TAG = "Person";
    private static final String KEY_PERSON_ID = "id";
    private static final String KEY_PERSON_NAME = "name";
    private static final String KEY_VIDEO = "videoPath";
    private static final String KEY_QUESTIONS = "questions";

    private final String mId;
    private String mName;
    private String videoPath;
    private List<Question> mQuestions;

    public Person() {
        this(UUID.randomUUID().toString());
    }

    private Person(String id) {
        mId = id;
        mQuestions = new ArrayList<>();
    }

    public String getId() {
        return mId;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public void addQuestion(@NonNull Question question) {
        mQuestions.add(question);
    }

    public void addVideo(Uri uri) {
        DataUploaderService.uploadData(uri, new DataUploaderService.ResponseHandler() {
            @Override
            public void onSuccess(String objectPath) {
                videoPath = objectPath;
            }

            @Override
            public void onProgress(long totalBytes, long bytesSent) {
                // show to UI
            }

            @Override
            public void onFailure() {
                // ignore for now
            }
        });
    }

    JSONObject serialize() {
        JSONObject personObj = new JSONObject();
        try {
            personObj.put(KEY_PERSON_ID, mId);
            personObj.put(KEY_PERSON_NAME, mName);
            personObj.put(KEY_VIDEO, videoPath);
            JSONArray questions = new JSONArray();
            for (Question question : mQuestions) {
                questions.put(question.serialize());
            }
            personObj.put(KEY_QUESTIONS, questions);
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR : " + e.getLocalizedMessage());
        }
        return personObj;
    }

    static @NonNull Person fromJsonObject(JSONObject jsonObject) throws JSONException {
        String personId = jsonObject.getString(KEY_PERSON_ID);
        Person person = new Person(personId);
        person.setName(jsonObject.getString(KEY_PERSON_NAME));
        person.videoPath = jsonObject.getString(KEY_VIDEO);
        JSONArray questionsArray = jsonObject.getJSONArray(KEY_QUESTIONS);
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionJson = questionsArray.getJSONObject(i);
            Question question = Question.fromJsonObject(questionJson);
            person.addQuestion(question);
        }
        return person;
    }
}
