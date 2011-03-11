package Harvester;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 09.03.11
 * Time: 19:18
 */

import java.sql.*;
import java.util.Properties;

import com.mysql.jdbc.Driver;


public class DatabaseAdapter {

    private static String tablename="cars";
    private static String url = "jdbc:mysql:///mydatabase";

    public void InsertRow(int price, String shortDscrptn,String longDscrptn, String city,int productYear)
    {
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Properties prop = new Properties();
            prop.put("user","root");
            prop.put("password","toor");
            prop.put("characterEncoding","utf8");
            con = DriverManager.getConnection(url, prop);

            //if(!con.isClosed())
                //System.out.println("Successfully connected");
            String query="INSERT INTO " +tablename+ " VALUES ( 0 , ? , ? , ? , ? , ? );";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, price);
            st.setString(2, "'" + shortDscrptn + "'");
            st.setString(3,"'" + longDscrptn + "'");
            st.setString(4,"'" + city + "'");
            st.setInt(5, productYear);
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
