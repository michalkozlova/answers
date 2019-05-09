package michal.edu.answers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmPhoneFragment extends Fragment {

    EditText etCode;
    Button btnConfirm;

    public ConfirmPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_confirm_phone, container, false);

        etCode = v.findViewById(R.id.etCode);
        btnConfirm = v.findViewById(R.id.btnConfirm);

        return v;
    }

}
