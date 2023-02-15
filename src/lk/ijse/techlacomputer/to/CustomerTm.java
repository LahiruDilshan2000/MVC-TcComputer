package lk.ijse.techlacomputer.to;

import javafx.scene.control.Button;

public class CustomerTm {
    private String cusId;
    private String cusName;
    private String cusAddress;
    private String cusContact;
    private Button option;

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusContact() {
        return cusContact;
    }

    public void setCusContact(String cusContact) {
        this.cusContact = cusContact;
    }

    public Button getOption() {
        return option;
    }

    public void setOption(Button option) {
        this.option = option;
    }

    public CustomerTm(String cusId, String cusName, String cusAddress, String cusContact, Button option) {
        this.cusId = cusId;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.cusContact = cusContact;
        this.option = option;
    }

    public CustomerTm() {
    }
}
