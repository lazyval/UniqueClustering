package Harvester;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 09.03.11
 * Time: 19:18
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;


public class DatabaseAdapter {

    public static void main(String argv[]) {
        Connection con = null;
        try{
            //that database exists on every MySQL server
            String url = "jdbc:mysql:///test";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, "root", "toor");
            if(!con.isClosed())
                System.out.println("Successfully connected");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        try {
		if(con != null)
			con.close();
	} catch(SQLException e) {}


            }
        }
