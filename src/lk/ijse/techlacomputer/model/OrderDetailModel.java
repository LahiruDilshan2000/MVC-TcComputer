package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.OrderDetail;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailModel {
    public static boolean addOrderDetail(ArrayList<OrderDetail> orderDetailArrayList) throws SQLException, ClassNotFoundException {
        for (OrderDetail orderDetail:orderDetailArrayList){
            if(!addOrderDetail(orderDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean addOrderDetail(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into OrderDetail VALUES(?,?,?,?)",orderDetail.getOrderId(),
                orderDetail.getItemCode(),
                orderDetail.getQty(),
                orderDetail.getUnitPrice());
    }
}
