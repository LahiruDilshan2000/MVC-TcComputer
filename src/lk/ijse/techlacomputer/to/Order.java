package lk.ijse.techlacomputer.to;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String orderDate;
    private String cusId;
    private ArrayList<OrderDetail>orderDetailArrayList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public ArrayList<OrderDetail> getOrderDetailArrayList() {
        return orderDetailArrayList;
    }

    public void setOrderDetailArrayList(ArrayList<OrderDetail> orderDetailArrayList) {
        this.orderDetailArrayList = orderDetailArrayList;
    }

    public Order(String orderId, String orderDate, String cusId, ArrayList<OrderDetail> orderDetailArrayList) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.cusId = cusId;
        this.orderDetailArrayList = orderDetailArrayList;
    }

    public Order() {
    }
}
