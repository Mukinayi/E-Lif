package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.NavigationActivity;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

import java.util.HashMap;

public class ConfirmationTransactionDeposit extends AppCompatActivity {
    NetworkConnection networkConnection;
    ProgressDialog pDialog;
    AlertDialog.Builder alb;
    Button btnvalidpay;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_transaction_deposit);
        Intent me = getIntent();

        networkConnection = new NetworkConnection(ConfirmationTransactionDeposit.this);
        pDialog = new ProgressDialog(ConfirmationTransactionDeposit.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargment en cours");
        final String amount = me.getStringExtra("amount");
        final String savingsid = networkConnection.storedDatas("savingid");
        final String borrowerid = networkConnection.storedDatas("borrowerid");
        btnvalidpay = (Button)findViewById(R.id.btnvalidpay);
        alb = new AlertDialog.Builder(ConfirmationTransactionDeposit.this);
        alb.setTitle("Deposité effectué");
        alb.setMessage("Déposit effectué avec succès au compte Epargne");
        alb.setCancelable(false);
        alb.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ConfirmationTransactionDeposit.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnvalidpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                HashMap dd = new HashMap();
                dd.put("amount",amount);
                dd.put("savingsid",savingsid);
                dd.put("borrowerid",borrowerid);

                if(networkConnection.isConnected()){
                    try {
                        PostResponseAsyncTask tache = new PostResponseAsyncTask(ConfirmationTransactionDeposit.this, dd, false, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                switch (s){
                                    case "180":
                                        networkConnection.writeToast("Balance insuffisante");
                                        pDialog.dismiss();
                                        break;
                                    case "201":
                                        networkConnection.writeToast("Echec deposit");
                                        pDialog.dismiss();
                                        break;
                                    case "":
                                        networkConnection.writeToast("Aucune reponse du serveur, veuillez ressayer");
                                        pDialog.dismiss();
                                        break;
                                    default:
                                        dialog = alb.create();
                                        dialog.show();
                                        break;
                                }
                                Log.i("computer",s);
                            }

                        });
                        tache.execute(networkConnection.getUrl()+"loanapi/APIS/confirmdeposit.php");
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

    }
}
