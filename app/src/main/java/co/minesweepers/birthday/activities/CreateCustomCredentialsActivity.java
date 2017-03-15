package co.minesweepers.birthday.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Memory;

public class CreateCustomCredentialsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonGenerateLink;
    private static final String HOSTNAME = "https://www.minesweepers.co";
    private static final String MEMORY_PATH = "/memory";
    private static final String URI_LABEL = "URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_credentials);
        mButtonGenerateLink = (Button) findViewById(R.id.button_generate_link);
        mButtonGenerateLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_generate_link:
                Memory.getInstance().upload();
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                Uri copyUri = Uri.parse(HOSTNAME + MEMORY_PATH + "/" + Memory.getInstance().getId());
                ClipData clip = ClipData.newUri(getContentResolver(), URI_LABEL, copyUri);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, R.string.link_copied_toast_msg, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
