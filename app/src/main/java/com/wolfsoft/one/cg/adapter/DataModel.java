package com.wolfsoft.one.cg.adapter;

/**
 * Created by EXACT-IT-DEV on 4/11/2018.
 */

public class DataModel {
    private String productname;
    private String minsolde;
    private String interet;
    private String echeance;


    public DataModel() {
    }

    public DataModel(String productname, String minsolde, String interet, String echeance) {
        this.productname = productname;
        this.minsolde = minsolde;
        this.interet = interet;
        this.echeance = echeance;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getMinsolde() {
        return minsolde;
    }

    public void setMinsolde(String minsolde) {
        this.minsolde = minsolde;
    }

    public String getInteret() {
        return interet;
    }

    public void setInteret(String interet) {
        this.interet = interet;
    }

    public String getEcheance() {
        return echeance;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }
}
