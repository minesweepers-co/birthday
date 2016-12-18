package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.minesweepers.birthday.Constants;

public class Person {

    private final String id;
    private String mName;
    private List<Question> mQuestions;

    public Person() {
        id = UUID.randomUUID().toString();
        mQuestions = new ArrayList<>();
    }

    public String getId() {
        return id;
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
            personObj.put(Constants.JSON_PERSON_NAME_KEY, mName);
            JSONArray questions = new JSONArray();
            for (Question question : mQuestions) {
                questions.put(question.serialize());
            }
            personObj.put(Constants.JSON_QUESTIONS_ARRAY_KEY, questions);
        } catch (JSONException e) {
            // ignore for now
        }
        return personObj;
    }


}
