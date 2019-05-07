package michal.edu.answers.UserDetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import michal.edu.answers.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        Spinner phoneExtention = v.findViewById(R.id.phoneExtention);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.phone_extentions, R.layout.phone_spinner_item);
        adapter.setDropDownViewResource(R.layout.phone_spinner_item);
        phoneExtention.setAdapter(adapter);


        return v;
    }

}
