package michal.edu.answers.Models;

import java.io.Serializable;

public class Answer implements Serializable {

    private String questionText;
    private int answerValue;

    public Answer(String questionText, int answerValue) {
        this.questionText = questionText;
        this.answerValue = answerValue;
    }

    public Answer() {
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "questionText='" + questionText + '\'' +
                ", answerValue=" + answerValue +
                '}';
    }
}
