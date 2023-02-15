package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.Customer;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public static ArrayList<Customer> getCustomerList() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select*from Customer");
        return getArrayList(rst);
    }
    private static ArrayList<Customer> getArrayList(ResultSet rst) throws SQLException {
        ArrayList<Customer>customerArrayList=new ArrayList<>();
        while (rst.next()){
            customerArrayList.add(new Customer(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)));
        }
        return customerArrayList;
    }

    public static ArrayList<Customer> searchCustomer(String cutId) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select*from Customer where name LIKE '"+cutId+"%'");
        return getArrayList(rst);
    }

    public static boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into Customer VALUES(?,?,?,?)",customer.getCusId(),
                customer.getCusName(),
                customer.getCusAddress(),
                customer.getCusContact());
    }

    public static boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update Customer set name=?,address=?,contact=? where id=?",customer.getCusName(),
                customer.getCusAddress(),
                customer.getCusContact(),
                customer.getCusId());
    }

    public static String getNextCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select id from Customer order by id DESC LIMIT 1");
        if(rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean deleteCustomer(String cutId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from Customer where id=?",cutId);
    }

    public static int getCustomerTotal() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select COUNT(id) from Customer");
        if(rst.next()){
            return rst.getInt(1);
        }
        return -1;
    }
}
