package Receptionist;

import com.hms.hms_test_2.User;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class Receptionist extends User
{
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// methods /////////////////////////////////////////////////////////////////////////////////////////////
	/*
	* 
	* 
	* 
	public Receptionist(String username)										==>   Constuctor

	public ArrayList<ArrayList<String>> getDoctorTimeTable()							==>   get doctor info
	public ArrayList<ArrayList<String>> getPatientInfo(String patientID)						==>   get patient info
	public boolean setPatientInfo(String patientInfo)								==>   update patient info		
	public ArrayList<ArrayList<String>> getAppointments()								==>   get appointment details		
	public boolean makeAppointment(String patienID, String doctorID, String dateTime, String timeSlotID)		==>   making an appointment
	public ArrayList<ArrayList<String>> doctorAppointmentAvailableTime(String doctorID)				==>   get times available for appointments
	

	* 
	*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Receptionist(String username)
	{
		super(username);		
	}
		
        public HashMap<String,String> getProfileInfo()
	{

		HashMap<String,String> infoHash = new HashMap<String,String>();	
		
		//////////////////////////////////// Getting data from database //////////////////////////////////////////////////////////////
		String sql = "SELECT person.*,sys_user.* "+
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
                            "WHERE user_id = '" + this.userID + "';";

            try{

                    super.dbOperator.customInsertion( sql );


            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}

            return result;
    }
        
	public ArrayList<ArrayList<String>> getDoctorTimeTable()
	{	
		String sql = 	"SELECT "+
                                "doctor_availability.day, doctor_availability.time_slot, "+
                                "doctor.experienced_areas, "+
                                "person.first_name, person.last_name " +
                                "FROM doctor_availability INNER JOIN doctor ON doctor_availability.slmc_reg_no = doctor.slmc_reg_no "+
                                "INNER JOIN sys_user ON sys_user.user_id = doctor.user_id "+
                                "INNER JOIN person ON person.person_id = sys_user.person_id;";

		
		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}
	
	public ArrayList<ArrayList<String>> getPatientInfo(String patientID)
	{	
		String sql = 	"SELECT "+
						"person.*, "+
						"patient.* "+
						"FROM person INNER JOIN patient ON patient.person_id = person.person_id "+
						"WHERE patient_id = '" + patientID + "';";

		
		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}
	
        /**
         * Create new patient profile in the database for a patient
         * @param patientInfo String containing basic info separated by commas and spaces
         * "nic 9532648675,gender f,date_of_birth 19950203,address 145|town1|Street1,mobile 0775123465,first_name heshan,last_name eranga,email erangamx@gmail.com"
         * @return boolean if the operation was successful or not
         */
	public String setPatientInfo(String patientInfo)
	{	
                String value = "";
		boolean result = true;
		try{			
			
			/////////////////// Generating the Patient ID //////////////////////////////////////////////////////////////
			String sql2 = "SELECT patient_id FROM patient WHERE patient_id = (SELECT MAX(patient_id) FROM patient);";
			String patientID = super.dbOperator.customSelection(sql2).get(1).get(0);

			char[] tmpID = patientID.toCharArray();
			int i = 3;
			for (i = 3; i < patientID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			
			String tmpID2 = Integer.toString(Integer.parseInt(patientID.substring(i,patientID.length()-2)) + 1 );
			while(tmpID2.length() < 4)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "hms" + tmpID2 + "pa";
			//System.out.println(tmpID2);
			/////////////////// Generating the Person ID //////////////////////////////////////////////////////////////
			sql2 = "SELECT person_id FROM person WHERE person_id = (SELECT MAX(person_id) FROM person);";
			String personID = super.dbOperator.customSelection(sql2).get(1).get(0);

			tmpID = personID.toCharArray();
			i = 3;
			for (i = 3; i < personID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			
			String tmpID3 = Integer.toString(Integer.parseInt(personID.substring(i,personID.length())) + 1 );
			while(tmpID3.length() < 5)
			{
				tmpID3 = "0" + tmpID3;
			}
			tmpID3 = "hms" + tmpID3;
			//System.out.println(tmpID3);
                        value = tmpID3;
			
			//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
			String columnNames = "" , columnNames2 = "";
			String rowData = "" , rowData2 = "";
			
			String[] field = patientInfo.split(",");
			int index = 0;
			for (String val : field)
			{
				if (index > 0){ columnNames += "," ; rowData += ","; } 	
				columnNames += val.split(" ")[0];
				if (index != 2) {rowData = rowData + "'";}
				rowData += val.split(" ")[1];	
				if (index != 2) {rowData += "'";}
				index++;		
			}
			
			columnNames += ",person_id";
			rowData += ",'" + tmpID3 + "'";
			
			columnNames2 += "patient_id,person_id";
			rowData2 += "'" + tmpID2 + "','" + tmpID3 + "'";
			

			//System.out.println(columnNames);
			//System.out.println(rowData);

			String sql = "INSERT INTO person (" + columnNames + ") VALUES (" + rowData +");";
			String sql3 = "INSERT INTO patient (" + columnNames2 + ") VALUES (" + rowData2 +");";
			/////////////////////////////// Adding data to database /////////////////////////////////////////////////	
			result = super.dbOperator.customInsertion(sql);
			result = super.dbOperator.customInsertion(sql3);
			
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace(); value = "false";}
		
		return value;
	}
        
        
        public boolean updatePatientInfo(String patientID,String info)
        {
            String sql =    "UPDATE person SET "+info+" "+
                            "WHERE person_id = (SELECT person_id FROM patient WHERE patient_id = '"+patientID+"');";


            boolean result = true;
            try{			
                    //System.out.println(sql);
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return result;
        
        }        
        
        
        public ArrayList<ArrayList<String>> getLabTestInfo()
	{	
	
            String sql =    "SELECT "+
                            "test_id, test_name "+
                            "FROM lab_test;";


            ArrayList<ArrayList<String>> data = null;
            try{			

                    data = super.dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            //System.out.println(data);
            return data;
	}
        
		
		
	public ArrayList<ArrayList<String>> getAppointments()
	{	
		
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		
		String sql = 	"SELECT "+
                                "appointment.date, appointment.patient_id, "+
                                "person.first_name, person.last_name "+
                                "FROM appointment INNER JOIN doctor ON appointment.slmc_reg_no = doctor.slmc_reg_no "+
                                "INNER JOIN person ON person.user_id = doctor.user_id " +
                                "WHERE date > '" + date + "';";

		
		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}	
        
		
	/**
         * 
         * @param patienID id of the patient making the appointment
         * @param doctorID id of the doctor 
         * @param day day of the week
         * @param timeSlot time slot of the appointment 
         * @return boolean if the appointment was successfully made or not
         */
	public String makeAppointment(String patienID, String doctorID, String day, String timeSlot)	
	{
                boolean nextWeek = false;
                if (Integer.parseInt(day) > 7)
                    nextWeek = true;
                
                day = Integer.toString(Integer.parseInt(day) - 7); 
                
		boolean result = true;
                String value = "false";
		try{			
			
			/////////////////// Generating the Appointment ID //////////////////////////////////////////////////////////////
			String sql2 = "SELECT appointment_id FROM appointment WHERE appointment_id = (SELECT MAX(appointment_id) FROM appointment);";
			String appointmentID = super.dbOperator.customSelection(sql2).get(1).get(0);

			char[] tmpID = appointmentID.toCharArray();
			int i = 3;
			for (i = 3; i < appointmentID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			
			String tmpID2 = Integer.toString(Integer.parseInt(appointmentID.substring(i,appointmentID.length())) + 1 );
			while(tmpID2.length() < 3)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "app" + tmpID2;
			//System.out.println(tmpID2);
                        value = tmpID2;
			
			int[] days = {1,2,3,4,5,6,7};
			int daysToAppointment = 0;
			int tmpday = Integer.parseInt(day);
			String appDate = "";
			
			try{
				
				//getting the number of days to the appointment
						
				Calendar calendar = Calendar.getInstance();
				int today = calendar.get(Calendar.DAY_OF_WEEK); 
				if (tmpday > today)
				{ 
					daysToAppointment = (tmpday - today);
				} else {
					daysToAppointment = (7 - today + tmpday);	
				}	
				
				//String untildate="2011-10-08";//can take any date in current format    
				SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
				Calendar cal = Calendar.getInstance();    
				cal.setTime( cal.getTime());    
				cal.add( Calendar.DATE, daysToAppointment );    
				appDate=dateFormat.format(cal.getTime());    
				System.out.println(appDate);
			}catch(Exception e){}	
			
			 
			
			
			String tmpTimeSlot = timeSlot.substring(0,5);
			String dateTime = appDate +" "+ tmpTimeSlot + ":00";
			
			String columnNames = "appointment_id,patient_id,slmc_reg_no,date,cancelled";
			String rowData = "'" + tmpID2 + "' , '" + patienID + "' , '" + doctorID + "' , '" + dateTime + "' , false ";
			
			//System.out.println(columnNames);
			//System.out.println(rowData);
	
			String sql7 = 	"INSERT INTO appointment (" + columnNames + ") VALUES (" + rowData +");";
                        
                        String sql8;
                        int tmpDay = Integer.parseInt(day);
                        
                        System.out.println(tmpDay + "########################");
                        
                        if (nextWeek == true)
                        {
                            sql8 = 	"UPDATE doctor_availability SET next_week_appointments = next_week_appointments + 1 WHERE "+
							"time_slot = '" + timeSlot +"' AND slmc_reg_no = '"+doctorID+"' AND day = '"+day+"';";
                        }else{
                            
                            sql8 = 	"UPDATE doctor_availability SET current_week_appointments = current_week_appointments + 1 WHERE "+
							"time_slot = '" + timeSlot +"' AND slmc_reg_no = '"+doctorID+"' AND day = '"+day+"';";
                        }    
                        
			String appointmentFee = "500";
			String billInfo = "patient_id " + patienID + ",appointment_fee " + appointmentFee; 
							
			String sql4 = "SELECT tmp_bill_id FROM tmp_bill WHERE patient_id = '" + patienID + "';";
			try{
				
				tmpID2 = super.dbOperator.customSelection(sql4).get(1).get(0);
				
				String sql5 = "UPDATE tmp_bill SET appointment_fee = ' " + appointmentFee + " ' WHERE tmp_bill_id = '" + tmpID2 + "';";
				/////////////////////////////// Adding data to database /////////////////////////////////////////////////
				result = super.dbOperator.customInsertion(sql5);
				
				
				
			} catch(Exception e) {
			
				sql2 = "SELECT tmp_bill_id FROM tmp_bill WHERE tmp_bill_id = (SELECT MAX(tmp_bill_id) FROM tmp_bill);";
				try{
					String billID = super.dbOperator.customSelection(sql2).get(1).get(0);

					tmpID = billID.toCharArray();
					for (i = 3; i < billID.length(); i++)
					{
						if  (tmpID[i] != '0') break; 
					} 
					
					tmpID2 = Integer.toString(Integer.parseInt(billID.substring(i,billID.length()-2)) + 1 );
					while(tmpID2.length() < 4)
					{
						tmpID2 = "0" + tmpID2;
					}
					tmpID2 = "hms" + tmpID2 + "tb";
				}catch(Exception ex) {tmpID2 = "hms0001tb";}	
				//System.out.println(tmpID2);
				
				
				
				//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
				columnNames = "";
				rowData = "";
				
				String[] field = billInfo.split(",");
				
				int index = 0;
				for (String val : field)
				{
					if (index > 0){ columnNames += "," ; rowData += ","; }
					
					//// Order of column feilds in the input string!!! ////
					columnNames += val.split(" ")[0];
					if (index < 3) {rowData = rowData + "'";}
					rowData += val.split(" ")[1];
					if (index < 3) {rowData += "'";}
					
					index++;
				}
				

				columnNames += ",tmp_bill_id";
				rowData += ",'" + tmpID2 + "'";

				//System.out.println(columnNames);
				//System.out.println(rowData);
				
				String sql = "INSERT INTO tmp_bill (" + columnNames + ") VALUES (" + rowData +");";
				/////////////////////////////// Adding data to database /////////////////////////////////////////////////
				System.out.println(sql);
                                System.out.println("*******************************************************");
                                result = super.dbOperator.customInsertion(sql);
			}				
			
			/////////////////////////////// Adding data to database /////////////////////////////////////////////////	
			
			result = super.dbOperator.customInsertion(sql7);
			result = super.dbOperator.customInsertion(sql8);
			
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}
		return value;
	}
        
        
        /**
         * 
         * @param patienID id of the patient making the appointment
         * @param doctorID id of the doctor 
         * @param testID test id 
         * @param day day of the week
         * @param timeSlot time slot of the appointment 
         * @return boolean if the appointment was successfully made or not
         */
	public String makeLabAppointment(String patienID, String doctorID, String testID, String day, String timeSlot)	
	{
		boolean result = true;
                String value = "false";
                String tmpID2 = "";
		try{			
			
			/////////////////// Generating the Appointment ID //////////////////////////////////////////////////////////////
			try{
                            String sql2 = "SELECT lab_appointment_id FROM lab_appointment WHERE lab_appointment_id = (SELECT MAX(lab_appointment_id) FROM lab_appointment);";
                            String appointmentID = super.dbOperator.customSelection(sql2).get(1).get(0);

                            char[] tmpID = appointmentID.toCharArray();
                            int i = 4;
                            for (i = 4; i < appointmentID.length(); i++)
                            {
                                    if  (tmpID[i] != '0') break; 
                            } 

                            tmpID2 = Integer.toString(Integer.parseInt(appointmentID.substring(i,appointmentID.length())) + 1 );
                            while(tmpID2.length() < 3)
                            {
                                    tmpID2 = "0" + tmpID2;
                            }
                            tmpID2 = "lapp" + tmpID2;
                            //System.out.println(tmpID2);
                            value = tmpID2;
                        } catch (Exception e) {
                           
                            value = "lapp001";
                            tmpID2 = value;
                        
                        }
                        
			int[] days = {1,2,3,4,5,6,7};
			int daysToAppointment = 0;
			int tmpday = Integer.parseInt(day);
			String appDate = "";
			
			try{
				
				//getting the number of days to the appointment
						
				Calendar calendar = Calendar.getInstance();
				int today = calendar.get(Calendar.DAY_OF_WEEK); 
				if (tmpday > today)
				{ 
					daysToAppointment = (tmpday - today);
				} else {
					daysToAppointment = (7 - today + tmpday);	
				}	
				
				//String untildate="2011-10-08";//can take any date in current format    
				SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
				Calendar cal = Calendar.getInstance();    
				cal.setTime( cal.getTime());    
				cal.add( Calendar.DATE, daysToAppointment );    
				appDate=dateFormat.format(cal.getTime());    
				System.out.println(appDate);
			}catch(Exception e){}	
			
			 
			
			
			String tmpTimeSlot = timeSlot.substring(0,5);
			String dateTime = appDate +" "+ tmpTimeSlot + ":00";
			
			String columnNames = "lab_appointment_id,test_id,patient_id,doctor_id,date,cancelled";
			String rowData = "'" + value + "' , '" + testID + "' , '" + patienID + "' , '" + doctorID + "' , '" + dateTime + "' , false ";
			
			//System.out.println(columnNames);
			//System.out.println(rowData);
	
			String sql7 = 	"INSERT INTO lab_appointment (" + columnNames + ") VALUES (" + rowData +");";
			String sql8 = 	"UPDATE lab_appointment_timetable SET current_week_appointments = current_week_appointments + 1 WHERE "+
							"time_slot = '" + timeSlot +"' AND app_test_id = '"+testID+"' AND app_day = '"+day+"';";
					
                        
                        String sql10 = "SELECT test_fee FROM lab_test WHERE test_id = '" + testID + "';";
                        ArrayList<ArrayList<String>> data = super.dbOperator.customSelection(sql10);
                        
                        
			String appointmentFee = data.get(1).get(0);
			String billInfo = "patient_id " + patienID + ",laboratory_fee " + appointmentFee; 
							
			String sql4 = "SELECT tmp_bill_id FROM tmp_bill WHERE patient_id = '" + patienID + "';";
			try{
				
				tmpID2 = super.dbOperator.customSelection(sql4).get(1).get(0);
				
				String sql5 = "UPDATE tmp_bill SET laboratory_fee = ' " + appointmentFee + " ' WHERE tmp_bill_id = '" + tmpID2 + "';";
				/////////////////////////////// Adding data to database /////////////////////////////////////////////////
				result = super.dbOperator.customInsertion(sql5);
				
				
				
			} catch(Exception e) {
			
				String sql2 = "SELECT tmp_bill_id FROM tmp_bill WHERE tmp_bill_id = (SELECT MAX(tmp_bill_id) FROM tmp_bill);";
				try{
					String billID = super.dbOperator.customSelection(sql2).get(1).get(0);

                                        char[] tmpID = billID.toCharArray();
                                        int i;
					for (i = 3; i < billID.length(); i++)
					{
						if  (tmpID[i] != '0') break; 
					} 
					
					tmpID2 = Integer.toString(Integer.parseInt(billID.substring(i,billID.length()-2)) + 1 );
					while(tmpID2.length() < 4)
					{
						tmpID2 = "0" + tmpID2;
					}
					tmpID2 = "hms" + tmpID2 + "tb";
				}catch(Exception ex) {tmpID2 = "hms0001tb";}	
				//System.out.println(tmpID2);
				
				
				
				//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
				columnNames = "";
				rowData = "";
				
				String[] field = billInfo.split(",");
				
				int index = 0;
				for (String val : field)
				{
					if (index > 0){ columnNames += "," ; rowData += ","; }
					
					//// Order of column feilds in the input string!!! ////
					columnNames += val.split(" ")[0];
					if (index < 3) {rowData = rowData + "'";}
					rowData += val.split(" ")[1];
					if (index < 3) {rowData += "'";}
					
					index++;
				}
				

				columnNames += ",tmp_bill_id";
				rowData += ",'" + tmpID2 + "'";

				//System.out.println(columnNames);
				//System.out.println(rowData);
				
				String sql = "INSERT INTO tmp_bill (" + columnNames + ") VALUES (" + rowData +");";
				/////////////////////////////// Adding data to database /////////////////////////////////////////////////
				result = super.dbOperator.customInsertion(sql);
			}				
			
			/////////////////////////////// Adding data to database /////////////////////////////////////////////////	
			
			result = super.dbOperator.customInsertion(sql7);
			result = super.dbOperator.customInsertion(sql8);
			
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();result = false;}
		return value;
	}
	
	
	public ArrayList<ArrayList<String>> doctorAppointmentAvailableTime(String doctorID)
	{	
		String sql = 	"SELECT "+
						"doctor_availability.day, doctor_availability.time_slot, doctor_availability.current_week_appointments "+
						"FROM doctor_availability "+
						"WHERE slmc_reg_no = '" + doctorID + "' " +
						"ORDER BY day;";

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
		//System.out.println(dayOfWeek);

		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		//System.out.println(data);
		
		ArrayList<ArrayList<String>> data2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> headData = new ArrayList<String>();
		headData.add("day");
		headData.add("session_start");
		headData.add("app_time");
		data2.add(headData);
		
		
		int arraySize = data.size();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		long difference = 0;
		
		for (int i = 1; i < arraySize; i++)
		{
			
			int availableDay = Integer.parseInt(data.get(i).get(0));
			int nextDay;
			if (availableDay > dayOfWeek) {nextDay = availableDay - dayOfWeek;}
			else {nextDay = 7-dayOfWeek + availableDay;}
			
			
			SimpleDateFormat format2 = new SimpleDateFormat("EEEE MMM dd");
			Date tmpAppointmentDay = new Date();
			Calendar calender0 = Calendar.getInstance(); 
			calender0.setTime(tmpAppointmentDay); 
			calender0.add(Calendar.DATE, nextDay);
			String appointmentDay = format2.format(calender0.getTime());
			
			int noOfCurrentAppointments = Integer.parseInt(data.get(i).get(2));
			String tmpTime = data.get(i).get(1);
			String tmptimeSlot[] = tmpTime.split("-");
			try{
			Date startTime = format.parse(tmptimeSlot[0]);
			Date endTime = format.parse(tmptimeSlot[1]);
			difference = endTime.getTime() - startTime.getTime();
			difference /= 60000;
			//System.out.println(difference);
			
			int availableSlots = (int)difference/5 - noOfCurrentAppointments;
			//System.out.println(availableSlots);

			
			Calendar calender = Calendar.getInstance();
			calender.setTime(startTime);
			calender.add(Calendar.MINUTE, noOfCurrentAppointments*5);
			String appTime = format.format(calender.getTime());
			
			
			ArrayList<String> tmpData = new ArrayList<String>();
			tmpData.add(appointmentDay);
			tmpData.add(tmptimeSlot[0]);
			tmpData.add(appTime);
			//System.out.println(tmpData);
			data2.add(tmpData);
			
			}catch(Exception e){e.printStackTrace();}
			
		} 
		
		//System.out.println(data2);
		return data2;
	}
        
        /**
         *
         * @return ArrayList<ArrayList<String>> with details about all the Doctors in the hospital
         * {doctorID, specializesAreas,doctorFirstName, doctorLastName, availableDays, days}
         * 
         */
        public ArrayList<ArrayList<String>> getDoctorSummary()
	{	
		
            String sql =    "SELECT "+
                            "doctor.slmc_reg_no, doctor.experienced_areas, "+
                            "person.first_name, person.last_name "+
                            "FROM doctor INNER JOIN person ON doctor.user_id = person.user_id;";


            ArrayList<ArrayList<String>> data = null;
            ArrayList<ArrayList<String>> data2 = null;
            ArrayList<String> tmpData = null;
            ArrayList<String> tmpData2 = new ArrayList<String>();
            try{			

                    data = super.dbOperator.customSelection(sql);
                    int size = data.size();

                    for (int i = 1; i < size; i++)
                    {
                        tmpData = data.remove(i);

                        String sql2 =   "SELECT day FROM doctor_availability " +
                                        "WHERE slmc_reg_no = '" + tmpData.get(0) + "';";

                        data2 = super.dbOperator.customSelection(sql2);
                        System.out.println(tmpData);
                        System.out.println(data2);                      
                        int size2 = data2.size();
                        String tmpString = "";
                        int count = 0;
                        for(int j = 1; j < size2; j++)
                        {
                            String tmp = data2.get(j).get(0);
                            if (!tmpData2.contains(tmp))
                            {
                                    tmpString += tmp + " ";
                                    tmpData2.add(tmp);
                                    count++;
                            }

                        }
                        tmpData.add(Integer.toString(count));
                        tmpData.add(tmpString);
                        data.add(i,tmpData);
                        tmpData2 = new ArrayList<String>();
                    }

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}		
	
        /**
         *
         * @return ArrayList<ArrayList<String>> with details about the Doctors currently available at the time
         * 
         */
        public ArrayList<ArrayList<String>> getCurrentlyAvailableDoctors()
	{	
		
		//String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		
		String sql = 	"SELECT "+
                                "person.first_name, person.last_name "+
                                "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id "+
                                "WHERE user_type='doctor' AND online = true;";

		
		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}	
        
        
        /**
         * 
         * @return ArrayList<String> consultation Areas of the doctor in the hospital
         */
        public ArrayList<String> getConsultationAreas()
	{	
		
		String sql = 	"SELECT "+
                                "doctor.experienced_areas "+
                                "FROM doctor;";

		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<String> data2 = new ArrayList<String>();
		try{			
			
			data = super.dbOperator.customSelection(sql);
			
			int size = data.size();
			for (int i = 1; i < size; i++)
			{
				String consultArea = data.get(i).get(0);
				if (!data2.contains(consultArea))
				{
					data2.add(consultArea);
				}	
			}
			
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data2;
	}	
        
        /**
         * 
         * @param ConsultationArea doctor consultation area
         * @return ArrayList<ArrayList<String>> with list of doctor's with registration numbers and names
         */
        public ArrayList<ArrayList<String>> getDoctor(String ConsultationArea)
	{	
		
		String sql = 	"SELECT "+
                                "doctor.slmc_reg_no, person.first_name, person.last_name "+
                                "FROM person INNER JOIN doctor ON person.user_id = doctor.user_id "+
                                "WHERE experienced_areas='"+ConsultationArea+"';";

		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<String> data2 = new ArrayList<String>();
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		System.out.println(data);
		return data;
	}
        
        /**
         * 
         * @return names of the doctors
         */
        public ArrayList<ArrayList<String>> getDoctors()
	{	
		
		String sql = 	"SELECT "+
                                "doctor.slmc_reg_no, person.first_name, person.last_name "+
                                "FROM person INNER JOIN doctor ON person.user_id = doctor.user_id;";

		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<String> data2 = new ArrayList<String>();
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		System.out.println(data);
		return data;
	}
        
        
        /**
         * 
         * @param registrtionNo doctors registration number
         * @return ArrayList<String> of days that doctor available in the hospital
         */
        public ArrayList<String> getAvailableDays(String registrtionNo)
	{	
		
		String sql2 = 	"SELECT day FROM doctor_availability " +
						"WHERE slmc_reg_no = '" + registrtionNo + "'"+
						"ORDER BY day ASC;";
				
		ArrayList<ArrayList<String>> data2 = null;
		ArrayList<String> data = new ArrayList<String>();		
		try{			
			
			data2 = super.dbOperator.customSelection(sql2);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		int size2 = data2.size();
		for(int j = 1; j < size2; j++)
		{
			String tmp = data2.get(j).get(0);
			if (!data.contains(tmp))
			{
				data.add(tmp);
			}
			
		}
		//System.out.println(data);
		return data;
	}
        
        /**
         * 
         * @param testName
         * @return available days
         */
        public ArrayList<String> getLabAvailableDays(String testName)
	{	
		
		String sql2 = 	"SELECT "+
                                "app_day "+
                                "FROM lab_appointment_timetable INNER JOIN lab_test ON "+
                                "lab_appointment_timetable.app_test_id = lab_test.test_id "+
                                "WHERE lab_test.test_name='"+ testName +"';";    
				
		ArrayList<ArrayList<String>> data2 = null;
		ArrayList<String> data = new ArrayList<String>();		
		try{	
			data2 = super.dbOperator.customSelection(sql2);
                        	
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		int size2 = data2.size();
		for(int j = 1; j < size2; j++)
		{
			String tmp = data2.get(j).get(0);
			if (!data.contains(tmp))
			{
				data.add(tmp);
			}
			
		}
		//System.out.println(data);
		return data;
	}
        
        /**
         * 
         * @param testName
         * @param day
         * @return available days
         */
        public ArrayList<String> getLabAvailableTimeSlots(String testName, String day)
	{	
		
		String sql2 = 	"SELECT "+
                                "time_slot "+
                                "FROM lab_appointment_timetable INNER JOIN lab_test ON "+
                                "lab_appointment_timetable.app_test_id = lab_test.test_id "+
                                "WHERE lab_test.test_name='"+ testName +"' AND lab_appointment_timetable.app_day = '"+day+"';";    
				
		ArrayList<ArrayList<String>> data2 = null;
		ArrayList<String> data = new ArrayList<String>();		
		try{			
                    
			data2 = super.dbOperator.customSelection(sql2);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		int size2 = data2.size();
		for(int j = 1; j < size2; j++)
		{
			String tmp = data2.get(j).get(0);
			if (!data.contains(tmp))
			{
				data.add(tmp);
			}
			
		}
		//System.out.println(data);
		return data;
	}
        
        /**
         * 
         * @param testName
         * @return available days
         */
        public ArrayList<String> getLabTestID(String testName)
	{	
		
		String sql2 = 	"SELECT "+
                                "test_id "+
                                "FROM lab_test "+
                                "WHERE lab_test.test_name='"+ testName +"';";    
				
		ArrayList<ArrayList<String>> data2 = null;
		ArrayList<String> data = new ArrayList<String>();		
		try{			
			
			data2 = super.dbOperator.customSelection(sql2);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		int size2 = data2.size();
		for(int j = 1; j < size2; j++)
		{
			String tmp = data2.get(j).get(0);
			if (!data.contains(tmp))
			{
				data.add(tmp);
			}
			
		}
		//System.out.println(data);
		return data;
	}
        
	
        /**
         * 
         * @param registrtionNo doctors registration number
         * @param day day selected for the Appointment
         * @return ArrayList<String> of Doctors available time
         * 
         */
	public ArrayList<String> getAvailableTime(String registrtionNo, String day)
	{	
		
		String sql2 = 	"SELECT time_slot FROM doctor_availability " +
                                "WHERE slmc_reg_no = '" + registrtionNo + "' AND day = '"+day+"' "+
                                "ORDER BY time_slot;";
				
		ArrayList<ArrayList<String>> data2 = null;
		ArrayList<String> data = new ArrayList<String>();		
		try{			
			
			data2 = super.dbOperator.customSelection(sql2);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		int size2 = data2.size();
		for(int j = 1; j < size2; j++)
		{
			String tmp = data2.get(j).get(0);
			data.add(tmp);
			
		}
		//System.out.println(data);
		return data;
	}
        
        /**
         * 
         * @param appointmentID appointment ID of the canceling appointment
         * @return boolean value if the operation was successful or not
         */
        public boolean cancelAppointment(String appointmentID)
	{
		boolean result = false;
		String sql = 	"UPDATE appointment " +
				"SET cancelled = true WHERE " +
				"appointment.appointment_id = '" + appointmentID + "';";
		
		try{			
			
			result = super.dbOperator.customInsertion(sql);
                        
                        if (result == true)
                        {
                            ArrayList<ArrayList<String>> data  = null;
                            sql =   "SELECT appointment.bill_id, bill.total FROM appointment INNER JOIN bill ON "+
                                    "appointment.bill_id = bill.bill_id WHERE appointment_id = '"+appointmentID+"'";
                            try{
                                data = super.dbOperator.customSelection(sql);
                                System.out.println(data);
                                
                                if ( ! data.get(1).get(0).equals("NULL"))
                                {
                                    String billID = data.get(1).get(0);
                                    String amount = data.get(1).get(1);
                                    
                                    String refundInfo = "bill_id "+billID+",payment_type docApp,reason no_reason,amount "+amount;
                                    result = refund(refundInfo);
                                    
                                    sql = "UPDATE bill SET refund = 1 WHERE bill_id = '"+billID+"'";
                                    try{
                                        result = super.dbOperator.customInsertion(sql);
                                    }catch(Exception e){}
                                }    
                                
                            }catch(Exception e){}
                            
                        }    
                        
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		return result;
	}
        
    public ArrayList<ArrayList<String>> getAppointmentDetails(String type,String value)
    {	
        
        String sql = "";
        
        switch (type)
        {
            case "d":
                sql =   "SELECT "+
                        "appointment.slmc_reg_no, appointment.date, person.first_name , person.last_name,patient.patient_id "+
                        "FROM appointment INNER JOIN patient ON appointment.patient_id = patient.patient_id "+
                        "INNER JOIN person ON person.person_id = patient.person_id "+
                        "WHERE appointment.slmc_reg_no = '"+value+"' AND appointment.cancelled = 0;";
                break;
                
            case "p":
                sql =   "SELECT "+
                        "patient.patient_id, appointment.date, person.first_name , person.last_name,appointment.slmc_reg_no "+
                        "FROM appointment INNER JOIN patient ON appointment.patient_id = patient.patient_id "+
                        "INNER JOIN person ON person.person_id = patient.person_id "+
                        "WHERE appointment.patient_id = '"+value+"' AND appointment.cancelled = 0;";
                break;    
                
            case "a":
                sql =   "SELECT "+
                        "appointment.slmc_reg_no, appointment.date, person.first_name , person.last_name,patient.patient_id "+
                        "FROM appointment INNER JOIN patient ON appointment.patient_id = patient.patient_id "+
                        "INNER JOIN person ON person.person_id = patient.person_id "+
                        "WHERE appointment_id = '"+value+"';";
                break;    
        
        }    
        
        ArrayList<ArrayList<String>> data = null;

        try{			

                data = super.dbOperator.customSelection(sql);
                
        }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
        //System.out.println(data);
        return data;
    }   
        
        
    public boolean refund(String refundInfo)
    {	
        boolean result = true;
        try{			

            /////////////////// Generating the Refund ID //////////////////////////////////////////////////////////////
            String sql2 = "SELECT refund_id FROM refund WHERE refund_id = (SELECT MAX(refund_id) FROM bill);";
            String refundID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = refundID.toCharArray();
            int i = 1;
            for (i = 1; i < refundID.length(); i++)
            {
                    if  (tmpID[i] != '0') break; 
            } 

            String tmpID2 = Integer.toString(Integer.parseInt(refundID.substring(i,refundID.length())) + 1 );
            while(tmpID2.length() < 4)
            {
                    tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "r" + tmpID2;
            //System.out.println(tmpID2);

            //////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
            String columnNames = "";
            String rowData = "";

            String[] field = refundInfo.split(",");

            int index = 0;
            for (String val : field)
            {
                if (index > 0){ columnNames += "," ; rowData += ","; }

                //// Order of column feilds in the input string!!! ////
                columnNames += val.split(" ")[0];
                if (index < 3) {rowData = rowData + "'";}
                rowData += val.split(" ")[1];
                if (index < 3) {rowData += "'";}

                index++;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            columnNames += ",refund_id,date";
            rowData += ",'" + tmpID2 + "','" + date +"'";

            //System.out.println(columnNames);
            //System.out.println(rowData);


            String sql = "INSERT INTO refund (" + columnNames + ") VALUES (" + rowData +");";
            /////////////////////////////// Adding data to database /////////////////////////////////////////////////	
            result = super.dbOperator.customInsertion(sql);

        }catch(ClassNotFoundException | SQLException e){e.printStackTrace(); result = false;}

        return result;
    }
        
        
        
        
        
        /**
         * 
         * @param appointmentID appointment ID of the canceling appointment
         * @return boolean value if the operation was successful or not
         */
        public boolean cancelLabAppointment(String appointmentID)
	{
		boolean result = false;
		String sql = 	"UPDATE lab_appointment " +
				"SET cancelled = true WHERE " +
				"lab_appointment.lab_appointment_id = '" + appointmentID + "';";
		
		try{			
			
			result = super.dbOperator.customInsertion(sql);
                        
                        if (result == true)
                        {
                            ArrayList<ArrayList<String>> data  = null;
                            sql =   "SELECT lab_appointment.bill_id, bill.total FROM lab_appointment INNER JOIN bill ON "+
                                    "lab_appointment.bill_id = bill.bill_id WHERE lab_appointment_id = '"+appointmentID+"'";
                            try{
                                data = super.dbOperator.customSelection(sql);
                                System.out.println(data);
                                
                                if ( ! data.get(1).get(0).equals("NULL"))
                                {
                                    String billID = data.get(1).get(0);
                                    String amount = data.get(1).get(1);
                                    
                                    String refundInfo = "bill_id "+billID+",payment_type labApp,reason no_reason,amount "+amount;
                                    result = refund(refundInfo);
                                    
                                    sql = "UPDATE bill SET refund = 1 WHERE bill_id = '"+billID+"'";
                                    try{
                                        result = super.dbOperator.customInsertion(sql);
                                    }catch(Exception e){}
                                }    
                                
                            }catch(Exception e){}
                            
                        }    
                        
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		return result;
	}
	
        
        /**
         * 
         * @param appID appointment ID of the searching appointment
         * @return ArrayList<ArrayList<String>> containing date, patient name, consultant name
         */
	public ArrayList<ArrayList<String>> getAppointmentDetails(String appID)
	{	
		String sql = 	"SELECT "+
                                "appointment.slmc_reg_no, appointment.date, person.first_name , person.last_name "+
                                "FROM appointment INNER JOIN patient ON appointment.patient_id = patient.patient_id "+
                                "INNER JOIN person ON person.person_id = patient.person_id "+
                                "WHERE appointment.appointment_id = '"+appID+"';";
		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<ArrayList<String>> data2 = null;
		
		try{			
			
			data = super.dbOperator.customSelection(sql);
			
			String doctorID = data.get(1).get(0);
			sql = 	"SELECT "+
					"person.first_name , person.last_name "+
					"FROM doctor INNER JOIN person ON doctor.user_id = person.user_id "+
					"WHERE doctor.slmc_reg_no = '"+doctorID+"';";
			
			data2 = super.dbOperator.customSelection(sql);
			data.add(data2.get(1));
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
		//System.out.println(data);
		return data;
	}
	
        public ArrayList<ArrayList<String>> getDoctorDetails()
	{	
		String sql = 	"SELECT "+
                                "doctor.slmc_reg_no, person.first_name, person.last_name "+
                                "FROM doctor INNER JOIN person ON doctor.user_id = person.user_id;";
		
		ArrayList<ArrayList<String>> data = null;
		
		
		try{			
			
			data = super.dbOperator.customSelection(sql);
			
			
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
		//System.out.println(data);
		return data;
	}
        
        
        
        /**
         * 
         * @param appID appointment ID of the searching appointment
         * @return ArrayList<ArrayList<String>> containing date, patient name, consultant name
         */
	public ArrayList<ArrayList<String>> getLabAppointmentDetails(String appID)
	{	
		String sql = 	"SELECT "+
                                "lab_appointment.doctor_id, lab_appointment.date, person.first_name , person.last_name "+
                                "FROM lab_appointment INNER JOIN patient ON lab_appointment.patient_id = patient.patient_id "+
                                "INNER JOIN person ON person.person_id = patient.person_id "+
                                "WHERE lab_appointment.lab_appointment_id = '"+appID+"';";
		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<ArrayList<String>> data2 = null;
		
		try{			
			
			data = super.dbOperator.customSelection(sql);
			
			String doctorID = data.get(1).get(0);
			sql = 	"SELECT "+
                                "person.first_name , person.last_name "+
                                "FROM doctor INNER JOIN person ON doctor.user_id = person.user_id "+
                                "WHERE doctor.slmc_reg_no = '"+doctorID+"';";
			
			data2 = super.dbOperator.customSelection(sql);
			data.add(data2.get(1));
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}	
		//System.out.println(data);
		return data;
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
        
    public ArrayList<ArrayList<String>> getAllNames()
        {
            ArrayList<ArrayList<String>> data = null;

            String sql =   "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth "+
                           "FROM person INNER JOIN patient ON person.person_id = patient.person_id;";
            
            
            try{
			data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
            
            

            return data;
        }  
        
	/*	
	public static void main(String[] args)
	{
		Receptionist receptionist =  new Receptionist("user005");
		receptionist.getDoctorTimeTable();
		receptionist.getPatientInfo("hms0001pa");
		//receptionist.setPatientInfo("nic 9532648675,gender f,date_of_birth 19950203,address 145|town1|Street1,mobile 0775123465,first_name heshan,last_name eranga,email erangamx@gmail.com,nationality khksjhs,religion skjhsak");
		receptionist.getAppointments();
		//receptionist.makeAppointment("hms0002pa","slmc0001","2016-10-10 16:30:00","t0001");
		receptionist.doctorAppointmentAvailableTime("slmc0001");
	}
        */
	
}	

