/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms_project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author madhulika
 */
public class database {
    
     // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/dbms";
    private static final String user = "root";
    private static final String password = "@sAp0123";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static PreparedStatement pst;
    private static ResultSet rs;

    database()
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,user,password);
           pst=con.prepareStatement("select email,pwd from User where email=? and pwd=?");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public int getUserId(String email)
    {
        int id=0;
        try {
            pst=con.prepareStatement("Select userid from User where email=?");
            pst.setString(1,email);
            
            rs=pst.executeQuery();
            while(rs.next())
            {
               id=rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    public void delBook(int userid,DefaultTableModel model,int selRowIndex)
    {
        try {
            String isbn=(String) model.getValueAt(selRowIndex,0);
            PreparedStatement pst;
            pst=con.prepareStatement("Delete from User_Book where userid=? and isbn=?");
            pst.setInt(1,userid);
            pst.setString(2,isbn);
            pst.executeUpdate();
            model.removeRow(selRowIndex);
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  public Boolean insertUserBook(int userid,String isbn,String desc,String price,String cp)
    {
        PreparedStatement pst;
       int cnt=0;
        try {
            pst=con.prepareStatement("Select count(isbn) from User_Book where userid=? and isbn=?");
            pst.setInt(1, userid);
            pst.setString(2,isbn);
            rs=pst.executeQuery();
            while(rs.next())
            {
                cnt=rs.getInt(1);
            }
            System.out.println(cnt);
           if(cnt==0)
           {
            pst=con.prepareStatement("insert into User_Book values(?,?,?,?,?)");
            pst.setInt(1,userid);
            pst.setString(2,isbn);  
            pst.setString(3,desc);
            pst.setDouble(4, Double.parseDouble(price));
            pst.setInt(5, Integer.parseInt(cp));
            
           int rows= pst.executeUpdate();
           System.out.print(rows);
           if(rows!=0)
            return true;   
           }
        } catch (SQLException ex) {
            
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String getName(String email)
    {
        String name="";
        try {
            pst=con.prepareStatement("Select fname,lname from User where email=?");
            pst.setString(1,email);
            
            rs=pst.executeQuery();
            while(rs.next())
            {
                name = rs.getString(1);
                name+=" ";
                name+=rs.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }
    
    public Boolean registerUser(String fname,String lname,String email,String pwd,String city,String phno)
    {
        try {
            pst=con.prepareStatement("Select email from User where email=?");
            pst.setString(1,email);
            
            if(pst.execute())
            {
                pst=con.prepareStatement("insert into User(fname,lname,email,pwd,city,phno) values(?,?,?,?,?,?)");
                pst.setString(1,fname);
                pst.setString(2,lname);
                pst.setString(3,email);
                pst.setString(4,pwd);
                pst.setString(5,city);
                pst.setString(6,phno);
            
                pst.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
      public Boolean checkLogin(String email,String pwd)
    {
        try {
                        
            pst.setString(1, email); //this replaces the 1st  "?" in the query for username
            pst.setString(2, pwd);    //this replaces the 2st  "?" in the query for password
            //executes the prepared statement
            rs=pst.executeQuery();
            if(rs.next())
            {
                //TRUE iff the query founds any corresponding data
                return true;
            }
            else
            {
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating"+e);
            return false;
        }
}
      
      public void myBooks(int userid,DefaultTableModel model)
      {
        
        try {
            model.getDataVector().removeAllElements();
            pst=con.prepareStatement("Select u.isbn,b.title,b.author,u.price,u.copies from User_Book u,"
                    + "Book_Details b where u.isbn=b.isbn and userid=?");
            pst.setInt(1,userid);
            
            rs=pst.executeQuery();
            while(rs.next())
            {
                model.addRow(new Object[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),
                rs.getInt(5)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
            
      }
      
     public boolean insertBookDetails(String isbn, String title, String author, String pub) {
        try{
            System.out.println(isbn+" "+title+" "+author+" "+pub);
               PreparedStatement pst1=con.prepareStatement("Insert into Book_Details values(?,?,?,?)");
               pst1.setString(1, isbn);
               pst1.setString(2, title);
               pst1.setString(3,author);
               pst1.setString(4,pub);
               
               int rows = pst1.executeUpdate();
               System.out.println(rows);
               if(rows!=0)
                   return true;
           
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean insertBookGenre(int userid, String isbn, List<String> genre) {
        try {
            pst=con.prepareStatement("insert into Book_Genre values(?,?,?)");
            for(String g:genre)
            {
                pst.setInt(1,userid);
                pst.setString(2,isbn);
                pst.setString(3,g);
                
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }

    boolean checkBookDetails(int userid, String bn) {
        try {
           PreparedStatement pst1=con.prepareStatement("delete from Book_Details where isbn=?");
           pst1.setString(1,bn);
           pst1.executeUpdate();
               return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    boolean searchTitle(int userid,String title,DefaultTableModel model) {
        try {
          
            PreparedStatement pst=con.prepareStatement("Select u.userid,u.isbn,title,author,publisher,price,"
                    + "copies from User_Book u join Book_Details b on u.isbn=b.isbn join User us on u.userid=us.userid "
                    + " where city=(Select city from User where userid=?) and title like ?");
            pst.setInt(1,userid);
            pst.setString(2, "%"+title+"%");
            //pst.setInt(2,userid);
            rs=pst.executeQuery();
            while(rs.next())
            {
                model.addRow(new Object[]{rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
                    rs.getDouble(6),rs.getInt(7),rs.getString(1)});
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
            return false;
    }

    String getDesc(int userid, String isbn) {
        try {
            PreparedStatement pst=con.prepareStatement("Select descr from User_Book where userid=? and isbn=?");
            pst.setInt(1,userid);
            pst.setString(2,isbn);
            rs=pst.executeQuery();
            while(rs.next())
            {
                return rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return " ";
    }

   List<String> getGenre(int userid, String isbn) {
           List<String> genre=new ArrayList<>();
       try {
            PreparedStatement pst=con.prepareStatement("Select genre from Book_Genre where userid=? and isbn=?");
            pst.setInt(1,userid);
            pst.setString(2,isbn);
            rs=pst.executeQuery();
            while(rs.next())
            {
                genre.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
       return genre;
    }

    boolean buyReq(int sellerid, int userid, int qty, String isbn,int cp) {
         try {
           
            if(qty<=cp)
            {
            PreparedStatement pst=con.prepareStatement("insert into Buy_Request values(?,?,?,?)");
            pst.setInt(1,sellerid);
            pst.setInt(2,userid);
            pst.setInt(3,qty);
            pst.setString(4,isbn);
            pst.executeUpdate();
           return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }

    void buyReqDisp(int userid, DefaultTableModel model) {
         try {
            PreparedStatement pst=con.prepareStatement("Select buyer_id,qty,isbn from Buy_Request where seller_id=?");
            pst.setInt(1,userid);
            rs=pst.executeQuery();
            while(rs.next())
            {
                int buyerid=rs.getInt(1);
                int qty=rs.getInt(2);
                String isbn=rs.getString(3);
               
                PreparedStatement pst1=con.prepareStatement("select b.title,u.fname,u.lname,ub.price,u.phno from User_Book ub join Book_Details b on ub.isbn=b.isbn cross join User u where u.userid=? and b.isbn=?");
            pst1.setInt(1,buyerid);
            pst1.setString(2, isbn);
            ResultSet rs1=pst1.executeQuery();
            while(rs1.next())
            {
                model.addRow(new Object[]{rs1.getString(1),
                rs1.getString(2)+" "+rs1.getString(3),
                rs1.getDouble(4)*qty,
                qty,
                rs1.getString(5)});
            }
                //addToTable(buyerid,qty,isbn,model);
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean accReq(int userid, Object valueAt, Object valueAt0, Object valueAt1 ) {
        try {
            PreparedStatement pst1=con.prepareStatement("Delete from Buy_Request where seller_id=? and buyer_id=(Select userid from User where concat(fname,' ',lname)=?) and isbn=(Select isbn from Book_Details where title=?)");
            pst1.setInt(1,userid);
            pst1.setString(2, valueAt.toString());
            pst1.setString(3,valueAt0.toString());
            
            pst1.executeUpdate();
            
            PreparedStatement pst2=con.prepareStatement("Update User_Book set copies=copies-? where userid=? and isbn=(Select isbn from Book_Details where title=?)");
            pst2.setInt(1,Integer.parseInt(valueAt1.toString()));
            pst2.setInt(2,userid);
            pst2.setString(3,valueAt0.toString());
            
            pst2.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    void mybuyReqTab(int userid, DefaultTableModel model) {
         try {
            PreparedStatement pst=con.prepareStatement("Select seller_id,qty,isbn from Buy_Request where buyer_id=?");
            pst.setInt(1,userid);
            rs=pst.executeQuery();
            while(rs.next())
            {
                int sellerid=rs.getInt(1);
                int qty=rs.getInt(2);
                String isbn=rs.getString(3);
                
                PreparedStatement pst1=con.prepareStatement("select b.title,u.fname,u.lname,ub.price,u.phno from User_Book ub join Book_Details b on ub.isbn=b.isbn cross join User u where u.userid=? and b.isbn=?");
            pst1.setInt(1,sellerid);
            pst1.setString(2, isbn);
            ResultSet rs1=pst1.executeQuery();
            while(rs1.next())
            {
                model.addRow(new Object[]{rs1.getString(1),
                rs1.getString(2)+" "+rs1.getString(3),
                rs1.getDouble(4)*qty,
                qty,
                rs1.getString(5)});
            }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    boolean searchGenre(int userid, String genre, DefaultTableModel model) {
try {
          
            PreparedStatement pst=con.prepareStatement("Select u.userid,u.isbn,title,author,publisher,price,"
                    + "copies from User_Book u join Book_Details b on u.isbn=b.isbn join Book_Genre g on"
                    + " u.userid=g.userid and u.isbn=g.isbn where genre=?");
            pst.setString(1,genre);
            //pst.setInt(2,userid);
            rs=pst.executeQuery();
            while(rs.next())
            {
                model.addRow(new Object[]{rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
                    rs.getDouble(6),rs.getInt(7),rs.getString(1)});
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
            return false;
    }

    boolean rejReq(int userid, Object valueAt, Object valueAt0) {
 try {
            PreparedStatement pst1=con.prepareStatement("Delete from Buy_Request where seller_id=? and buyer_id=(Select userid from User where concat(fname,' ',lname)=?) and isbn=(Select isbn from Book_Details where title=?)");
            pst1.setInt(1,userid);
            pst1.setString(2, valueAt.toString());
            pst1.setString(3,valueAt0.toString());
            
            pst1.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

   

}
