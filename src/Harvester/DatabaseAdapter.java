package Harvester;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 09.03.11
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseAdapter {

    public static void main(String argv[]) {
        int IdShop;
        String NameShop;
        String AdShop;
        String myTabl;
        String myTabl1;
        int IdOrder;
        String NameOrder;
        String Quantity;
        Connection con = null;
        {
            try {
                String url = "jdbc:mysql://192.168.200.219/test";
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (InstantiationException e) {
                    // TODO Автоматически созданный блок catch
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Автоматически созданный блок catch
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Автоматически созданный блок catch
                    System.err.println("Can't find JDBC driver");
                    e.printStackTrace();
                }
                con = DriverManager.getConnection(url, "test", "1q2w3e");
                ResultSet q = con.createStatement().executeQuery(
                        "select * from Shop");

                while (q.next()) {
                    IdShop = q.getInt(1);
                    NameShop = q.getString(2);
                    AdShop = q.getString(3);

                    myTabl = ("id=" + IdShop + ", name=" + NameShop
                            + ", AdShop=" + AdShop.toString());

                    System.out.println(myTabl);

                }
                q.close();
                ResultSet q1 = con.createStatement().executeQuery(
                        "select * from test.Order");
                IdOrder = q1.getInt(1);
                NameOrder = q1.getString(2);
                Quantity = q1.getString(3);
                myTabl1 = ("id=" + IdOrder + ", name=" + NameOrder
                        + ", Quantity=" + Quantity.toString());
                System.out.println(myTabl1);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("DB connection error");
            } finally {
                if (con != null) {

                    try {
                        con.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}