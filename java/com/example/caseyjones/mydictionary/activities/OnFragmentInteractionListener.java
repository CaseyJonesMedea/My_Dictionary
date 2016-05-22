package com.example.caseyjones.mydictionary.activities;

import com.example.caseyjones.mydictionary.models.word.WordJS;

/**
 * Created by CaseyJones on 14.05.2016.
 */
public interface OnFragmentInteractionListener {

    void choseWord(WordJS word);
    void saveWord(WordJS word);
    void exitApp();
    void logOut();
    void passwordReset();
    void deleteUser();
}
