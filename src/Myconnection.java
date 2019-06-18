

import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

 /*   private Connection con;
   private Statement st;
   private ResultSet rs;*/

 public   class Myconnection 
 {

   
  public static  Connection getconnection() 
   {
        Connection con=null;
       try{
           
           Class.forName("com.mysql.cj.jdbc.Driver");
         con=DriverManager.getConnection("jdbc:mysql://localhost/client?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
        //   con=DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12264169","sql12264169","AauQZCE3bU");
        //   st=con.createStatement();
            // return con;
       }catch(Exception ex){
           System.out.print("Error :"+ex);
       }
     return con;
   }
 
   
}
