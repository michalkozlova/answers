package michal.edu.answers.Branch;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import michal.edu.answers.R;
import michal.edu.answers.StartFeedbackFragment;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder>{

    private List<Branch> branches;
    private FragmentActivity activity;

    public BranchAdapter(List<Branch> branches, FragmentActivity activity) {
        this.branches = branches;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.branch_item, viewGroup, false);
        return new BranchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder branchViewHolder, int i) {
        Branch branch = branches.get(i);

        branchViewHolder.smallWhiteButton.setText(branch.getBranchNameEng());

        branchViewHolder.smallWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new StartFeedbackFragment()).addToBackStack("").commit();
                //Toast.makeText(activity, "hey-hey!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder{

        final Button smallWhiteButton;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);

            smallWhiteButton = (Button) itemView.findViewById(R.id.smallWhiteButton);
        }
    }
}
