package co.minesweepers.birthday.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Memory;

public class CreateCustomCredentialsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CustomCredsActivity";
    private Button mButtonGenerateLink;
    private static final String HTTPS = "https";
    private static final String HOSTNAME = "www.minesweepers.co";
    private static final String MEMORY_PATH = "memory";
    private static final String URI_LABEL = "URI";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_credentials);
        mButtonGenerateLink = (Button) findViewById(R.id.button_generate_link);
        mButtonGenerateLink.setOnClickListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_generate_link:
                Memory.getInstance().upload();
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                Uri copyUri = new Uri.Builder()
                        .scheme(HTTPS)
                        .authority(HOSTNAME)
                        .appendEncodedPath(MEMORY_PATH)
                        .appendQueryParameter(Constants.MEMORY_ID_QUERY_PARAM, Memory.getInstance().getId())
                        .build();
                Log.d(TAG, "Deeplink uri = " + copyUri.toString());
                ClipData clip = ClipData.newUri(getContentResolver(), URI_LABEL, copyUri);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, R.string.link_copied_toast_msg, Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.EVENT_ID_FINISH_CREATING_MEMORY);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                break;
        }
    }
}
