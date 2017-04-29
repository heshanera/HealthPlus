package com.hms.hms_test_2;

import java.util.*;

public class Validate
{
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	
    /**
     * 
     * @param nic National Identity Card No
     * @return empty ArrayList<Integer> with birth year, days and gender respectively
     * ArrayList<Integer> is empty if the string is not an nic no
     * 
     */
    public static ArrayList<String> NIC(String nic)
    {
        ArrayList<String> result = new ArrayList<String>();
        // Checking the length of the String
        if (nic.length() == 10)
        {
            char V = nic.charAt(9);
            // checking the last character
            if ( (V  == 'V') ||  (V  == 'v') )
            {
                try{

                    int yearPart = Integer.parseInt( nic.substring(0,2) );
                    int dayPart = Integer.parseInt( nic.substring(2,5) );
                    int lastPart = Integer.parseInt( nic.substring(5,9) );

                    String birthYear = "19" + yearPart;
                    String gender = "m";
                    if ( dayPart > 500 )
                    {
                            gender = "f";
                            dayPart -= 500;	
                    }
                    result.add(birthYear);
                    result.add(Integer.toString(dayPart));
                    result.add(gender);

                } catch (Exception e) {}	
            }
        }
        return result;
    }

    /**
     * 
     * @param mob mobile number
     * @return String "1" if correct else return correct format
     */
    public static String mobile(String mob)
    {
            String result = "0xx-xxx-xxxx";

            if ( mob.length() == 10 )
            {
                    result = "1"; 
                    int x;
                    for(int j = 0 ; j < mob.length() ; j++)
                    {
                            x = (int)mob.charAt(j);
                            if( x < 48 || x > 57 )
                            result = "0xx-xxx-xxxx";   
                    }

            }
            return result;
    }

    /**
     * 
     * @param patientID Patient ID of the patient
     * @return String "1" if correct else return correct format
     */
    public static String patientID(String patientID)
    {
            String result = "hmsxxxxpa";

            if ( patientID.length() == 9 )
            {
                    if ( patientID.substring(0,3).equals("hms") )
                    {
                            if ( patientID.substring(7,9).equals("pa") )
                            {
                                    result = "1";
                            }
                    }
            }
            return result;
    }

    /**
     * 
     * @param email email 
     * @return String "1" if correct else return correct format
     */
    public static String email(String email)
    {
            String result = "1";
            Boolean match = email.matches(EMAIL_REGEX);
            if ( match == false ) 
            {
                result = "abc@mail.com";
            }
            return result;
    }
    
    

    public static String testID(String patientID)
    {
            String result = "hmsxxxxpa";

            if ( patientID.length() == 9 )
            {
                    if ( patientID.substring(0,3).equals("hms") )
                    {
                            if ( patientID.substring(7,9).equals("pa") )
                            {
                                    result = "1";
                            }
                    }
            }
            return result;
    }

    /**
     * 
     * @param value as a String
     * @return true or false  
     */
    public static boolean checkInt(String value)
    {
            boolean result = false;

            try{
                    Integer.parseInt(value); 
                    result = true;
            }catch(Exception e){ result = false; }

            return result;
    }
    
    /**
     * 
     * @param value
     * @return 
     */
    public static String appointmentID(String patientID)
    {
            String result = "1";

            if ( patientID.length() == 6 )
            {
                    if ( patientID.substring(0,3).equals("app") )
                    {
                        if ( checkInt(patientID.substring(3,6)) == true )
                        {
                            result = "1";
                        }
                        else { result = "appxxx"; }
                    }
                    else result = "appxxx";
                    
            } else if ( patientID.length() == 7 ) {
                
                    if ( patientID.substring(0,4).equals("lapp") )
                    {
                        if ( checkInt(patientID.substring(4,7)) == true )
                        {   
                            result = "1";
                        }
                        else { result = "appxxx"; }    
                    }
                    else result = "lappxxx";
            } else if ( patientID.length() > 7 ) {
            
                result = "appxxx";
            }   
            return result;
    }

    /*
    public static void main(String args[])
    {
            System.out.println( Validate.NIC("943562172V") );
            System.out.println( Validate.patientID("hms0001pa") );
            System.out.println( Validate.mobile("asdqwezxca") );
            System.out.println( Validate.email("erangamx@gmail.com") );
    }
    */

}
