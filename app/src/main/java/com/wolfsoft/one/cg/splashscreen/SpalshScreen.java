package com.wolfsoft.one.cg.splashscreen;

import android.Manifest;
import android.app.Service;
import android.content.Context;
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
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.wolfsoft.one.cg.NavigationActivity;
import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.login.Login;
import com.wolfsoft.one.cg.network.NetworkConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class SpalshScreen extends AppCompatActivity {
    NetworkConnection networkConnection;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        networkConnection = new NetworkConnection(this);
        final String numcompte = networkConnection.storedDatas("numcompte");
        final String idcompte = networkConnection.storedDatas("idcompte");
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        final String URL = networkConnection.getUrl();

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            Permissions.check(SpalshScreen.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, "Vous allez devoir autoriser quelques permissions, cliquer sur OK pour voir ces permissions ", new Permissions.Options().setSettingsDialogTitle("Avertissement!").setRationaleDialogTitle("Autorisation accès"), new PermissionHandler() {
                @Override
                public void onGranted() {
                    imei = networkConnection.getImeiNumber();
                    if(imei!=null){
                        checkImei(imei);
                    }else{
                        noImei();
                    }
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                    finish();
                    super.onDenied(context, deniedPermissions);
                }

                @Override
                public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                    finish();
                    return super.onBlocked(context, blockedList);
                }

                @Override
                public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                    finish();
                    super.onJustBlocked(context, justBlockedList, deniedPermissions);
                }
            });
        }else{
            imei = networkConnection.getImeiNumber();
            if(imei!=null){
                checkImei(imei);
            }else{
                noImei();
            }
        }

        //String imei = "362523432421083";

    }

    public void noImei(){
            Intent noi = new Intent(SpalshScreen.this,NoImei.class);
            noi.putExtra("message","Dispositif sans IMEI\n Avec un dispositif sans imei, il est impossible de se connecter au Système Lifouta");
            startActivity(noi);
    }

    public void checkImei(String imei){
        networkConnection = new NetworkConnection(this);
        final String numcompte = networkConnection.storedDatas("numcompte");
        final String idcompte = networkConnection.storedDatas("idcompte");
        TelephonyManager tm = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        final String URL = networkConnection.getUrl();
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
