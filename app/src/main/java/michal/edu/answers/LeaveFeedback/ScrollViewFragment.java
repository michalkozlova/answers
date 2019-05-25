package michal.edu.answers.LeaveFeedback;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import michal.edu.answers.Adapters.QuestionAdapter;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollViewFragment extends Fragment {

    private RecyclerView rvQuestions;
    private Button btnNext;
    private Store thisStore;
    private SharedPreferences sharedPref;

    public static ScrollViewFragment newInstance(Store store) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_scroll_view, container, false);

        rvQuestions = v.findViewById(R.id.rvQuestions);
        btnNext = v.findViewById(R.id.btnNext);

        btnNext.setVisibility(View.VISIBLE);

        thisStore = (Store) getArguments().getSerializable("store");

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        QuestionAdapter adapter = new QuestionAdapter(thisStore.getQuestionnaire().get(0).getQuestions(), getActivity(), 0, sharedPref);
        rvQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvQuestions.setAdapter(adapter);

        return v;
    }

}
