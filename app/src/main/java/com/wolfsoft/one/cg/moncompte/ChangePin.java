package com.wolfsoft.one.cg.moncompte;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.NavigationActivity;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

import java.util.HashMap;

public class ChangePin extends AppCompatActivity {
    Button btnchangepin;
    EditText etoldpass,etnewpin,etconfpin;
    NetworkConnection networkConnection;
    ProgressDialog pDialog;
    AlertDialog.Builder alb;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        networkConnection = new NetworkConnection(ChangePin.this);
        pDialog = new ProgressDialog(ChangePin.this);
        alb = new AlertDialog.Builder(ChangePin.this);
        alb.setTitle("Changement PIN");
        pDialog.setMessage("Chargement");
        pDialog.setCancelable(false);
        final String URL = networkConnection.getUrl();
        etoldpass = (EditText)findViewById(R.id.etoldpass);
        etnewpin = (EditText)findViewById(R.id.etnewpin);
        etconfpin = (EditText)findViewById(R.id.etconfpin);
        btnchangepin = (Button)findViewById(R.id.btnchangepin);

        btnchangepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap ddt = new HashMap();
                ddt.put("myaccount",networkConnection.storedDatas("numcompte"));
                ddt.put("oldpin",etoldpass.getText().toString());
                ddt.put("newpin",etnewpin.getText().toString());
                if(etoldpass.getText().toString().isEmpty() || etnewpin.getText().toString().isEmpty() || etconfpin.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez remplir tous les champs");
                    pDialog.dismiss();
                }else{
                    if(!etconfpin.getText().toString().equals(etnewpin.getText().toString())){
                        networkConnection.writeToast("Les nouveaux PIN doivent être identique");
                        pDialog.dismiss();
                    }else{
                        pDialog.show();
                        if(networkConnection.isConnected()){
                            try {
                                PostResponseAsyncTask tache = new PostResponseAsyncTask(ChangePin.this, ddt, false, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String s) {
                                        switch (s){
                                            case "180":
                                                    networkConnection.writeToast("Votre numéro de compte n'est pas reconnu");
                                                    pDialog.dismiss();
                                                break;
                                            case "181":
                                                    networkConnection.writeToast("Ancien PIN incorrect");
                                                    pDialog.dismiss();
                                                break;
                                            case "201":
                                                    networkConnection.writeToast("Echec changement PIN");
                                                    pDialog.dismiss();
                                                 break;
                                            case "":
                                                    networkConnection.writeToast("Aucune reponse du serveur");
                                                    pDialog.dismiss();
                                                break;
                                            default:
                                                alb.setMessage("Votre PIN a été modifié avec succès!!!\n Veuillez vous rassurer que vous l'avez retenu.");
                                                alb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent main = new Intent(ChangePin.this, NavigationActivity.class);
                                                        startActivity(main);
                                                        finish();
                                                    }
                                                });
                                                dialog = alb.create();
                                                dialog.show();
                                                break;
                                        }
                                    }
                                });
                                tache.execute(URL+"lifoutacourant/APIS/changementpin.php");
                            }catch (Exception e){
                                networkConnection.writeToast("Erreur de connexion au serveur");
                                pDialog.dismiss();
                            }
                        }else{
                            networkConnection.writeToast("Erreur connexion internet");
                            pDialog.dismiss();
                        }
                    }
                }
            }
        });


    }
}
