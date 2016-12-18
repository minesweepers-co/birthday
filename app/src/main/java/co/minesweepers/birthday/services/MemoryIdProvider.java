package co.minesweepers.birthday.services;

import java.util.UUID;

public class MemoryIdProvider {

    static String id = null;

    static String getId() {
        if(id == null){
            id = UUID.randomUUID().toString();
        }
        return id;
    }

}
