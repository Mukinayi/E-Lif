package com.wolfsoft.one.cg.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.wolfsoft.one.cg.R;

import java.util.ArrayList;

/**
 * Created by EXACT-IT-DEV on 4/11/2018.
 */

public class AdapterSavingProduct extends ArrayAdapter implements View.OnClickListener{
         Context context;
         ArrayList<DataModel> dataModelArrayList;


    public AdapterSavingProduct(@NonNull Context context,ArrayList<DataModel> dataModelArrayList) {
        super(context, R.layout.list_row,dataModelArrayList);
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }
    

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        
        return super.getView(position, convertView, parent);
    }
}
