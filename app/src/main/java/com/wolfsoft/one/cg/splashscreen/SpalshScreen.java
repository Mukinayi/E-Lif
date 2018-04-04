package com.wolfsoft.one.cg.splashscreen;

import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.wolfsoft.one.cg.NavigationActivity;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.login.Login;
import com.wolfsoft.one.cg.network.NetworkConnection;

import java.util.HashMap;

public class SpalshScreen extends AppCompatActivity {
    NetworkConnection networkConnection;
    AlertDialog.Builder al;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        networkConnection = new NetworkConnection(this);
        final String numcompte = networkConnection.storedDatas("numcompte");
        final String idcompte = networkConnection.storedDatas("idcompte");
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        al = new AlertDialog.Builder(SpalshScreen.this);
        al.setTitle("Message");
        StringBuilder stb = new StringBuilder();
        stb.append("Vous devez donnez l'autorisation à l'application d'avoir accès au téléphone");
        al.setMessage(stb.toString());
        al.setPositiveButton("Parametres", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                finish();
            }
        });
        al.setCancelable(false);

        String imei = "";
        final String URL = networkConnection.getUrl();

        try {
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            if(tm.getDeviceId()==null){
                                Intent noi = new Intent(SpalshScreen.this,NoImei.class);
                                noi.putExtra("message","Dispositif sans imei");
                                startActivity(noi);
                                finish();
                            }else{
                                imei = tm.getDeviceId();
                            }
                        }else{
                            dialog = al.create();
                            dialog.show();
                        }

                }else{

                }

        }catch (Exception e){
            networkConnection.writeToast("Veuillez données à l'application les permissions requise");
        }

        HashMap dt = new HashMap();
        dt.put("deviceimei",imei);
        if(numcompte!=null){
            dt.put("idcompte",idcompte);
            if(networkConnection.isConnected()){
                PostResponseAsyncTask tache = new PostResponseAsyncTask(this, dt, false, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        try {
                            switch (s){
                                case "180":
                                    Toast.makeText(getApplicationContext(),"Appareil non encore connecté",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SpalshScreen.this, Login.class);
                                    startActivity(i);
                                    finish();
                                    break;
                                case "181":
                                    Toast.makeText(getApplicationContext(),"Dispositif désactivé",Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(SpalshScreen.this, NoImei.class);
                                    it.putExtra("message","Dispositif désactivé");
                                    startActivity(it);
                                    finish();
                                    break;
                                default:
                                    Intent main = new Intent(SpalshScreen.this,NavigationActivity.class);
                                    startActivity(main);
                                    finish();
                                    break;
                            }
                        }catch (Exception e){
                            networkConnection.writeToast("Aucune réponse du serveur");
                            finish();
                        }
                    }
                });
                tache.execute(URL+"lifoutacourant/APIS/compare.php");
            }else{
                Toast.makeText(getApplicationContext(),"Erreur connexion intenet", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(SpalshScreen.this, NoImei.class);
                it.putExtra("message","Erreur connexion intenet");
                startActivity(it);
                finish();
            }
        }else{
            Intent i = new Intent(SpalshScreen.this, Login.class);
            startActivity(i);
            finish();
        }

    }
}
