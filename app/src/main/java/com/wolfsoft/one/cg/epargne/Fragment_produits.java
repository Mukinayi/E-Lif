package com.wolfsoft.one.cg.epargne;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.adapter.CustomAdapter;
import com.wolfsoft.one.cg.adapter.DataModel;
import com.wolfsoft.one.cg.network.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_produits extends Fragment {
    @Nullable
    NetworkConnection networkConnection;
    ListView listView;
    static CustomAdapter customAdapter;
    ArrayList<DataModel> dataModels;
    ProgressDialog progressDialog;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fproduits = inflater.inflate(R.layout.fragment_produits,container,false);
        TextView tvp = (TextView)fproduits.findViewById(R.id.prods);
        tvp.setText("Les produits d\'épargne disponibles");
        tvp.setTextSize(17f);
        final Context context = getContext();
        networkConnection = new NetworkConnection(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Recherche des produits");
        final String URL = networkConnection.getUrl();
        listView = (ListView)fproduits.findViewById(R.id.lvproducts);
        dataModels = new ArrayList<>();
        progressDialog.setMessage("Récuperation des produits en cours...");
        progressDialog.setCancelable(false);
        //
        alb = new AlertDialog.Builder(context);
        alb.setCancelable(true);


        try {
            if (networkConnection.isConnected()){
                HashMap postData = new HashMap();
                postData.put("svpro","seth");
                progressDialog.show();
                PostResponseAsyncTask tache = new PostResponseAsyncTask(context, postData, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        switch (s){
                            case "180":
                                    networkConnection.writeToast("Aucun produit disponible");
                                    progressDialog.dismiss();
                                break;
                            default:
                                progressDialog.setMessage("Récuperation terminée");
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                                try {
                                        JSONArray jsonArray = new JSONArray(s);
                                        for(int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            dataModels.add(new DataModel("Epargne "+jsonObject.getString("name"), String.format("%,.2f",Double.parseDouble(jsonObject.getString("minimum_balance"))) +" CFA", jsonObject.getString("interest_rate") +" %",jsonObject.getString("interest_posting")+" mois"));
                                        }
                                        customAdapter = new CustomAdapter(dataModels, context);
                                        listView.setAdapter(customAdapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                DataModel dataModel = dataModels.get(position);
                                                StringBuilder stb = new StringBuilder();
                                                alb.setTitle(dataModel.getTxtproduct());
                                                stb.append("Solde Minimum : "+dataModel.getTxtminsolde()+"\n");
                                                stb.append("Taux d\'intérêt : "+dataModel.getTxtinteret() +"\n");
                                                stb.append("Rémunéré après chaque : " +dataModel.getTxtecheance());
                                                alb.setMessage(stb.toString());
                                                dialog = alb.create();
                                                dialog.show();
                                            }
                                        });
                                    }catch (JSONException e){
                                        networkConnection.writeToast("Erreur des données");
                                        progressDialog.dismiss();

                                    }
                                break;
                        }

                    }
                });
                tache.execute(URL+"loanapi/APIS/savingproducts.php");
            }else{
                networkConnection.writeToast("Erreur connexion internet");
            }
        }catch (Exception e){
            networkConnection.writeToast("Erreur serveur");
        }



        return fproduits;
    }
}
