package co.minesweepers.birthday.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.services.SharedPreferenceService;

public class GetRecipientActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameTxt;
    private ImageView mNameContinueBtn;
    private TextView mUploadImageText;
    private TextView mRecipientNameInfo;
    private ImageView mUploadImageView;
    private Button mButtonContinue;
    private Memory mMemory;

    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipient);
        mMemory = Memory.getInstance();
        mNameContinueBtn = (ImageView) findViewById(R.id.recipient_name_continue);
        mNameContinueBtn.bringToFront();
        mNameContinueBtn.setOnClickListener(this);
        mNameTxt = (EditText) findViewById(R.id.recipient_name);
        mRecipientNameInfo = (TextView) findViewById(R.id.recipient_name_info);
        mButtonContinue = (Button) findViewById(R.id.button_recipient_done);
        mButtonContinue.setOnClickListener(this);
        mUploadImageText = (TextView) findViewById(R.id.get_recipient_upload_pic_info);
        mUploadImageView = (ImageView) findViewById(R.id.get_recipient_upload_pic);
        mUploadImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recipient_name_continue:
                if (Utils.isEmpty(mNameTxt.getText().toString())) {
                    Toast.makeText(getApplicationContext(), R.string.recipient_name_empty, Toast.LENGTH_SHORT).show();
                } else {
                    closeKeyboard();

                    mUploadImageText.setVisibility(View.VISIBLE);
                    mUploadImageView.setVisibility(View.VISIBLE);
                    mNameContinueBtn.setVisibility(View.INVISIBLE);

                    mNameTxt.setFocusable(false);
                    mNameTxt.setClickable(false);
                }
                break;

            case R.id.get_recipient_upload_pic:
                showPictureChooser();
                break;

            case R.id.button_recipient_done:
                Intent viewMemoryIntent = new Intent(this, AddPeopleActivity.class);
                startActivity(viewMemoryIntent);
                break;
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showPictureChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.image_chooser_intent_title)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    final Uri imageUri = data.getData();
                    mMemory.setRecipient(mNameTxt.getText().toString(), imageUri, new Utils.GenericOperationListener() {
                        @Override
                        public void onSuccess() {
                            SharedPreferenceService.save(GetRecipientActivity.this, mMemory, true);
                            mUploadImageText.setVisibility(View.GONE);
                            mRecipientNameInfo.setVisibility(View.GONE);
                            mUploadImageView.setImageURI(imageUri);
                            mButtonContinue.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getApplicationContext(), R.string.generic_retry_error, Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }
    }
}
