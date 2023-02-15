package lk.ijse.techlacomputer.to;

public class Item {
    private String itemCode;
    private String itemBrand;
    private String itemName;
    private int qtyOnHand;
    private double unitPrice;

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

    public Item(String itemCode, String itemBrand, String itemName, int qtyOnHand, double unitPrice) {
        this.itemCode = itemCode;
        this.itemBrand = itemBrand;
        this.itemName = itemName;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }

    public Item() {
    }
}
