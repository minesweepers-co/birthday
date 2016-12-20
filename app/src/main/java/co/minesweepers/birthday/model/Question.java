package co.minesweepers.birthday.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import co.minesweepers.birthday.services.DataUploaderService;

public class Question {
    private static final String TAG = "Question";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_OPTION1 = "option1";
    private static final String KEY_OPTION2 = "option2";
    private static final String KEY_OPTION3 = "option3";
    private static final String KEY_OPTION4 = "option4";
    private static final String KEY_CORRECT_OPTION = "correctOption";
    private static final String KEY_AUDIO_PATH = "audioPath";

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;
    private String audioUri;

    private void addAudio(Uri uri) {
        DataUploaderService.uploadData(uri, new DataUploaderService.ResponseHandler() {
            @Override
            public void onSuccess(String objectPath) {
                audioUri = objectPath;
            }

            @Override
            public void onProgress(long totalBytes, long bytesSent) {
                // show to UI
            }

            @Override
            public void onFailure() {
                // ignore for now
            }
        });
    }

    JSONObject serialize() {
        JSONObject questionObj = new JSONObject();
        try {
            // TODO : don't send stuff if its null
            questionObj.put(KEY_QUESTION, question);
            questionObj.put(KEY_OPTION1, option1);
            questionObj.put(KEY_OPTION2, option2);
            questionObj.put(KEY_OPTION3, option3);
            questionObj.put(KEY_OPTION4, option4);
            questionObj.put(KEY_CORRECT_OPTION, correctOption);
            questionObj.put(KEY_AUDIO_PATH, audioUri);
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR : " + e.getLocalizedMessage());
        }
        return questionObj;
    }

    static @NonNull Question fromJsonObject(JSONObject questionJson) throws JSONException {
        Builder builder = new Builder()
                .setQuestion(questionJson.getString(KEY_QUESTION))
                .setOption1(questionJson.getString(KEY_OPTION1))
                .setOption2(questionJson.getString(KEY_OPTION2));

        if (questionJson.has(KEY_OPTION3)) {
            builder.setOption3(questionJson.getString(KEY_OPTION3));
        }

        if (questionJson.has(KEY_OPTION4)) {
            builder.setOption3(questionJson.getString(KEY_OPTION4));
        }

        builder.setCorrectOption(questionJson.getInt(KEY_CORRECT_OPTION));
        return builder.question();
    }

    public static class Builder {
        private Question mQuestionObject;

        public Builder() {
            mQuestionObject = new Question();
        }

        public Builder setQuestion(@NonNull String question) {
            mQuestionObject.question = question;
            return this;
        }

        public Builder setOption1(@NonNull String option1) {
            mQuestionObject.option1 = option1;
            return this;
        }

        public Builder setOption2(@NonNull String option2) {
            mQuestionObject.option2 = option2;
            return this;
        }

        public Builder setOption3(String option3) {
            mQuestionObject.option3 = option3;
            return this;
        }

        public Builder setOption4(String option4) {
            mQuestionObject.option4 = option4;
            return this;
        }

        public Builder setCorrectOption(int correctOption) {
            mQuestionObject.correctOption = correctOption;
            return this;
        }

        public Builder setAudio(Uri uri) {
            mQuestionObject.addAudio(uri);
            return this;
        }

        public Question question() {
            return mQuestionObject;
        }
    }
}
