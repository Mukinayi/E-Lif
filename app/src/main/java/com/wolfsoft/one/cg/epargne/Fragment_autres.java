package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_autres extends Fragment {
    NetworkConnection networkConnection;
    Context context;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fautres = inflater.inflate(R.layout.fragment_autres,container,false);
        context = getContext();
        networkConnection = new NetworkConnection(context);
        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Traitement...");
        pDialog.setMessage("Chargement en cours");
        pDialog.setIcon(R.drawable.ic_lifouta);
        pDialog.setCancelable(false);
        alb = new AlertDialog.Builder(context);
        alb.setCancelable(true);
        alb.setTitle("Information");
        StringBuilder stb = new StringBuilder();
        stb.append("Le Lorem Ipsum est simplement du faux texte employé" +
                "dans la composition et la mise en page avant impression. Le Lorem Ipsum" +
                " est le faux texte standard de l'imprimerie depuis les années 1500," +
                " quand un peintre anonyme assembla ensemble des morceaux de texte pour " +
                "réaliser un livre spécimen de polices de texte. Il n'a pas fait que survivre" +
                " cinq siècles, mais s'est aussi adapté à la bureautique informatique, sans que son" +
                " contenu n'en soit modifié. Il a été popularisé dans les années 1960 grâce à la vente" +
                " de feuilles Letraset contenant des passages du Lorem Ipsum, et, plus récemment, par son inclusion " +
                "dans des applications de mise en page de texte, comme Aldus PageMaker." +
                "Le Lorem Ipsum est simplement du faux texte employé" +
                "dans la composition et la mise en page avant impression. Le Lorem Ipsum" +
                " est le faux texte standard de l'imprimerie depuis les années 1500," +
                " quand un peintre anonyme assembla ensemble des morceaux de texte pour " +
                "réaliser un livre spécimen de polices de texte. Il n'a pas fait que survivre" +
                " cinq siècles, mais s'est aussi adapté à la bureautique informatique, sans que son" +
                " contenu n'en soit modifié. Il a été popularisé dans les années 1960 grâce à la vente" +
                " de feuilles Letraset contenant des passages du Lorem Ipsum, et, plus récemment, par son inclusion " +
                "dans des applications de mise en page de texte, comme Aldus PageMaker.");
        alb.setMessage(stb.toString());
        alb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = alb.create();


        final LinearLayout linearChoose =(LinearLayout)fautres.findViewById(R.id.chooseoption);
        final LinearLayout linearsignaccount =(LinearLayout)fautres.findViewById(R.id.signsaccount);
        final LinearLayout linksaccount =(LinearLayout)fautres.findViewById(R.id.linksaccount);
        final LinearLayout mainlay = (LinearLayout)fautres.findViewById(R.id.mainlay);
        final Spinner chooseoption = (Spinner)fautres.findViewById(R.id.spinneroption);
        arrayList = new ArrayList<>();
        arrayList.add("Choisissez une action");
        arrayList.add("Connexion au compte Epargne");
        arrayList.add("Création du compte Epargne");
        arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,arrayList);
        chooseoption.setAdapter(arrayAdapter);
        if(networkConnection.storedDatas("borrowerid")==null){
            linearChoose.animate().rotation(360f).setDuration(2000);
            linearChoose.setVisibility(View.VISIBLE);
            //linearLayout.animate().rotation(720f).setDuration(1000);
        }else{
            if(networkConnection.storedDatas("savingid")==null){
                linksaccount.animate().rotation(360f).setDuration(2000);
                linksaccount.setVisibility(View.VISIBLE);
            }else {
                mainlay.animate().rotation(360f).setDuration(1500);
                mainlay.setVisibility(View.VISIBLE);
            }
        }
        Button btnscreate = (Button)fautres.findViewById(R.id.btnscreate);
        Button btnoption = (Button)fautres.findViewById(R.id.btnchoose);
        btnoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = chooseoption.getSelectedItem().toString();
                if(response.equals("Connexion au compte Epargne")){
                    linksaccount.animate().rotation(360f).setDuration(2000);
                    linksaccount.setVisibility(View.VISIBLE);

                    linearsignaccount.animate().rotation(-360f).setDuration(2000);
                    linearsignaccount.setVisibility(View.GONE);

                    linearChoose.animate().rotation(360f).setDuration(2000);
                    linearChoose.setVisibility(View.GONE);
                }else if(response.equals("Création du compte Epargne")){
                    linearsignaccount.animate().rotation(360f).setDuration(2000);
                    linearsignaccount.setVisibility(View.VISIBLE);

                    linksaccount.animate().rotation(-360f).setDuration(2000);
                    linksaccount.setVisibility(View.GONE);

                    linearChoose.animate().rotation(360f).setDuration(2000);
                    linearChoose.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(),"Veuillez choisir une action",Toast.LENGTH_SHORT).show();
                    linearsignaccount.animate().rotation(-360f).setDuration(2000);
                    linearsignaccount.setVisibility(View.GONE);

                    linksaccount.animate().rotation(-360f).setDuration(2000);
                    linksaccount.setVisibility(View.GONE);
                }
            }
        });



        final CheckBox checkBox = (CheckBox)fautres.findViewById(R.id.cbreadme);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    dialog.show();
                }else{
                    dialog.dismiss();
                }
            }
        });

        checkBox.setChecked(false);
        dialog.dismiss();
        btnscreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    Toast.makeText(getContext(),"Très bien",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Vous devez d'abord cocher pour lire avant de passer",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnlinksaccount = (Button)fautres.findViewById(R.id.btnlinksaccount);
        final EditText etsaccusername = (EditText)fautres.findViewById(R.id.etsaccusername);
        final EditText etsaccpass = (EditText)fautres.findViewById(R.id.etsaccpass);
        final String URL = networkConnection.getUrl();

        btnlinksaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postdata = new HashMap();
                postdata.put("username",etsaccusername.getText().toString());
                postdata.put("password",etsaccpass.getText().toString());

                if(etsaccusername.getText().toString().isEmpty() || etsaccpass.getText().toString().isEmpty()){
                    networkConnection.writeToast("Vous devez remplir tous les champs");
                    pDialog.dismiss();

                }else{
                    pDialog.show();
                    if(networkConnection.isConnected()){
                        try {
                            PostResponseAsyncTask tache = new PostResponseAsyncTask(context, postdata, false, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    pDialog.dismiss();
                                    switch (s){
                                        case "180":
                                            networkConnection.writeToast("Utilisateur non reconnu");
                                            pDialog.dismiss();
                                            break;
                                        case "181":
                                            networkConnection.writeToast("Mot de passe incorrecte");
                                            pDialog.dismiss();
                                            break;
                                        case "182":
                                            networkConnection.writeToast("Compte inactif");
                                            pDialog.dismiss();
                                            break;
                                        case "183":
                                            networkConnection.writeToast("Compte blacklisté");
                                            pDialog.dismiss();
                                            break;
                                        case "184":
                                            networkConnection.writeToast("vous n'avez souscris à aucun produit épargne");
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
                                                if(networkConnection.saveSavinsData(jsonObject.getString("borrower_id"),jsonObject.getString("id"))){
                                                    networkConnection.writeToast("Compte rélié avec succès");
                                                    linksaccount.setVisibility(View.GONE);
                                                    mainlay.animate().rotation(360f).setDuration(1500);
                                                    mainlay.setVisibility(View.VISIBLE);
                                                }else{
                                                    networkConnection.writeToast("echec stockage des données");

                                                }
                                            }catch (JSONException e){
                                                networkConnection.writeToast("Erreur des données");
                                            }
                                            break;
                                    }
                                    Log.i("retour",s);
                                }
                            });
                            tache.execute(URL+"loanapi/APIS/borrowerconnexion.php");
                        }catch (Exception e){
                            networkConnection.writeToast("Erreur connexion au serveur");
                            pDialog.dismiss();
                        }
                    }else{
                        networkConnection.writeToast("Aucune connexion internet");
                        pDialog.dismiss();
                    }
                }
            }
        });





        return fautres;
    }
}
