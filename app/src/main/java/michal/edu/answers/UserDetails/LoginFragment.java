package michal.edu.answers.UserDetails;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnRegistration = v.findViewById(R.id.btnRegistration);
        Spinner phoneExtention = v.findViewById(R.id.spinnerDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.phone_extentions, R.layout.phone_spinner_item);
        adapter.setDropDownViewResource(R.layout.phone_spinner_item);
        phoneExtention.setAdapter(adapter);

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4954F7")));


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, new RegistrationFragment()).commit();
            }
        });

        return v;
    }

}
