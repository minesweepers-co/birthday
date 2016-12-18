package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person {

    private static final String KEY_PERSON_ID = "id";
    private static final String KEY_PERSON_NAME = "name";
    private static final String KEY_QUESTIONS = "questions";

    private final String mId;
    private String mName;
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

    public JSONObject serialize() {
        JSONObject personObj = new JSONObject();
        try {
            personObj.put(KEY_PERSON_ID, mId);
            personObj.put(KEY_PERSON_NAME, mName);
            JSONArray questions = new JSONArray();
            for (Question question : mQuestions) {
                questions.put(question.serialize());
            }
            personObj.put(KEY_QUESTIONS, questions);
        } catch (JSONException e) {
            // ignore for now
        }
        return personObj;
    }

    static @NonNull Person fromJsonObject(JSONObject jsonObject) throws JSONException {
        String personId = jsonObject.getString(KEY_PERSON_ID);
        Person person = new Person(personId);
        person.setName(jsonObject.getString(KEY_PERSON_NAME));
        JSONArray questionsArray = jsonObject.getJSONArray(KEY_QUESTIONS);
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionJson = questionsArray.getJSONObject(i);
            Question question = Question.fromJsonObject(questionJson);
            person.addQuestion(question);
        }
        return person;
    }
}
