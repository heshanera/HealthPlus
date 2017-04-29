package com.hms.hms_test_2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class User
{
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// methods /////////////////////////////////////////////////////////////////////////////////////////////
	/*
	* 
	* 
	* 
	public User(String username)										==> Constructor
	public boolean login(String user,String username,String password)	==> login to user account
	
	* 
	*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public DatabaseOperator dbOperator;
	public String username;
	public String userID;
	public String userType;
        
        public String database = "";
        public String dbUsername = "";
        public String dbPassword = "";
        
        public User() throws IOException
	{
            InputStream inputStream = null;
            
            try {
                Properties prop = new Properties();
                String propFileName = "config.properties";

                inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

                if (inputStream != null) {
                        prop.load(inputStream);
                } else {
                        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                }

                //String connection = prop.getProperty("connection");
                this.dbUsername = prop.getProperty("user");
                this.dbPassword = prop.getProperty("password");
                this.database = prop.getProperty("database");

                System.out.println(dbUsername+" "+dbPassword +" "+database);
                
            } catch (Exception e) {
                    System.out.println("Exception: " + e);
            } finally {
                    try{
                    inputStream.close();
                }catch(Exception e){} 
            }
            
		this.dbOperator = new DatabaseOperator();
		try{
			dbOperator.connect(dbUsername,dbPassword);
			dbOperator.useDatabse(database);
                        
		}catch(SQLException | ClassNotFoundException e){e.printStackTrace();}
	}
	
	public User(String username)
	{
            InputStream inputStream = null;
            
            try {
                Properties prop = new Properties();
                String propFileName = "config.properties";

                inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

                if (inputStream != null) {
                        prop.load(inputStream);
                } else {
                        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                }

                //String connection = prop.getProperty("connection");
                this.dbUsername = prop.getProperty("user");
                this.dbPassword = prop.getProperty("password");
                this.database = prop.getProperty("database");

                System.out.println(dbUsername+" "+dbPassword +" "+database);
                
            } catch (Exception e) {
                    System.out.println("Exception: " + e);
            } finally {
                
                try{
                    inputStream.close();
                }catch(Exception e){}    
            }
            
            
            this.dbOperator = new DatabaseOperator();
            try{
                dbOperator.connect(dbUsername,dbPassword);
                dbOperator.useDatabse(database);
                this.username = username;
                ArrayList<ArrayList<String>> result = dbOperator.showTableData("sys_user","user_id,user_type",("user_name = '" + username + "'"));
                this.userID = result.get(0).get(0);
                this.userType = result.get(0).get(1);

                //System.out.println(userID);
                //System.out.println(userType);

            }catch(SQLException | ClassNotFoundException e){e.printStackTrace();}
	}
	
	public String checkUser(String username, String password)
        {
            DatabaseOperator tmpOperator = new DatabaseOperator();
            String access = "false";
            try{
                    ArrayList<ArrayList<String>> result = tmpOperator.showTableData("sys_user","password,user_type",("user_name = '" + username + "'"));
                    System.out.println(result);    
                    String userPassword = result.get(0).get(0);
                    String userType = result.get(0).get(1);

                    if ( userPassword.equals(password) ) access = userType;

            }catch(SQLException | ClassNotFoundException e){e.printStackTrace();}
            return access;        
        }

        public boolean saveLogin(String username)
        {
           
           String sql =    "UPDATE sys_user SET online=1,login=NOW() "+
                            "WHERE user_name ='"+username+"';";
          
            boolean result = true;

            try{			
                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){}	
            
            return result;
        }  
        
        public boolean saveLogout(String username)
        {
           
           String sql =    "UPDATE sys_user SET online=0,logout=NOW() "+
                            "WHERE user_name ='"+username+"';";
          
            boolean result = true;

            try{			
                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){}	
            
            return result;
        } 
        
        public boolean sendMessage(String sender, String receiver,String subject, String message)
	{	
            
            String messID = "msg00001";
            
            /////////////////// Generating the Message ID //////////////////////////////////////////////////////////////
            try{
                String sql2 = "SELECT message_id FROM user_message WHERE message_id = (SELECT MAX(message_id) FROM user_message);";
                String messageID = dbOperator.customSelection(sql2).get(1).get(0);

                char[] tmpID = messageID.toCharArray();
                int i = 3;
                for (i = 3; i < messageID.length(); i++)
                {
                        if  (tmpID[i] != '0') break; 
                } 

                String tmpID2 = Integer.toString(Integer.parseInt(messageID.substring(i,messageID.length())) + 1 );
                while(tmpID2.length() < 5)
                {
                        tmpID2 = "0" + tmpID2;
                }
                messID = "msg" + tmpID2;
                
            }catch(Exception e){e.printStackTrace(); messID = "msg00001";}
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String messDate = dtf.format(now);
            
            String sql =    "INSERT INTO user_message "+
                            "(message_id,reciver,sender,subject,message,date) VALUES "+
                            "('"+messID+"','"+receiver+"','"+sender+"','"+subject+"','"+message+"','"+messDate+"');";

            boolean result = true;

            try{			

                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            //System.out.println(data);
            return result;
	}
        
        public ArrayList<ArrayList<String>> getMessages()
	{	
            String sql =    "SELECT "+
                            "sender,subject,message,date,rd,message_id "+
                            "FROM user_message "+
                            "WHERE reciver ='"+this.userID+"' "+
                            "ORDER BY date DESC;";
            
            ArrayList<ArrayList<String>> data = null;

            try{			
                    data = dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){}	
            //System.out.println(data);
            return data;
	}
        
        public boolean deleteMessage(String msgID)
	{	
            String sql =    "DELETE FROM user_message "+
                            "WHERE message_id ='"+msgID+"';";

            boolean result = true;

            try{			
                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){}	
            
            return result;
	}
        
        public String getProfilePic()
	{	
            String image =  "p2.png";
            String sql =    "SELECT "+
                            "profile_pic "+
                            "FROM sys_user "+
                            "WHERE sys_user.user_name = '"+this.username+"';";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

                    image = data.get(1).get(0);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            //System.out.println(data);
            return image;
	}
        
        public String getProfilePic(String userID)
	{	
            String image =  "p2.png";
            String sql =    "SELECT "+
                            "profile_pic "+
                            "FROM sys_user "+
                            "WHERE sys_user.user_name = '"+userID+"';";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

                    image = data.get(1).get(0);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            //System.out.println(data);
            return image;
	}
		
        
        public boolean setProfilePic(String name)
	{	
            
            String sql =    "UPDATE sys_user "+
                            "SET profile_pic = '"+name+"'"+
                            "WHERE sys_user.user_name = '"+this.username+"';";

            boolean result = true;

            try{			

                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            //System.out.println(data);
            return result;
	}
        
        public ArrayList<ArrayList<String>> getName(String userID)
	{	
            String sql =    "SELECT "+
                            "person.first_name, person.last_name, sys_user.user_type "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE sys_user.user_id = '"+userID+"';";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            System.out.println(data);
            return data;
	}
        
        public ArrayList<ArrayList<String>> getUserNameAndID()
	{	
            String sql =    "SELECT "+
                            "person.first_name, person.last_name, sys_user.user_type,sys_user.user_id "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id;";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            System.out.println(data);
            return data;
	}
        
        
        
        
        public ArrayList<ArrayList<String>> getCurrentUserNameAndID()
	{	
            String sql =    "SELECT "+
                            "person.first_name, person.last_name, sys_user.user_type,sys_user.user_id "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE sys_user.user_id = '"+this.userID+"';";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            System.out.println(data);
            return data;
	}
        
        public boolean setMessageRead(String msgID)
        {
             String sql =   "UPDATE user_message "+
                            "SET rd = '"+1+"'"+
                            "WHERE user_message.message_id = '"+msgID+"';";

            boolean result = true;

            try{			

                    result = dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            //System.out.println(data);
            return result;
        }        
        
        public String getMessageSenderInfo(String msgid)
	{	
            String sql =    "SELECT "+
                            "user_message.sender "+
                            "FROM user_message WHERE user_message.message_id = '"+msgid+"';";

            ArrayList<ArrayList<String>> data = null;

            try{			

                    data = dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
            
            String result = "";
            try{
            
                result = data.get(1).get(0);
                
            }catch(Exception e){}
            
            
            return result;
	}
/*
	public static void main(String[] args)
	{
		User u1 = new User("user001");
		boolean access = u1.login("sys_user","user001","pass");
		System.out.println(access);
	}
*/

} 

