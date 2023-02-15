package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.RepairReducesItemDetail;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReducesItemDetailModel {
    public static boolean addReducesDetail(ArrayList<RepairReducesItemDetail> repairReducesItemDetails) throws SQLException, ClassNotFoundException {
        for (RepairReducesItemDetail repairReducesItemDetail:repairReducesItemDetails){
            if(!addReducesDetail(repairReducesItemDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean addReducesDetail(RepairReducesItemDetail repairReducesItemDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into ReducesItemDetail VALUES(?,?,?,?)",repairReducesItemDetail.getRepairId(),
                repairReducesItemDetail.getItemCode(),
                repairReducesItemDetail.getQty(),
                repairReducesItemDetail.getUnitPrice());
    }
}
