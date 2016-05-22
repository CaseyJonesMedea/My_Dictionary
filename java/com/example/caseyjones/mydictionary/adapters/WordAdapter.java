package com.example.caseyjones.mydictionary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.activities.OnFragmentInteractionListener;
import com.example.caseyjones.mydictionary.models.word.WordJS;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder>  {

    private Context context;
    private ArrayList<WordJS>words;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    public WordAdapter(ArrayList<WordJS> words, Context context) {
        this.words = words;
        this.context = context;
        onFragmentInteractionListener = (OnFragmentInteractionListener)context;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word,parent,false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.wordTV.setText(words.get(position).getDef().get(0).getText());
        holder.transcriptionTV.setText("[" + words.get(position).getDef().get(0).getTs() + "]");
        holder.translationTV.setText(words.get(position).getDef().get(0).getTr().get(0).getText());

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        LinearLayout ll;
        TextView wordTV;
        TextView transcriptionTV;
        TextView translationTV;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordTV = (TextView)itemView.findViewById(R.id.word);
            transcriptionTV = (TextView)itemView.findViewById(R.id.transcription);
            translationTV = (TextView)itemView.findViewById(R.id.translation);
            ll = (LinearLayout)itemView.findViewById(R.id.cell);
            ll.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           onFragmentInteractionListener.choseWord(words.get(getAdapterPosition()));
        }
    }
}
