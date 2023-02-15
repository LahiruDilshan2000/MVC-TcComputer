package lk.ijse.techlacomputer.to;

import java.util.ArrayList;

public class Repair {
    private String RepairId;
    private String itemType;
    private String itemName;
    private String RepairDate;
    private double amount;
    private String cusId;
    private ArrayList<RepairReducesItemDetail> repairReducesItemDetails;

    public String getRepairId() {
        return RepairId;
    }

    public void setRepairId(String repairId) {
        RepairId = repairId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRepairDate() {
        return RepairDate;
    }

    public void setRepairDate(String repairDate) {
        RepairDate = repairDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public ArrayList<RepairReducesItemDetail> getRepairReducesItemDetails() {
        return repairReducesItemDetails;
    }

    public void setRepairReducesItemDetails(ArrayList<RepairReducesItemDetail> repairReducesItemDetails) {
        this.repairReducesItemDetails = repairReducesItemDetails;
    }

    public Repair(String repairId, String itemType, String itemName, String repairDate, double amount, String cusId, ArrayList<RepairReducesItemDetail> repairReducesItemDetails) {
        RepairId = repairId;
        this.itemType = itemType;
        this.itemName = itemName;
        RepairDate = repairDate;
        this.amount = amount;
        this.cusId = cusId;
        this.repairReducesItemDetails = repairReducesItemDetails;
    }

    public Repair() {
    }
}
