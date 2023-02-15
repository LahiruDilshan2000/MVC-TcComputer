package lk.ijse.techlacomputer.to;

import com.jfoenix.controls.JFXButton;

public class ItemDescriptionTm {
    private String itemCode;
    private String itemBrand;
    private String itemName;
    private double unitPrice;
    private int qty;
    private double total;

    @Override
    public String toString() {
        return "ItemDescriptionTm{" +
                "itemCode='" + itemCode + '\'' +
                ", itemBrand='" + itemBrand + '\'' +
                ", itemName='" + itemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", total=" + total +
                ", option=" + option +
                '}';
    }

    private JFXButton option;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public JFXButton getOption() {
        return option;
    }

    public void setOption(JFXButton option) {
        this.option = option;
    }

    public ItemDescriptionTm(String itemCode, String itemBrand, String itemName, double unitPrice, int qty, double total, JFXButton option) {
        this.itemCode = itemCode;
        this.itemBrand = itemBrand;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
        this.option = option;
    }

    public ItemDescriptionTm() {
    }
}
