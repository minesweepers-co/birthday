package co.minesweepers.birthday.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.services.DataUploaderService;

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;
    private String audioUri;

    public void addAudio(Uri uri) {
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

    public JSONObject serialize() {
        JSONObject questionObj = new JSONObject();
        try {
            // TODO : don't send stuff if its null
            questionObj.put(Constants.JSON_QUESTION_KEY, question);
            questionObj.put(Constants.JSON_OPTION_1_KEY, option1);
            questionObj.put(Constants.JSON_OPTION_2_KEY, option2);
            questionObj.put(Constants.JSON_OPTION_3_KEY, option3);
            questionObj.put(Constants.JSON_OPTION_4_KEY, option4);
            questionObj.put(Constants.JSON_CORRECT_OPTION_KEY, correctOption);
            questionObj.put(Constants.JSON_AUDIO_PATH_KEY, audioUri);
        } catch (JSONException e) {
            // ignore for now
        }
        return questionObj;
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
