package com.wolfsoft.one.cg.payment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.MainActivity;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.NavigationActivity;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Confirmation extends AppCompatActivity {
    Button btnOTPconf;
    EditText etOTPconf;
    ImageButton imgbtnresendotp;
    NetworkConnection networkConnection;
    ProgressDialog progressDialog;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent me = getIntent();
        final String recipientaccount = me.getStringExtra("recipientaccount");
        final String senderaccount = me.getStringExtra("senderaccount");
        final String optclient = me.getStringExtra("optclient");
        final String transtype = me.getStringExtra("transtype");

        networkConnection = new NetworkConnection(Confirmation.this);
        progressDialog = new ProgressDialog(Confirmation.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Traitement");
        progressDialog.setMessage("EN cours de chargement");
        final String URL = networkConnection.getUrl();

        btnOTPconf = (Button)findViewById(R.id.btnOTPconf);
        etOTPconf = (EditText)findViewById(R.id.etOTPconf);
        imgbtnresendotp = (ImageButton)findViewById(R.id.imgbtnresendotp);
        alb = new AlertDialog.Builder(Confirmation.this);
        alb.setTitle("Résumé du paiement");
        alb.setIcon(R.drawable.ic_lifouta);
        final StringBuilder stb = new StringBuilder();

        btnOTPconf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap dt = new HashMap();
                dt.put("senderaccount",senderaccount);
                dt.put("receiveraccount",recipientaccount);
                dt.put("optclient",etOTPconf.getText().toString());
                dt.put("transtype",transtype);

                if(etOTPconf.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez renseigner le OTP");
                    progressDialog.dismiss();
                }else{
                    progressDialog.show();
                    if(networkConnection.isConnected()){
                        try {
                            PostResponseAsyncTask tache = new PostResponseAsyncTask(Confirmation.this, dt, false, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    switch (s){
                                        case "180":
                                            networkConnection.writeToast("OTP non reconnu");
                                            progressDialog.dismiss();
                                            break;
                                        case "181":
                                            networkConnection.writeToast("OTP expiré");
                                            progressDialog.dismiss();
                                            break;
                                        case "201":
                                            networkConnection.writeToast("Echec transaction");
                                            progressDialog.dismiss();
                                            break;
                                        case "":
                                            networkConnection.writeToast("Aucune reponse du serveur");
                                            progressDialog.dismiss();
                                            break;
                                        default:
                                            progressDialog.dismiss();
                                            try {
                                                JSONArray jsonArray = new JSONArray(s);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                stb.append("Expéditeur : " +jsonObject.getString("senderaccount")+"\n");
                                                stb.append("Bénéficiaire : "+jsonObject.getString("recipientaccount")+"\n");
                                                stb.append("Montant : "+jsonObject.getString("amount")+" CFA\n");
                                                stb.append("Frais : "+jsonObject.getString("systemfees")+" CFA\n");
                                                stb.append("Date : "+jsonObject.getString("datetimetrans")+"\n");
                                                stb.append("Réf : "+jsonObject.getString("idtrans")+"\n\n");
                                                alb.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent main = new Intent(Confirmation.this, NavigationActivity.class);
                                                        startActivity(main);
                                                        finish();
                                                    }
                                                });
                                                alb.setMessage(stb.toString());

                                                alb.setPositiveButton("Partager", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent shareInt = new Intent(Intent.ACTION_SEND);
                                                        shareInt.setType("text/plain");
                                                        String subject = "Résumé transaction";
                                                        String Body = stb.toString();
                                                        shareInt.putExtra(Intent.EXTRA_SUBJECT,subject);
                                                        shareInt.putExtra(Intent.EXTRA_TEXT,Body);
                                                        startActivity(Intent.createChooser(shareInt,"Choisissez un application"));
                                                        finish();
                                                    }
                                                });
                                                dialog = alb.create();
                                                dialog.show();


                                            }catch (JSONException e){
                                                networkConnection.writeToast("Erreur des données");
                                                progressDialog.dismiss();
                                            }


                                            break;
                                    }
                                    Log.i("retpay",s);
                                }
                            });
                            tache.execute(URL+"lifoutacourant/APIS/confirmtransaction.php");
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

        imgbtnresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap dt = new HashMap();
                dt.put("senderaccount",senderaccount);
                dt.put("optclient",optclient);
                dt.put("transtype",transtype);
                progressDialog.setTitle("Renvoi OTP");
                progressDialog.show();
                if(networkConnection.isConnected()){
                    try {
                        PostResponseAsyncTask resend = new PostResponseAsyncTask(Confirmation.this, dt, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                try {
                                    switch (s){
                                        case "180":
                                            progressDialog.dismiss();
                                            networkConnection.writeToast("Aucun OTP disponible");
                                            break;
                                        case "201":
                                            progressDialog.dismiss();
                                            networkConnection.writeToast("Echec renvoi OTP");
                                            break;
                                        case "":
                                            progressDialog.dismiss();
                                            networkConnection.writeToast("Aucune reponse du serveur");
                                            break;
                                        default:
                                            progressDialog.dismiss();
                                            networkConnection.writeToast("OTP renvoyé avec succès");
                                            break;
                                    }
                                }catch (Exception e){
                                    progressDialog.dismiss();
                                    networkConnection.writeToast("Aucune reponse du serveur");
                                }
                            }
                        });
                        resend.execute(URL+"lifoutacourant/APIS/resendsms.php");
                    }catch (Exception e){
                        progressDialog.dismiss();
                        networkConnection.writeToast("Erreur connexion au serveur");
                    }
                }else{
                    progressDialog.dismiss();
                    networkConnection.writeToast("Erreur connexion internet");
                }
            }
        });
    }
}
