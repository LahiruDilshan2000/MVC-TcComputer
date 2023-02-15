package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.SuppliesDetail;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesDetailModel {
    public static boolean addSuppliesDetail(ArrayList<SuppliesDetail> suppliesDetailArrayList) throws SQLException, ClassNotFoundException {
        for(SuppliesDetail suppliesDetail:suppliesDetailArrayList){
            if(!addSuppliesDetail(suppliesDetail)){
                return false;
            }
        }
        return true;
    }

    private static boolean addSuppliesDetail(SuppliesDetail suppliesDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into SuppliesDetail VALUES(?,?,?,?)",suppliesDetail.getSuppliesCode(),
                suppliesDetail.getItemCode(),
                suppliesDetail.getQty(),
                suppliesDetail.getUnitPrice());
    }
}
