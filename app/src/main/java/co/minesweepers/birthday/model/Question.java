package co.minesweepers.birthday.model;

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;

    public static class Builder {
        private Question mQuestionObject;

        public Builder() {
            mQuestionObject = new Question();
        }

        public Builder setQuestion(String question) {
            mQuestionObject.question = question;
            return this;
        }

        public Builder setOption1(String option1) {
            mQuestionObject.option1 = option1;
            return this;
        }

        public Builder setOption2(String option2) {
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

        public Question question() {
            return mQuestionObject;
        }
    }
}
