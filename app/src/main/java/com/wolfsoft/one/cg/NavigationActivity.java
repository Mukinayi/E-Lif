package com.wolfsoft.one.cg;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.credits.Credits;
import com.wolfsoft.one.cg.network.NetworkConnection;
import com.wolfsoft.one.cg.payment.Payment;
import com.wolfsoft.one.cg.transfert.Transfert;
import com.wolfsoft.one.cg.virement.Virement;

import java.util.ArrayList;
import java.util.HashMap;


public class NavigationActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    AlertDialog.Builder alb;
    NetworkConnection networkConnection;
    AlertDialog dialog;
    LinearLayout linearLayout;
    // http://192.168.1.130/chetana/bikes.php

    private Toolbar toolbar;

    private RelativeLayout relativeClose;

    // It is for horizontal bar chart

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private NavigationAdapter adapter;
    private ListView list;
    private ArrayList<NavigationItems> items;


    private String[] NAME = {"Mon solde", "Virement" , "Transfert", "Payer", "Crédits Téléphone", "Demande crédit","Compte Epargne",
            "Paiement SNE","Paiement SNDE","Bouquets numériques"
           };


    Fragment newFragment;
    android.support.v4.app.FragmentTransaction transaction;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        setToolbar();




        relativeClose = (RelativeLayout)findViewById(R.id.relativeClose);


        transaction = getSupportFragmentManager().beginTransaction();
        newFragment = new HomeFragment();
        transaction.replace(R.id.frame_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();


            list = (ListView) findViewById(R.id.list);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        items = new ArrayList<NavigationItems>();




            for (int i = 0; i < NAME.length; i++) {

                    NavigationItems nav = new NavigationItems(NAME[i] );
                    items.add(nav);

        }

            adapter = new NavigationAdapter(NavigationActivity.this, items);
            list.setAdapter(adapter);


        list.setSelection(0);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (position){
                        case 0:
                                monsolde();
                            break;
                        case 1:
                            Intent intent = new Intent(NavigationActivity.this, Virement.class);
                            startActivity(intent);
                            break;
                        case 2:
                            Intent transfert = new Intent(NavigationActivity.this, Transfert.class);
                            startActivity(transfert);
                            break;
                        case 3:
                            Intent paiement = new Intent(NavigationActivity.this, Payment.class);
                            startActivity(paiement);
                            break;
                        case 4:
                            Intent credits = new Intent(NavigationActivity.this, Credits.class);
                            startActivity(credits);
                            break;
                    }
                    drawerLayout.closeDrawer(relativeClose);
                }


            });


            initDrawer();


    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,toolbar , R.string.drawer_open, R.string.drawer_close
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);



            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);



            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }









    private void setToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("");



    }





    @Override
    public void onBackPressed() {

    }

    public void monsolde(){
        networkConnection = new NetworkConnection(NavigationActivity.this);
        alb = new AlertDialog.Builder(NavigationActivity.this);
        final StringBuilder str = new StringBuilder();
        final String URL = networkConnection.getUrl();
        final String numcompte = networkConnection.storedDatas("numcompte");
        progressDialog = new ProgressDialog(NavigationActivity.this);
        progressDialog.setTitle("Demande de solde");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Récupération du solde");
        progressDialog.show();

        HashMap dt = new HashMap();
        dt.put("moncompte",numcompte);
        if(networkConnection.isConnected()){
            try {
                PostResponseAsyncTask p = new PostResponseAsyncTask(NavigationActivity.this, dt, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        try {
                            switch (s){
                                case "180":
                                    networkConnection.writeToast("Une erreure est survenue");
                                    progressDialog.dismiss();
                                    break;
                                case "":
                                    networkConnection.writeToast("Aucune reponse du serveur");
                                    progressDialog.dismiss();
                                    break;
                                default:
                                    progressDialog.dismiss();
                                    alb.setTitle("Information du solde");
                                    str.append("Votre solde est de : \n\n\n");
                                    str.append(String.format("%,.2f",Double.parseDouble(s)) +" CFA");
                                    alb.setMessage(str.toString());
                                    alb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog = alb.create();
                                    dialog.show();
                                    LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.hf);

                                    //Bitmap b = networkConnection.Capturer(relativeLayout);
                                    //networkConnection.SaveScreen(b,"SOLDE");


                                    break;
                            }
                        }catch (Exception e){
                            networkConnection.writeToast("Une erreur est survenue");
                            progressDialog.dismiss();
                        }
                    }
                });
                p.execute(URL+"lifoutacourant/APIS/solde.php");
            }catch (Exception e){
                networkConnection.writeToast("Erreur connexion serveur");
                progressDialog.dismiss();
            }
        }else{
            networkConnection.writeToast("Erreur connexion internet");
            progressDialog.dismiss();
        }
    }
}
