package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.User;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogModel {
    public static ResultSet getDetails(String userName) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM User WHERE userID= ?",userName);
        return result;
    }

    public static boolean addAccount(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert into User VALUES(?,?,?)",user.getUserName(),
                user.getPassword(),
                user.getRole());
    }

    public static boolean updateDetails(User user, String userId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Update User set userID=?,password=? where userID=?",user.getUserName(),
                user.getPassword(),userId);
    }
}
