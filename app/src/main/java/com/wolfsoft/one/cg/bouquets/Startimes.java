package com.wolfsoft.one.cg.bouquets;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

public class Startimes extends AppCompatActivity {
    Button btnstartimes;
    EditText etstartimes;
    ProgressDialog progressDialog;
    NetworkConnection networkConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startimes);

        etstartimes = (EditText)findViewById(R.id.etstartimes);
        btnstartimes = (Button)findViewById(R.id.btnstartimes);
        networkConnection = new NetworkConnection(Startimes.this);

        progressDialog = new ProgressDialog(Startimes.this);
        progressDialog.setMessage("En cours traitement");

        btnstartimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etstartimes.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez renseigner le numéro de la carte");
                }else{
                    progressDialog.show();

                }
            }
        });
    }
}
