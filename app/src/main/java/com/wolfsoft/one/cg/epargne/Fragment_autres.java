package com.wolfsoft.one.cg.epargne;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wolfsoft.one.cg.R;
import com.wolfsoft.one.cg.network.NetworkConnection;

/**
 * Created by EXACT-IT-DEV on 4/9/2018.
 */

public class Fragment_autres extends Fragment {
    NetworkConnection networkConnection;
    Context context;
    AlertDialog.Builder alb;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fautres = inflater.inflate(R.layout.fragment_autres,container,false);
        context = getContext();
        networkConnection = new NetworkConnection(context);
        alb = new AlertDialog.Builder(context);
        alb.setCancelable(true);
        alb.setTitle("Information");
        StringBuilder stb = new StringBuilder();
        stb.append("Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500, quand un peintre anonyme assembla ensemble des morceaux de texte pour réaliser un livre spécimen de polices de texte. Il n'a pas fait que survivre cinq siècles, mais s'est aussi adapté à la bureautique informatique, sans que son contenu n'en soit modifié. Il a été popularisé dans les années 1960 grâce à la vente de feuilles Letraset contenant des passages du Lorem Ipsum, et, plus récemment, par son inclusion dans des applications de mise en page de texte, comme Aldus PageMaker.");
        alb.setMessage(stb.toString());
        alb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = alb.create();

        LinearLayout linearLayout =(LinearLayout)fautres.findViewById(R.id.signsaccount);
        if(networkConnection.storedDatas("borrowerid")==null){
            linearLayout.animate().rotation(720f).setDuration(1000);
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            if(networkConnection.storedDatas("savingid")==null){

            }else {

            }
        }
        Button btnscreate = (Button)fautres.findViewById(R.id.btnscreate);
        final CheckBox checkBox = (CheckBox)fautres.findViewById(R.id.cbreadme);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    dialog.show();
                }else{

                }
            }
        });
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


        return fautres;
    }
}
