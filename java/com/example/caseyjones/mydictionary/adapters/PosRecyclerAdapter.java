package com.example.caseyjones.mydictionary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.models.word.PosWord;
import com.example.caseyjones.mydictionary.models.word.TranslateWord;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class PosRecyclerAdapter extends RecyclerView.Adapter<PosRecyclerAdapter.PosViewHolder> {

    private ArrayList<PosWord> posWords;
    private Context context;

    public PosRecyclerAdapter(ArrayList<PosWord> posWords, Context context) {
        this.posWords = posWords;
        this.context = context;
    }

    @Override
    public PosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_pos,parent,false);
        return new PosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosViewHolder holder, int position) {
        holder.pos.setText(posWords.get(position).getPos());
        ArrayList <TranslateWord> words = posWords.get(position).getTr();
        ArrayList<String> tr = new ArrayList<>();
        for(int i = 0; i < words.size(); i++){
            tr.add(words.get(i).getText());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, tr);
        holder.listView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return posWords.size();
    }

    class PosViewHolder extends RecyclerView.ViewHolder {

        TextView pos;
        ListView listView;

        public PosViewHolder(View itemView) {
            super(itemView);
            pos = (TextView)itemView.findViewById(R.id.pos);
            listView = (ListView)itemView.findViewById(R.id.listView);
        }
    }
}
