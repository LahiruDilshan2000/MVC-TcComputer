package lk.ijse.techlacomputer.model;

import lk.ijse.techlacomputer.to.Income;
import lk.ijse.techlacomputer.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OutcomeModel {
    public static boolean addOutcome(Income income) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert into OutCome VALUES(?,?,?,?,?)",income.getIncomeId(),
                income.getDate(),
                income.getMonth(),
                income.getYear(),
                income.getTotal());
    }

    public static ArrayList<Integer> getYear() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select year from Outcome group by year");
        ArrayList<Integer>year=new ArrayList<>();
        while (rst.next()){
            year.add(rst.getInt(1));
        }
        return year;
    }

    public static ArrayList<Income> getSelectedOutcome(int year, int month) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select * from Outcome where month=? and year=? ORDER BY date ASC",month,year);
        ArrayList<Income>incomes=new ArrayList<>();
        while (rst.next()){
            incomes.add(new Income(rst.getString(1),
                    rst.getInt(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getDouble(5)));
        }
        return incomes;
    }

    public static double getTotalOutCome(int year, int month) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("select total from Outcome where month=? and year=?",month,year);
        double total=0;
        while (rst.next()){
            total+= rst.getDouble(1);
        }
        return total;
    }
}
