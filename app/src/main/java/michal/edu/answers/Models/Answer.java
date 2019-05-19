package michal.edu.answers.Models;

import java.io.Serializable;

public class Answer implements Serializable {

    private String questionID;
    private float answerValue;

    public Answer() {
    }

    public Answer(String questionID, float answerValue) {
        this.questionID = questionID;
        this.answerValue = answerValue;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public float getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "questionID='" + questionID + '\'' +
                ", answerValue=" + answerValue +
                '}';
    }
}
