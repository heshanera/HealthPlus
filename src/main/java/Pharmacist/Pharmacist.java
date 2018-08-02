package Pharmacist;

import com.hms.hms_test_2.User;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Pharmacist extends User
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
	
	private String pharmacistID;
	
       /***
        *
        * @param username 
        */
	public Pharmacist(String username)
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
	
	public ArrayList<ArrayList<String>> getPrescriptionInfo(String patientID)
	{	
		String sql =    "SELECT drugs_dose FROM prescription WHERE patient_id = '"+ patientID +"'" +
                                "AND date = (SELECT MAX(date) FROM prescription);";
						
		ArrayList<ArrayList<String>> data = null;	
		try{			
			data = super.dbOperator.customSelection(sql);
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}
        
        public ArrayList<ArrayList<String>> getPrescribedDoc(String patientID)
	{	
		String sql =    "SELECT consultant_id FROM prescription WHERE patient_id = '"+ patientID +"'" +
                                "AND date = (SELECT MAX(date) FROM prescription);";
						
		ArrayList<ArrayList<String>> data = null;	
		try{			
			data = super.dbOperator.customSelection(sql);
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}

	/*
	public ArrayList<ArrayList<String>> getMedByName(String medName)
	{	
		String sql = "SELECT drug_id,drug_type,drug_unit,unit_price FROM drug "+
						"WHERE drug_name = '" + medName +"';";

		ArrayList<ArrayList<String>> data = null;	
		try{			
			data = super.dbOperator.customSelection(sql);
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}
	*/
	public ArrayList<ArrayList<String>> getpharmacyHistory(int rows)
	{	
		String sql = 	"SELECT "+
						"pharmacy_history.date, pharmacy_history.no_of_drugs, pharmacy_history.excluded, "+
						"bill.bill_id, bill.pharmacy_fee, "+
						"prescription.drugs_dose, prescription.patient_id,"+
						"person.first_name, person.last_name " +
						"FROM pharmacy_history INNER JOIN bill ON pharmacy_history.bill_id = bill.bill_id "+
						"INNER JOIN prescription ON bill.patient_id = prescription.patient_id "+
						"INNER JOIN patient ON patient.patient_id = prescription.patient_id "+
						"INNER JOIN person ON patient.person_id = person.person_id "+
						"LIMIT " + rows + ";";

		
		ArrayList<ArrayList<String>> data = null;	
		try{			
			data = super.dbOperator.customSelection(sql);
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		//System.out.println(data);
		return data;
	}
	
	public ArrayList<ArrayList<String>> getDrugInfo(String drugID)
	{	
		String sql = 	"SELECT "+
						"drug.drug_name, drug.drug_type, drug.drug_unit,drug.unit_price, "+
						"pharmacy_stock.stock_id, pharmacy_stock.manufac_name, pharmacy_stock.manufac_date, pharmacy_stock.exp_date, pharmacy_stock.supplier_id "+
						"FROM drug INNER JOIN pharmacy_stock ON drug.drug_id = pharmacy_stock.drug_id "+
						"WHERE drug.drug_id = '" + drugID + "';";

		
		ArrayList<ArrayList<String>> data = null;
		ArrayList<ArrayList<String>> remainingQuantity = null;	
		try{			
			data = super.dbOperator.customSelection(sql);
			remainingQuantity = super.dbOperator.showTableData("pharmacy_stock","remaining_quantity","drug_id = '" + drugID + "'");
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
		
		int noOfStocks = remainingQuantity.size();
		int total = 0;
		for (int i = 0; i < noOfStocks; i++)
		{
			total += Integer.parseInt(remainingQuantity.get(i).get(0)); 
		}
		
		ArrayList<String> totalArray = new ArrayList<String>();
		totalArray.add(Integer.toString(total));
		data.add(totalArray);													// Adding the total quantity of the drug type to the result array			
		
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
        
	
	public boolean addNewDrug(String drugInfo)
	{	
		boolean result = true;
	
		try{			
			
			/////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
			String sql2 = "SELECT drug_id FROM drug WHERE drug_id = (SELECT MAX(drug_id) FROM drug);";
			String drugID = super.dbOperator.customSelection(sql2).get(1).get(0);

			char[] tmpID = drugID.toCharArray();
			int i = 1;
			for (i = 1; i < drugID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			
			String tmpID2 = Integer.toString(Integer.parseInt(drugID.substring(i,drugID.length())) + 1 );
			while(tmpID2.length() < 4)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "d" + tmpID2;
			
			//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
			String columnNames = "";
			String rowData = "";
			
			String[] field = drugInfo.split(",");
			
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
			
			columnNames += ",drug_id";
			rowData += ",'" + tmpID2 + "'";
			
			//System.out.println(columnNames);
			//System.out.println(rowData);
			
			String sql = "INSERT INTO drug (" + columnNames + ") VALUES (" + rowData +");";
			/////////////////////////////// Adding data to database /////////////////////////////////////////////////
			result = super.dbOperator.customInsertion(sql);
			
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace(); result = false;}

		return result;
	}
	
	public boolean addNewStock(String stockInfo)
	{	
		boolean result = true;
	
		try{			
			
			/////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
			String sql2 = "SELECT stock_id FROM pharmacy_stock WHERE stock_id = (SELECT MAX(stock_id) FROM pharmacy_stock);";
			String stockID = super.dbOperator.customSelection(sql2).get(1).get(0);

			char[] tmpID = stockID.toCharArray();
			int i = 3;
			for (i = 3; i < stockID.length(); i++)
			{
				if  (tmpID[i] != '0') break; 
			} 
			
			String tmpID2 = Integer.toString(Integer.parseInt(stockID.substring(i,stockID.length())) + 1 );
			while(tmpID2.length() < 4)
			{
				tmpID2 = "0" + tmpID2;
			}
			tmpID2 = "stk" + tmpID2;
			
			//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
			String columnNames = "";
			String rowData = "";
			
			String[] field = stockInfo.split(",");
			
			int index = 0;
			for (String val : field)
			{
				if (index > 0){ columnNames += "," ; rowData += ","; }
				columnNames += val.split(" ")[0];
				if ((index == 2) || (index == 5) || (index == 6)) {rowData = rowData + "'";}
				rowData += val.split(" ")[1];
				if ((index == 2) || (index == 5) || (index == 6)) {rowData = rowData + "'";}
				
				index++;
			}
			
			String date = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			columnNames += ",stock_id,date";
			rowData += ",'" + tmpID2 + "'," + date;
			
			//System.out.println(columnNames);
			//System.out.println(rowData);
			
			String sql = "INSERT INTO pharmacy_stock (" + columnNames + ") VALUES (" + rowData +");";
			/////////////////////////////// Adding data to database /////////////////////////////////////////////////
			result = super.dbOperator.customInsertion(sql);
			
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace(); result = false;}

		return result;
	}
	
	
	public ArrayList<ArrayList<String>> getStockInfo(String supplierID)
	{	
		String sql = 	"SELECT "+
						"pharmacy_stock.stock_id, pharmacy_stock.stock, pharmacy_stock.remaining_quantity, pharmacy_stock.date, pharmacy_stock.manufac_date, pharmacy_stock.exp_date,"+
						"drug.drug_name, drug.drug_unit "+
						"FROM pharmacy_stock INNER JOIN drug ON pharmacy_stock.drug_id = drug.drug_id "+
						"WHERE pharmacy_stock.supplier_id = '" + supplierID + "' "+
						"ORDER BY pharmacy_stock.stock_id;";

		
		ArrayList<ArrayList<String>> data = null;
		try{			
			
			data = super.dbOperator.customSelection(sql);
				
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
		
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
        
        
        /**
         * 
         * @return ArrayList<ArrayList<String>> of supplier id and delivered stocks
         */
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
        
        /**
         * 
         * @return HashMap<String,String> of supplier name and supplier id
         */
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
        
        public ArrayList<ArrayList<String>> getSupplierNames2()
	{
            String sql =    "SELECT suppliers.supplier_name " + 
                            "FROM suppliers;";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}


            return data;
            
        }
        
        public HashMap<String,String> getDrugPrices()
	{	
            String sql = 	"SELECT brand_name, drug_unit, unit_price " + 
                                "FROM drug_brand_names;";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}


            HashMap<String,String> pricesSummary = new HashMap<>(); 

            int tmpSize = data.size();
            for (int i = 1; i < tmpSize; i++)
            {
                    String tmp = data.get(i).get(2) + " " + data.get(i).get(1);
                    pricesSummary.put(data.get(i).get(0), tmp );

            }

            //System.out.println(pricesSummary);
            return pricesSummary;
	}
        
        public ArrayList<ArrayList<String>> getGenericNames()
        {
            String sql = "SELECT generic_name "+
                         "FROM drug_brand_names;";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //System.out.println(data);
            return data;
        
        }
        
        public ArrayList<ArrayList<String>> getBrandNames(String Name)
        {
            String sql = "SELECT brand_name "+
                         "FROM drug_brand_names WHERE generic_name = '"+Name+"';";

            ArrayList<ArrayList<String>> data = null;
            try{
                    data = super.dbOperator.customSelection(sql);
            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        
        public ArrayList<ArrayList<String>> getDrugStockID(String drugName)
	{	
            String sql = 	"SELECT pharmacy_stock.stock_id,pharmacy_stock.remaining_quantity " + 
                                "FROM pharmacy_stock "+
                                "INNER JOIN drug_brand_names "+
                                "ON drug_brand_names.brand_id = pharmacy_stock.brand_id "+
                                "WHERE drug_brand_names.brand_name = '"+drugName+"';";

            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return data;
	}
        
        public boolean reduceStock(int qt,String stkID)
	{	
            String sql =    "UPDATE pharmacy_stock "+
                            "SET remaining_quantity = remaining_quantity -"+qt+
                            " WHERE stock_id = '"+stkID+"';";
            boolean result = true;
            
      
            try{			
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
	}
        
        
        public String checkForGenName(String genName)
        {
            String sql = "SELECT drug_id FROM drug WHERE drug_name = '"+genName+"' LIMIT 1";
            String id = "";
            
            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);
                    id = data.get(1).get(0);
                    System.out.println(id);

            }catch(Exception e){id = "0";}

            return id;
        }   
        
        public String addNewDrug2(String genName)
        {
            boolean result = true;
            String id = "";
            try{			

                /////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
                String sql2 = "SELECT drug_id FROM drug WHERE drug_id = (SELECT MAX(drug_id) FROM drug);";
                String drugID = super.dbOperator.customSelection(sql2).get(1).get(0);

                char[] tmpID = drugID.toCharArray();
                int i = 1;
                for (i = 1; i < drugID.length(); i++)
                {
                        if  (tmpID[i] != '0') break; 
                } 

                String tmpID2 = Integer.toString(Integer.parseInt(drugID.substring(i,drugID.length())) + 1 );
                while(tmpID2.length() < 4)
                {
                        tmpID2 = "0" + tmpID2;
                }
                tmpID2 = "d" + tmpID2;
                id = tmpID2;
            }catch(Exception e) {}    
            
            
            String sql = "INSERT INTO drug VALUES ('"+id+"','"+genName+"',0);";
            
            result = true;
            try{			
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return id;
        }        
        
        public String checkForBrandName(String brandName)
        {
            String sql = "SELECT brand_id FROM drug_brand_names WHERE brand_name = '"+brandName+"' LIMIT 1";
            String id = "";
            
            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);
                    id = data.get(1).get(0);
                    System.out.println(id);

            }catch(Exception e){id = "0";}

            return id;
        }   
        
        public String addNewBrand(String brandName,String genName,String type,String unit,String price)
        {
            boolean result = true;
            String id = "";
            try{			

                /////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
                String sql2 = "SELECT brand_id FROM drug_brand_names WHERE brand_id = (SELECT MAX(brand_id) FROM drug_brand_names);";
                String brandID = super.dbOperator.customSelection(sql2).get(1).get(0);

                char[] tmpID = brandID.toCharArray();
                int i = 2;
                for (i = 2; i < brandID.length(); i++)
                {
                        if  (tmpID[i] != '0') break; 
                } 

                String tmpID2 = Integer.toString(Integer.parseInt(brandID.substring(i,brandID.length())) + 1 );
                while(tmpID2.length() < 4)
                {
                        tmpID2 = "0" + tmpID2;
                }
                tmpID2 = "br" + tmpID2;
                id = tmpID2;
            }catch(Exception e) {}    
            
            
            String sql = "INSERT INTO drug_brand_names VALUES ('"+id+"','"+brandName+"','"+genName+"','"+type+"','"+unit+"','"+price+"');";
            
            result = true;
            try{			
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return id;
        }  
        
        public String checkForSupplierName(String suppName)
        {
            String sql = "SELECT supplier_id FROM suppliers WHERE supplier_name = '"+suppName+"' LIMIT 1";
            String id = "";
            
            ArrayList<ArrayList<String>> data = null;
            try{			
                    data = super.dbOperator.customSelection(sql);
                    id = data.get(1).get(0);
                    System.out.println(id);
                   
            }catch(Exception e){id = "0";}

            return id;
        }   
        
        public String addNewSupplier(String suppName)
        {
            boolean result = true;
            String id = "";
            try{			

                /////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
                String sql2 = "SELECT supplier_id FROM suppliers WHERE supplier_id = (SELECT MAX(supplier_id) FROM suppliers);";
                String drugID = super.dbOperator.customSelection(sql2).get(1).get(0);

                char[] tmpID = drugID.toCharArray();
                int i = 3;
                for (i = 3; i < drugID.length(); i++)
                {
                        if  (tmpID[i] != '0') break; 
                } 

                String tmpID2 = Integer.toString(Integer.parseInt(drugID.substring(i,drugID.length())) + 1 );
                while(tmpID2.length() < 4)
                {
                        tmpID2 = "0" + tmpID2;
                }
                tmpID2 = "sup" + tmpID2;
                id = tmpID2;
            }catch(Exception e) {}    
            
            
            String sql = "INSERT INTO suppliers VALUES ('"+id+"','"+suppName+"');";
            
            result = true;
            try{			
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return id;
        
        }    
        
        public boolean updateStock(String drugID,String brandID,String stock,String manuDate,String expDate,String suppID,String date)
        {
            boolean result = true;
            String id = "";
            try{			

                /////////////////// Generating the Drug ID //////////////////////////////////////////////////////////////
                String sql2 = "SELECT stock_id FROM pharmacy_stock WHERE stock_id = (SELECT MAX(stock_id) FROM pharmacy_stock);";
                String stockID = super.dbOperator.customSelection(sql2).get(1).get(0);

                char[] tmpID = stockID.toCharArray();
                int i = 3;
                for (i = 3; i < stockID.length(); i++)
                {
                        if  (tmpID[i] != '0') break; 
                } 

                String tmpID2 = Integer.toString(Integer.parseInt(stockID.substring(i,stockID.length())) + 1 );
                while(tmpID2.length() < 4)
                {
                        tmpID2 = "0" + tmpID2;
                }
                tmpID2 = "stk" + tmpID2;
                id = tmpID2;
            }catch(Exception e) {}   
            
            String sql =    "INSERT INTO pharmacy_stock VALUES ("+
                            "'"+id+"','"+drugID+"','"+brandID+"','"+stock+"','"+stock+"','"+manuDate+"','"+expDate+"','"+suppID+"','"+date+"'"+
                            ");";
            
            result = true;
            try{			
                    result = super.dbOperator.customInsertion(sql);

            }catch(ClassNotFoundException | SQLException e){e.printStackTrace();}

            return result;
            
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
        
        public boolean updatePharmacistInfo(String info)
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

		String sql =	"UPDATE pharmacist SET " + column_data + " "  +
						"WHERE pharmacist_id = '" + this.pharmacistID + "';";
		
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
        
        public boolean bill(String billInfo, String patientID, String pharmacyFee)
	{
		
		boolean result = true;
	
		try{			
			
			/////////////////// Generating the Bill ID //////////////////////////////////////////////////////////////
			
			String sql3 = "SELECT tmp_bill_id FROM tmp_bill WHERE patient_id = '" + patientID + "';";
			String tmpID2;
			try{
				
				tmpID2 = super.dbOperator.customSelection(sql3).get(1).get(0);
				
				String sql = "UPDATE tmp_bill SET pharmacy_fee = '" + pharmacyFee + "' WHERE tmp_bill_id = '" + tmpID2 + "';";
				/////////////////////////////// Adding data to database /////////////////////////////////////////////////
				super.dbOperator.customInsertion(sql);
				
				
				
			} catch(Exception e) {
                            
                            try{
			
				String sql2 = "SELECT tmp_bill_id FROM tmp_bill WHERE tmp_bill_id = (SELECT MAX(tmp_bill_id) FROM tmp_bill);";
				String billID = super.dbOperator.customSelection(sql2).get(1).get(0);

				char[] tmpID = billID.toCharArray();
				int i = 3;
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
				
				//System.out.println(tmpID2);
                            } catch (Exception ex) {tmpID2 = "hms0001tb";}    
                                
				
				//////////////////////////////// SQL for adding the row ////////////////////////////////////////////////
				String columnNames = "";
				String rowData = "";
				
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
				super.dbOperator.customInsertion(sql);
			}	
	
		}catch(ClassNotFoundException | SQLException e){e.printStackTrace();}
			
		return result;	
	}

        /*
	public static void main(String[] args)
	{
            
		Pharmacist pharmacist = new Pharmacist("user003");
		pharmacist.getProfileInfo();
		pharmacist.getPrescriptionInfo("hms0001pa");
		pharmacist.getMedByName("drug2");
		pharmacist.getpharmacyHistory(20);
		pharmacist.getDrugInfo("d0001");
		//pharmacist.addNewDrug("drug_name drug6,drug_type tablet,drug_unit mg,unit_price 3,dangerous_drug false");
		//pharmacist.addNewStock("stock 600,remaining_quantity 500,manufac_name name2,manufac_date 20150406,exp_date 20160413,supplier_id sup0001,drug_id d0003");
		pharmacist.getStockInfo("sup0002");
		
               
		
	}
        */

}	

