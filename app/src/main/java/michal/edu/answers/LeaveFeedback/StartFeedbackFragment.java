package michal.edu.answers.LeaveFeedback;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import michal.edu.answers.DataSource;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFeedbackFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private DataSource dataSource = DataSource.getInstance();
    private ImageView cardImage;
    private TextView firstLetter, tvStoreNameBranchName, tvFullDate, tvFullTime;
    private Button btnNext;
    private View bottomBar;
    private Store thisStore;
    private String thisBranchName, fullDate, fullTime, fullDateAndTime, userID;
    private long timestamp;
    private SharedPreferences sharedPref;

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

        try {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (NullPointerException e){
            System.out.println(e);
            userID = "Anonymous";
        }

        getFullDateAndTime();

        setInitialView(v);

        tvFullDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        tvFullTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    timeChanged();
//TODO: take of comment
//                saveInfoToSharedPref();
//
//                getActivity()
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, FirstSectionFragment.newInstance(thisStore, thisBranchName))
//                        .disallowAddToBackStack()
//                        .commit();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        //spinnerMonth = v.findViewById(R.id.spinnerMonth);
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        tvStoreNameBranchName = v.findViewById(R.id.tvStoreNameBranchName);
        btnNext = v.findViewById(R.id.btnNext);
        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);
//        etDate = v.findViewById(R.id.etDate);
//        etHour = v.findViewById(R.id.etHour);
//        etMin = v.findViewById(R.id.etMin);
        tvFullDate = v.findViewById(R.id.tvFullDate);
        tvFullTime = v.findViewById(R.id.tvFullTime);

        fullDate = fullDateAndTime.substring(6, 16);
        tvFullDate.setText(fullDate);

        fullTime = fullDateAndTime.substring(0, 5);
        tvFullTime.setText(fullTime);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        thisStore = (Store) getArguments().getSerializable("store");
        thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());
        tvStoreNameBranchName.setText(thisStore.getStoreName() + " / " + thisBranchName);

//        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getContext(), R.array.months, R.layout.spinner_item_month);
//        monthAdapter.setDropDownViewResource(R.layout.spinner_item_month);
//        spinnerMonth.setAdapter(monthAdapter);

        bottomBar.clearAnimation();
        bottomBar.animate().translationY(bottomBar.getHeight()).setDuration(400);

    }

    private void getFullDateAndTime(){
        timestamp = System.currentTimeMillis();
        System.out.println(timestamp);

        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyyy");
        fullDateAndTime = formatter.format(new Date(timestamp));
        System.out.println("Full date and Time: " + fullDateAndTime);
    }

    private int day(){
        String dayString = fullDateAndTime.substring(6, 8);
        return Integer.valueOf(dayString);
    }

    private int month(){
        String monthString = fullDateAndTime.substring(9, 11);
        return Integer.valueOf(monthString)-1;
    }

    private int year(){
        String yearString = fullDateAndTime.substring(12, 16);
        return Integer.valueOf(yearString);
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(getContext(), this, year(), month(), day()).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(getContext(), "Done " + dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_SHORT).show();
        fullDate = dayOfMonth + "/" + (month+1) + "/" + year;
        tvFullDate.setText(fullDate);
    }


    private int hour(){
        String hourString = fullDateAndTime.substring(0, 2);
        return Integer.valueOf(hourString);
    }
    private int min(){
        String minString = fullDateAndTime.substring(3, 5);
        return Integer.valueOf(minString);
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(getContext(), this, hour(), min(), true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(getContext(), "Done: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        fullTime = hourOfDay + ":" + minute;
        tvFullTime.setText(fullTime);
    }

    private long timeChanged(){
        //TODO: fix the method
        System.out.println(timestamp + " before time was changed");
        //long month = spinnerMonth.getSelectedItemId() + 1;

        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyy");

        try {
            Date mDate = formatter.parse(fullTime + " " + fullDate);
            timestamp = mDate.getTime();
            System.out.println(timestamp + " after time was changed");
            String newFullTime = formatter.format(new Date(timestamp));
            System.out.println(newFullTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    private void saveInfoToSharedPref(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("customerID", userID);
        editor.putString("storeID", thisStore.getStoreID());
        editor.putString("branchName", thisBranchName);
        editor.putLong("timestamp", timestamp);
        editor.apply();
    }
}
