package co.minesweepers.birthday.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.services.FirebaseDBService;

public class Question {
    private static final String TAG = "Question";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_OPTION1 = "option1";
    private static final String KEY_OPTION2 = "option2";
    private static final String KEY_OPTION3 = "option3";
    private static final String KEY_OPTION4 = "option4";
    private static final String KEY_CORRECT_OPTION = "correctOption";
    private static final String KEY_AUDIO_PATH = "audioPath";

    private static final int MAX_ALLOWED_OPTIONS = 4;

    private String question;
    private List<String> options;

    private int correctOption;
    private String audioUri;

    private Question() {
        options = Arrays.asList("", "", "", ""); // Initializing an empty list so that we can use 'set' method on the list
    }

    public String getQuestion() {
        return question;
    }

    public @NonNull List<String> getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public Builder builder() {
        return new Builder(this);
    }

    private void addAudio(Uri uri) {
        FirebaseDBService.uploadData(uri, new FirebaseDBService.ResponseHandler() {
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

    public boolean isValid() {
        if (Utils.isEmpty(question)) {
            return false;
        }

        int numOfOptions = 0;
        for (String option: options) {
            if (!Utils.isEmpty(option)) {
                numOfOptions++;
            }
        }

        return numOfOptions >= 2; // at least 2 options should be set for question to be valid
    }

    JSONObject serialize() {
        JSONObject questionObj = new JSONObject();
        try {
            // TODO : don't send stuff if its null
            questionObj.put(KEY_QUESTION, question);
            questionObj.put(KEY_OPTION1, options.get(0));
            questionObj.put(KEY_OPTION2, options.get(1));
            questionObj.put(KEY_OPTION3, options.get(2));
            questionObj.put(KEY_OPTION4, options.get(3));
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
                .setOption(0, questionJson.getString(KEY_OPTION1))
                .setOption(1, questionJson.getString(KEY_OPTION2));

        if (questionJson.has(KEY_OPTION3)) {
            builder.setOption(2, questionJson.getString(KEY_OPTION3));
        }

        if (questionJson.has(KEY_OPTION4)) {
            builder.setOption(3, questionJson.getString(KEY_OPTION4));
        }

        builder.setCorrectOption(questionJson.getInt(KEY_CORRECT_OPTION));
        return builder.question();
    }

    public static class Builder {
        private Question mQuestionObject;

        public Builder() {
            mQuestionObject = new Question();
        }

        private Builder(Question question) {
            mQuestionObject = question;
        }

        public Builder setQuestion(@NonNull String question) {
            mQuestionObject.question = question;
            return this;
        }

        public Builder setOption(int position, @NonNull String option) {
            mQuestionObject.options.set(position, option);
            return this;
        }

        public Builder setCorrectOption(int correctOption) {
            if (correctOption < 0 || correctOption >= MAX_ALLOWED_OPTIONS) {
                throw new IllegalArgumentException("Correct option cannot be less than 0 or greater than max allowed options: " + MAX_ALLOWED_OPTIONS);
            }
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
