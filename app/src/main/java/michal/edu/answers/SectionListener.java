package michal.edu.answers;

import java.util.ArrayList;

import michal.edu.answers.Models.Section;

public interface SectionListener {
    void onSectionCallback(ArrayList<Section> questionnaire);
}
