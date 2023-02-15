package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.db.DBConnection;
import lk.ijse.techlacomputer.to.Income;
import lk.ijse.techlacomputer.to.Order;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderModel {
    public static String getNextOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select orderID from Orders order by orderID DESC LIMIT 1");
        return rst.next() ? rst.getString(1) : null;
    }

    public static boolean addOrder(Order order,double total) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isOrderAdded = CrudUtil.execute("insert into Orders VALUES(?,?,?)", order.getOrderId(),
                    order.getOrderDate(),
                    order.getCusId());
            if (isOrderAdded) {
                boolean isOrderDetailAdded = OrderDetailModel.addOrderDetail(order.getOrderDetailArrayList());
                if (isOrderDetailAdded) {
                    boolean isUpdateItem = ItemModel.ReduceItemQty(order.getOrderDetailArrayList());
                    if (isUpdateItem) {
                        boolean isIncomeAdded=IncomeModel.addIncome(new Income(order.getOrderId(),
                                        LocalDate.now().getDayOfMonth(),
                                        LocalDate.now().getMonthValue(),
                                        LocalDate.now().getYear(),
                                        total));
                        if(isIncomeAdded) {
                            DBConnection.getInstance().getConnection().commit();
                            return true;
                        }
                    }
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static int getTodayOrder(String date) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select COUNT(orderID)from Orders where date LIKE '"+date+"%'");
        if (rst.next()){
            return rst.getInt(1);
        }
        return -1;
    }

}
