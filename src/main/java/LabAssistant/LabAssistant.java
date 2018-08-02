/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LabAssistant;

import com.hms.hms_test_2.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author heshan
 */
public class LabAssistant extends User{
    
    private String labAssistantID;
	
    public LabAssistant(String username)
    {
            super(username);

            try{
                    labAssistantID = super.dbOperator.showTableData("lab_assistant","lab_assistant_id",("user_id = '" + super.userID + "'")).get(0).get(0);
            }catch(ClassNotFoundException | SQLException e){}

    }

    public HashMap<String,String> getProfileInfo()
    {
        /*
        String[] columnNamesDoc = {"person_id","nic","gender","date_of_birth","address","mobile","user_id",
                                                                "first_name","last_name","email","nationality","religion",
                                                                "slmc_reg_no","user_id","available_days","education","training",
                                                                "experienced_areas","experience","achievements"};
        */
        HashMap<String,String> infoHash = new HashMap<String,String>();	

        //////////////////////////////////// Getting data from database //////////////////////////////////////////////////////////////
        String sql= "SELECT person.*,sys_user.*,lab_assistant.* "+
                    "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id INNER JOIN lab_assistant ON person.user_id = lab_assistant.user_id  "+
                    "WHERE (sys_user.user_id = '"+userID+"' AND person.user_id = '"+userID+"' AND lab_assistant.user_id = '"+userID+"');";

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
    
    
    public boolean updateProfileInfo(String info)
    {
            boolean result = true;
            String column_data = "";

            String[] tmpInfo = info.split("#");
            for(String s : tmpInfo)
            {
                    String[] tmpData = s.split(" ");
                    column_data += tmpData[0] + "='" + tmpData[1] + "',";
                    //System.out.println(column_data);
            }

            column_data = column_data.substring(0,column_data.length()-1);

            String sql =	"UPDATE person SET " + column_data + " "  +
                            "WHERE person_id = (SELECT person_id FROM sys_user WHERE user_name = '" + super.username + "');";

            try{

                    super.dbOperator.customInsertion( sql );


            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

            return result;
    }

    public boolean updateLabAssistantInfo(String info)
    {
            boolean result = true;
            String column_data = "";

            String[] tmpInfo = info.split("#");
            for(String s : tmpInfo)
            {
                    String[] tmpData = s.split(" ");
                    column_data += tmpData[0] + "='" + s.substring(tmpData[0].length(),s.length()) + "',";
                    //column_data += tmpData[0] + "='" + tmpData[1] + "',";
                    //System.out.println(column_data);
            }

            column_data = column_data.substring(0,column_data.length()-1);

            String sql =	"UPDATE lab_assistant SET " + column_data + " "  +
                            "WHERE lab_assistant_id = '" + this.labAssistantID + "';";

            try{

                    super.dbOperator.customInsertion( sql );


            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

            return result;
    }

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

            String sql =	"UPDATE sys_user SET " + column_data + " "  +
                            "WHERE user_id = '" + this.userID + "';";

            try{

                    super.dbOperator.customInsertion( sql );


            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

            return result;
    }
    
    
    public ArrayList<String> getLabTestNames()
    {
        //////////////////////////////////// Getting data from database //////////////////////////////////////////////////////////////

        ArrayList<ArrayList<String>> data = null;
        try{
                data = super.dbOperator.customSelection("SELECT test_name FROM lab_test;");
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        ///////////// Putting data into ArrayList /////////////////////////////////////////////////////////////////////////////////////

        ArrayList<String> data2 = new ArrayList<String>();
        int size = data.size();
        for (int i = 1; i < size; i++)
        {
            data2.add(data.get(i).get(0));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

       //System.out.println(data2);
        return data2;
    }
    
    public String getLabTestInfo(String name)
    {
        //////////////////////////////////// Getting data from database //////////////////////////////////////////////////////////////

        ArrayList<ArrayList<String>> data = null;
        try{
            
            String sql = "SELECT test_description FROM lab_test WHERE test_name='" + name + "';";
            data = super.dbOperator.customSelection(sql);
            
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        ///////////// Putting data into ArrayList /////////////////////////////////////////////////////////////////////////////////////

        String result = data.get(1).get(0);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

       //System.out.println(data2);
        return result;
    }
    
    //patientDetails except age//
    public ArrayList<ArrayList<String>> getPatientDetails(String appID)
    {

            ArrayList<ArrayList<String>> data = null;

            String sql1 =   "SELECT person.first_name,person.last_name,person.gender,person.date_of_birth,"+
                            "lab_appointment.test_id "+
                            "FROM lab_appointment INNER JOIN patient ON patient.patient_id = lab_appointment.patient_id "+
                            "INNER JOIN person ON person.person_id = patient.person_id " +
                            "WHERE lab_appointment.lab_appointment_id = '"+appID+"';";   
		

            try{			

                    data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return data;
    }
    
    public ArrayList<ArrayList<String>> getPrescriptions(String patientId)
    {

            ArrayList<ArrayList<String>> data = null;

            String sql1 =   "SELECT prescription.prescription_id,prescription.date,prescription.tests,"+
                            "person.first_name,person.last_name "+
                            "FROM prescription INNER JOIN doctor ON doctor.slmc_reg_no = prescription.consultant_id "+
                            "INNER JOIN person ON person.user_id = doctor.user_id " +
                            "WHERE patient_id = '"+patientId+"' ORDER BY prescription.date DESC LIMIT 5";
		
            try{			

                    data = super.dbOperator.customSelection(sql1);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return data;
    }   
    
    
    public ArrayList<String> lastMonthsAppointments()
    {
            ArrayList<ArrayList<String>> data = null;
            ArrayList<String> data2 = new ArrayList<String>();
            String sql1="";

            try{
                
                for (int i= 1 ; i < 6 ; i++) 
                {
                    int tmp = i*30;
                    sql1 = "SELECT COUNT(' date ') AS count FROM lab_appointment WHERE month(date) = month( CURRENT_DATE - INTERVAL "+tmp+" DAY );";
                    
                    data = super.dbOperator.customSelection(sql1);
                    data2.add(data.get(1).get(0));
                }

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return data2;
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

            month = month * 30;
            
            try{
                
                for (int i= 0 ; i < 8 ; i++) 
                {
                    sql1 = "SELECT COUNT(date) AS count FROM "+ tests.get(i) +" WHERE (date < curDate() AND date > ( CURRENT_DATE - INTERVAL 40 DAY) );";
                    data = super.dbOperator.customSelection(sql1);
                    data2.add(data.get(1).get(0));
                }

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            data3.add(tests);
            data3.add(data2);
            return data3;
    }
    
    
    public ArrayList<ArrayList<String>> getAppointments()
    {
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    
        try{
                
            String sql1 = "SELECT date,patient_id,cancelled FROM lab_appointment WHERE (month(curDate()) >= month(date)) AND (year(curDate()) = year(date));";
            data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
        
        return data;
    }
    
    
    public String getTodayAppointments()
    {
        ArrayList<ArrayList<String>> data = null;
        String result = "";    
        try{

            String sql1 = "SELECT COUNT(' date ') AS count FROM lab_appointment WHERE (day(curDate()) = day(date)) AND (month(curDate()) = month(date)) AND (year(curDate()) = year(date))";
            data = super.dbOperator.customSelection(sql1);
            result = data.get(1).get(0);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return result;
    }
    
    /**
     * Insert to PathologyTest full report
     * @param appointment_id
     * @param appearance
     * @param sgRefractometer
     * @param ph
     * @param protein
     * @param glucose
     * @param ketoneBodies
     * @param bilirubin
     * @param urobilirubin
     * @param contrifugedDepositsphaseContrastMicroscopy
     * @param pusCells
     * @param redCells
     * @param epithelialCells
     * @param casts
     * @param cristals
     * @return boolean
     */
    public String UrineFullReport(String appointment_id,String appearance, String sgRefractometer,String ph,String protein,String glucose,String ketoneBodies,String bilirubin,String urobilirubin,String contrifugedDepositsphaseContrastMicroscopy,String pusCells,String redCells,String epithelialCells,String casts,String cristals ){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_ur_id FROM UrineFullReport WHERE tst_ur_id = (SELECT MAX(tst_ur_id) FROM UrineFullReport);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 2; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "ur" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "ur0001"; 
        }
        
        
        String sql2 = "INSERT INTO UrineFullReport(tst_ur_id, appointment_id, appearance,sgRefractometer,ph,protein,glucose,ketoneBodies,bilirubin,urobilirubin,contrifugedDepositsphaseContrastMicroscopy,pusCells,redCells,epithelialCells,casts,cristals,date) "+
        "VALUES('"+id+"','"+appointment_id+"','"+appearance+"','"+sgRefractometer+"','"+ph+"','"+protein+"','"+glucose+"','"+ketoneBodies+"','"+bilirubin+"','"+urobilirubin+"','"+contrifugedDepositsphaseContrastMicroscopy+"','"+pusCells+"','"+redCells+"','"+epithelialCells+"','"+casts+"','"+cristals+"',NOW())";

        try{			
                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    
    /**
     * Insert to LipidTest full report
     * @param appointment_id
     * @param cholestrolHDL
     * @param cholestrolLDL
     * @param triglycerides
     * @param totalCholestrolLDLHDLratio
     * @return boolean
     */
    public String LipidTest(String appointment_id,String cholestrolHDL, String cholestrolLDL,String triglycerides,String totalCholestrolLDLHDLratio ){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_li_id FROM LipidTest WHERE tst_li_id = (SELECT MAX(tst_li_id) FROM LipidTest);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 2; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "li" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "li0001"; 
        }
            
            
            
        String sql2 = "INSERT INTO LipidTest(tst_li_id , appointment_id, cholestrolHDL,cholestrolLDL,triglycerides,totalCholestrolLDLHDLratio,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+cholestrolHDL+"','"+cholestrolLDL+"','"+triglycerides+"','"+totalCholestrolLDLHDLratio+"',NOW())";

        try{			
            System.out.println(t);
            t = super.dbOperator.customInsertion(sql2);
            System.out.println(t);
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
        
        return id;
    }
    
    
    /**
     * Insert to blood group
     * @param app_id
     * @param bloodG
     * @param rhD
     * @return boolean
     */
    public String BloodGroupingTest(String app_id, String bloodG, String rhD )
    {

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_bloodG_id FROM BloodGroupingRh WHERE tst_bloodG_id = (SELECT MAX(tst_bloodG_id) FROM BloodGroupingRh);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 2; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "bg" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "bg0001"; 
        }
        
        
        String sql2 = "INSERT INTO BloodGroupingRh(tst_bloodG_id, appointment_id, BloodGroup, rhesusD,date) "+
        "VALUE('"+id+"','"+app_id+"','"+bloodG+"','"+rhD+"',NOW())";

        try{			

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    
    /**
     * Insert to completeBloodCount report
     * @param tst_CBC_id
     * @param prescription_id
     * @param totalWhiteCellCount
     * @param differentialCount
     * @param neutrophils
     * @param lymphocytes
     * @param monocytes
     * @param eosonophils
     * @param basophils
     * @param haemoglobin
     * @param redBloodCells
     * @param meanCellVolume
     * @param haematocrit
     * @param meanCellHaemoglobin
     * @param mchConcentration
     * @param redCellsDistributionWidth
     * @param plateletCount
     * @return boolean
     */
    public String completeBloodCount(String appointment_id,String totalWhiteCellCount,String differentialCount,
            String neutrophils,String lymphocytes,String monocytes,String eosonophils,String basophils,
            String haemoglobin,String redBloodCells,String meanCellVolume,String haematocrit,String meanCellHaemoglobin,
            String mchConcentration,String redCellsDistributionWidth,String plateletCount){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_CBC_id FROM completeBloodCount WHERE tst_CBC_id = (SELECT MAX(tst_CBC_id) FROM completeBloodCount);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 3; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "cbc" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "cbc0001"; 
        }
        
        String sql2 = "INSERT INTO completeBloodCount(tst_CBC_id , appointment_id, totalWhiteCellCount,differentialCount,neutrophils,lymphocytes,monocytes,eosonophils,basophils,haemoglobin,redBloodCells,meanCellVolume,haematocrit,meanCellHaemoglobin, mchConcentration,redCellsDistributionWidth,plateletCount,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+totalWhiteCellCount+"','"+differentialCount+"','"+neutrophils+"','"+lymphocytes+"','"+monocytes+"','"+eosonophils+"','"+basophils+"','"+haemoglobin+"','"+redBloodCells+"','"+meanCellVolume+"','"+haematocrit+"','"+meanCellHaemoglobin+"','"+mchConcentration+"','"+redCellsDistributionWidth+"','"+plateletCount+"',NOW())";

        try{			

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    /**
     * Insert to RFT full report
     * @param appointment_id
     * @param creatinine
     * @param urea
     * @param totalBilirubin
     * @param directBilirubin
     * @param sgotast
     * @param sgptalt
     * @param alkalinePhospates
     * @return boolean
     */
    public String RenalFunctionTest(String appointment_id,String creatinine,String urea,String totalBilirubin,String directBilirubin,String sgotast,String sgptalt,String alkalinePhospates  ){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_renal_id FROM RenalFunctionTest WHERE tst_renal_id = (SELECT MAX(tst_renal_id) FROM RenalFunctionTest);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 2; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "re" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "re0001"; 
        }
        
        String sql2 = "INSERT INTO RenalFunctionTest(tst_renal_id, appointment_id, creatinine,urea,totalBilirubin,directBilirubin,sgotast,sgptalt,alkalinePhospates,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+creatinine+"','"+urea+"','"+totalBilirubin+"','"+directBilirubin+"','"+sgotast+ "','"+sgptalt+ "','"+alkalinePhospates+"',NOW())";

        try{			

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    /**
     * Insert to CPK full report
     * @param appointment_id
     * @param cpkTotal
     * @return boolean
     */
    public String SeriumCreatinePhosphokinaseTotal(String appointment_id,String cpkTotal ){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_SCPT_id FROM SeriumCreatinePhosphokinaseTotal WHERE tst_SCPT_id = (SELECT MAX(tst_SCPT_id) FROM SeriumCreatinePhosphokinaseTotal);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 4; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "scpt" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "scpt0001"; 
        }
        
        String sql2 = "INSERT INTO SeriumCreatinePhosphokinaseTotal(tst_SCPT_id, appointment_id, cpkTotal,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+cpkTotal+"',NOW())";

        try{			

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    /**
     * Insert to HIV full report
     * @param appointment_id
     * @param hiv12ELISA
     * @return 
     */
    public String SeriumCreatinePhosphokinase(String appointment_id,String hiv12ELISA ){

        boolean t = true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_SCP_id FROM SeriumCreatinePhosphokinase WHERE tst_SCP_id = (SELECT MAX(tst_SCP_id) FROM SeriumCreatinePhosphokinase);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 3; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "scp" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "scp0001"; 
        }
        
        String sql2 = "INSERT INTO SeriumCreatinePhosphokinase(tst_SCP_id, appointment_id, hiv12ELISA,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+hiv12ELISA+"',NOW())";

        try{			

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    
    /**
     * Insert to LFT full report
     * @param appointment_id
     * @param totalProtein
     * @param albumin
     * @param globulin
     * @param totalBilirubin
     * @param directBilirubin
     * @param sgotast
     * @param sgptalt
     * @param alkalinePhospates
     * @return boolean
     */
    public String liverFunctionTest(String appointment_id,String totalProtein,String albumin,String globulin, String totalBilirubin,
                                        String directBilirubin,String sgotast,String sgptalt,String alkalinePhospates  )
    {

        boolean t =true;
        String id = "";
        
        try{			
			
            /////////////////// Generating the Result ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT tst_liver_id FROM LiverFunctionTest WHERE tst_liver_id = (SELECT MAX(tst_liver_id) FROM LiverFunctionTest);";
            String repID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = repID.toCharArray();
            int i = 0;
            for (i = 2; i < repID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(repID.substring(i,repID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "lv" + tmpID2;
            id = tmpID2;
            
        }catch(Exception e){
            
            id = "lv0001"; 
        }
        
        String sql2 = "INSERT INTO LiverFunctionTest(tst_liver_id, appointment_id, totalProtein,albumin,globulin,totalBilirubin,directBilirubin,sgotast,sgptalt,alkalinePhospates,date) "+
        "VALUE('"+id+"','"+appointment_id+"','"+totalProtein+"','"+albumin+"','"+globulin+"','"+totalBilirubin+"','"+directBilirubin+"','"+sgotast+"','"+sgptalt+"','"+alkalinePhospates+"',NOW())";

        try{                                                                                    

                t = super.dbOperator.customInsertion(sql2);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return id;
    }
    
    public ArrayList<ArrayList<String>> getUrineFullReport(String id)
    {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  UrineFullReport WHERE tst_ur_id = '"+id+"'";
                
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    
    public ArrayList<ArrayList<String>> getLipidTestReport(String id)
    {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  LipidTest WHERE tst_li_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    public ArrayList<ArrayList<String>> getBloodGroupingRh(String id)
    {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  BloodGroupingRh WHERE tst_bloodG_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    public ArrayList<ArrayList<String>> getCompleteBloodCount(String id){
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  completeBloodCount WHERE tst_CBC_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    public ArrayList<ArrayList<String>> getLiverFunctionTest(String id){
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  LiverFunctionTest WHERE tst_liver_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    public ArrayList<ArrayList<String>> getRenalFunctionTest(String id){
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  RenalFunctionTest WHERE tst_renal_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }

    public ArrayList<ArrayList<String>> getSeriumCreatinePhosphokinaseTotal(String id){
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT * FROM  SeriumCreatinePhosphokinaseTotal WHERE tst_SCPT_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }
    
    
    public ArrayList<ArrayList<String>> getSeriumCreatinePhosphokinase(String id)
    {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

            sql1 = "SELECT * FROM  SeriumCreatinePhosphokinase WHERE tst_SCP_id = '"+id+"'";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }
    
    public ArrayList<ArrayList<String>> getPatientInfo(String appID)
    {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

            sql1 =  "SELECT person.first_name,person.last_name,person.gender, person.date_of_birth "+
                    "FROM person INNER JOIN patient ON patient.person_id = person.person_id "+
                    "INNER JOIN lab_appointment ON lab_appointment.patient_id = patient.patient_id "+
                    "WHERE lab_appointment.lab_appointment_id='"+appID+"';";
        
        try{                                                                                    

                data = super.dbOperator.customSelection(sql1);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

        //System.out.println(data);
        return data;
    }
    
}
