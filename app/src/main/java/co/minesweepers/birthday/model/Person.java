package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
