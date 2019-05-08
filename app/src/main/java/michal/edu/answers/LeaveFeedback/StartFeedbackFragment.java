package michal.edu.answers.LeaveFeedback;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import michal.edu.answers.DataSource;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFeedbackFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ImageView cardImage;
    private TextView firstLetter, tvStoreNameBranchName;
    private Button btnNext;
    private Spinner spinnerMonth;

    public static StartFeedbackFragment newInstance(Store store, String branchName) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        args.putSerializable("branchName", branchName);
        StartFeedbackFragment fragment = new StartFeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_feedback, container, false);

        spinnerMonth = v.findViewById(R.id.spinnerMonth);
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        tvStoreNameBranchName = v.findViewById(R.id.tvStoreNameBranchName);
        btnNext = v.findViewById(R.id.btnNext);

        final Store thisStore = (Store) getArguments().getSerializable("store");
        final String thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());
        tvStoreNameBranchName.setText(thisStore.getStoreName() + " / " + thisBranchName);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, FirstSectionFragment.newInstance(thisStore, thisBranchName))
                        .addToBackStack("")
                        .commit();
            }
        });

        return v;
    }

}