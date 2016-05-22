package com.example.caseyjones.mydictionary.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.adapters.PosRecyclerAdapter;
import com.example.caseyjones.mydictionary.models.word.WordJS;



/**
 * Created by CaseyJones on 14.05.2016.
 */
public class WordFragment extends Fragment {

    private TextView wordInfo;
    private TextView transcriptionInfo;
    private RecyclerView recyclerPos;


    private Context context;

    private WordJS word;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        word = (WordJS) bundle.getSerializable("word");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, null);
        initViews(view);
        wordInfo.setText(word.getDef().get(0).getText());
        transcriptionInfo.setText("[" + word.getDef().get(0).getTs() + "]");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerPos.setLayoutManager(linearLayoutManager);
        PosRecyclerAdapter adapter = new PosRecyclerAdapter(word.getDef(), context);
        recyclerPos.setAdapter(adapter);
        return view;
    }

    private void initViews(View view) {
        wordInfo = (TextView) view.findViewById(R.id.wordInfo);
        transcriptionInfo = (TextView) view.findViewById(R.id.transcriptionInfo);
        recyclerPos = (RecyclerView) view.findViewById(R.id.recyclerPos);
    }

    public static WordFragment newInstance(WordJS word) {
        WordFragment fragment = new WordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("word", word);
        fragment.setArguments(bundle);
        return fragment;
    }
}
