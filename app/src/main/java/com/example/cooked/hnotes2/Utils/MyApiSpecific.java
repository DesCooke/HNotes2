package com.example.cooked.hnotes2.Utils;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import com.example.cooked.hnotes2.MainActivity;

import java.io.InputStream;


//
// All functions return true/false
//
public class MyApiSpecific extends AppCompatActivity
{
    public static MyApiSpecific apiSpecific=null;
    public Context _context;

    public static MyApiSpecific myApiSpecific()
    {
        if(apiSpecific == null)
            apiSpecific=new MyApiSpecific(MainActivity.getInstance());

        return (apiSpecific);
    }


    public MyApiSpecific(Context context)
    {
            _context=context;
    }

    public int GetImageOrientation(Uri imageUri)
    {
        try {
            InputStream input = _context.getContentResolver().openInputStream(imageUri);
            ExifInterface ei;
            if (Build.VERSION.SDK_INT > 23)
                ei = new ExifInterface(input);
            else
                ei = new ExifInterface(imageUri.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //myMessages().LogMessage("***ORIENTATION*** of " + imageUri.getPath() + " is " + String.valueOf(orientation));
            return orientation;
        } catch (Exception e)
        {

        }
        return(0);
    }
    public int GetHour(TimePicker time)
    {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentHour());

            }
            return (time.getHour());
    }

    public int GetMinute(TimePicker time)
    {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentMinute());
            }
            return (time.getMinute());
    }

    public void SetMinute(TimePicker time, int minute)
    {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentMinute(minute);
                return;
            } else
            {
                time.setMinute(minute);
            }
    }

    public void SetHour(TimePicker time, int hour)
    {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentHour(hour);
                return;
            } else
            {
                time.setHour(hour);
            }
    }

    public int getTheColor(int resColor)
    {
            if(Build.VERSION.SDK_INT < 23)
                return (ContextCompat.getColor(_context, resColor));

            return (_context.getColor(resColor));
    }


}
