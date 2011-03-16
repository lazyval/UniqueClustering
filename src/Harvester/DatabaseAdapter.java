package Harvester;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 09.03.11
 * Time: 19:18
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseAdapter {

    private static String tablename="cars";
    private static String url = "jdbc:mysql:///mydatabase";

    public void InsertRow(int price, String shortDscrptn, String longDscrptn, String city, int productYear, int cluster)
    {
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Properties prop = new Properties();
            prop.put("user","root");
            prop.put("password","toor");
            prop.put("characterEncoding","utf8");
            con = DriverManager.getConnection(url, prop);

            String query="INSERT INTO " +tablename+ " VALUES ( 0 , ? , ? , ? , ? , ?, ? );";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, price);
            st.setString(2,shortDscrptn);
            st.setString(3,longDscrptn);
            st.setString(4,city);
            st.setInt(5, productYear);
            st.setInt(6,cluster);
            System.out.println(st.toString());
            st.executeUpdate();


        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
		if(con != null)
			con.close();
	    }
        catch(SQLException e) {}
    }
}
