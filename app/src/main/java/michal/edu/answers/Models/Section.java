package michal.edu.answers.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Section implements Serializable {

    private final static String STORE_APPEARANCE_1 = "Store Appearance";
    private final static String STORE_CLERK_2 = "Store Clerk";
    private final static String STORE_MERCHANDISE_3 = "Merchandise";

    private final static String RESTAURANT_APPEARANCE_1 = "Restaurant Appearance";
    private final static String RESTAURANT_STAFF_2 = "Restaurant Staff";
    private final static String RESTAURANT_FOOD_3 = "Food";

    private String sectionName;
    private ArrayList<Question> questions;

    public Section() {
    }

    public Section(String sectionName, ArrayList<Question> questions) {
        this.sectionName = sectionName;
        this.questions = questions;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", questions=" + questions +
                '}';
    }
}
