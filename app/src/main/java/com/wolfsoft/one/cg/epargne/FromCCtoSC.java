package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        /*networkConnection = new NetworkConnection(FromCCtoSC.this);

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

                                                //View alview = inflater.inflate(R.layout.alertdialogview,container);
                                                View alview = getLayoutInflater().inflate(R.layout.alertdialogview,null);

                                                final EditText etcodeconfpay = (EditText)alview.findViewById(R.id.etcodeconfpay);

                                                alb.setView(alview);

                                                final String retOtp = jsonObject.getString("optclient");
                                                final String recipientaccount = jsonObject.getString("recipientaccount");
                                                final String transtype = jsonObject.getString("transtype");
                                                final String senderaccount = jsonObject.getString("senderaccount");
                                                        alb.setNegativeButton("Confirmer", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(final DialogInterface dialog, int which) {
                                                                if(etcodeconfpay.getText().toString().isEmpty()){
                                                                    networkConnection.writeToast("Veuillez renseigner le code OTP");
                                                                }else{
                                                                    pDialog.show();
                                                                    if(etcodeconfpay.getText().toString() != retOtp){
                                                                        networkConnection.writeToast("OTP incorrecte");
                                                                    }else{
                                                                        HashMap dd = new HashMap();
                                                                        dd.put("savingsid",networkConnection.storedDatas("savingid"));
                                                                        dd.put("borrowerid",networkConnection.storedDatas("borrowerid"));
                                                                        dd.put("recipientaccount",recipientaccount);
                                                                        dd.put("transtype",transtype);
                                                                        dd.put("senderaccount",senderaccount);
                                                                        dd.put("optclient",retOtp);

                                                                        if(networkConnection.isConnected()){
                                                                            try {
                                                                                PostResponseAsyncTask ppt = new PostResponseAsyncTask(FromCCtoSC.this, dd, false, new AsyncResponse() {
                                                                                    @Override
                                                                                    public void processFinish(String s) {
                                                                                        switch (s){
                                                                                            case "170":
                                                                                                networkConnection.writeToast("OTP non existant");
                                                                                                pDialog.dismiss();
                                                                                                break;
                                                                                            case "171":
                                                                                                networkConnection.writeToast("OTP expiré");
                                                                                                pDialog.dismiss();
                                                                                                break;
                                                                                            case "172":
                                                                                                networkConnection.writeToast("Echec mis à jour");
                                                                                                pDialog.dismiss();
                                                                                                break;
                                                                                            case "173":
                                                                                                networkConnection.writeToast("Aucune reponse du Payment Gateway");
                                                                                                pDialog.dismiss();
                                                                                                break;
                                                                                            case "201":
                                                                                                networkConnection.writeToast("Paiement réussie, mais Echec Deposit au compte courant");
                                                                                                pDialog.dismiss();
                                                                                                try {
                                                                                                    Thread.sleep(2000);
                                                                                                } catch (InterruptedException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                                dialog.dismiss();
                                                                                                break;
                                                                                            case "":
                                                                                                networkConnection.writeToast("Paiement réussie, mais serveur");
                                                                                                pDialog.dismiss();
                                                                                                try {
                                                                                                    Thread.sleep(2000);
                                                                                                } catch (InterruptedException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                                dialog.dismiss();
                                                                                                break;
                                                                                            default:
                                                                                                dialog.dismiss();
                                                                                                networkConnection.writeToast("Déposit effectué avec succès");
                                                                                                try {
                                                                                                    Thread.sleep(2000);
                                                                                                } catch (InterruptedException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                                dialog.dismiss();
                                                                                                break;
                                                                                        }
                                                                                    }
                                                                                });
                                                                                ppt.execute(URL+"loanapi/APIS/confirmdeposit.php");
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
                                                            }
                                                        });

                                                        alb.setPositiveButton("Renvoi OTP", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                HashMap dt = new HashMap();
                                                                dt.put("senderaccount",senderaccount);
                                                                dt.put("optclient",retOtp);
                                                                dt.put("transtype",transtype);
                                                                pDialog.setTitle("Renvoi OTP");
                                                                pDialog.show();
                                                                if(networkConnection.isConnected()){
                                                                    try {
                                                                        PostResponseAsyncTask resend = new PostResponseAsyncTask(FromCCtoSC.this, dt, false, new AsyncResponse() {
                                                                            @Override
                                                                            public void processFinish(String s) {
                                                                                try {
                                                                                    switch (s){
                                                                                        case "180":
                                                                                            pDialog.dismiss();
                                                                                            networkConnection.writeToast("Aucun OTP disponible");
                                                                                            break;
                                                                                        case "201":
                                                                                            pDialog.dismiss();
                                                                                            networkConnection.writeToast("Echec renvoi OTP");
                                                                                            break;
                                                                                        default:
                                                                                            pDialog.dismiss();
                                                                                            networkConnection.writeToast("OTP renvoyé avec succès");
                                                                                            break;
                                                                                    }
                                                                                }catch (Exception e){
                                                                                    pDialog.dismiss();
                                                                                    networkConnection.writeToast("Aucune reponse du serveur");
                                                                                }
                                                                            }
                                                                        });
                                                                        resend.execute(URL+"lifoutacourant/APIS/resendsms.php");
                                                                    }catch (Exception e){
                                                                        pDialog.dismiss();
                                                                        networkConnection.writeToast("Erreur connexion au serveur");
                                                                    }
                                                                }else{
                                                                    pDialog.dismiss();
                                                                    networkConnection.writeToast("Erreur connexion internet");
                                                                }
                                                            }
                                                        });
                                                dialog = alb.create();
                                                dialog.show();
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
        });*/
    }
}
