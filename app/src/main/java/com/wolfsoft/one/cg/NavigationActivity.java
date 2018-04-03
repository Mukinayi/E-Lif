package com.wolfsoft.one.cg;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class NavigationActivity extends ActionBarActivity {

    // http://192.168.1.130/chetana/bikes.php

    private Toolbar toolbar;

    private RelativeLayout relativeClose;

    // It is for horizontal bar chart

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private NavigationAdapter adapter;
    private ListView list;
    private ArrayList<NavigationItems> items;


    private String[] NAME = {"Mon solde", "Virement" , "Transfert", "Payer", "Crédits Téléphone", "Services Publics"
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

                   // selectItem(position);
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
}
