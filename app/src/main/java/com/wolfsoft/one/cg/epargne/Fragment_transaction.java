package com.wolfsoft.one.cg.epargne;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfsoft.one.cg.R;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_transaction extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ftransactions = inflater.inflate(R.layout.fragment_transaction,container,false);
        return ftransactions;
    }
}
