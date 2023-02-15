package lk.ijse.techlacomputer.to;

import java.util.ArrayList;

public class Supplies {
    private String suppliesCode;
    private String date;
    private String supplierId;
    private ArrayList<SuppliesDetail> suppliesDetailArrayList;

    public String getSuppliesCode() {
        return suppliesCode;
    }

    public void setSuppliesCode(String suppliesCode) {
        this.suppliesCode = suppliesCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public ArrayList<SuppliesDetail> getSuppliesDetailArrayList() {
        return suppliesDetailArrayList;
    }

    public void setSuppliesDetailArrayList(ArrayList<SuppliesDetail> suppliesDetailArrayList) {
        this.suppliesDetailArrayList = suppliesDetailArrayList;
    }

    public Supplies(String suppliesCode, String date, String supplierId, ArrayList<SuppliesDetail> suppliesDetailArrayList) {
        this.suppliesCode = suppliesCode;
        this.date = date;
        this.supplierId = supplierId;
        this.suppliesDetailArrayList = suppliesDetailArrayList;
    }

    public Supplies() {
    }
}
