package lk.ijse.techlacomputer.to;

import javafx.scene.control.Button;

public class ItemTm {
    private String itemCode;
    private String itemBrand;
    private String itemName;
    private int qtyOnHand;
    private double unitPrice;
    private Button option;

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

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Button getOption() {
        return option;
    }

    public void setOption(Button option) {
        this.option = option;
    }

    public ItemTm(String itemCode, String itemBrand, String itemName, int qtyOnHand, double unitPrice, Button option) {
        this.itemCode = itemCode;
        this.itemBrand = itemBrand;
        this.itemName = itemName;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
        this.option = option;
    }

    public ItemTm() {
    }
}
