package michal.edu.answers.LeaveFeedback;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import michal.edu.answers.Adapters.QuestionAdapter;
import michal.edu.answers.DataSource;
import michal.edu.answers.Listeners.SectionListener;
import michal.edu.answers.Models.Section;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdSectionFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ProgressBar progressBar;
    private TextView tvTitleThirdSectionFeedback, firstLetter;
    private ImageView cardImage;
    private RecyclerView rvQuestions;
    private Button btnNext;

    public static ThirdSectionFragment newInstance(Store store, String branchName) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        args.putSerializable("branchName", branchName);
        ThirdSectionFragment fragment = new ThirdSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        tvTitleThirdSectionFeedback = v.findViewById(R.id.tvTitleSectionFeedback);
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        rvQuestions = v.findViewById(R.id.rvQuestions);
        btnNext = v.findViewById(R.id.btnNext);


        final Store thisStore = (Store) getArguments().getSerializable("store");
        final String thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.getQuestionnaireFromFirebase(thisStore, new SectionListener() {
            @Override
            public void onSectionCallback(ArrayList<Section> questionnaire) {
                tvTitleThirdSectionFeedback.setText("FEEDBACK: " + questionnaire.get(2).getSectionName());

                QuestionAdapter adapter = new QuestionAdapter(questionnaire.get(2).getQuestions(), getActivity());
                rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
                rvQuestions.setAdapter(adapter);

            }
        });

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        progressBar.setProgress(4);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, CommentFragment.newInstance(thisStore, thisBranchName))
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        return v;
    }

}