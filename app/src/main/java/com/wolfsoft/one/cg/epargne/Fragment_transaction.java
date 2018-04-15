package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;
import com.wolfsoft.one.cg.payment.Confirmation;
import com.wolfsoft.one.cg.payment.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_transaction extends Fragment {
    NetworkConnection networkConnection;
    Context context;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    ProgressDialog pDialog;
    EditText etdepositamount;
    EditText etpincc;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View ftransactions = inflater.inflate(R.layout.fragment_transaction,container,false);
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

                final String URL = networkConnection.getUrl();

                EditText etccourant = (EditText)ftransactions.findViewById(R.id.etccourant);
                etccourant.setText(networkConnection.storedDatas("numcompte"));
                etccourant.setEnabled(false);
                etdepositamount = (EditText)ftransactions.findViewById(R.id.etdepositamount);
                etpincc = (EditText)ftransactions.findViewById(R.id.etpinlifoupay);
                pDialog = new ProgressDialog(context);
                pDialog.setTitle("Traitement en cours");
                pDialog.setMessage("Chargement en cours, veuillez patientez");
                //pDialog.setIcon(R.drawable.ic_lifouta);
                pDialog.setCancelable(false);


                Button btntransact = (Button)ftransactions.findViewById(R.id.btntransact);
                btntransact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Random num = new Random(9999);
                        final HashMap hashMap = new HashMap();
                        hashMap.put("senderaccount",networkConnection.storedDatas("numcompte"));
                        hashMap.put("receiveraccount",NetworkConnection.RECIPIENT_ACCOUNT);
                        hashMap.put("description","Approvisionnement du compte épargne");
                        hashMap.put("amount",etdepositamount.getText().toString());
                        hashMap.put("invoicenumber","LIF"+String.valueOf(num.nextInt()));
                        hashMap.put("senderpin",etpincc.getText().toString());
                        alb = new AlertDialog.Builder(context);
                        alb.setTitle("Confirmation Paiement");
                        alb.setCancelable(false);


                        if(etdepositamount.getText().toString().isEmpty() || etpincc.getText().toString().isEmpty()){
                            networkConnection.writeToast("Veuillez remplir tous les chmaps");
                            pDialog.dismiss();
                        }else{
                            pDialog.show();
                            if(networkConnection.isConnected()){
                                try {
                                    PostResponseAsyncTask pdata = new PostResponseAsyncTask(context, hashMap, false, new AsyncResponse() {
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
                                                    networkConnection.writeToast("Aucune reponse du serveur");
                                                    pDialog.dismiss();
                                                    break;
                                                default:
                                                    pDialog.dismiss();
                                                    try {
                                                        JSONArray jsonArray = new JSONArray(s);
                                                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                                                        //View alview = inflater.inflate(R.layout.alertdialogview,container);
                                                        View alview = LayoutInflater.from(context).inflate(R.layout.alertdialogview, null);
                                                        alb.setView(alview);
                                                        dialog = alb.create();
                                                        dialog.show();

                                                        final String retOtp = jsonObject.getString("optclient");
                                                        final String recipientaccount = jsonObject.getString("recipientaccount");
                                                        final String transtype = jsonObject.getString("transtype");
                                                        final String senderaccount = jsonObject.getString("senderaccount");
                                                        final EditText etcodeconfpay = (EditText)alview.findViewById(R.id.etcodeconfpay);
                                                        final String amount = jsonObject.getString("amount");
                                                        Button btndialogconfig = (Button)alview.findViewById(R.id.btndialogconfig);
                                                        ImageButton imgdialogresend = (ImageButton)alview.findViewById(R.id.imgdialogresend);
                                                        final ProgressBar progressBar = (ProgressBar)alview.findViewById(R.id.progressBar);

                                                        btndialogconfig.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                HashMap dd = new HashMap();
                                                                dd.put("savingsid",networkConnection.storedDatas("savingid"));
                                                                dd.put("borrowerid",networkConnection.storedDatas("borrowerid"));
                                                                dd.put("recipientaccount",recipientaccount);
                                                                dd.put("transtype",transtype);
                                                                dd.put("senderaccount",senderaccount);
                                                                dd.put("optclient",retOtp);
                                                                dd.put("amount",amount);

                                                                if(etcodeconfpay.getText().toString().isEmpty()){
                                                                    networkConnection.writeToast("Veuillez renseigner le code OTP");
                                                                }else{
                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                    if(networkConnection.isConnected()){
                                                                        try {
                                                                            PostResponseAsyncTask ppt = new PostResponseAsyncTask(context, dd, false, new AsyncResponse() {
                                                                                @Override
                                                                                public void processFinish(String s) {
                                                                                    Log.i("jade",s);
                                                                                    switch (s){
                                                                                        case "170":
                                                                                            networkConnection.writeToast("OTP non existant");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        case "171":
                                                                                            networkConnection.writeToast("OTP expiré");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        case "172":
                                                                                            networkConnection.writeToast("Echec mis à jour");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        case "173":
                                                                                            networkConnection.writeToast("Aucune reponse du Payment Gateway");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        case "201":
                                                                                            networkConnection.writeToast("Paiement réussie, mais Echec Deposit au compte courant");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        case "":
                                                                                            networkConnection.writeToast("Paiement réussie, mais serveur");
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            break;
                                                                                        default:
                                                                                            //dialog.dismiss();
                                                                                            networkConnection.writeToast("Déposit effectué avec succès");
                                                                                            progressBar.setVisibility(View.GONE);
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
                                                        });

                                                        imgdialogresend.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                HashMap dt = new HashMap();
                                                                dt.put("senderaccount",senderaccount);
                                                                dt.put("optclient",retOtp);
                                                                dt.put("transtype",transtype);
                                                                pDialog.setTitle("Renvoi OTP");
                                                                pDialog.show();
                                                                if(networkConnection.isConnected()){
                                                                    try {
                                                                        PostResponseAsyncTask resend = new PostResponseAsyncTask(context, dt, false, new AsyncResponse() {
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
        return ftransactions;
    }

}
