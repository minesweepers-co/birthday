package co.minesweepers.birthday;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

import static co.minesweepers.birthday.App.FIREBASE_ANALYTICS;

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

    public static void logEvent(String event, String eventId) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventId);
        FIREBASE_ANALYTICS.logEvent(event, bundle);
    }

    public interface GenericOperationListener {
        public void onSuccess();
        public void onFailure();
    }
}
