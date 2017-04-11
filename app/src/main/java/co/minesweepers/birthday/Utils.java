package co.minesweepers.birthday;

import android.net.Uri;
import android.util.Log;

import java.io.File;

public class Utils {
    private static final String TAG = "Utils";

    public static boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    private static boolean isUriCleanable(Uri uri) {
        return uri.getPath().contains(App.PACKAGE_NAME);
    }

    public static void cleanResource(Uri uri) {
        if(!isUriCleanable(uri)) {
            return;
        }
        File file = new File(uri.getPath());
        boolean success = file.delete();
        if(!success) {
            Log.e(TAG, "delete failed for -" + uri.getPath());
        }
    }
}
