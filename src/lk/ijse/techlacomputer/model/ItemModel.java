package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.Item;
import lk.ijse.techlacomputer.to.OrderDetail;
import lk.ijse.techlacomputer.to.RepairReducesItemDetail;
import lk.ijse.techlacomputer.to.SuppliesDetail;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {
    public static ArrayList<Item> getItemList() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select*from Item");
        ArrayList<Item>itemArrayList=new ArrayList<>();
        while(rst.next()){
            itemArrayList.add(new Item(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Integer.parseInt(rst.getString(4)),
                    Double.parseDouble(rst.getString(5))));
        }
        return itemArrayList;
    }

    public static boolean addItem(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into Item values(?,?,?,?,?)",item.getItemCode(),
                item.getItemBrand(),
                item.getItemName(),
                String.valueOf(item.getQtyOnHand()),
                String.valueOf(item.getUnitPrice()));
    }

    public static boolean updateItem(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update Item set brand=?,name=?,qtyOnHand=?,unitPrice=? where itemCode=?",
                item.getItemBrand(),
                item.getItemName(),
                String.valueOf(item.getQtyOnHand()),
                String.valueOf(item.getUnitPrice()),
                item.getItemCode());

    }

    public static boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from Item where itemCode=?",
                itemCode);
    }

    public static ArrayList<Item> searchItem(String text) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select*from Item where brand Like '"+text+"%'");
        ArrayList<Item>itemArrayList=new ArrayList<>();
        while (rst.next()){
            itemArrayList.add(new Item(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Integer.parseInt(rst.getString(4)),
                    Double.parseDouble(rst.getString(5))));
        }
        return itemArrayList;
    }

    public static String getNextItemCode() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select itemCode from Item order by itemCode DESC LIMIT 1");
        return rst.next() ? rst.getString(1) : null;
    }

    public static boolean ReduceItemQty(ArrayList<OrderDetail> orderDetailArrayList) throws SQLException, ClassNotFoundException {
        for (OrderDetail orderDetail: orderDetailArrayList){
            if(!ReduceItemQty(orderDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean ReduceItemQty(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update Item set qtyOnHand=qtyOnHand-? where itemCode=?",orderDetail.getQty(),
                orderDetail.getItemCode());
    }

    public static boolean ReduceItemQtys(ArrayList<RepairReducesItemDetail> repairReducesItemDetails) throws SQLException, ClassNotFoundException {
        for (RepairReducesItemDetail repairReducesItemDetail: repairReducesItemDetails){
            if(!ReduceItemQty(repairReducesItemDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean ReduceItemQty(RepairReducesItemDetail repairReducesItemDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update Item set qtyOnHand=qtyOnHand-? where itemCode=?",repairReducesItemDetail.getQty(),
                repairReducesItemDetail.getItemCode());
    }

    public static boolean upgradeItem(ArrayList<SuppliesDetail> suppliesDetailArrayList) throws SQLException, ClassNotFoundException {
        for (SuppliesDetail suppliesDetail: suppliesDetailArrayList){
            if(!upgradeItem(suppliesDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean upgradeItem(SuppliesDetail suppliesDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Update Item set qtyOnHand=qtyOnHand+? where itemCode=?",suppliesDetail.getQty(),
                suppliesDetail.getItemCode());
    }

    public static ArrayList<Item> getRemaningItems() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select * from Item where qtyOnHand<20");
        ArrayList<Item>itemArrayList=new ArrayList<>();
        while (rst.next()){
            itemArrayList.add(new Item(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Integer.parseInt(rst.getString(4)),
                    Double.parseDouble(rst.getString(5))));
        }
        return itemArrayList;
    }
}
