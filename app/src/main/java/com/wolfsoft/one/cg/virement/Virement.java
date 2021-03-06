package com.wolfsoft.one.cg.virement;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Virement extends AppCompatActivity {
    Button btnvirement;
    EditText etrecipient,etsenderpin,etvirementamount;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    StringBuilder stringBuilder = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virement);


        networkConnection = new NetworkConnection(Virement.this);
        progressDialog = new ProgressDialog(Virement.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Traitement");
        progressDialog.setMessage("En cours de chargement");



        final String  URL = networkConnection.getUrl();
        final String numcompte = networkConnection.storedDatas("numcompte");

        etrecipient = (EditText)findViewById(R.id.etrecipient);
        etsenderpin = (EditText)findViewById(R.id.etsenderpin);
        etvirementamount = (EditText)findViewById(R.id.etvirementamount);
        btnvirement = (Button)findViewById(R.id.btnvirement);
        builder = new AlertDialog.Builder(Virement.this);

        btnvirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap dt = new HashMap();
                dt.put("beneficiaire",etrecipient.getText().toString());
                dt.put("expediteur",numcompte);
                dt.put("montant",etvirementamount.getText().toString());
                dt.put("senderpin",etsenderpin.getText().toString());
                if(etrecipient.getText().toString().isEmpty() || etsenderpin.getText().toString().isEmpty() || etvirementamount.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez remplir tous les champs");
                    progressDialog.dismiss();
                }else{
                    progressDialog.show();
                    if(networkConnection.isConnected()){
                        try {
                            PostResponseAsyncTask tache = new PostResponseAsyncTask(Virement.this, dt, false, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    try {
                                        switch (s){
                                            case "180":
                                                networkConnection.writeToast("Le compte bénéficiaire n\'existe pas");
                                                progressDialog.dismiss();
                                                break;
                                            case "181":
                                                networkConnection.writeToast("Le compte bénéficiaire est désactivé");
                                                progressDialog.dismiss();
                                                break;
                                            case "182":
                                                networkConnection.writeToast("Votre compte n\'existe pas");
                                                progressDialog.dismiss();
                                                break;
                                            case "183":
                                                networkConnection.writeToast("Votre compte est désactivé");
                                                progressDialog.dismiss();
                                                break;
                                            case "184":
                                                networkConnection.writeToast("Votre solde est insuffisant");
                                                progressDialog.dismiss();
                                                break;
                                            case "185":
                                                networkConnection.writeToast("Votre PIN est incorrecte");
                                                progressDialog.dismiss();
                                                break;
                                            default:
                                                progressDialog.dismiss();
                                                networkConnection.writeToast("Virement réussi");
                                                try {
                                                    JSONArray jsonArray = new JSONArray(s);
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    stringBuilder.append("VIREMENT" +"\n");
                                                    stringBuilder.append("Expéditeur : " +jsonObject.getString("senderaccount") +"\n");
                                                    stringBuilder.append("Bénéficiaire : " +jsonObject.getString("recipientaccount") +"\n");
                                                    stringBuilder.append("Montant : " +jsonObject.getString("amount") +" CFA \n");
                                                    stringBuilder.append("Frais : " +jsonObject.getString("systemfees") +" CFA\n");
                                                    stringBuilder.append("Etat : Réussi \n");
                                                    stringBuilder.append("Réf : " +jsonObject.getString("idtrans") +"\n");

                                                    builder.setTitle("Détail de la transaction");
                                                    builder.setCancelable(false);
                                                    builder.setMessage(stringBuilder.toString());
                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Virement.this, NavigationActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    alertDialog = builder.create();
                                                    alertDialog.show();

                                                }catch (JSONException je){
                                                    networkConnection.writeToast("Erreur de données");
                                                    progressDialog.dismiss();
                                                }
                                                break;
                                        }
                                    }catch (Exception e){
                                        networkConnection.writeToast("Aucune reponse du serveur");
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            tache.execute(URL+"lifoutacourant/APIS/virement.php");
                        }catch (Exception e){
                            networkConnection.writeToast("Erreur serveur");
                            progressDialog.dismiss();
                        }
                    }else{
                        networkConnection.writeToast("Erreur connexion internet");
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}
