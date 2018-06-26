package com.example.android.questionnaire.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.questionnaire.R;
import com.example.android.questionnaire.data.Options;
import com.example.android.questionnaire.data.Question;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter {

    private ArrayList<Question> questions;
    private boolean[] correctAnswers;

    public ResultsAdapter(ArrayList<Question> questions, boolean[] validateAnswers) {
        this.questions = questions;
        this.correctAnswers = validateAnswers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_stats, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;

        Question question = questions.get(position);

        cardViewHolder.questionTextView.setText(question.getQuestion());

        StringBuilder correctAnswer = new StringBuilder();
        StringBuilder userAnswer = new StringBuilder();

        String[] options = question.getOptions();

        Options optionsType = question.getOptionsType();
        switch (optionsType) {
            case RADIOBUTTON:
                correctAnswer.append(options[question.getAnswerId().get(0)]);
                if (question.getUserSetAnswerId() != null && question.getUserSetAnswerId().size() > 0) {
                    if (correctAnswers[position]) {
                        userAnswer = correctAnswer;
                    } else {
                        userAnswer.append(options[question.getUserSetAnswerId().get(0)]);
                    }
                }
                break;
            case CHECKBOX:
                for (int i = 0; i < question.getAnswerId().size(); i++) {
                    if (i > 0) correctAnswer.append(", ");
                    correctAnswer.append(options[question.getAnswerId().get(i)]);
                }
                if (question.getUserSetAnswerId() != null && question.getUserSetAnswerId().size() > 0) {
                    if (correctAnswers[position]) {
                        userAnswer = correctAnswer;
                    } else {
                        for (int j = 0; j < question.getUserSetAnswerId().size(); j++) {
                            if (j > 0) userAnswer.append(", ");
                            userAnswer.append(options[question.getUserSetAnswerId().get(j)]);
                        }
                    }
                }
                break;
            case EDITTEXT:
                correctAnswer.append(question.getAnswer());
                if (!TextUtils.isEmpty(question.getUserAnswer())) {
                    if (correctAnswers[position]) {
                        userAnswer = correctAnswer;
                    } else {
                        userAnswer.append(question.getUserAnswer().substring(0, 1).toUpperCase()).append(question.getUserAnswer().substring(1));
                    }
                }
                break;
        }

        cardViewHolder.correctAnswerTextView.setText(correctAnswer);
        cardViewHolder.userAnswerTextView.setText(TextUtils.isEmpty(userAnswer) ? "Unanswered" : userAnswer);
        if (correctAnswers[position]) {
            cardViewHolder.userAnswerTextView.setTextColor(Color.GREEN);
        } else {
            cardViewHolder.userAnswerTextView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTextView;
        private TextView userAnswerTextView;
        private TextView correctAnswerTextView;

        CardViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question_text_view);
            userAnswerTextView = itemView.findViewById(R.id.user_answer);
            correctAnswerTextView = itemView.findViewById(R.id.correct_answer);
        }
    }


}