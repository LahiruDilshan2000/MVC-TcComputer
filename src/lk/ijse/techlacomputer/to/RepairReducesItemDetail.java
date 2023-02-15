package lk.ijse.techlacomputer.to;

public class RepairReducesItemDetail {
    private String repairId;
    private String itemCode;
    private int qty;
    private double unitPrice;

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
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

    public RepairReducesItemDetail(String repairId, String itemCode, int qty, double unitPrice) {
        this.repairId = repairId;
        this.itemCode = itemCode;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public RepairReducesItemDetail() {
    }
}
