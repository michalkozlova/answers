package michal.edu.answers;

import java.util.ArrayList;

import michal.edu.answers.Models.Question;

public interface QuestionListener {
        void onSectionCallback(ArrayList<Question> questionnaire);
}
