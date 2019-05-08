package michal.edu.answers.Models;

import java.io.Serializable;

public class Question implements Serializable {

    private String questionText;
    private int questionType;

    public static final int YES_NO = 0;
    public static final int ONE_FIVE = 1;

    public Question() {
    }

    public Question(String questionText, int questionType) {
        this.questionText = questionText;
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", questionType=" + questionType +
                '}';
    }
}
