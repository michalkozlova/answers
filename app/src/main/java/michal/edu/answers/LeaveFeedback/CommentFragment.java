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

    private DatabaseReference reference;
    private DataSource dataSource = DataSource.getInstance();
    private ProgressBar progressBar;
    private TextView firstLetter;
    private ImageView cardImage;
    private Button btnDone;
    private Store thisStore;
    private String thisBranchName;
    private FragmentManager manager;
    private SharedPreferences sharedPref;


    public static CommentFragment newInstance(Store store, String branchName) {

        Bundle args = new Bundle();
        args.putSerializable("store", store);
        args.putSerializable("branchName", branchName);
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

                System.out.println(sharedPref);

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

        manager = getFragmentManager();

        thisStore = (Store) getArguments().getSerializable("store");
        thisBranchName = (String) getArguments().getSerializable("branchName");

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

    private void readDataFromSharedPref(){
        String customerID = sharedPref.getString("customerID", "Anonymous");
        String storeID = sharedPref.getString("storeID", "storeID");
        String branchName = sharedPref.getString("branchName", "branchName");
        long timestamp = sharedPref.getLong("timestamp", 000000);

        ArrayList<SectionAnswer> sectionAnswers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            SectionAnswer sectionAnswer = new SectionAnswer();
            sectionAnswer.setSectionName(thisStore.getQuestionnaire().get(i).getSectionName());
            ArrayList<Answer> answers = new ArrayList<>();
            int x = thisStore.getQuestionnaire().get(i).getQuestions().size();
            for (int j = 0; j < x; j++) {
                Float answerValue = sharedPref.getFloat(i + "" + j, (float) 0.0);
                Answer answer = new Answer(i + "" + j, answerValue);
                answers.add(answer);
            }
            sectionAnswer.setAnswers(answers);

            sectionAnswers.add(sectionAnswer);
        }

        ArrayList<Branch> branches = thisStore.getBranches();
        String city = "";
        for (Branch branch : branches) {
            if (branch.getBranchName() == thisBranchName){
                city = branch.getBranchAddress().getCity();
            }
        }

        Feedback feedback = new Feedback(customerID, storeID, branchName, timestamp, "new comment", city, sectionAnswers);
        System.out.println(feedback);

        saveToFB(feedback);



    }

    private void saveToFB(Feedback feedback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String key = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(feedback.getStoreID()).child(key);
        ref.setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    private void deleteDataFromSharedPref(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }


}
