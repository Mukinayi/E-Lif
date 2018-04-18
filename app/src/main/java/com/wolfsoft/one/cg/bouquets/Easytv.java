package com.wolfsoft.one.cg.bouquets;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

public class Easytv extends AppCompatActivity {
    Button btneasy;
    EditText eteasy;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easytv);

        eteasy = (EditText)findViewById(R.id.eteasy);
        btneasy = (Button)findViewById(R.id.btneasy);
        networkConnection = new NetworkConnection(Easytv.this);

        progressDialog = new ProgressDialog(Easytv.this);
        progressDialog.setMessage("En cours traitement");

        btneasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eteasy.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez renseigner le numéro de la carte");
                }else{
                    progressDialog.show();

                }
            }
        });


    }
}
