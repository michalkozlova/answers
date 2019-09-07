package michal.edu.answers.UserDetails;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import michal.edu.answers.Models.Customer;
import michal.edu.answers.R;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private static final String TAG = "PhoneAuth";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    private EditText etName, etPassword, etConfirmPassword, etNumber;
    private Button btnRegistration;
    private Spinner phoneExtention;
    private String verificationCode;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            onViewStateRestored(savedInstanceState);
        }

        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        setInitialView(v);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNameValid() | !isPhoneValid() | !isPasswordConfirmed() | !isPhoneValid()){
                    return;
                }

                System.out.println("tapped");

                startPhoneNumberVerification(number());

            }
        });



        return v;
    }


//    //TODO: 176 - 187
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        //TODO: updateUI(currentUser);
//
//        // [START_EXCLUDE]
//        if (mVerificationInProgress && isPhoneValid()) {
//            startPhoneNumberVerification(number());
//        }
//        // [END_EXCLUDE]
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;

        getVerificationCode();
    }



    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }



    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]


    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            //TODO: updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]



                            //Save user details in Firebase Database:
                            String customerID = user.getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerID);
                            Customer customer = new Customer(customerID, name(), number());
                            ref.setValue(customer);

                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, PersonalDetailsFragment.newInstance(customer))
                                    .disallowAddToBackStack()
                                    .commit();


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                Toast.makeText(getContext(), "Invalid code.", Toast.LENGTH_LONG).show();
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //TODO: updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]


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

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));
//        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
//        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4954F7")));

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // TODO: Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    etNumber.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Log.d(TAG, "Quota exceeded.");
                    // [END_EXCLUDE]
                }

                // TODO: Show a message and update the UI
                // [START_EXCLUDE]
                //updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // TODO: Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
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
        System.out.println("+972" + builder);
        return "+972" + builder;
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
        } else if (number().length() != 13){
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

    private AlertDialog alertDialog;
    private void getVerificationCode() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Put verification code you've received by SMS:");

//        builder.setMessage("Put verification code you've received by SMS:");

        builder.setView(R.layout.verification_code);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                if (alertDialog != null) {
                    EditText editText = alertDialog.findViewById(R.id.etVerificationCode);

                    verificationCode = editText.getText().toString();

                    if (TextUtils.isEmpty(verificationCode)) {
                        editText.setError("Cannot be empty");
                        return;
                    }
                    verifyPhoneNumberWithCode(mVerificationId, verificationCode);
                    alertDialog = null;
                }
            }
        });

        builder.setNeutralButton("Resend code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: not to close alertDialog
                resendVerificationCode(number(), mResendToken);
            }
        });

        alertDialog = builder.show();
    }

}
