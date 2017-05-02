package co.minesweepers.birthday;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

public class App extends Application {

    public static String PACKAGE_NAME;
    public static File APP_STORAGE_DIRECTORY;
    public static FirebaseAnalytics FIREBASE_ANALYTICS;

    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        APP_STORAGE_DIRECTORY = getExternalFilesDir(null);
        FIREBASE_ANALYTICS = FirebaseAnalytics.getInstance(getApplicationContext());
    }
}
