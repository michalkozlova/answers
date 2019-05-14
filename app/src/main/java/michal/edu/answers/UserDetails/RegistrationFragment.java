package michal.edu.answers.UserDetails;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
public class RegistrationFragment extends Fragment {

    EditText etName, etPassword, etConfirmPassword, etNumber;
    Button btnRegistration;
    Spinner phoneExtention;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        setInitialView(v);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

//        btnRegistration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isNameValid() | !isPhoneValid() | !isPasswordConfirmed() | !isPhoneValid()){
//                    return;
//                }
//
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        number(),        // Phone number to verify
//                        60,                 // Timeout duration
//                        TimeUnit.SECONDS,   // Unit of timeout
//                        this,               // Activity (for callback binding)
//                        mCallbacks);        // OnVerificationStateChangedCallbacks
//            }
//        });

        return v;
    }

    private void register() {
        if (!isNameValid() | !isPasswordValid() | !isPasswordConfirmed() | !isPhoneValid()) {
            return;
        }

        showProgress(true);

        Task<AuthResult> task = FirebaseAuth.getInstance().createUserWithEmailAndPassword(number() + "@" + number() + ".com", password());
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                System.out.println("DONE");
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
                showProgress(false);
                Snackbar.make(btnRegistration, e.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    private void setInitialView(View v){
        phoneExtention = v.findViewById(R.id.spinnerExtention);
        etName = v.findViewById(R.id.etName);
        etPassword = v.findViewById(R.id.etPassword);
        etConfirmPassword = v.findViewById(R.id.etConfirmPassword);
        etNumber = v.findViewById(R.id.etNumber);
        btnRegistration = v.findViewById(R.id.btnRegistration);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.phone_extentions, R.layout.spinner_item_phone);
        adapter.setDropDownViewResource(R.layout.spinner_item_phone);
        phoneExtention.setAdapter(adapter);
    }

    private String name() {
        return etName.getText().toString();
    }

    private String password() {
        return etPassword.getText().toString();
    }

    private String confirmPassword() {
        return etConfirmPassword.getText().toString();
    }

    private String number() {
        StringBuilder builder = new StringBuilder();
        builder.append(phoneExtention.getSelectedItem().toString() + etNumber.getText().toString());
        builder.deleteCharAt(0);
        return "972" + builder;
    }

    private boolean isNameValid() {
        if (name().isEmpty()) {
            etName.setError("Please put first name");
            return false;
        } else {
            return true;
        }
    }

    private boolean isPasswordValid() {
        if (password().isEmpty()) {
            etPassword.setError("Password can not be empty!");
            return false;
        }
        boolean isPasswordValid = password().length() >= 6;
        if (!isPasswordValid) {
            etPassword.setError("Password must contain at least 6 characters");
        }
        return isPasswordValid;
    }

    private boolean isPasswordConfirmed() {
        if (confirmPassword().isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            return false;
        } else if (!confirmPassword().equals(password())) {
            etConfirmPassword.setError("Password is not the same");
            return false;
        } else {
            return true;
        }
    }

    private boolean isPhoneValid(){
        if(number().isEmpty()){
            etNumber.setError("Please put phone number");
            return false;
        } else if (number().length() != 12){
            System.out.println(number().length());
            etNumber.setError("Not correct number");
            return false;
        }else {
            return true;
        }
    }


    ProgressDialog dialog;
    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());

            dialog.setCancelable(true);
            dialog.setTitle("Please wait");
            dialog.setMessage("You will be registered soon!");
        }
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

}
