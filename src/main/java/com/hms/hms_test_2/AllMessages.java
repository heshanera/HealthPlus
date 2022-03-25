package com.hms.hms_test_2;

import javafx.scene.image.ImageView;

public class AllMessages
{
    private ImageView imageView1;
    private String string;
    private ImageView imageView2;

    private String sender;
    private String message;
    private String date;
    private String subject;
    private String name;
    private String type;
    private String id;


    AllMessages(ImageView img, String string, ImageView img2, String sender, String message,String date,String subject,String name,String type, String id )
    {
        this.imageView1 = img;
        this.imageView2 = img2;
        this.string = string;

        this.sender = sender;
        this.message = message;
        this.date = date;
        this.subject = subject;
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public void setImageView1(ImageView value)
    {
        imageView1 = value;
    }

    public ImageView getImageView1()
    {
        return imageView1;
    }

    public void setImageView2(ImageView value)
    {
        imageView2 = value;
    }

    public ImageView getImageView2()
    {
        return imageView2;
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
