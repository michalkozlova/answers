package michal.edu.answers.UserDetails;


import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button btnLogin, btnRegistration;
    Spinner spinnerExtention;
    EditText etPhoneNumber, etPassword;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        setInitialView(v);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistrationForm();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return v;
    }

    private void signIn() {
        if (!isPhoneValid() | !isPasswordValid()){
            return;
        }

        showProgress(true);
        Task<AuthResult> task = FirebaseAuth.getInstance().signInWithEmailAndPassword(number() + "@" + number() + ".com", password());
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                showProgress(false);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new PersonalDetailsFragment())
                        .disallowAddToBackStack()
                        .commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("not signed in");
            }
        });
    }

    private void goToRegistrationForm() {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, new RegistrationFragment()).commit();
    }

    private void setInitialView(View v){
        btnRegistration = v.findViewById(R.id.btnRegistration);
        btnLogin = v.findViewById(R.id.btnLogin);
        spinnerExtention = v.findViewById(R.id.spinnerExtention);
        etPhoneNumber = v.findViewById(R.id.etPhoneNumber);
        etPassword = v.findViewById(R.id.etPassword);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.phone_extentions, R.layout.spinner_item_phone);
        adapter.setDropDownViewResource(R.layout.spinner_item_phone);
        spinnerExtention.setAdapter(adapter);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));
//        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
//        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4954F7")));
    }

    private String number() {
        StringBuilder builder = new StringBuilder();
        builder.append(spinnerExtention.getSelectedItem().toString() + etPhoneNumber.getText().toString());
        builder.deleteCharAt(0);
        return "972" + builder;
    }

    private String password(){return etPassword.getText().toString();}


    private boolean isPhoneValid(){
        if(number().isEmpty()){
            etPhoneNumber.setError("Please put phone number");
            return false;
        } else if (number().length() != 12){
            System.out.println(number().length());
            etPhoneNumber.setError("Not correct number");
            return false;
        }else {
            return true;
        }
    }


    private boolean isPasswordValid(){
        if (password().isEmpty()){
            etPassword.setError("Password can not be empty!");
            return false;
        }
        boolean isPasswordValid = password().length() >= 6;
        if (!isPasswordValid){
            etPassword.setError("Password must contain at least 6 characters");
        }
        return isPasswordValid;
    }


    ProgressDialog dialog;
    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("Logging you in...");
        }
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
