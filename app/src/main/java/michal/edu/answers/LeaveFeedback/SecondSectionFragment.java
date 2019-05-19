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
public class SecondSectionFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ProgressBar progressBar;
    private TextView tvTitleSecondSectionFeedback, firstLetter;
    private ImageView cardImage;
    private RecyclerView rvQuestions;
    private Button btnNext;
    private Store thisStore;
    private String thisBranchName;

    public static SecondSectionFragment newInstance(Store store, String branchName) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        args.putSerializable("branchName", branchName);
        SecondSectionFragment fragment = new SecondSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section, container, false);

        setInitialView(v);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, ThirdSectionFragment.newInstance(thisStore, thisBranchName))
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        progressBar = v.findViewById(R.id.progressBar);
        tvTitleSecondSectionFeedback = v.findViewById(R.id.tvTitleSectionFeedback);
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        rvQuestions = v.findViewById(R.id.rvQuestions);
        btnNext = v.findViewById(R.id.btnNext);

        thisStore = (Store) getArguments().getSerializable("store");
        thisBranchName = (String) getArguments().getSerializable("branchName");

//        dataSource.getQuestionnaireFromFirebase(thisStore, new SectionListener() {
//            @Override
//            public void onSectionCallback(ArrayList<Section> questionnaire) {
//                tvTitleSecondSectionFeedback.setText("FEEDBACK: " + questionnaire.get(1).getSectionName());
//
//                QuestionAdapter adapter = new QuestionAdapter(questionnaire.get(1).getQuestions(), getActivity());
//                rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
//                rvQuestions.setAdapter(adapter);
//
//            }
//        });

        tvTitleSecondSectionFeedback.setText("FEEDBACK: " + thisStore.getQuestionnaire().get(1).getSectionName());

        QuestionAdapter adapter = new QuestionAdapter(thisStore.getQuestionnaire().get(1).getQuestions(), getActivity(), 1);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestions.setAdapter(adapter);

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        progressBar.setProgress(3);
    }

}
