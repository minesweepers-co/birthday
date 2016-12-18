package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Memory {
    private final String id;
    private static Memory sInstance;
    private Map<String, Person> mPersons;

    private Memory() {
        id = UUID.randomUUID().toString();
        mPersons = new HashMap<>();
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
        return id;
    }
}
