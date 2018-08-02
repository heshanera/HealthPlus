package com.hms.hms_test_2;

import javafx.scene.image.ImageView;

public class AllMessages 
{
     private ImageView image;
     private String string;
     private ImageView image2;
     
     private String sender;
     private String message;
     private String date;
     private String subject;
     private String name;
     private String type;
     private String id;
     

     AllMessages(ImageView img, String string, ImageView img2, String sender, String message,String date,String subject,String name,String type, String id ) 
     {
         this.image = img;
         this.image2 = img2;
         this.string = string;
         
         this.sender = sender;
         this.message = message;
         this.date = date;
         this.subject = subject;
         this.name = name;
         this.type = type;
         this.id = id;
     }

     public void setImage(ImageView value) 
     {
         image = value;
     }

     public ImageView getImage() 
     {
         return image;
     }
     
     public void setImage2(ImageView value) 
     {
         image2 = value;
     }

     public ImageView getImage2() 
     {
         return image2;
     }

     public void setSring(String string) 
     {
         this.string = string;
     }

     public String getString() 
     {
         return this.string;
     }
     
     public void setSender(String string) 
     {
         this.sender = string;
     }

     public String getSender() 
     {
         return this.sender;
     }
     
     public void setMessage(String string) 
     {
         this.message = string;
     }

     public String getMessage() 
     {
         return this.message;
     }
     
     public void setDate(String string) 
     {
         this.date = string;
     }

     public String getDate() 
     {
         return this.date;
     }
     
     public void setSubject(String string) 
     {
         this.subject = string;
     }

     public String getSubject() 
     {
         return this.subject;
     }
     
     public void setType(String string) 
     {
         this.type = string;
     }

     public String getType() 
     {
         return this.type;
     }
     
     public void setName(String string) 
     {
         this.name = string;
     }

     public String getName() 
     {
         return this.name;
     }
     
     public void setID(String string) 
     {
         this.id = string;
     }

     public String getID() 
     {
         return this.id;
     }
 }
