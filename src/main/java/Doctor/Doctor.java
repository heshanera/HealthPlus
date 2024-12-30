package Doctor;

import com.hms.hms_test_2.User;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Doctor extends User {
	public String slmcRegNo;

	public String getUsername() {
		return super.username;
	}

	/**
	 * Constructor to initialize the Doctor object with a specified username.
	 * 
	 * @param username the username to initialize the Doctor object
	 */
	public Doctor(String username) {
		super(username);

		try {
			slmcRegNo = super.dbOperator.showTableData("doctor", "slmc_reg_no", ("user_id = '" + super.userID + "'"))
					.get(0).get(0);
		} catch (ClassNotFoundException | SQLException e) {
		}

	}

	/**
	 * Retrieves the profile information of the doctor.
	 * 
	 * @return a HashMap containing the profile information
	 */
	public HashMap<String, String> getProfileInfo() {
		/*
		 * String[] columnNamesDoc =
		 * {"person_id","nic","gender","date_of_birth","address","mobile","user_id",
		 * "first_name","last_name","email","nationality","religion",
		 * "slmc_reg_no","user_id","available_days","education","training",
		 * "experienced_areas","experience","achievements"};
		 */
		HashMap<String, String> infoHash = new HashMap<String, String>();

		////////////////////// Getting data from database //////////////////////
		String sql = "SELECT person.*,sys_user.*,doctor.* " +
				"FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id INNER JOIN doctor ON person.user_id = doctor.user_id  "
				+
				"WHERE (sys_user.user_id = '" + userID + "' AND person.user_id = '" + userID
				+ "' AND doctor.user_id = '" + userID + "');";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		////////////////////// Putting data into hashmap //////////////////////

		if (data != null) {
			ArrayList<String> columns = data.get(0);
			ArrayList<String> dataRow = data.get(1);

			int length = columns.size();
			for (int i = 0; i < length; i++) {
				infoHash.put(columns.get(i), dataRow.get(i));
			}
		}

		////////////////////////////////////////////////////////////////////////
		return infoHash;
	}

	/**
	 * Updates the basic profile information of the doctor.
	 * 
	 * @param info the information to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateProfileInfo(String info) {
		boolean result = true;
		String column_data = "";

		String[] tmpInfo = info.split("#");
		for (String s : tmpInfo) {
			String[] tmpData = s.split(" ");
			column_data += tmpData[0] + "='" + tmpData[1] + "',";
		}

		column_data = column_data.substring(0, column_data.length() - 1);

		String sql = "UPDATE person SET " + column_data + " " +
				"WHERE person_id = (SELECT person_id FROM sys_user WHERE user_id = (SELECT user_id FROM doctor WHERE slmc_reg_no = '"
				+ this.slmcRegNo + "'));";

		try {
			super.dbOperator.customInsertion(sql);
		} catch (ClassNotFoundException | SQLException e) {
			result = false;
		}

		return result;
	}

	/**
	 * Updates the doctor's specific details (e.g., specialization, experience,
	 * etc.).
	 * 
	 * @param info the information to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateDoctorInfo(String info) {
		boolean result = true;
		String column_data = "";

		String[] tmpInfo = info.split("#");
		for (String s : tmpInfo) {
			String[] tmpData = s.split(" ");
			column_data += tmpData[0] + "='" + s.substring(tmpData[0].length(), s.length()) + "',";

		}

		column_data = column_data.substring(0, column_data.length() - 1);

		String sql = "UPDATE doctor SET " + column_data + " " +
				"WHERE slmc_reg_no = '" + this.slmcRegNo + "';";

		try {

			super.dbOperator.customInsertion(sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Updates the doctor's account information (e.g., username, password, etc.).
	 * 
	 * @param info the information to be updated
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateAccountInfo(String info) {
		boolean result = true;
		String column_data = "";

		String[] tmpInfo = info.split("#");
		for (String s : tmpInfo) {
			String[] tmpData = s.split(" ");
			column_data += tmpData[0] + "='" + s.substring(tmpData[0].length() + 1, s.length()) + "',";
		}

		column_data = column_data.substring(0, column_data.length() - 1);

		String sql = "UPDATE sys_user SET " + column_data + " " +
				"WHERE user_id = (SELECT user_id FROM doctor WHERE slmc_reg_no = '" + this.slmcRegNo + "');";

		try {

			super.dbOperator.customInsertion(sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Retrieves the doctor's timetable.
	 * 
	 * @return a list of arrays representing the doctor's timetable
	 */
	public ArrayList<ArrayList<String>> doctorTimeTable() {
		////////////////////// Getting data from database //////////////////////

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.showTableData("doctor_availability", "day,time_slot,time_slot_id",
					"slmc_reg_no = (SELECT slmc_reg_no FROM doctor WHERE user_id = '" + this.userID + "');");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		////////////////////// Putting data into ArrayList //////////////////////

		if (data != null) {
			String[] columnArray = { "day", "time_slot" };
			data.add(0, new ArrayList<String>(Arrays.asList(columnArray)));
		}
		return data;
	}

	public boolean removeDoctorTime(String day, String slot) {
		////////////////////// removing data from database //////////////////////
		boolean result = true;

		String sql = "DELETE FROM doctor_availability WHERE slmc_reg_no = '" + this.slmcRegNo + "' AND day = '" + day
				+ "'  AND time_slot = '" + slot + "' LIMIT 1";

		try {
			super.dbOperator.customDeletion(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * Removes a time slot from the doctor's timetable.
	 * 
	 * @param id the ID of the time slot to be removed
	 * @return true if the time slot was successfully removed, false otherwise
	 */
	public boolean removeDoctorTime(String id) {
		////////////////////// removing data from database //////////////////////
		boolean result = true;

		String sql = "DELETE FROM doctor_availability WHERE time_slot_id = '" + id + "' ;";

		try {
			super.dbOperator.customDeletion(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;

	}

	/**
	 * Adds a new time slot to the doctor's timetable.
	 * 
	 * @param day      the day of the week
	 * @param timeSlot the time slot to be added
	 * @return true if the time slot was successfully added, false otherwise
	 */
	public boolean doctorTimeTableAddSlot(String day, String timeSlot) {
		boolean result = true;

		////////////////////// Getting data from database //////////////////////
		try {

			String sql = "SELECT time_slot_id FROM doctor_availability WHERE time_slot_id = (SELECT MAX(time_slot_id) FROM doctor_availability);";
			String timeSlotID = super.dbOperator.customSelection(sql).get(1).get(0);

			char[] tmpID = timeSlotID.toCharArray();
			int i = 1;
			for (i = 1; i < timeSlotID.length(); i++) {
				if (tmpID[i] != '0')
					break;
			}

			String tmpID2 = Integer.toString(Integer.parseInt(timeSlotID.substring(i, timeSlotID.length())) + 1);
			while (tmpID2.length() < 4) {
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "t" + tmpID2;

			sql = tmpID2 + "," + slmcRegNo + "," + day + "," + timeSlot + ",0,0";
			super.dbOperator.addTableRow("doctor_availability", sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * Retrieves the list of appointments for the doctor.
	 * 
	 * @return a list of arrays containing appointment details
	 */
	public ArrayList<ArrayList<String>> getAppointments() {
		////////////////////// Getting data from database //////////////////////

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.showTableData("appointment", "patient_id,date",
					"slmc_reg_no = '" + slmcRegNo + "' AND cancelled = false");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		////////////////////// Putting data into ArrayList //////////////////////

		if (data != null) {
			String[] columnArray = { "patient", "date" };
			data.add(0, new ArrayList<String>(Arrays.asList(columnArray)));
		}
		return data;
	}

	public ArrayList<ArrayList<String>> getTestResults(String searchType, String searchWord) {
		////////////////////// Getting data from database //////////////////////
		String sql = "";

		switch (searchType) {
			case "patientID":
				sql = "SELECT person.first_name, person.last_name, person.date_of_birth, person.gender," +
						"tests.date,tests.patient_id,tests.lab_assistant,tests.consultant_id,tests.test_result " +
						"FROM person " +
						"JOIN patient " +
						"ON person.person_id=patient.person_id " +
						"JOIN tests " +
						"ON patient.patient_id=tests.patient_id " +
						"WHERE patient.patient_id = '" + searchWord + "' " +
						"ORDER BY tests.date DESC;";
				break;
			case "nic":
				sql = "SELECT person.first_name, person.last_name, person.date_of_birth, person.gender," +
						"tests.date,tests.patient_id,tests.lab_assistant,tests.consultant_id,tests.test_result " +
						"FROM person " +
						"JOIN patient " +
						"ON person.person_id=patient.person_id " +
						"JOIN tests " +
						"ON patient.patient_id=tests.patient_id " +
						"WHERE person.person_id = (SELECT person.person_id FROM person WHERE person.nic = '"
						+ searchWord + "') " +
						"ORDER BY tests.date DESC;";
				break;
			case "testID":
				sql = "SELECT person.first_name, person.last_name, person.date_of_birth, person.gender," +
						"tests.date,tests.patient_id,tests.lab_assistant,tests.consultant_id,tests.test_result " +
						"FROM person " +
						"JOIN patient " +
						"ON person.person_id=patient.person_id " +
						"JOIN tests " +
						"ON patient.patient_id=tests.patient_id " +
						"WHERE patient.patient_id = (SELECT tests.patient_id FROM tests WHERE tests.test_result_id = '"
						+ searchWord + "');";
				break;
		}

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Retrieves the test results for a patient based on the search criteria.
	 * 
	 * @param searchType the type of search (e.g., test type, patient ID)
	 * @param searchWord the search keyword (e.g., patient ID or test name)
	 * @return a list of arrays containing the test results
	 */
	public ArrayList<ArrayList<String>> getTestResults(String testID) {
		HashMap<String, String> tables = new HashMap<String, String>();
		tables.put("lv", "LiverFunctionTest");
		tables.put("bg", "BloodGroupingRh");
		tables.put("li", "LipidTest");
		tables.put("re", "RenalFunctionTest");
		tables.put("scp", "SeriumCreatinePhosphokinase");
		tables.put("scpt", "SeriumCreatinePhosphokinaseTotal");
		tables.put("ur", "UrineFullReport");
		tables.put("cbc", "completeBloodCount");

		String tableName = "";

		String prefx = testID.substring(0, 2);
		boolean flg = false;

		switch (prefx) {
			case "lv":
				tableName = tables.get(prefx);
				flg = true;
				break;
			case "bg":
				tableName = tables.get(prefx);
				flg = true;
				break;
			case "li":
				tableName = tables.get(prefx);
				flg = true;
				break;
			case "re":
				tableName = tables.get(prefx);
				flg = true;
				break;
			case "ur":
				tableName = tables.get(prefx);
				flg = true;
				break;
		}

		if (!flg) {
			prefx = testID.substring(0, 4);
			switch (prefx) {
				case "scpt":
					tableName = tables.get(prefx);
					flg = true;
					break;
			}

		}

		if (!flg) {
			prefx = testID.substring(0, 3);
			switch (prefx) {
				case "scp":
					tableName = tables.get(prefx);
					flg = true;
					break;
				case "cbc":
					tableName = tables.get(prefx);
					flg = true;
					break;
			}

		}

		HashMap<String, String> tableId = new HashMap<String, String>();
		tableId.put("LiverFunctionTest", "tst_liver_id");
		tableId.put("BloodGroupingRh", "tst_bloodG_id");
		tableId.put("LipidTest", "tst_li_id");
		tableId.put("RenalFunctionTest", "tst_renal_id");
		tableId.put("SeriumCreatinePhosphokinase", "tst_SCP_id");
		tableId.put("SeriumCreatinePhosphokinaseTotal", "tst_SCPT_id");
		tableId.put("UrineFullReport", "tst_ur_id");
		tableId.put("completeBloodCount", "tst_CBC_id");

		String testIdCol = tableId.get(tableName);

		////////////////////// Getting data from database //////////////////////

		String sql = "SELECT * " +
				"FROM " + tableName + " " +
				"WHERE " + testIdCol + " = '" + testID + "';";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		if (data != null) {
			ArrayList<String> meta = new ArrayList<String>();
			meta.add(prefx);
			data.add(meta);
		}

		return data;
	}

	/**
	 * Retrieves patient information based on the search criteria.
	 * 
	 * @param searchType the type of search (e.g., patient ID, name)
	 * @param searchWord the search keyword (e.g., patient ID or name)
	 * @return a nested list of arrays containing patient information
	 */
	public ArrayList<ArrayList<ArrayList<String>>> getPatientInfo(String searchType, String searchWord) {
		////////////////////// Getting data from database //////////////////////
		ArrayList<ArrayList<String>> personalData = null;
		ArrayList<ArrayList<String>> medicalData = null;
		ArrayList<ArrayList<String>> historyData = null;

		try {
			switch (searchType) {

				case "nic":
					personalData = super.dbOperator
							.customSelection("SELECT * FROM person WHERE nic = '" + searchWord + "'");
					medicalData = super.dbOperator.customSelection(
							"SELECT patient.drug_allergies_and_reactions,patient.patient_id FROM patient WHERE patient_id = "
									+
									"(SELECT patient_id FROM patient WHERE person_id = (SELECT person_id FROM person WHERE nic = '"
									+ searchWord + "'));");
					historyData = super.dbOperator
							.customSelection("SELECT medical_history.history FROM medical_history WHERE patient_id = " +
									"(SELECT patient_id FROM patient WHERE person_id = (SELECT person_id FROM person WHERE nic = '"
									+ searchWord + "'))  ORDER BY date DESC;");
					break;

				case "id":
					personalData = super.dbOperator.customSelection(
							"SELECT * FROM person WHERE person_id = (SELECT patient.person_id FROM patient WHERE patient_id = '"
									+ searchWord + "')");
					medicalData = super.dbOperator.customSelection(
							"SELECT patient.drug_allergies_and_reactions FROM patient WHERE patient_id = '" + searchWord
									+ "'");
					historyData = super.dbOperator.customSelection(
							"SELECT medical_history.date,medical_history.history FROM medical_history WHERE patient_id = '"
									+ searchWord + "' ORDER BY date DESC");
					break;
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		ArrayList<ArrayList<ArrayList<String>>> data = new ArrayList<ArrayList<ArrayList<String>>>();
		data.add(personalData);
		data.add(medicalData);
		data.add(historyData);
		return data;
	}

	public ArrayList<ArrayList<String>> searchByName(String namePart) {
		ArrayList<ArrayList<String>> data = null;

		String nameParts[] = namePart.split(" ");
		String sql = "";
		if (nameParts.length == 1) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + nameParts[0] + "%' " +
					" LIMIT 10;";
		} else if (nameParts.length == 2) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + nameParts[0] + "%' AND person.last_name LIKE '" + nameParts[1]
					+ "%' " +
					" LIMIT 10;";
		} else if (nameParts.length > 2) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + nameParts[0] + "%' AND person.last_name LIKE '" + nameParts[1]
					+ "%' " +
					" LIMIT 10;";
		}

		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<ArrayList<String>> getAllNames() {
		ArrayList<ArrayList<String>> data = null;

		String sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
				"FROM person INNER JOIN patient ON person.person_id = patient.person_id;";

		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Adds diagnostic details for a patient.
	 * 
	 * @param diagnostic the diagnostic information
	 * @param patientID  the ID of the patient being diagnosed
	 * @return true if the diagnostic details were successfully added, false
	 *         otherwise
	 */
	public boolean diagnose(String diagnostic, String patientID) {
		boolean result = true;
		////////////////////// Adding data to database //////////////////////
		try {

			String sql = "SELECT history_id FROM medical_history WHERE history_id = (SELECT MAX(history_id) FROM medical_history);";
			String historyID = super.dbOperator.customSelection(sql).get(1).get(0);

			char[] tmpID = historyID.toCharArray();
			int i = 3;
			for (i = 3; i < historyID.length(); i++) {
				if (tmpID[i] != '0')
					break;
			}

			String tmpID2 = Integer.toString(Integer.parseInt(historyID.substring(i, historyID.length())) + 1);
			while (tmpID2.length() < 4) {
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "his" + tmpID2;

			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			String sql2 = "INSERT INTO medical_history VALUES (" +
					"'" + tmpID2 + "','" + patientID + "','" + this.slmcRegNo + "','" + date + "','" + diagnostic
					+ "')";
			super.dbOperator.customInsertion(sql2);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	public boolean bill(String billInfo, String patientID, String labFee) {

		boolean result = true;

		try {

			////////////////////// Generating the Bill ID //////////////////////

			String sql3 = "SELECT tmp_bill_id FROM tmp_bill WHERE patient_id = '" + patientID + "';";
			String tmpID2;
			try {

				tmpID2 = super.dbOperator.customSelection(sql3).get(1).get(0);

				String sql = "UPDATE tmp_bill SET laboratory_fee = '" + labFee + "' WHERE tmp_bill_id = '" + tmpID2
						+ "';";
				////////////////////// Adding data to database //////////////////////
				super.dbOperator.customInsertion(sql);

			} catch (Exception e) {

				String sql2 = "SELECT tmp_bill_id FROM tmp_bill WHERE tmp_bill_id = (SELECT MAX(tmp_bill_id) FROM tmp_bill);";
				String billID = super.dbOperator.customSelection(sql2).get(1).get(0);

				char[] tmpID = billID.toCharArray();
				int i = 3;
				for (i = 3; i < billID.length(); i++) {
					if (tmpID[i] != '0')
						break;
				}

				tmpID2 = Integer.toString(Integer.parseInt(billID.substring(i, billID.length() - 2)) + 1);
				while (tmpID2.length() < 4) {
					tmpID2 = "0" + tmpID2;
				}
				tmpID2 = "hms" + tmpID2 + "tb";

				////////////////////// SQL for adding the row //////////////////////
				String columnNames = "";
				String rowData = "";

				String[] field = billInfo.split(",");

				int index = 0;
				for (String val : field) {
					if (index > 0) {
						columnNames += ",";
						rowData += ",";
					}

					columnNames += val.split(" ")[0];
					if (index < 3) {
						rowData = rowData + "'";
					}
					rowData += val.split(" ")[1];
					if (index < 3) {
						rowData += "'";
					}

					index++;
				}

				columnNames += ",tmp_bill_id";
				rowData += ",'" + tmpID2 + "'";

				String sql = "INSERT INTO tmp_bill (" + columnNames + ") VALUES (" + rowData + ");";
				////////////////////// Adding data to database //////////////////////
				super.dbOperator.customInsertion(sql);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getLabFee(String test) {

		String fee = "";
		String sql = "SELECT test_fee FROM lab_test WHERE test_name = '" + test + "';";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
			fee = data.get(1).get(0);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return fee;
	}

	/**
	 * Adds allergy information for a patient.
	 * 
	 * @param allergies the allergy information
	 * @param patientID the ID of the patient with allergies
	 * @return true if the allergy information was successfully added, false
	 *         otherwise
	 */
	public boolean allergies(String allergies, String patientID) {
		boolean result = true;
		////////////////////// Adding data to database //////////////////////
		try {

			String sql0 = "SELECT drug_allergies_and_reactions FROM patient WHERE patient_id = '" + patientID + "';";
			ArrayList<ArrayList<String>> data = super.dbOperator.customSelection(sql0);
			String currentAllergies = data.get(1).get(0);

			currentAllergies += "," + allergies;

			String sql = "UPDATE patient SET drug_allergies_and_reactions = '" + currentAllergies + "' " +
					"WHERE patient_id = '" + patientID + "';";
			super.dbOperator.customInsertion(sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Adds prescription details (e.g., drugs and tests) for a patient.
	 * 
	 * @param drugs     the prescribed drugs
	 * @param tests     the prescribed tests
	 * @param patientID the ID of the patient
	 * @return true if the prescription details were successfully added, false
	 *         otherwise
	 */
	public boolean prescribe(String drugs, String tests, String patientID) {
		boolean result = true;
		////////////////////// Adding data to database //////////////////////
		try {

			String sql = "SELECT prescription_id FROM prescription WHERE prescription_id = (SELECT MAX(prescription_id) FROM prescription);";
			String prescID = super.dbOperator.customSelection(sql).get(1).get(0);

			char[] tmpID = prescID.toCharArray();
			int i = 4;
			for (i = 4; i < prescID.length(); i++) {
				if (tmpID[i] != '0')
					break;
			}

			String tmpID2 = Integer.toString(Integer.parseInt(prescID.substring(i, prescID.length())) + 1);
			while (tmpID2.length() < 5) {
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "pres" + tmpID2;

			String doctorID = this.slmcRegNo;
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

			if (tests.equals(""))
				tests = "NULL";
			if (drugs.equals(""))
				tests = "NULL";

			sql = tmpID2 + "," + patientID + "," + doctorID + "," + date + "," + drugs + "," + tests;
			super.dbOperator.addTableRow("prescription", sql);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	public ArrayList<ArrayList<String>> getDrugInfo() {

		////////////////////// Getting data from database //////////////////////
		String sql = "";

		sql = "SELECT * " +
				"FROM drug;";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return data;

	}

	public ArrayList<String> getDrugGenericInfo() {
		String sql = "";

		sql = "SELECT generic_name " +
				"FROM drug_brand_names;";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		ArrayList<String> data2 = new ArrayList<String>();

		if (data != null) {
			int size = data.size();
			for (int i = 1; i < size; i++) {
				String genericName = data.get(i).get(0);
				if (!data2.contains(genericName))
					data2.add(genericName);
			}
		}
		return data2;

	}

	public ArrayList<String> getDrugBrandInfo(String genericName) {

		String sql = "";

		sql = "SELECT brand_name " +
				"FROM drug_brand_names WHERE generic_name = '" + genericName + "';";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		ArrayList<String> data2 = new ArrayList<String>();

		if (data != null) {
			int size = data.size();
			for (int i = 1; i < size; i++) {
				data2.add(data.get(i).get(0));
			}
		}
		return data2;

	}

	public ArrayList<ArrayList<String>> getPatientAttendance(String doctorID) {

		String sql = "";

		if (doctorID.equals("All")) {

			sql = "SELECT date FROM appointment WHERE date > (CURRENT_DATE - INTERVAL 12 MONTH) ORDER BY date ASC;";

		} else {

			sql = "SELECT date FROM appointment " +
					"WHERE ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (slmc_reg_no = '" + doctorID + "') " +
					"ORDER BY date ASC;";
		}

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return data;

	}

	public String getTodayAppointments() {
		String sql = "SELECT COUNT(date) AS 'Appointments' FROM appointment WHERE ( day(date)=day(CURRENT_DATE) AND slmc_reg_no='"
				+ this.slmcRegNo + "');";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String apps = "";
		if (data != null) {
			data.get(1).get(0);
		}
		return apps;

	}

	public ArrayList<ArrayList<String>> nameSuggestor(String word) {
		String sql = "";
		String[] wordList = word.split(" ");

		if (wordList.length == 1) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + wordList[0] + "%' " +
					" LIMIT 10;";
		} else if (wordList.length == 2) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + wordList[0] + "%' AND person.last_name LIKE '" + wordList[1]
					+ "%' " +
					" LIMIT 10;";
		} else if (wordList.length > 2) {
			sql = "SELECT patient.patient_id,person.first_name,person.last_name,person.date_of_birth " +
					"FROM person INNER JOIN patient ON person.person_id = patient.person_id  " +
					"WHERE person.first_name LIKE '" + wordList[0] + "%' AND person.last_name LIKE '" + wordList[1]
					+ "%' " +
					" LIMIT 10;";
		}

		ArrayList<ArrayList<String>> data = null;
		try {
			if (word.length() >= 1) {
				data = super.dbOperator.customSelection(sql);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return data;

	}

	public ArrayList<ArrayList<String>> getTestInfo() {

		String sql = "";

		sql = "SELECT test_id, test_name, test_description " +
				"FROM lab_test;";

		ArrayList<ArrayList<String>> data = null;
		try {
			data = super.dbOperator.customSelection(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public ArrayList<ArrayList<String>> getLabPatientInfo(String appID) {
		String sql1;
		ArrayList<ArrayList<String>> data = null;

		sql1 = "SELECT person.first_name,person.last_name,person.gender, person.date_of_birth " +
				"FROM person INNER JOIN patient ON patient.person_id = person.person_id " +
				"INNER JOIN lab_appointment ON lab_appointment.patient_id = patient.patient_id " +
				"WHERE lab_appointment.lab_appointment_id='" + appID + "';";

		try {

			data = super.dbOperator.customSelection(sql1);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return data;
	}
}
