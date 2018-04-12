package com.wolfsoft.one.cg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wolfsoft.one.cg.R;


import java.util.ArrayList;

/**
 * Created by EXACT-IT-DEV on 1/29/2018.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtProduct;
        TextView txtMindeposit;
        TextView txtInteret;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.list_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;
        switch (v.getId())
        {

        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            viewHolder.txtProduct = (TextView) convertView.findViewById(R.id.tvprodname);
            viewHolder.txtMindeposit = (TextView) convertView.findViewById(R.id.tvmindeposit);
            viewHolder.txtInteret = (TextView) convertView.findViewById(R.id.tvinteret);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        //lastPosition = position;


        viewHolder.txtProduct.setText(dataModel.getTxtproduct());
        viewHolder.txtMindeposit.setText(dataModel.getTxtminsolde());
        viewHolder.txtInteret.setText(dataModel.getTxtinteret());
        // Return the completed view to render on screen
        return convertView;
    }
}
