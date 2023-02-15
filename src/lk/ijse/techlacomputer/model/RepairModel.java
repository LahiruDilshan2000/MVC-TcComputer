package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.db.DBConnection;
import lk.ijse.techlacomputer.to.Income;
import lk.ijse.techlacomputer.to.Repair;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RepairModel {
    public static String getNextRepairId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select repairID from Repair ORDER BY repairID DESC LIMIT 1");
        return rst.next() ? rst.getString(1) : null ;
    }

    public static boolean addRepairWithReducesItem(Repair repair,double total) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isRepairAdded = CrudUtil.execute("insert into Repair VALUES(?,?,?,?,?,?)", repair.getRepairId(),
                    repair.getItemType(),
                    repair.getItemName(),
                    repair.getRepairDate(),
                    repair.getAmount(),
                    repair.getCusId());
            if (isRepairAdded) {
                boolean isReducesDetailAdded = ReducesItemDetailModel.addReducesDetail(repair.getRepairReducesItemDetails());
                if (isReducesDetailAdded) {
                    boolean isUpdateItem = ItemModel.ReduceItemQtys(repair.getRepairReducesItemDetails());
                    if (isUpdateItem) {
                        boolean isIncomeAdded=IncomeModel.addIncome(new Income(repair.getRepairId(),
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

    public static boolean addRepairWithoutReducesItem(Repair repair,double total) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isRepairAdded = CrudUtil.execute("insert into Repair VALUES(?,?,?,?,?,?)", repair.getRepairId(),
                    repair.getItemType(),
                    repair.getItemName(),
                    repair.getRepairDate(),
                    repair.getAmount(),
                    repair.getCusId());
            if (isRepairAdded) {
                boolean isIncomeAdded=IncomeModel.addIncome(new Income(repair.getRepairId(),
                        LocalDate.now().getDayOfMonth(),
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear(),
                        total));
                if(isIncomeAdded) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static int getTodayRepair(String date) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select COUNT(repairID)from Repair where date LIKE '"+date+"%'");
        if (rst.next()){
            return rst.getInt(1);
        }
        return -1;
    }
}
