package com.wolfsoft.one.cg.epargne;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfsoft.one.cg.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompteEpargne extends Fragment {


    public CompteEpargne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compte_epargne, container, false);
    }

}
