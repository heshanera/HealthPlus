/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author heshan
 */
package Admin;

import com.hms.hms_test_2.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author heshan
 */
public class Admin extends User
{

    /**
     * Constructor of the class
     * @param username username of the Administrator
     */
    Admin(String username) 
    {
        super(username);
    }
    
    /**
     * @return the username
     */
    public String getUsername()
    {
        return super.username;
    }
    
    /**
     * Returns Administrator Profile Info
     * @return HashMap<String,String> with key is column name in database table and value is the stored value 
     */
    public HashMap<String,String> getProfileInfo()
    {

            HashMap<String,String> infoHash = new HashMap<>();	

            //////////////////////////////////// Getting data from database //////////////////////////////////////////////////////////////
            String sql =    "SELECT person.*,sys_user.* "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE (sys_user.user_id = '"+userID+"' AND person.user_id = '"+userID+"');";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            ///////////// Putting data into hashmap /////////////////////////////////////////////////////////////////////////////////////

            ArrayList<String> columns = data.get(0);
            ArrayList<String> dataRow = data.get(1);

            int length = columns.size();
            for (int i = 0; i < length; i++)
            {
                    infoHash.put(columns.get(i),dataRow.get(i));	
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //System.out.println(infoHash);
            return infoHash;
    }
    
    /**
     * Update the basic info of the administrator account 
     * @param info updated profile information given as a space separated string
     * @return boolean if the operation was successful or not
     */
    public boolean updateProfileInfo(String info)
    {
        boolean result = true;
        String column_data = "";

        String[] tmpInfo = info.split("#");
        for(String s : tmpInfo)
        {
                String[] tmpData = s.split(" ");
                int tmplen = tmpData[0].length() + 1;
                column_data += tmpData[0] + "='" + s.substring(tmplen, s.length()) + "',";
                //System.out.println(column_data);
        }

        column_data = column_data.substring(0,column_data.length()-1);

        String sql =	"UPDATE person SET " + column_data + " "  +
                        "WHERE person_id = (SELECT person_id FROM sys_user WHERE user_id = '" + this.userID + "');";

        try{

                super.dbOperator.customInsertion( sql );


        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

        return result;
    }
    
    /**
     * Update the account info of the administrator account 
     * @param info updated profile information given as a space separated string
     * @return boolean if the operation was successful or not
     */
    public boolean updateAccountInfo(String info)
    {
            boolean result = true;
            String column_data = "";

            String[] tmpInfo = info.split("#");
            for(String s : tmpInfo)
            {
                    String[] tmpData = s.split(" ");
                    column_data += tmpData[0] + "='" + s.substring(tmpData[0].length()+1,s.length()) + "',";
                    //System.out.println(column_data);
            }

            column_data = column_data.substring(0,column_data.length()-1);

            String sql =    "UPDATE sys_user SET " + column_data + " "  +
                            "WHERE user_id = '" + this.userID + "');";

            try{

                    super.dbOperator.customInsertion( sql );


            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

            return result;
    }
    
    /**
     * Create new User Accounts
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param userType user type of the account
     * @param nic NIC of the user
     * @param mobile mobile no of the user
     * @param slmcReg if the user is a doctor SLMC registration number of the user else empty string "" 
     * @return boolean if new account created or not
     */
    public ArrayList<String> createNewUser(String firstName,String lastName,String userType,String nic,String mobile,String slmcReg)
	{
		boolean result = false;
                ArrayList<String> data = new ArrayList<String>();
		
		/////////////////// Generating the Person ID ////////////////////////////////////////////////////////////
		String personId="";
		String userId=""; 
		String userName="";
		
		try
		{		
			String sql0 = "SELECT person_id FROM person WHERE person_id = (SELECT MAX(person_id) FROM person);";
			String personID = super.dbOperator.customSelection(sql0).get(1).get(0);

			char[] tmpID = personID.toCharArray();
			int i = 3;
			for (i = 3; i < personID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			//System.out.println(personID);
			
			String tmpID2 = Integer.toString(Integer.parseInt(personID.substring(i,personID.length())) + 1 );
			while(tmpID2.length() < 5)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "hms" + tmpID2;
			personId =tmpID2;
			//System.out.println(tmpID2);
		}catch(Exception e) {e.printStackTrace();}
		
		/////////////////// Generating the User ID ////////////////////////////////////////////////////////////
		
		try
		{		
			String sql0 = "SELECT user_id FROM sys_user WHERE user_id = (SELECT MAX(user_id) FROM sys_user);";
			String userID = super.dbOperator.customSelection(sql0).get(1).get(0);

			char[] tmpID = userID.toCharArray();
			int i = 3;
			for (i = 3; i < userID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			//System.out.println(userID);
			
			String tmpID2 = Integer.toString(Integer.parseInt(userID.substring(i,userID.length()-1)) + 1 );
			while(tmpID2.length() < 4)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "hms" + tmpID2 + "u";
			userId = tmpID2;
                        
                        data.add(userId);
			//System.out.println(tmpID2);
		}catch(Exception e) {e.printStackTrace();}
		
		/////////////////// Generating the User name ////////////////////////////////////////////////////////////
		try
		{		
			String sql0 = "SELECT user_name FROM sys_user WHERE user_name = (SELECT MAX(user_name) FROM sys_user);";
			String userID = super.dbOperator.customSelection(sql0).get(1).get(0);

			char[] tmpID = userID.toCharArray();
			int i = 4;
			for (i = 4; i < userID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			//System.out.println(userID);
			
			String tmpID2 = Integer.toString(Integer.parseInt(userID.substring(i,userID.length())) + 1 );
			while(tmpID2.length() < 3)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "user" + tmpID2;
			userName = tmpID2;
                        
                        data.add(userName);
			//System.out.println(userName);
		}catch(Exception e) {e.printStackTrace();}
		
		
		///////////////////// Enter Data into Person table ///////////////////////////////////////////////////////
		
		String sql1 = 	"INSERT INTO person(person_id,first_name,last_name,nic,mobile) " +
						"VALUES ('" + personId + "','" + firstName + "','" + lastName + "','" + nic + "','" + mobile +"');";
						
		String sql2 = 	"INSERT INTO sys_user(person_id,user_id,user_name,user_type,password) " +
						"VALUES ('" + personId + "','" + userId + "','" + userName + "','" + userType + "', '1234' );";
						
		String sql3 = 	"UPDATE person SET user_id = '" +userId+ "' WHERE person_id = '" + personId + "';";		
		
		
		String sql4="";
		
		String doctorID="";
		String labAssistantID="";
		String pharmacistID="";
		
		switch (userType)
		{
			case "doctor":	
				sql4 = "INSERT INTO doctor(slmc_reg_no,user_id) " +
						"VALUES ('" + slmcReg + "','" + userId +"');";
	
				break;
			
			case "lab_assistant":
				try
				{		
					String sql0 = "SELECT lab_assistant_id FROM lab_assistant WHERE lab_assistant_id = (SELECT MAX(lab_assistant_id) FROM lab_assistant);";
					String userID = super.dbOperator.customSelection(sql0).get(1).get(0);

					char[] tmpID = userID.toCharArray();
					int i = 3;
					for (i = 3; i < userID.length(); i++)
					{
						if  (tmpID[i] != '0') break; 
					} 
					//System.out.println(userID);
					
					String tmpID2 = Integer.toString(Integer.parseInt(userID.substring(i,userID.length()-1)) + 1 );
					while(tmpID2.length() < 4)
					{
						tmpID2 = "0" + tmpID2;
					}
					tmpID2 = "hms" + tmpID2+"l";
					labAssistantID = tmpID2;
					//System.out.println(labAssistantID);
				}catch(Exception e) {e.printStackTrace();}
				
				sql4 = "INSERT INTO lab_assistant(lab_assistant_id,user_id) " +
						"VALUES ('" + labAssistantID + "','" + userId +"');";
						
				break;		
			
			case "pharmacist":
				try
				{		
					String sql0 = "SELECT pharmacist_id FROM pharmacist WHERE pharmacist_id = (SELECT MAX(pharmacist_id) FROM pharmacist);";
					String userID = super.dbOperator.customSelection(sql0).get(1).get(0);

					char[] tmpID = userID.toCharArray();
					int i = 3;
					for (i = 3; i < userID.length(); i++)
					{
						if  (tmpID[i] != '0') break; 
					} 
					//System.out.println(userID);
					
					String tmpID2 = Integer.toString(Integer.parseInt(userID.substring(i,userID.length()-1)) + 1 );
					while(tmpID2.length() < 4)
					{
						tmpID2 = "0" + tmpID2;
					}
					tmpID2 = "hms" + tmpID2+"p";
					pharmacistID = tmpID2;
					//System.out.println(pharmacistID);
				}catch(Exception e) {e.printStackTrace();}
				
				sql4 = "INSERT INTO pharmacist(pharmacist_id,user_id) " +
						"VALUES ('" + pharmacistID + "','" + userId +"');";
						
				break;		
				
		}
		
                /*
                System.out.println(sql1);
                System.out.println(sql2);
                System.out.println(sql3);
                System.out.println(sql4);
		*/
                
		try{
			super.dbOperator.customInsertion(sql1);
			super.dbOperator.customInsertion(sql2);
			super.dbOperator.customInsertion(sql3);

                        
                        
                        
                        
			
			if ( (userType == "doctor") || (userType == "pharmacist") || (userType == "lab_assistant") )
			{
				super.dbOperator.customInsertion(sql4);
			}
                        
		}catch(Exception e){e.printStackTrace();}	
		
                data.add("1234");
                
		return data;	
		
	}
	
    /**
     *
     * @return
     */
    public ArrayList<ArrayList<String>> getDatabaseInfo()
	{
			
		return null;
	}
	
    /**
     * Get the Login Information and online status of the system user 
     * @param userType
     * @return ArrayList<ArrayList<String>> that contains login info
     */
    public ArrayList<ArrayList<String>> getUserInfo(String userType)
    {
            String sql =    "SELECT person.first_name,person.last_name,person.nic,person.mobile,person.email,sys_user.login,sys_user.online,sys_user.profile_pic,sys_user.user_id "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE (sys_user.user_type = '"+userType+"');";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				

            //System.out.println(data);
            return data;
    }
    	
    /**
     * Get the Login Information and online status of the patients to web site 
     * @return ArrayList<ArrayList<String>> of online info
     */
    public ArrayList<ArrayList<String>> getPersonInfo()
	{
		String sql =    "SELECT person.first_name,person.last_name,patient_useraccount.login,patient_useraccount.online "+
                                "FROM person INNER JOIN patient_useraccount ON person.person_id = patient_useraccount.person_id "+
				"WHERE (patient_useraccount.online = true);";
		
		ArrayList<ArrayList<String>> data = null;
		try{
			data = super.dbOperator.customSelection(sql);
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				
		
		//System.out.println(data);
		return data;
	}
        
    /**
     * Get the Online user at the time
     * @return ArrayList<ArrayList<String>> of online info
     */
    public ArrayList<ArrayList<String>> getOnlineInfo()
    {
            String sql =    "SELECT person.first_name,person.last_name,sys_user.login,sys_user.online,sys_user.user_type "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE (sys_user.online = true);";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				

            //System.out.println(data);
            return data;
    }
    
    
    public ArrayList<ArrayList<String>> getOnlineInfo2()
    {
            String sql =    "SELECT person.first_name,person.last_name,person.nic,person.mobile,person.email,sys_user.login,sys_user.online,sys_user.profile_pic,sys_user.user_id "+
                            "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                            "WHERE (sys_user.online = 1);";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				

            //System.out.println(data);
            return data;
    }
    
    public String getSysUserCount(String userType)
    {
        String result = "";
        
        String sql =    "SELECT COUNT(' amount ') AS count FROM sys_user WHERE user_type='" + userType + "';";
		
        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
                result = data.get(1).get(0);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				
	
        return result;
    } 
    
    public String getPatientCount()
    {
        String result = "";
        
        String sql =    "SELECT COUNT(' amount ') AS count FROM patient_useraccount;";
		
        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
                result = data.get(1).get(0);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				
	
        return result;
    }
    
    public String getAllPatientCount()
    {
        String result = "";
        
        String sql =    "SELECT COUNT(' amount ') AS count FROM patient;";
		
        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
                result = data.get(1).get(0);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				
	
        return result;
    }
    
    public String getOnlineCount()
    {
        String result = "";
        
        String sql =    "SELECT COUNT(' amount ') AS count FROM sys_user WHERE online='1';";
		
        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
                result = data.get(1).get(0);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				
	
        return result;
    }
    
    
    public ArrayList<ArrayList<String>> checkConnection()
    {
            String sql =    "SELECT sys_user.user_name "+
                            "FROM sys_user  "+
                            "WHERE (sys_user.user_id = '"+ this.userID +"');";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				

            //System.out.println(data);
            return data;
    }
    
    public boolean export( String ip, String databaseSchema, String user, String pass,String path )
    {
        boolean result = true;
        String port="3306";
        
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        String date_to_string = dateformatyyyyMMdd.format(dateNow);
        System.out.println("date into yyyyMMdd format: " + date_to_string);
        
        String ss="backup.sql";
        String fullName  = path + " " + date_to_string + " " + ss;
        
        String dumpCommand = "mysqldump " + databaseSchema + " -h " + ip + " -u " + user +" -p" + pass;
        
        Runtime rt = Runtime.getRuntime();
        
        File test=new File(fullName);
        
        PrintStream ps;
        
        try{
                Process child = rt.exec(dumpCommand);
                ps=new PrintStream(test);
                InputStream in = child.getInputStream();
                int ch;
                while ((ch = in.read()) != -1) 
                {
                        ps.write(ch);
                        //System.out.write(ch); //to view it by console
                }

                InputStream err = child.getErrorStream();
                while ((ch = err.read()) != -1) 
                {
                        System.out.write(ch);
                }
        }catch(IOException exc) { result = false;}
        
        return result;
    }
    
    public ArrayList<ArrayList<String>> getSchemaSize(String schemaName)
    {
        /*
            String sql =  "SELECT table_schema 'DB Name',Round(Sum(data_length + index_length) / 1024 / 1024, 1) "+
                          "'DB Size in MB' FROM information_schema.tables WHERE table_schema = '"+schemaName+"' "+
                          "GROUP BY table_schema;";
        */
        String sql =  "SELECT table_schema 'DB Name',Round(Sum(data_length + index_length) / 1024 / 1024, 1) "+
                          "'DB Size in MB' FROM information_schema.tables "+
                          ";";
        
            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}				

            //System.out.println(data);
            return data;
    }
    
    public static String getDirectorySize(String path)
    {

        String command = "du -hs " + path;
        Runtime rt = Runtime.getRuntime();
        PrintStream ps;
        String size = "";
        try{
            Process child = rt.exec(command);
            InputStream in = child.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) 
            {
                    size += (char)ch;
            }
            size = size.split("\t")[0];
            System.out.println(size);

            InputStream err = child.getErrorStream();
            while ((ch = err.read()) != -1) 
            {
                    System.out.write(ch);
            }
        }catch(Exception exc) {
                exc.printStackTrace();
        }
        return size;
    }
    
    public ArrayList<ArrayList<String>> getPatientAttendence(String doctorID)
    {

        String sql = "";

        if ( doctorID.equals("All") ) {

            sql =   "SELECT date FROM appointment WHERE date > (CURRENT_DATE - INTERVAL 12 MONTH) ORDER BY date ASC;";

        } else {

            sql =   "SELECT date FROM appointment "+
                    "WHERE ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (slmc_reg_no = '"+doctorID+"') "+
                    "ORDER BY date ASC;";
        }    


        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;

    }
        
    public ArrayList<ArrayList<String>> getDoctorNames()
    {
        String sql =    "SELECT person.first_name,person.last_name,doctor.user_id,doctor.slmc_reg_no "+
			"FROM person INNER JOIN doctor ON person.user_id = doctor.user_id;";

        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;
    }        
    
    public ArrayList<ArrayList<String>> lastMonthsReports(int month)
    {
            ArrayList<ArrayList<String>> data = null;
            ArrayList<String> data2 = new ArrayList<String>();
            ArrayList<ArrayList<String>> data3 = new ArrayList<ArrayList<String>>();
            
            String sql1="";

            ArrayList<String> tests = new ArrayList<String>();
            tests.add("BloodGroupingRh");
            tests.add("LipidTest");
            tests.add("LiverFunctionTest");
            tests.add("RenalFunctionTest");
            tests.add("SeriumCreatinePhosphokinase");
            tests.add("SeriumCreatinePhosphokinaseTotal");
            tests.add("UrineFullReport");
            tests.add("completeBloodCount");

           
            try{
                
                for (int i= 0 ; i < 8 ; i++) 
                {
                    sql1 = "SELECT COUNT(date) AS count FROM "+ tests.get(i) +" WHERE (date < curDate() AND date > ( CURRENT_DATE - INTERVAL "+month+" MONTH) );";
                    data = super.dbOperator.customSelection(sql1);
                    data2.add(data.get(1).get(0));
                }

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            data3.add(tests);
            data3.add(data2);
            return data3;
    }
    
    public ArrayList<ArrayList<String>> getDocAppointments()
    {

        String sql =    "SELECT date FROM appointment "+
                        "WHERE ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) "+
                        "ORDER BY date ASC;";
        


        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;

    }
    
    public ArrayList<ArrayList<String>> getLabAppointments()
    {

        String sql =    "SELECT date FROM lab_appointment "+
                        "WHERE ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) "+
                        "ORDER BY date ASC;";
        


        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;

    }
    
    public ArrayList<ArrayList<String>> getCancelledDocAppointments()
    {

        String sql =    "SELECT date FROM appointment "+
                        "WHERE ( ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (cancelled = 1) )"+
                        "ORDER BY date ASC;";
        


        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;

    }
    
    public ArrayList<ArrayList<String>> getCancelledLabAppointments()
    {

        String sql =    "SELECT date FROM lab_appointment "+
                        "WHERE ( ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (cancelled = 1) )"+
                        "ORDER BY date ASC;";
        


        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}   

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return data;

    }
    
    public ArrayList<ArrayList<String>> getStockSummary()
    {	
            String sql = 	"SELECT * FROM drug;";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);

                    ArrayList<String> tmp = data.remove(0);
                    tmp.add("amount");
                    tmp.add("suppliers");
                    data.add(0,tmp);

                    int noOfDrugs = data.size() - 1;
                    for(int i = 1; i <= noOfDrugs; i++)
                    {
                            String drugId = data.get(i).get(0);
                            sql = 	"SELECT pharmacy_stock.remaining_quantity, pharmacy_stock.supplier_id " + 
                                            "FROM pharmacy_stock WHERE drug_id = '"+ drugId +"';";

                            ArrayList<ArrayList<String>> data2 = super.dbOperator.customSelection(sql);		
                            //System.out.println(data2);

                            int tmpSize = data2.size();
                            int totalAmount = 0;
                            ArrayList<String> suppliers = new ArrayList<String>();
                            for(int j = 1; j < tmpSize; j++)
                            {
                                    totalAmount += Integer.parseInt(data2.get(j).get(0));
                                    String supplier = data2.get(j).get(1);

                                    if (!suppliers.contains(supplier))
                                    {
                                            suppliers.add(supplier);
                                    }

                            }

                            int noOfSuppliers = suppliers.size();

                            tmp = data.remove(i);
                            tmp.add(Integer.toString(totalAmount));
                            tmp.add(Integer.toString(noOfSuppliers));
                            data.add(i,tmp);

                    }

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return data;
    }
    
    public HashMap<String,String> getDrugGenericInfo()
    {
        String sql = "SELECT brand_name, generic_name "+
                     "FROM drug_brand_names;";

        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection(sql);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}


        ArrayList<String> data2 = new ArrayList<String>(); 

        HashMap<String,String> genericDetails = new HashMap<String,String>(); 

        int size = data.size();
        for(int i=1; i < size; i++)
        {
            genericDetails.put(data.get(i).get(0),data.get(i).get(1));
        }    

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //System.out.println(data);
        return genericDetails;

    }
    
    
    public ArrayList<ArrayList<String>> getDrugNames()
    {	
        String sql = 	"SELECT brand_id,brand_name " + 
                            "FROM drug_brand_names;";

        ArrayList<ArrayList<String>> data = null;
        try{			
                data = super.dbOperator.customSelection(sql);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        return data;
    }
    
    public ArrayList<ArrayList<String>> getDrugAmounts(String brandID)
    {	
        String sql = 	"SELECT remaining_quantity " + 
                            "FROM pharmacy_stock "+
                            "WHERE brand_id = '"+brandID+"';";

        ArrayList<ArrayList<String>> data = null;
        try{			
                data = super.dbOperator.customSelection(sql);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        return data;
    }
    
    public HashMap<String,String> getSupplierNames()
    {
        String sql =    "SELECT suppliers.supplier_id, suppliers.supplier_name " + 
                        "FROM suppliers;";

        ArrayList<ArrayList<String>> data = null;
        try{			
                data = super.dbOperator.customSelection(sql);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}


        HashMap<String,String> supplierNames = new HashMap<>(); 

        int tmpSize = data.size();
        for (int i = 1; i < tmpSize; i++)
        {
                supplierNames.put(data.get(i).get(0), data.get(i).get(1) );
        }

        //System.out.println(supplierNames);
        return supplierNames;

    }
    
    
    public ArrayList<ArrayList<String>> getSupplierSummary()
    {	
            String sql = 	"SELECT pharmacy_stock.remaining_quantity, pharmacy_stock.supplier_id " + 
                            "FROM pharmacy_stock;";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            data.remove(0);
            int tmpSize = data.size();

            ArrayList<String> suppliers = new ArrayList<String>();
            ArrayList<String> stock = new ArrayList<String>();

            for (int i = 0; i < tmpSize; i++)
            {
                    String supplier = data.get(i).get(1);
                    if (!suppliers.contains(supplier))
                    {
                            suppliers.add(supplier);
                            stock.add(data.get(i).get(0));
                    } else {
                            int sup = suppliers.indexOf(supplier);
                            int tmp = Integer.parseInt(stock.remove(sup));
                            tmp += Integer.parseInt(data.get(i).get(0));
                            stock.add(sup,Integer.toString(tmp));
                    }

            }

            ArrayList<ArrayList<String>> data2 = new ArrayList<ArrayList<String>>();
            data2.add(suppliers);
            data2.add(stock);

            //System.out.println(data2);
            return data2;
    }
    
    public ArrayList<ArrayList<String>> lastTotalIncome(String month1,String month2)
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                /*    
                sql1 =  "SELECT bill_date,total FROM bill "+
                        "WHERE (bill_date < ( CURRENT_DATE - INTERVAL "+month2+" MONTH) "+
                        "AND bill_date > ( CURRENT_DATE - INTERVAL "+month1+" MONTH) ) "+
                        "ORDER by bill_date ASC;";
                */
                sql1 =  "SELECT bill_date,total FROM bill "+
                        "WHERE (bill_date < '"+month2+"' "+
                        "AND bill_date > '"+month1+"' AND refund=0) "+
                        "ORDER by bill_date ASC;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    public ArrayList<ArrayList<String>> pharmacyIncome(String month1,String month2)
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                /*    
                sql1 =  "SELECT bill_date,total FROM bill "+
                        "WHERE (bill_date < ( CURRENT_DATE - INTERVAL "+month2+" MONTH) "+
                        "AND bill_date > ( CURRENT_DATE - INTERVAL "+month1+" MONTH) ) "+
                        "ORDER by bill_date ASC;";
                */
                sql1 =  "SELECT bill_date,pharmacy_fee FROM bill "+
                        "WHERE (bill_date < '"+month2+"' "+
                        "AND bill_date > '"+month1+"' AND refund=0) "+
                        "ORDER by bill_date ASC;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    public ArrayList<ArrayList<String>> laboratoryIncome(String month1,String month2)
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                /*    
                sql1 =  "SELECT bill_date,total FROM bill "+
                        "WHERE (bill_date < ( CURRENT_DATE - INTERVAL "+month2+" MONTH) "+
                        "AND bill_date > ( CURRENT_DATE - INTERVAL "+month1+" MONTH) ) "+
                        "ORDER by bill_date ASC;";
                */
                sql1 =  "SELECT bill_date,laboratory_fee FROM bill "+
                        "WHERE (bill_date < '"+month2+"' "+
                        "AND bill_date > '"+month1+"' AND refund=0) "+
                        "ORDER by bill_date ASC;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    public ArrayList<ArrayList<String>> appointmentIncome(String month1,String month2)
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                /*    
                sql1 =  "SELECT bill_date,total FROM bill "+
                        "WHERE (bill_date < ( CURRENT_DATE - INTERVAL "+month2+" MONTH) "+
                        "AND bill_date > ( CURRENT_DATE - INTERVAL "+month1+" MONTH) ) "+
                        "ORDER by bill_date ASC;";
                */
                sql1 =  "SELECT bill_date,appointment_fee FROM bill "+
                        "WHERE (bill_date < '"+month2+"' "+
                        "AND bill_date > '"+month1+"' AND refund=0) "+
                        "ORDER by bill_date ASC;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    
    public ArrayList<ArrayList<String>> getSysUser(String userid)
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                
                sql1 = "SELECT person.*,sys_user.user_name,sys_user.user_type,sys_user.online,sys_user.suspend "+
                       "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                       "WHERE (sys_user.user_id = '"+userid+"' AND person.user_id = '"+userid+"');";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    public boolean suspendUser(String userid)
    {
            String sql1="";
            boolean result = true;
                    
            try{
                
                
                sql1 = "UPDATE sys_user SET suspend = 1 WHERE user_id = '"+userid+"';";
                
                //System.out.println(sql1);
                result = super.dbOperator.customInsertion(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
    } 
    
    public boolean unsuspendUser(String userid)
    {
            String sql1="";
            boolean result = true;
                    
            try{
                
                
                sql1 = "UPDATE sys_user SET suspend = 0 WHERE user_id = '"+userid+"';";
                
                //System.out.println(sql1);
                result = super.dbOperator.customInsertion(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
    } 
    
    public String getsuspendUser()
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            String  result = "";
                    
            try{
                
                
                sql1 = "SELECT COUNT(user_id) FROM sys_user WHERE suspend = 1;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);
                result = data.get(1).get(0);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
    } 
    
    public String getActiveUser()
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            String  result = "";
                    
            try{
                
                
                sql1 = "SELECT COUNT(user_id) FROM sys_user WHERE suspend = 0;";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);
                result = data.get(1).get(0);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
    } 
    
    
    public ArrayList<ArrayList<String>> getSuspendedUsers()
    {
            ArrayList<ArrayList<String>> data = null;
            
            String sql1="";
            
            try{
                
                
                sql1 = "SELECT person.first_name,person.last_name,sys_user.user_id,sys_user.user_type,sys_user.online "+
                       "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                       "WHERE (sys_user.suspend = 1);";
                
                //System.out.println(sql1);
                data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
    }
    
    
    public boolean resetPassword(String userid)
    {
        boolean result = true;
            
        try{

            String sql1 = "UPDATE sys_user SET password='123456' WHERE user_id = '"+userid+"';";
            //System.out.println(sql1);
            result = super.dbOperator.customInsertion(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        return result;
    
    
    }        
    
    
    /*
    public static void main(String args[])
    {
	Admin admin = new Admin("user006");
	//admin.getProfileInfo();	
	//admin.createNewUser("heshan","eranga","admin","943562172V","0712453714");	
	//admin.getUserInfo("doctor");
		
    }
    */
}
