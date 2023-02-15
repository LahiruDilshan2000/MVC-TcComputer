package lk.ijse.techlacomputer.to;

public class Supplier {
    private String supId;
    private String supName;
    private String supAddress;
    private String supContact;

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddress) {
        this.supAddress = supAddress;
    }

    public String getSupContact() {
        return supContact;
    }

    public void setSupContact(String supContact) {
        this.supContact = supContact;
    }

    public Supplier(String supId, String supName, String supAddress, String supContact) {
        this.supId = supId;
        this.supName = supName;
        this.supAddress = supAddress;
        this.supContact = supContact;
    }

    public Supplier() {
    }
}
