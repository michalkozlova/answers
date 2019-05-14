package michal.edu.answers.LeaveFeedback;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import michal.edu.answers.DataSource;
import michal.edu.answers.MainActivity;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFeedbackFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ImageView cardImage;
    private TextView firstLetter, tvStoreNameBranchName;
    private EditText etDate, etHour, etMin;
    private Button btnNext;
    private Spinner spinnerMonth;
    private View bottomBar;
    private Store thisStore;
    private String thisBranchName;
    private long timestamp;

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

        setInitialView(v);

        getTime();
        changeTime();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, FirstSectionFragment.newInstance(thisStore, thisBranchName))
                        .disallowAddToBackStack()
                        .commit();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        spinnerMonth = v.findViewById(R.id.spinnerMonth);
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        tvStoreNameBranchName = v.findViewById(R.id.tvStoreNameBranchName);
        btnNext = v.findViewById(R.id.btnNext);
        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);
        etDate = v.findViewById(R.id.etDate);
        etHour = v.findViewById(R.id.etHour);
        etMin = v.findViewById(R.id.etMin);

        thisStore = (Store) getArguments().getSerializable("store");
        thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());
        tvStoreNameBranchName.setText(thisStore.getStoreName() + " / " + thisBranchName);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        bottomBar.clearAnimation();
        bottomBar.animate().translationY(bottomBar.getHeight()).setDuration(400);

    }

    private void getTime(){
        timestamp = System.currentTimeMillis();
        System.out.println(timestamp);

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String fullTime = formatter.format(new Date(timestamp));
        System.out.println(fullTime);

        String hour = fullTime.substring(0, 2);
        System.out.println("Hours: " + hour);
        etHour.setText(hour);

        String min = fullTime.substring(3, 5);
        System.out.println(min);
        etMin.setText(min);

        String day = fullTime.substring(6, 8);
        System.out.println(day);
        etDate.setText(day);

        String months = fullTime.substring(9, 11);
        Integer mon = Integer.valueOf(months);
        spinnerMonth.setSelection(mon-1);
    }

    private void changeTime(){
        String day = etDate.getText().toString();
        long month = spinnerMonth.getSelectedItemId() + 1;
        String hour = etHour.getText().toString();
        String min = etMin.getText().toString();
        System.out.println("change time: " + month);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyy");
        try {
            Date mDate = dateFormat.parse(hour + ":" + min + " " + day + "/" + month + "/2019");
            timestamp = mDate.getTime();
            System.out.println(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
