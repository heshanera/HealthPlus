package Cashier;

import com.hms.hms_test_2.User;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author heshan
 */
public class Cashier extends User {
    /**
     * Constructor to initialize Cashier with username.
     * 
     * @param username
     */
    public Cashier(String username) {
        super(username);
    }

    /**
     * Retrieves profile information of the cashier.
     * 
     * @return a HashMap containing profile information (key-value pairs)
     */
    public HashMap<String, String> getProfileInfo() {

        HashMap<String, String> infoHash = new HashMap<String, String>();

        //////////////////////////////////// Getting data from database
        String sql = "SELECT person.*,sys_user.* " +
                "FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id " +
                "WHERE (sys_user.user_id = '" + userID + "' AND person.user_id = '" + userID + "');";

        ArrayList<ArrayList<String>> data = null;
        try {
            data = super.dbOperator.customSelection(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        ///////////// Putting data into hashmap
        if (data != null) {
            ArrayList<String> columns = data.get(0);
            ArrayList<String> dataRow = data.get(1);

            int length = columns.size();
            for (int i = 0; i < length; i++) {
                infoHash.put(columns.get(i), dataRow.get(i));
            }
        }
        return infoHash;
    }

    /**
     * Generates a bill based on the provided bill information.
     * 
     * @param billInfo the details of the bill to be generated
     * @return bill id if the bill was successfully generated, "0" otherwise
     */
    public String bill(String billInfo) {

        String result = "0";

        try {

            /////////////////// Generating the Bill ID
            String sql2 = "SELECT bill_id FROM bill WHERE bill_id = (SELECT MAX(bill_id) FROM bill);";
            String billID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = billID.toCharArray();
            int i = 3;
            for (i = 3; i < billID.length(); i++) {
                if (tmpID[i] != '0')
                    break;
            }

            String tmpID2 = Integer.toString(Integer.parseInt(billID.substring(i, billID.length() - 1)) + 1);
            while (tmpID2.length() < 4) {
                tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "hms" + tmpID2 + "b";

            //////////////////////////////// SQL for adding the row
            String columnNames = "";
            String rowData = "";

            String[] field = billInfo.split(",");

            int index = 0;
            for (String val : field) {
                if (index > 0) {
                    columnNames += ",";
                    rowData += ",";
                }

                //// Order of column feilds in the input string!!!
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

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            columnNames += ",bill_id,bill_date";
            rowData += ",'" + tmpID2 + "','" + date + "'";

            String sql = "INSERT INTO bill (" + columnNames + ") VALUES (" + rowData + ");";
            /////////////////////////////// Adding data to database
            boolean res = super.dbOperator.customInsertion(sql);
            if (res)
                result = tmpID2;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return result;
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
     *
     * @param patientID
     * @return
     */
    public boolean removeFromTempBill(String patientID) {
        boolean result = false;
        String sql = "DELETE FROM tmp_bill WHERE patient_id = '" + patientID + "';";

        try {

            result = super.dbOperator.customInsertion(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves details of a bill based on its ID.
     * 
     * @param billID the ID of the bill
     * @return a list containing bill details
     */
    public ArrayList<ArrayList<String>> getBillInfo(String billID) {
        String sql = "SELECT *" +
                "FROM bill " +
                "WHERE bill.bill_id = '" + billID + "';";

        ArrayList<ArrayList<String>> data = null;
        try {

            data = super.dbOperator.customSelection(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Processes a refund based on the provided refund information.
     * 
     * @param refundInfo the details of the refund to be processed
     * @return true if the refund was successful, false otherwise
     */
    public boolean refund(String refundInfo) {
        boolean result = true;
        try {

            /////////////////// Generating the Refund ID
            String sql2 = "SELECT refund_id FROM refund WHERE refund_id = (SELECT MAX(refund_id) FROM bill);";
            String refundID = super.dbOperator.customSelection(sql2).get(1).get(0);

            char[] tmpID = refundID.toCharArray();
            int i = 1;
            for (i = 1; i < refundID.length(); i++) {
                if (tmpID[i] != '0')
                    break;
            }

            String tmpID2 = Integer.toString(Integer.parseInt(refundID.substring(i, refundID.length())) + 1);
            while (tmpID2.length() < 4) {
                tmpID2 = "0" + tmpID2;
            }
            tmpID2 = "r" + tmpID2;

            //////////////////////////////// SQL for adding the row
            String columnNames = "";
            String rowData = "";

            String[] field = refundInfo.split(",");

            int index = 0;
            for (String val : field) {
                if (index > 0) {
                    columnNames += ",";
                    rowData += ",";
                }

                //// Order of column fields in the input string!!! ////
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

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            columnNames += ",refund_id,date";
            rowData += ",'" + tmpID2 + "','" + date + "'";

            String sql = "INSERT INTO refund (" + columnNames + ") VALUES (" + rowData + ");";
            /////////////////////////////// Adding data to database
            result = super.dbOperator.customInsertion(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public ArrayList<ArrayList<String>> getWaitingRefunds() {
        String sql = "SELECT * FROM refund;";

        ArrayList<ArrayList<String>> data = null;
        try {

            data = super.dbOperator.customSelection(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean makeRefund(String id) {
        String sql = "DELETE FROM refund WHERE refund_id = '" + id + "'";

        boolean result = true;
        try {

            result = super.dbOperator.customInsertion(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getNoOfRefunds() {
        String sql = "SELECT COUNT(refund_id) AS 'no' FROM refund;";
        String data = null;
        try {

            data = super.dbOperator.customSelection(sql).get(1).get(0);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Retrieves the payment history of patients.
     * 
     * @param rows the number of rows to fetch
     * @return a list containing the payment history of patients
     */
    public ArrayList<ArrayList<String>> getPaymentHistory(int rows) {
        String sql = "SELECT bill.patient_id, bill.bill_date, bill.doctor_fee, bill.hospital_fee," +
                "bill.pharmacy_fee, bill.laboratory_fee, bill.appointment_fee, bill.total,bill.bill_id " +
                "FROM bill ORDER BY bill_date DESC LIMIT " + rows + ";";

        ArrayList<ArrayList<String>> data = null;
        try {

            data = super.dbOperator.customSelection(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     *
     * @param info
     * @return
     */
    public boolean updateProfileInfo(String info) {
        boolean result = true;
        String column_data = "";

        String[] tmpInfo = info.split("#");
        for (String s : tmpInfo) {
            String[] tmpData = s.split(" ");
            int tmplen = tmpData[0].length() + 1;
            column_data += tmpData[0] + "='" + s.substring(tmplen, s.length()) + "',";
        }

        column_data = column_data.substring(0, column_data.length() - 1);

        String sql = "UPDATE person SET " + column_data + " " +
                "WHERE person_id = (SELECT person_id FROM sys_user WHERE user_id = '" + this.userID + "');";

        try {

            super.dbOperator.customInsertion(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     *
     * @param info
     * @return
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
                "WHERE user_id = '" + this.userID + "';";

        try {

            super.dbOperator.customInsertion(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     *
     * @param value
     * @return
     */
    public ArrayList<ArrayList<String>> getPatientDetails(String value) {
        String sql1 = "";
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT tmp_bill.*,person.first_name,person.last_name FROM tmp_bill" +
                " INNER JOIN patient ON tmp_bill.patient_id = patient.patient_id" +
                " INNER JOIN person ON person.person_id = patient.person_id" +
                " WHERE tmp_bill.patient_id = '" + value + "' ;";

        try {

            data = super.dbOperator.customSelection(sql1);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     *
     * @param value
     * @return
     */
    public ArrayList<ArrayList<String>> getDoctorName(String value) {
        String sql1;
        ArrayList<ArrayList<String>> data = null;

        sql1 = "SELECT person.first_name,person.last_name " +
                "FROM person INNER JOIN doctor ON person.user_id = doctor.user_id " +
                "WHERE doctor.slmc_reg_no = (SELECT consultant_id FROM tmp_bill WHERE patient_id = '" + value + "');";

        try {

            data = super.dbOperator.customSelection(sql1);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;

    }

    public ArrayList<ArrayList<String>> getCancelledDocAppointments() {

        String sql = "SELECT date FROM appointment " +
                "WHERE ( ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (cancelled = 1) )" +
                "ORDER BY date ASC;";

        ArrayList<ArrayList<String>> data = null;
        try {
            data = super.dbOperator.customSelection(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return data;

    }

    public ArrayList<ArrayList<String>> getCancelledLabAppointments() {

        String sql = "SELECT date FROM lab_appointment " +
                "WHERE ( ( date > (CURRENT_DATE - INTERVAL 12 MONTH) ) & (cancelled = 1) )" +
                "ORDER BY date ASC;";

        ArrayList<ArrayList<String>> data = null;
        try {
            data = super.dbOperator.customSelection(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return data;

    }
}
