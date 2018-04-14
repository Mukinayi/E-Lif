package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    AlertDialog.Builder alb;
    AlertDialog dialog;
    ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ftransactions = inflater.inflate(R.layout.fragment_transaction,container,false);
        context = getContext();
        TextView labelMessage = (TextView)ftransactions.findViewById(R.id.labelMessage);
        networkConnection = new NetworkConnection(context);
        LinearLayout translay = (LinearLayout)ftransactions.findViewById(R.id.translay);
        LinearLayout layoutuncon = (LinearLayout)ftransactions.findViewById(R.id.layoutuncon);



        if(networkConnection.storedDatas("borrowerid")==null){
            layoutuncon.setVisibility(View.VISIBLE);
            translay.setVisibility(View.GONE);
            labelMessage.setText("Veuillez rélier un compte ou en créer");
            labelMessage.setTextSize(17f);

        }else{
            if(networkConnection.storedDatas("savingid")==null){
                labelMessage.setText("Veuillez rélier un produit d\'épargne");
                labelMessage.setTextSize(17f);
                layoutuncon.setVisibility(View.VISIBLE);
                translay.setVisibility(View.GONE);
            }else {
                labelMessage.setText("Effectuer une transaction");
                layoutuncon.setVisibility(View.GONE);
                translay.setVisibility(View.VISIBLE);
            }
        }

        EditText etccourant = (EditText)ftransactions.findViewById(R.id.etccourant);
        etccourant.setText(networkConnection.storedDatas("numcompte"));
        etccourant.setEnabled(false);
        final EditText etdepositamount = (EditText)ftransactions.findViewById(R.id.etdepositamount);
        final EditText etpincc = (EditText)ftransactions.findViewById(R.id.etpincc);

        Button btntransact = (Button)ftransactions.findViewById(R.id.btntransact);
        btntransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alb = new AlertDialog.Builder(context);
                alb.setTitle("Information");
                alb.setMessage("Cette partie est en construction, veuillez patienter quelques heures");
                dialog = alb.create();

                if(etdepositamount.getText().toString().isEmpty() || etpincc.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez remplir tous les chmaps");
                    dialog.dismiss();
                }else{
                    dialog.show();
                }
            }
        });







        return ftransactions;
    }
}
