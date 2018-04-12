package com.wolfsoft.one.cg.adapter;

/**
 * Created by EXACT-IT-DEV on 1/29/2018.
 */

public class DataModel {
    String txtproduct;
    String txtminsolde;
    String txtinteret;



    String txtecheance;

    public DataModel(String txtproduct, String txtminsolde, String txtinteret, String txtecheance) {
        this.txtproduct = txtproduct;
        this.txtminsolde = txtminsolde;
        this.txtinteret = txtinteret;
        this.txtecheance = txtecheance;
    }

    public String getTxtproduct() {
        return txtproduct;
    }

    public void setTxtproduct(String txtproduct) {
        this.txtproduct = txtproduct;
    }

    public String getTxtminsolde() {
        return txtminsolde;
    }

    public void setTxtminsolde(String txtminsolde) {
        this.txtminsolde = txtminsolde;
    }

    public String getTxtinteret() {
        return txtinteret;
    }

    public void setTxtinteret(String txtinteret) {
        this.txtinteret = txtinteret;
    }

    public String getTxtecheance() {
        return txtecheance;
    }

    public void setTxtecheance(String txtecheance) {
        this.txtecheance = txtecheance;
    }

}
