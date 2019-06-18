
 


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.net.*;
//import static sun.security.jgss.GSSUtil.login;

 
public class clientHandler implements Runnable {
  static String[] history=new String[1000];
    private Socket socket;
    //dah kda zy el section
  //  private static String use;//dah 3lshan 23rf min el user ely d5l
    public boolean checkusername(String uname)
    {
        PreparedStatement ps;
         ResultSet rs;
         boolean check=false;
                      String query="SELECT * FROM `client` WHERE `Username` =? ";
       
         try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, uname);
         
            rs=ps.executeQuery();
            if(rs.next())
            {
              check =true;
            
            }
           
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
         return check;
    }
    public clientHandler(Socket c)
    {
        this.socket = c;
    }
     @Override
    public void run()
    {
       
        try
        {
          // JOptionPane.showMessageDialog(null, "hand");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            //4.perform IO with client
            while (true)
            {
              //   JOptionPane.showMessageDialog(null, "no");
               //dah ely 2olt 3lih bi2ra ana ba3ml 2ih
                int know=dis.read();
               //hna 1 y3ny ana fl register
                if(know==1)
                {
              JOptionPane.showMessageDialog(null, "client want to register");
                //bia5od el data mn el client
               String     fn = dis.readUTF();
                 String    sn = dis.readUTF();
                 String    username= dis.readUTF();
                 String     pass = dis.readUTF();
                String       amount = dis.readUTF();
            
                //bn3ml connect bl data base
                if(checkusername(username))
                {
                    dos.writeUTF("the username exist");
                }
                   PreparedStatement ps;
               String query="INSERT INTO `client`(`firstname`, `secondname`, `Username`, `password`, `amount`) VALUES (?,?,?,?,?)";
                try{
       //   JOptionPane.showMessageDialog(null,amount);
            ps=Myconnection.getconnection().prepareStatement(query);
           ps.setString(1, fn);
            ps.setString(2, sn);
            ps.setString(3, username);
            ps.setString(4, pass);
            ps.setString(5,amount);
           // JOptionPane.showMessageDialog(null,ps.executeUpdate());
            if(ps.executeUpdate()>0)
            {
                JOptionPane.showMessageDialog(null, "new user");
                
            }
             dos.writeUTF("done");
             }catch(Exception ex){
           System.out.print("btngaan");
       }
                }
                //hna login
                else if(know ==2)
                {
                     JOptionPane.showMessageDialog(null, "client want to login");
                    PreparedStatement ps;
                      ResultSet rs;
                      //get username and pass from client
                  String    username= dis.readUTF();
                 String     pass = dis.readUTF();
                 String query="SELECT * FROM `client` WHERE `Username` =? AND `password` =?";
        try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, username);
             ps.setString(2, pass);
            rs=ps.executeQuery();
            if(rs.next())
            {
               
                  dos.writeUTF("done");
                
            }
            else
            {
                  dos.writeUTF("Renter correct password or username "); 
            }
         
        } catch (SQLException ex) {
            System.out.print("error");
        }
       
                }
                //hna y3rf m3ah kam
         else if(know==3)
                {
                     JOptionPane.showMessageDialog(null, "client want to know amount");
                    String username=dis.readUTF();
                    PreparedStatement pss;
        ResultSet rss;
       String query1="SELECT * FROM `client` WHERE `Username` =? ";
       
         try {
            pss=Myconnection.getconnection().prepareStatement(query1);
            pss.setString(1, username);
         
            rss=pss.executeQuery();
            if(rss.next())
            {
                dos.writeUTF(rss.getString(6));
                  dos.writeUTF("done");
            }
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
                }  
         //hna y7ot flos
                else if(know==4)
                {
                      JOptionPane.showMessageDialog(null, "client want to add amount");
                     PreparedStatement ps;
                    ResultSet rs;
                        double ini=0;
                        String username=dis.readUTF();
                     //    JOptionPane.showMessageDialog(null, username);
                double am=dis.readDouble();
           String query="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, username);
            

            rs=ps.executeQuery();
            if(rs.next())
            {
              ini = rs.getDouble("amount");
              ini+=am;
              int id =rs.getInt("id");
              if(history[id]==null)
                  history[id]="add in your account="+am+"\n";
              else
              history[id]+="add in your account="+am+"\n";
              //  JOptionPane.showMessageDialog(null, id);
       //  JOptionPane.showMessageDialog(null, history[id]);
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         String query2="update client set amount = ? where Username = ?";
        //  JOptionPane.showMessageDialog(null, username);
        try {
           ps=Myconnection.getconnection().prepareStatement(query2);
         ps.setString   (1,Double.toString(ini) );
      ps.setString(2, username);

      ps.executeUpdate();
      
                dos.writeUTF("done");
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
                }
                //w hna his7b
                else if(know==5)
                {
                    JOptionPane.showMessageDialog(null, "client want to withdraw amount");
                    PreparedStatement ps;
                ResultSet rs;
             double ini=0;
                double add=0;
                String username=dis.readUTF();
                double am=dis.readDouble();
             String query="SELECT * FROM `client` WHERE `Username` =? ";
       
         try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, username);
         
            rs=ps.executeQuery();
            if(rs.next())
            {
              String  ad=rs.getString(6);
              add=Double.parseDouble(ad);//fiha 2l rakm ely fl database
            
            }
           
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
         dos.writeDouble(add);
         if(am<add)
         {
            
          // JOptionPane.showMessageDialog(null, "the amount you want to add is larger then yours");
         
       String query1="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            ps=Myconnection.getconnection().prepareStatement(query1);
            ps.setString(1, username);
            

            rs=ps.executeQuery();
            if(rs.next())
            {
              ini = rs.getDouble(6);
           ini-=am;
              int id =rs.getInt("id");
              if(history[id]==null)
                  history[id]="withdraw from your account="+am+"\n";
              else
               history[id]+="withdraw from your account="+am+"\n";
             
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         String query2="update client set amount = ? where Username = ?";
          //JOptionPane.showMessageDialog(null, username);
        try {
           ps=Myconnection.getconnection().prepareStatement(query2);
         ps.setString   (1,Double.toString(ini) );
      ps.setString(2, username);

      ps.executeUpdate();
      
           dos.writeUTF("done");
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
                }
         
                }
                else if(know ==6)
                {
                 JOptionPane.showMessageDialog(null, "client want to change amount to another account");
                    PreparedStatement ps;
                      ResultSet rs;
                      //get username and pass from client
                   String    username1= dis.readUTF();
                  String    username2= dis.readUTF();
                //  JOptionPane.showMessageDialog(null,username1);
                //  JOptionPane.showMessageDialog(null, username2);
                 String     pass = dis.readUTF();
                 double     amount = dis.readDouble();
                 String query="SELECT * FROM `client` WHERE `Username` =? AND `password` =?";
        try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, username2);
             ps.setString(2, pass);
            rs=ps.executeQuery();
            if(rs.next())
            {
               //JOptionPane.showMessageDialog(null, "correct account");
               //27sb m3aya asln el mabl3' wla
                PreparedStatement pss;
                ResultSet rss;
             double ini=0;
                double add=0;
              
              //  double am=dis.readDouble();
             String query5="SELECT * FROM `client` WHERE `Username` =? ";
       
         try {
            pss=Myconnection.getconnection().prepareStatement(query5);
            pss.setString(1, username1);
         
            rss=pss.executeQuery();
            if(rss.next())
            {
              String  ad=rss.getString(6);
              add=Double.parseDouble(ad);//fiha 2l rakm ely fl database
              //JOptionPane.showMessageDialog(null,add+"el rakm database");
            }
           
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
       //  dos.writeDouble(add);
         if(amount<add)
         {
            
          // JOptionPane.showMessageDialog(null, "the amount you want to add is larger then yours");
         
       String query1="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            pss=Myconnection.getconnection().prepareStatement(query1);
            pss.setString(1, username1);
            
           //JOptionPane.showMessageDialog(null, username1);
            rss=pss.executeQuery();
            if(rss.next())
            {
              ini = rss.getDouble(6);
                //JOptionPane.showMessageDialog(null, ini+"bardo");
           ini-=amount;
             int id =rss.getInt("id");
              //   JOptionPane.showMessageDialog(null,id);
              //     JOptionPane.showMessageDialog(null,history[id]);
              if(history[id]==null)
                  history[id]="move "+amount+" to "+username2+"\n";
              else
                 history[id]+="move "+amount+" to "+username2+"\n";
            
               //JOptionPane.showMessageDialog(null,ini+"ba3d man2s");
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         String query2="update client set amount = ? where Username = ?";
          //JOptionPane.showMessageDialog(null, username);
        try {
           pss=Myconnection.getconnection().prepareStatement(query2);
         pss.setString   (1,Double.toString(ini) );
      pss.setString(2, username1);

      pss.executeUpdate();
      //azwdo lltany
       PreparedStatement pse;
                    ResultSet rse;
                        double inii=0;
                      
           String query9="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            pse=Myconnection.getconnection().prepareStatement(query9);
            pse.setString(1, username2);
            

            rse=pse.executeQuery();
            if(rse.next())
            {
              inii = rse.getDouble(6);
                 //JOptionPane.showMessageDialog(null, inii+"abl mazawd");
                inii+=amount;
          //JOptionPane.showMessageDialog(null, inii+"ba3d ma azad");
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         String query8="update client set amount = ? where Username = ?";
        //  JOptionPane.showMessageDialog(null, username);
        try {
           pse=Myconnection.getconnection().prepareStatement(query8);
         pse.setString   (1,Double.toString(inii) );
      pse.setString(2, username2);

      pse.executeUpdate();
      
                dos.writeUTF("done");
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         //  dos.writeUTF("done");
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
                }
         else
         {
             dos.writeUTF("you don't have money "); 
         }
                 // dos.writeUTF("done");
                
            }
            else
            {
                  dos.writeUTF("Renter correct password or username "); 
            }
         
        } catch (SQLException ex) {
            System.out.print("error");
        }
                }
                else if(know ==7)
                {
              JOptionPane.showMessageDialog(null, "client want to move from diffrent bank");
             
                String uname1=dis.readUTF();
                String uname2=dis.readUTF();
                  String pass=dis.readUTF();
                  Double amount=dis.readDouble();
                   String ip=dis.readUTF();
                        double ini=0;
                double add=0;
               PreparedStatement pss;
                     ResultSet rss;
              //  double am=dis.readDouble();
             String query5="SELECT * FROM `client` WHERE `Username` =? ";
       
         try {
            pss=Myconnection.getconnection().prepareStatement(query5);
            pss.setString(1, uname1);
         
            rss=pss.executeQuery();
            if(rss.next())
            {
              String  ad=rss.getString(6);
              add=Double.parseDouble(ad);//fiha 2l rakm ely fl database
              //JOptionPane.showMessageDialog(null,add+"el rakm database");
            }
           
        } catch (SQLException ex) {
             System.out.println("Something went wrong ");
        }
       //  dos.writeDouble(add);
       String str="";
       boolean flag=false;
         if(amount<add)
         {
            try
        {
          server.s="server";
            //connect to server
            
         Socket cc = new Socket(ip, 1111);
         JOptionPane.showMessageDialog(null, "connect to server");
            flag=true;
         DataOutputStream doss = new DataOutputStream(cc.getOutputStream());
            DataInputStream diss = new DataInputStream(cc.getInputStream());
               while(true)
               {
                  
                   doss.writeUTF(uname2);
                   doss.writeUTF(pass);
                   doss.writeDouble(amount);
                    str=diss.readUTF();
                   if(str.equals("done"))
                       break;
                   else
                       dos.writeUTF(str);
                   
               }
         
         /*       while (true)
            {
                
                PreparedStatement ps;
                     ResultSet rs;
             String query="SELECT * FROM `client` WHERE `Username` =? AND `password` =?";
        try {
            ps=Myconnection.getconnection().prepareStatement(query);
            ps.setString(1, uname2);
             ps.setString(2, pass);
            rs=ps.executeQuery();
            if(rs.next())
            {
               
                  PreparedStatement pse;
                    ResultSet rse;
                        double inii=0;
                      
           String query9="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            pse=Myconnection.getconnection().prepareStatement(query9);
            pse.setString(1, uname2);
            

            rse=pse.executeQuery();
            if(rse.next())
            {
              inii = rse.getDouble(6);
                 //JOptionPane.showMessageDialog(null, inii+"abl mazawd");
                inii+=amount;
          //JOptionPane.showMessageDialog(null, inii+"ba3d ma azad");
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         String query8="update client set amount = ? where Username = ?";
        //  JOptionPane.showMessageDialog(null, username);
        try {
           pse=Myconnection.getconnection().prepareStatement(query8);
         pse.setString   (1,Double.toString(inii) );
      pse.setString(2, uname2);

      pse.executeUpdate();
      
              //  dos.writeUTF("done");
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
                
            }
            else
            {
                  JOptionPane.showMessageDialog(null, "Renter correct password or username ");
            }
         
        } catch (SQLException ex) {
            System.out.print("error");
        }
       
              
                    break;
          
                 }
            
            
            cc.close();*/
            server.s="client";
     }
               catch (IOException ex)
        {
             System.out.println("Something went wronggg");
        }
           if(flag==true)
           {
     String query1="SELECT * FROM `client` WHERE `Username` =?   ";
              
         try {
            pss=Myconnection.getconnection().prepareStatement(query1);
            pss.setString(1, uname1);
            
//JOptionPane.showMessageDialog(null, uname1);
            rss=pss.executeQuery();
            if(rss.next())
            {
              ini = rss.getDouble(6);
                //JOptionPane.showMessageDialog(null, ini+"bardo");
           ini-=amount;
             int id =rss.getInt("id");
              //   JOptionPane.showMessageDialog(null,id);
              //     JOptionPane.showMessageDialog(null,history[id]);
              if(history[id]==null)
                  history[id]="move "+amount+" to "+uname2+"\n";
              else
                 history[id]+="move "+amount+" to "+uname2+" form "+ip+"\n";
            
               //JOptionPane.showMessageDialog(null,ini+"ba3d man2s");
        //    ps.setString(2, amount);
            }
           
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
          String query2="update client set amount = ? where Username = ?";
          
        try {
           pss=Myconnection.getconnection().prepareStatement(query2);
         pss.setString   (1,Double.toString(ini) );
      pss.setString(2, uname1);

      pss.executeUpdate();
              dos.writeUTF("done");
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
         }
           else
               dos.writeUTF("cann't connect");
         }
            else
         {
             dos.writeUTF("you don't have money "); 
         }
      
         
         
                    
                }
                else if(know==8)
                {
                    JOptionPane.showMessageDialog(null, "client want to know history");
                    String username=dis.readUTF();
                    PreparedStatement pss;
                     ResultSet rss;
            String query1="SELECT * FROM `client` WHERE `Username` =? ";
       
          try {
            pss=Myconnection.getconnection().prepareStatement(query1);
            pss.setString(1, username);
         
            rss=pss.executeQuery();
            if(rss.next())
            {
                int ip=rss.getInt(1);
               // JOptionPane.showMessageDialog(null, ip);
               //  JOptionPane.showMessageDialog(null, history[ip]);
               if(history[ip]==null)
                   dos.writeUTF("no history");
               else
                dos.writeUTF(history[ip]);
              
                  dos.writeUTF("done");
            }
           
        } catch (SQLException ex) {
            System.out.println("Something went wrong ");
        }
                }
           //connect the database
//               set_data(fn,sn,username,pass,amount);
                   // String userChoice = dis.readUTF();
               
                    break;
                
            }

            //5. close comm with client
            dos.close();
            dis.close();
             //System.out.println("bt2fl ");
            socket.close();
     
        } catch (Exception e)
        {
            System.out.println("Something went wrong ");
        }
    
}

}
