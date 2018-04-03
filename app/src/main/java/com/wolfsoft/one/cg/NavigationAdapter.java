package com.wolfsoft.one.cg;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class NavigationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavigationItems> listPojo;

    public NavigationAdapter(Context context, ArrayList<NavigationItems> listPojo) {
        super();
        this.context = context;
        this.listPojo = listPojo;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listPojo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listPojo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        MyViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.list_navigation, null);

            holder = new MyViewHolder();

            holder.title = (TextView) convertView
                    .findViewById(R.id.title);

            holder.linear = (LinearLayout) convertView
                    .findViewById(R.id.linear);

            holder.three =  (TextView) convertView
                    .findViewById(R.id.three);




            // At here set animation by their position.. Means if position = 0.. then animation start on textview with i*50 = 0 startOffset

            convertView.setTag(holder);

        } else {

            holder = (MyViewHolder) convertView.getTag();
        }

        NavigationItems rowItem = (NavigationItems) getItem(position);
        holder.title.setText(rowItem.getName());

        if (position == 0){

            holder.linear.setBackgroundColor(Color.parseColor("#f8f8f8"));
            holder.three.setVisibility(View.VISIBLE);
        }else {
            holder.linear.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.three.setVisibility(View.GONE);
        }




        return convertView;
    }

    class MyViewHolder {


        private TextView title;
        private LinearLayout linear;
        private TextView three;


    }


}
