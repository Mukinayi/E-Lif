package com.wolfsoft.one.cg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wolfsoft.one.cg.epargne.Fragment_autres;
import com.wolfsoft.one.cg.epargne.Fragment_produits;
import com.wolfsoft.one.cg.epargne.Fragment_transaction;

/**
 * Created by EXACT-IT-DEV on 4/12/2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment_produits fp = new Fragment_produits();
                return  fp;
            case 1:
                Fragment_transaction ft =  new Fragment_transaction();
                return ft;
            case 2:
                Fragment_autres fa = new Fragment_autres();
                return fa;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
            /*switch (position){
                case 0:
                    return "Produits d\'epargne";
                case 1:
                    return "Transcations Epargne";
                case 2:
                    return "Autres";
            }*/
        return null;
    }
}
