package com.wolfsoft.one.cg.epargne;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_transaction extends Fragment {
    NetworkConnection networkConnection;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ftransactions = inflater.inflate(R.layout.fragment_transaction,container,false);
        context = getContext();
        TextView labelMessage = (TextView)ftransactions.findViewById(R.id.labelMessage);
        networkConnection = new NetworkConnection(context);
        if(networkConnection.storedDatas("borrowerid")==null){
            labelMessage.setText("Veuillez rélier un ");
            labelMessage.setTextSize(17f);
        }else{
            if(networkConnection.storedDatas("savingid")==null){
                labelMessage.setText("Veuillez rélier un produit d\'épargne au ");
                labelMessage.setTextSize(17f);
            }else {
                labelMessage.setText("Effectuer une transaction");
            }
        }





        return ftransactions;
    }
}
