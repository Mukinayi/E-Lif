package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.FragmentManager;
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
    /*NetworkConnection networkConnection;
    Context context;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    ProgressDialog pDialog;
    EditText etdepositamount;
    EditText etpincc;
    ProgressBar progressBar;
    public String URL;
    public String retour;
    public View v;
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

                                                        final String retOtp = jsonObject.getString("optclient");
                                                        final String recipientaccount = jsonObject.getString("recipientaccount");
                                                        final String transtype = jsonObject.getString("transtype");
                                                        final String senderaccount = jsonObject.getString("senderaccount");
                                                        final String amount = jsonObject.getString("amount");

                                                        Confirmdeposit confirmdeposit = new Confirmdeposit();
                                                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                        fragmentManager.beginTransaction().replace(R.id.main_frag,confirmdeposit).commit();

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

    public String Confirmer(String recipientaccount,String transtype,String senderaccount,String retOtp){
        networkConnection = new NetworkConnection(getContext());
        HashMap dd = new HashMap();
        //dd.put("savingsid",networkConnection.storedDatas("savingid"));
        //dd.put("borrowerid",networkConnection.storedDatas("borrowerid"));
        dd.put("recipientaccount",recipientaccount);
        dd.put("transtype",transtype);
        dd.put("senderaccount",senderaccount);
        dd.put("optclient",retOtp);
        //dd.put("amount",amount);
        if(networkConnection.isConnected()){
            try {
                PostResponseAsyncTask ppt = new PostResponseAsyncTask(context, dd, false, new AsyncResponse() {
                    @Override
                    public void processFinish(final String s) {
                        retour = s;
                    }
                });
                ppt.execute(networkConnection.getUrl()+"loanapi/APIS/confirmtransaction.php");
            }catch (Exception e){
                networkConnection.writeToast("Erreur du serveur");
                pDialog.dismiss();
            }
        }else{
            networkConnection.writeToast("Erreur connexion internet");
            pDialog.dismiss();
        }
        return retour;
    }*/

}
