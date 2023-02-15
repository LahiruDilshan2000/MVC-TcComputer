package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.db.DBConnection;
import lk.ijse.techlacomputer.to.Income;
import lk.ijse.techlacomputer.to.Supplies;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SuppliesModel {
    public static String getNextSuppliesCode() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select suppliesCode from Supplies Order by suppliesCode DESC LIMIT 1");
        return rst.next() ? rst.getString(1) : null;
    }

    public static boolean addSupplies(Supplies supplies, double total) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isSuppliesAdded = CrudUtil.execute("insert into Supplies VALUES(?,?,?)", supplies.getSuppliesCode(),
                    supplies.getDate(),
                    supplies.getSupplierId());
            if (isSuppliesAdded) {
                boolean isSuppliesDetailAdded = SuppliesDetailModel.addSuppliesDetail(supplies.getSuppliesDetailArrayList());
                if (isSuppliesDetailAdded) {
                    boolean isUpdateItem = ItemModel.upgradeItem(supplies.getSuppliesDetailArrayList());
                    if (isUpdateItem) {
                        boolean isAddedOutcome = OutcomeModel.addOutcome(new Income(supplies.getSuppliesCode(),
                                LocalDate.now().getDayOfMonth(),
                                LocalDate.now().getMonthValue(),
                                LocalDate.now().getYear(),
                                total));
                        if (isAddedOutcome) {
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
}
