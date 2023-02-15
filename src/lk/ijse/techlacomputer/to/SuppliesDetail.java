package lk.ijse.techlacomputer.to;


public class SuppliesDetail {
    private String SuppliesCode;
    private String itemCode;
    private int qty;
    private double unitPrice;

    public String getSuppliesCode() {
        return SuppliesCode;
    }

    public void setSuppliesCode(String suppliesCode) {
        SuppliesCode = suppliesCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public SuppliesDetail(String suppliesCode, String itemCode, int qty, double unitPrice) {
        SuppliesCode = suppliesCode;
        this.itemCode = itemCode;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public SuppliesDetail() {
    }
}
