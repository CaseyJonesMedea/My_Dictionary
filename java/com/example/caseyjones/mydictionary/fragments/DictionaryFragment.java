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
import android.widget.Button;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.adapters.WordAdapter;
import com.example.caseyjones.mydictionary.dialogs.SearchDialog;
import com.example.caseyjones.mydictionary.models.word.WordJS;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public class DictionaryFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<WordJS>words;
    private Context context;
    private WordAdapter adapter;
    private Button btnSearch;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        words = (ArrayList<WordJS>) bundle.getSerializable("words");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary,null);
        initViews(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WordAdapter(words,context);
        recyclerView.setAdapter(adapter);
        btnSearch.setOnClickListener(this);
        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        btnSearch = (Button)view.findViewById(R.id.search);
    }

    public static DictionaryFragment newInstance(ArrayList<WordJS>words){
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("words",words);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void updateCollection(WordJS wordJS){
        words.add(wordJS);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getFragmentManager(), "search");
    }
}
