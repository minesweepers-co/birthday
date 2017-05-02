package co.minesweepers.birthday.services;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;

import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.model.Memory;

public class SharedPreferenceService {

    private static final String SHARED_PREFERENCE_NAME_CREATE_MEMORY= "create_memory";
    private static final String SHARED_PREFERENCE_NAME_VIEW_MEMORY= "view_memory";
    private static final String SHARED_PREFERENCE_KEY_MEMORY= "memory";

    public static void save(Context context, Memory memory, boolean isCreateMode) {
        final String json = memory.serialize().toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getSharedPreferenceName(isCreateMode), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCE_KEY_MEMORY, json);
        editor.apply();
    }

    public static void restore(Context context, boolean isCreateMode) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getSharedPreferenceName(isCreateMode), Context.MODE_PRIVATE);
        String savedJson = sharedPreferences.getString(SHARED_PREFERENCE_KEY_MEMORY, null);
        Memory.fromJson(savedJson);
    }

    private static String getSharedPreferenceName(boolean isCreateMode) {
        return isCreateMode ? SHARED_PREFERENCE_NAME_CREATE_MEMORY : SHARED_PREFERENCE_NAME_VIEW_MEMORY;
    }

    public static boolean isViewableMemoryAvailable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME_VIEW_MEMORY, Context.MODE_PRIVATE);
        String savedJson = sharedPreferences.getString(SHARED_PREFERENCE_KEY_MEMORY, null);
        return !Utils.isEmpty(savedJson);
    }
}
