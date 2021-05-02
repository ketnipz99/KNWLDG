package com.example.knwldg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;


//Utility object class of tutorial
public class Tutorial {
    public String description;
    public int ques_id;
    private Bitmap tutImage;

    public Tutorial() {}

    public Tutorial(String description, int ques_id, Bitmap tutImage) {
        this.description = description;
        this.ques_id = ques_id;
        this.tutImage = tutImage;
    }

    public String getTutorial() {
        return description;
    }

    public void setTutorial(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return tutImage;
    }

    //Convert image retrieve in db to bitmap
    public void setImage (byte[] imageBlob) {
        byte[] bytes = imageBlob;
        this.tutImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }
}
