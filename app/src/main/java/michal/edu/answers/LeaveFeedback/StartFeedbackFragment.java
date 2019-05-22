package michal.edu.answers.LeaveFeedback;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

                if (timeIsValid()) {
                    //TODO: take of comment
                saveInfoToSharedPref();

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, FirstSectionFragment.newInstance(thisStore, thisBranchName))
                        .disallowAddToBackStack()
                        .commit();
                } else {
                    timeIsNotCorrectDialog();
                }
            }
        });

        return v;
    }

    private void setInitialView(View v){
        cardImage = v.findViewById(R.id.cardImage);
        firstLetter = v.findViewById(R.id.firstLetter);
        tvStoreNameBranchName = v.findViewById(R.id.tvStoreNameBranchName);
        btnNext = v.findViewById(R.id.btnNext);
        bottomBar = getActivity().getWindow().findViewById(R.id.navigation);
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

    private void showDatePickerDialog() {
        int year = Integer.valueOf(fullDate.substring(6, 10));
        int month = Integer.valueOf(fullDate.substring(3, 5));
        int day = Integer.valueOf(fullDate.substring(0, 2));
        new DatePickerDialog(getContext(), this, year, month-1, day).show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dayString = "";
        if (dayOfMonth < 10){
            dayString = "0" + dayOfMonth;
        }else {
            dayString = String.valueOf(dayOfMonth);
        }

        String monthString = "";
        if ((month+1) < 10){
            monthString = "0" + (month+1);
        }else {
            monthString = String.valueOf((month+1));
        }

        fullDate = dayString + "/" + monthString + "/" + year;
        tvFullDate.setText(fullDate);
    }

    private void showTimePickerDialog() {
        int hour = Integer.valueOf(fullTime.substring(0,2));
        int min = Integer.valueOf(fullTime.substring(3,5));
        new TimePickerDialog(getContext(), this, hour, min, true).show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourString = "";
        if (hourOfDay < 10){
            hourString = "0" + hourOfDay;
        } else if (hourOfDay == 24){
            hourString = "00";
        } else {
            hourString = String.valueOf(hourOfDay);
        }

        String minuteString = "";
        if (minute < 10){
            minuteString = "0" + minute;
        }else {
            minuteString = String.valueOf(minute);
        }

        fullTime = hourString + ":" + minuteString;
        tvFullTime.setText(fullTime);
    }

    private boolean timeIsValid(){
            //System.out.println(timestamp + " before time was changed");

            SimpleDateFormat formatter = new SimpleDateFormat("kk:mm dd/MM/yyy");

            try {
                Date mDate = formatter.parse(fullTime + " " + fullDate);
                timestamp = mDate.getTime();
                //System.out.println(timestamp + " after time was changed");
//                String newFullTime = formatter.format(new Date(timestamp));
//                System.out.println(newFullTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        if (timestamp > System.currentTimeMillis()){
            System.out.println("SET:     " + timestamp);
            System.out.println("CURRENT: " + System.currentTimeMillis());
            return false;
        }else {
            return true;
        }

    }

    private void saveInfoToSharedPref(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("customerID", userID);
        editor.putString("storeID", thisStore.getStoreID());
        editor.putString("branchName", thisBranchName);
        editor.putLong("timestamp", timestamp);
        editor.apply();
    }

    private void timeIsNotCorrectDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Time is not correct");
        builder.setMessage("Time of the purchase can't be in future");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
