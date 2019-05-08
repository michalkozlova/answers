package michal.edu.answers;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import michal.edu.answers.Models.Question;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    private List<Question> questionList;
    private FragmentActivity activity;

    public QuestionAdapter(List<Question> questionList, FragmentActivity activity) {
        this.questionList = questionList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item_question, viewGroup, false);

        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder questionViewHolder, int i) {
        Question question = questionList.get(i);

        questionViewHolder.tvQuestionText.setText(question.getQuestionText());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder{

        TextView tvQuestionText;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
        }
    }
}
