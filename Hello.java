import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Hello {
    public static void main(String[] args)
    {
        String mysqlHost = "155.248.215.20";
        String port = "3306";
        String user = "user8";
        String pwd = "7dcGc#$q5@/,>n\"B";
        Connection conn = null;
        try
        {
            System.out.println("hello world 2");
            conn = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + port + "/factor_hw", user, pwd);
            Statement st = conn.createStatement();
      
            String query = "SELECT * FROM water_samples";
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                System.out.println("hello world 4");
                int id = rs.getInt("id");
                String site = rs.getString("site");
                Float chloroform = rs.getFloat("chloroform");
                Float bromoform = rs.getFloat("bromoform");
                Float bromodichloromethane = rs.getFloat("bromodichloromethane");
                Float dibromichloromethane = rs.getFloat("dibromichloromethane");
                
                // print the results
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, site, chloroform, bromoform, bromodichloromethane, dibromichloromethane);
            }
            st.close();
            conn.close();
        }
        catch (SQLException ex) 
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}