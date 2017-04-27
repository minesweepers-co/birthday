package co.minesweepers.birthday.model;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipient {

    private String name;
    private String imagePath;
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_PATH = "imagePath";
    private static final String TAG = "Recipient";

    public Recipient(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_NAME, name);
            jsonObject.put(KEY_IMAGE_PATH, imagePath);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException when serializing Recipient - " + e.getLocalizedMessage());
        }
        return jsonObject;
    }

    public static @NonNull Recipient fromJson(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString(KEY_NAME);
        String imagePath = jsonObject.getString(KEY_IMAGE_PATH);
        return new Recipient(name, imagePath);
    }
}