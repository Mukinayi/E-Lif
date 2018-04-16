package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class FromCCtoSC extends AppCompatActivity {
    NetworkConnection networkConnection;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    ProgressDialog pDialog;
    EditText etdepositamount;
    EditText etpincc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_ccto_sc);
        networkConnection = new NetworkConnection(FromCCtoSC.this);

        final String URL = networkConnection.getUrl();

        EditText etccourant = (EditText)findViewById(R.id.etccourant);
        etccourant.setText(networkConnection.storedDatas("numcompte"));
        etccourant.setEnabled(false);
        etdepositamount = (EditText)findViewById(R.id.etdepositamount);
        etpincc = (EditText)findViewById(R.id.etpinlifoupay);
        pDialog = new ProgressDialog(FromCCtoSC.this);
        pDialog.setTitle("Traitement en cours");
        pDialog.setMessage("Chargement en cours, veuillez patientez");
        pDialog.setIcon(R.drawable.ic_lifouta);
        pDialog.setCancelable(false);
        Button btntransact = (Button)findViewById(R.id.btntransact);
        btntransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("senderaccount",networkConnection.storedDatas("numcompte"));
                hashMap.put("receiveraccount",NetworkConnection.RECIPIENT_ACCOUNT);
                hashMap.put("description","Approvisionnement du compte épargne");
                hashMap.put("amount",etdepositamount.getText().toString());
                Random num = new Random(9999);
                hashMap.put("invoicenumber","LIF"+String.valueOf(num.nextInt()));
                hashMap.put("senderpin",etpincc.getText().toString());
                alb = new AlertDialog.Builder(FromCCtoSC.this);
                alb.setTitle("Confirmation Paiement");
                alb.setCancelable(false);


                if(etdepositamount.getText().toString().isEmpty() || etpincc.getText().toString().isEmpty()){
                    networkConnection.writeToast("Veuillez remplir tous les chmaps");
                    pDialog.dismiss();
                }else{
                    pDialog.show();
                    if(networkConnection.isConnected()){
                        try {
                            PostResponseAsyncTask pdata = new PostResponseAsyncTask(FromCCtoSC.this, hashMap, false, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.i("seth",s);
                                    switch (s){
                                        case "180":
                                            networkConnection.writeToast("Votre compte n\'est pas reconnu");
                                            pDialog.dismiss();
                                            break;
                                        case "181":
                                            networkConnection.writeToast("Votre compte est désactivé");
                                            pDialog.dismiss();
                                            break;
                                        case "182":
                                            networkConnection.writeToast("Votre PIN est incorrecte");
                                            pDialog.dismiss();
                                            break;
                                        case "183":
                                            networkConnection.writeToast("Compte bénéficiaire non reconnu");
                                            pDialog.dismiss();
                                            break;
                                        case "184":
                                            networkConnection.writeToast("Compte bénéficiaire désactivé");
                                            pDialog.dismiss();
                                            break;
                                        case "185":
                                            networkConnection.writeToast("Votre solde est insuffisant");
                                            pDialog.dismiss();
                                            break;
                                        case "":
                                            networkConnection.writeToast("VAucune reponse du serveur");
                                            pDialog.dismiss();
                                            break;
                                        default:
                                            pDialog.dismiss();
                                            try {
                                                JSONArray jsonArray = new JSONArray(s);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);



                                                final String retOtp = jsonObject.getString("optclient");
                                                final String recipientaccount = jsonObject.getString("recipientaccount");
                                                final String transtype = jsonObject.getString("transtype");
                                                final String senderaccount = jsonObject.getString("senderaccount");
                                                final String amount = jsonObject.getString("amount");

                                                Intent conf = new Intent(FromCCtoSC.this,ConfirmerDeposit.class);
                                                conf.putExtra("optclient",retOtp);
                                                conf.putExtra("recipientaccount",recipientaccount);
                                                conf.putExtra("transtype",transtype);
                                                conf.putExtra("senderaccount",senderaccount);
                                                conf.putExtra("amount",amount);
                                                startActivity(conf);
                                                finish();
                                            }catch (JSONException je){
                                                networkConnection.writeToast("Erreur de données");
                                                Log.i("donnes",s);
                                                pDialog.dismiss();
                                            }

                                            break;
                                    }
                                    Log.i("retourdeposit",s);
                                    Log.i("retourdepo",etpincc.getText().toString());
                                }
                            });
                            pdata.execute(URL+"lifoutacourant/APIS/payer.php");
                        }catch (Exception e){
                            networkConnection.writeToast("Erreur du serveur");
                            pDialog.dismiss();
                        }
                    }else{
                        networkConnection.writeToast("Erreur connexion internet");
                        pDialog.dismiss();
                    }

                }
            }
        });
    }
}
