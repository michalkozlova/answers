package michal.edu.answers.UserDetails;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import michal.edu.answers.Models.Customer;
import michal.edu.answers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailsFragment extends Fragment {

    private Button btnLogout;
    private TextView tvCustomerName, tvNumber;

    public static PersonalDetailsFragment newInstance(Customer customer) {

        Bundle args = new Bundle();
        args.putSerializable("customer", customer);
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_details, container, false);

        setInitialView(v);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new LoginFragment())
                        .disallowAddToBackStack().commit();
            }
        });

        return v;
    }

    private void setInitialView(View v){
        btnLogout = v.findViewById(R.id.btnLogout);
        tvCustomerName = v.findViewById(R.id.tvCustomerName);
        tvNumber = v.findViewById(R.id.tvNumber);

        Customer customer = (Customer) getArguments().getSerializable("customer");

        tvCustomerName.setText(customer.getCustomerName());
        tvNumber.setText(customer.getCustomerPhone());

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#ffEA4C5F"));
        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        navigation.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ff4954F7")));
    }

}
