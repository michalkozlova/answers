package michal.edu.answers.LeaveFeedback;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import michal.edu.answers.AllStoresFragment;
import michal.edu.answers.DataSource;
import michal.edu.answers.Listeners.StoreListener;
import michal.edu.answers.Models.Answer;
import michal.edu.answers.Models.Branch;
import michal.edu.answers.Models.Feedback;
import michal.edu.answers.Models.SectionAnswer;
import michal.edu.answers.Models.Store;
import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private DataSource dataSource = DataSource.getInstance();
    private ProgressBar progressBar;
    private TextView firstLetter;
    private EditText etComment;
    private ImageView cardImage;
    private Button btnDone;
    private Store thisStore;
//    private String thisBranchName;
    private FragmentManager manager;
    private SharedPreferences sharedPref;


    public static CommentFragment newInstance(Store store) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
//        args.putSerializable("branchName", branchName);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_comment, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        setInitialView(v);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneDialog();

                readDataFromSharedPref();

                deleteDataFromSharedPref();

                dataSource.getStoresFromFirebase(new StoreListener() {
                    @Override
                    public void onStoreCallBack(ArrayList<Store> stores) {
                        manager.beginTransaction().replace(R.id.container, AllStoresFragment.newInstance(stores)).commit();
                    }
                });
            }
        });

        return v;
    }

    private void setInitialView(View v){
        progressBar = v.findViewById(R.id.progressBar);
        firstLetter = v.findViewById(R.id.firstLetter);
        cardImage = v.findViewById(R.id.cardImage);
        btnDone = v.findViewById(R.id.btnDone);
        etComment = v.findViewById(R.id.etComment);

        manager = getFragmentManager();

        thisStore = (Store) getArguments().getSerializable("store");
//        thisBranchName = (String) getArguments().getSerializable("branchName");

        dataSource.setStoreLogo(thisStore, cardImage, firstLetter, getContext());

        progressBar.setProgress(5);
    }

    private void doneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Thank you!");
        //TODO: different message depends on feedback result
        builder.setMessage("We really appreciate your time");
        builder.show();
    }

    private String getComment(){
        String comment;
        if (etComment.getText() != null){
            comment = etComment.getText().toString();
        }else {
            comment = " ";
        }
        return comment;
    }

    private void readDataFromSharedPref(){
        String customerID = sharedPref.getString("customerID", "Anonymous");
        String storeID = sharedPref.getString("storeID", "storeID");
        String branchName = sharedPref.getString("branchName", "branchName");
        String city = sharedPref.getString("city", "city");
        long timestamp = sharedPref.getLong("timestamp", 000000);

        ArrayList<SectionAnswer> sectionAnswers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            SectionAnswer sectionAnswer = new SectionAnswer();
            sectionAnswer.setSectionName(thisStore.getQuestionnaire().get(i).getSectionName());
            ArrayList<Answer> answers = new ArrayList<>();
            int x = thisStore.getQuestionnaire().get(i).getQuestions().size();
            for (int j = 0; j < x; j++) {
                Float answerValue = sharedPref.getFloat(i + "" + j, (float) 0.0);
                int questionType = sharedPref.getInt(i + "" + j + "T", 0);
                Answer answer = new Answer(i + "" + j, questionType, answerValue);
                answers.add(answer);
            }
            sectionAnswer.setAnswers(answers);

            sectionAnswers.add(sectionAnswer);
        }


        Feedback feedback = new Feedback(customerID, storeID, branchName, timestamp, getComment(), city, sectionAnswers);
        System.out.println(feedback);

        saveToFB(feedback);
    }

    private void saveToFB(Feedback feedback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(feedback.getStoreID()).child(key);
        ref.setValue(feedback);

    }

    private void deleteDataFromSharedPref(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }


}
