package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.Supplier;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public static ArrayList<Supplier> getSupplierList() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select*from Supplier");
        ArrayList<Supplier>supplierArrayList=new ArrayList<>();
        while(rst.next()){
            supplierArrayList.add(new Supplier(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)));
        }
        return supplierArrayList;
    }

    public static boolean addSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into Supplier values(?,?,?,?)",supplier.getSupId(),
                supplier.getSupName(),
                supplier.getSupAddress(),
                supplier.getSupContact());
    }

    public static boolean updateSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update Supplier set name=?,address=?,contact=? where id=?",supplier.getSupName(),
                supplier.getSupAddress(),
                supplier.getSupContact(),
                supplier.getSupId());
    }

    public static boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from Supplier where id=?",supplierId);
    }

    public static ArrayList<Supplier> searchSupplier(String text) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select*from Supplier where name LIKE '"+text+"%'");
        ArrayList<Supplier>supplierArrayList=new ArrayList<>();
        while (rst.next()){
            supplierArrayList.add(new Supplier(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)));
        }
        return supplierArrayList;
    }

    public static String getNextSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select id from Supplier order by id DESC LIMIT 1");
        return rst.next() ? rst.getString(1) : null;
    }

    public static int getSupplierTotal() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select COUNT(id) from Supplier");
        if(rst.next()){
            return rst.getInt(1);
        }
        return -1;
    }
}
