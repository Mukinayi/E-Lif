package com.wolfsoft.one.cg.epargne;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfsoft.one.cg.R;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_produits extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fproduits = inflater.inflate(R.layout.fragment_produits,container,false);
        TextView tvp = (TextView)fproduits.findViewById(R.id.prods);
        tvp.setText("Les produits disponibles");
        tvp.setTextSize(12f);
        return fproduits;
    }
}
