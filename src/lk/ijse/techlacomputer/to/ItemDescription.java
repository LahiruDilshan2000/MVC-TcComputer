package lk.ijse.techlacomputer.to;

public class ItemDescription {
    private String itemCode;
    private String itemBrand;
    private String itemName;
    private double unitPrice;
    private int qty;
    private double total;

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

    public ItemDescription(String itemCode, String itemBrand, String itemName, double unitPrice, int qty, double total) {
        this.itemCode = itemCode;
        this.itemBrand = itemBrand;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public ItemDescription() {
    }
}
