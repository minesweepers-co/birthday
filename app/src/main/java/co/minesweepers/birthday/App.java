package co.minesweepers.birthday;

import android.app.Application;

public class App extends Application {

    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE_NAME = getApplicationContext().getPackageName();
    }
}
