package IntermediatePharmacist;

import com.hms.hms_test_2.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class InterPharmacist extends User
{

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// methods /////////////////////////////////////////////////////////////////////////////////////////////
	/*
	*
	*
	*
	public Pharmacist(String username)											==>   Constuctor
	 
	public HashMap<String,String> getProfileInfo()								==>   Get profile info
	public ArrayList<ArrayList<String>> getPrescriptionInfo(String patientID)	==>   Get prescription
	public ArrayList<ArrayList<String>> getMedByName(String medName)			==>   Get medicine info (Adding med to prescription)
	public ArrayList<ArrayList<String>> getpharmacyHistory(int rows)			==>   Get pharmacy history of patients
	public ArrayList<ArrayList<String>> getDrugInfo(String drugID)				==>   Get medicine info
	public boolean addNewDrug(String drugInfo)						==>   Add new drug
	public boolean addNewStock(String stockInfo)						==>   Add new drug stock	
	public ArrayList<ArrayList<String>> getStockInfo(String supplierID)			==>   Get stock details 

	*
	*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




//	/***
//	 *
//	 * @param username
//	 */

	private String pharmacistID;


	public InterPharmacist(String username)
	{
		super(username);

		try{
			pharmacistID = super.dbOperator.showTableData("pharmacist","pharmacist_id",("user_id = '" + super.userID + "'")).get(0).get(0);
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
		String sql = "SELECT person.*,sys_user.*,pharmacist.* "+
				"FROM person INNER JOIN sys_user ON person.user_id = sys_user.user_id INNER JOIN pharmacist ON person.user_id = pharmacist.user_id  "+
				"WHERE (sys_user.user_id = '"+userID+"' AND person.user_id = '"+userID+"' AND pharmacist.user_id = '"+userID+"');";

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





//Donot Remove
public ArrayList<ArrayList<String>> getStockSummary2()
	{
		String sql = 	"SELECT brand_id, brand_name, drug_type,drug_unit, unit_price FROM drug_brand_names;";

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
                            String brandId = data.get(i).get(0);
                            sql = "SELECT pharmacy_stock.remaining_quantity, pharmacy_stock.supplier_id " +
                                  "FROM pharmacy_stock WHERE brand_id = '"+ brandId +"';";

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
//		Donot remove
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

//		Donot remove
        public HashMap<String,String> getSupplierNames()
	{
            String sql =    "SELECT suppliers.supplier_id, suppliers.supplier_name " + 
                            "FROM suppliers;";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}


            HashMap<String,String> supplierNames = new HashMap<>(); 

            int tmpSize = data.size();
            for (int i = 1; i < tmpSize; i++)
            {
                    supplierNames.put(data.get(i).get(0), data.get(i).get(1) );
            }

            //System.out.println(supplierNames);
            return supplierNames;
            
        }
        


//        Donot remove
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

}	

