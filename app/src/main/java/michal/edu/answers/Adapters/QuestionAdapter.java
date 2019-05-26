package michal.edu.answers.Adapters;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import michal.edu.answers.Models.Question;
import michal.edu.answers.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    private List<Question> questionList;
    private FragmentActivity activity;
    private int sectionID;
    private SharedPreferences sharedPref;

    public QuestionAdapter(List<Question> questionList, FragmentActivity activity, int sectionID, SharedPreferences sharedPref) {
        this.questionList = questionList;
        this.activity = activity;
        this.sectionID = sectionID;
        this.sharedPref = sharedPref;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_question, viewGroup, false);
        return new QuestionViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder questionViewHolder, int i) {
        final Question question = questionList.get(i);

        questionViewHolder.tvQuestionText.setText(question.getQuestionText());

        if (question.getQuestionType() == 0) {
            questionViewHolder.ratingBar.setVisibility(View.GONE);
        }else {
            questionViewHolder.radioGroup.setVisibility(View.GONE);
        }

        questionViewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat(question.getQuestionID(), rating);
                editor.putInt(question.getQuestionID() + "T", question.getQuestionType());
                editor.apply();
                System.out.println("rating " + rating);
            }
        });


        questionViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                float answerValue;
                if (checkedId == questionViewHolder.rbYes.getId()){
                    answerValue = (float) 1.0;
                }else {
                    answerValue = (float) 0.0;
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat(question.getQuestionID(), answerValue);
                editor.putInt(question.getQuestionID() + "T", question.getQuestionType());
                editor.apply();
                System.out.println("answerValue " + answerValue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        TextView tvQuestionText;
        RatingBar ratingBar;
        RadioGroup radioGroup;
        RadioButton rbYes, rbNo;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            rbYes = itemView.findViewById(R.id.rbYes);
            rbNo = itemView.findViewById(R.id.rbNo);
        }
    }
}
