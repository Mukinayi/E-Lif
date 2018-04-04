package com.wolfsoft.one.cg;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolfsoft.one.cg.network.NetworkConnection;

/**
 * Created by one on 20/8/16.
 */
public class HomeFragment extends Fragment {
    NetworkConnection networkConnection;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context  = getContext();
        networkConnection = new NetworkConnection(context);
        final String owner = networkConnection.storedDatas("fname") + " " +networkConnection.storedDatas("lname");
        TextView fullname = (TextView)view.findViewById(R.id.tvfullname);
        TextView numcompte = (TextView)view.findViewById(R.id.tvnumcompte);
        TextView adresse = (TextView)view.findViewById(R.id.tvadresse);
        TextView typeclient = (TextView)view.findViewById(R.id.tvaccounttype);
        TextView portable = (TextView)view.findViewById(R.id.tvportable);
        portable.setText(networkConnection.storedDatas("phone"));
        typeclient.setText(networkConnection.storedDatas("typecompte"));
        adresse.setText(networkConnection.storedDatas("adresse"));
        numcompte.setText(networkConnection.storedDatas("numcompte"));

        fullname.setText(owner);
        return view;

    }
}
