package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;


public class DiceFragment extends Fragment {


    /**
     * Creates view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.fragment_dice, container, false);



        return v;
    }

}
