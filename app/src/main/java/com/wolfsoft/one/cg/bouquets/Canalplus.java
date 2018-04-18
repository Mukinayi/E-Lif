package com.wolfsoft.one.cg.bouquets;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

public class Canalplus extends AppCompatActivity {
    Button btncanalplus;
    EditText etcanalcardnumber;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canalplus);

        etcanalcardnumber = (EditText)findViewById(R.id.etcanalcardnumber);
        btncanalplus = (Button)findViewById(R.id.btncanalplus);
        networkConnection = new NetworkConnection(Canalplus.this);

        progressDialog = new ProgressDialog(Canalplus.this);
        progressDialog.setMessage("En cours traitement");

        btncanalplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etcanalcardnumber.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez renseigner le numéro de la carte");
                }else{
                    progressDialog.show();

                }
            }
        });


    }
}
