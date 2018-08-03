package com.example.cooked.hnotes2.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.ImageView;

import com.example.cooked.hnotes2.MainActivity;
import com.example.cooked.hnotes2.Utils.*;
import com.example.cooked.hnotes2.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.example.cooked.hnotes2.Utils.MyApiSpecific.myApiSpecific;


public class ImageUtils
{
    private Context _context;
    private Resources res;
    private static ImageUtils myImageUtils = null;
    
    public ImageUtils(Context context)
    {
        _context = context;
        res = context.getResources();
    }
    
    public static ImageUtils imageUtils()
    {
        if (myImageUtils == null)
            myImageUtils = new ImageUtils(MainActivity.getInstance());
        
        return (myImageUtils);
    }
    
    private void ShowError(String argFunction, String argMessage)
    {
        //myMessages().ShowError("Error in ImageUtils::" + argFunction, argMessage);
    }
    
    public ArrayList<InternalImageItem> listInternalImages()
    {
        try
        {
            ArrayList<InternalImageItem> l_array = new ArrayList<>();
            
            File directory = new File(res.getString(R.string.picture_path));
            File[] files = directory.listFiles();
            
            if (files == null)
                return (null);
            
            if (files.length == 0)
                return (null);
            
            for (File file : files)
            {
                l_array.add(new InternalImageItem(file.getName(), 0));
            }
            return (l_array);
        }
        catch (Exception e)
        {
            ShowError("listInternalImages", e.getMessage());
        }
        return (null);
    }
    
    // Checks to make sure a filename exists
    // Returns: true(worked)/false(failed)
    //
    private boolean validFilename(String filename, MyBoolean retBoolean)
    {
        try
        {
            if (filename.length() == 0)
                return (false);
            File f = new File(res.getString(R.string.picture_path) + "/" + filename);
            retBoolean.Value = f.exists();
            return (true);
        }
        catch (Exception e)
        {
            ShowError("validFilename", e.getMessage());
        }
        return (false);
        
    }
    
    /*
    ** resizes image to 256x256
    ** puts image in destImageView
    ** if no image - default 'imagemissing' is used
    ** Returns: true(worked)/false(failed)
    */
    public boolean getPageHeaderImage(Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        
        if (argFilename.length() == 0)
            return (true);
        
        if (validFilename(argFilename, lValid) == false)
            return (false);
        
        try
        {
            if (lValid.Value == true)
            {
                Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + argFilename));
                
                MyBitmap myBitmap = new MyBitmap();
                if (!ScaleBitmapFromUrl(uri, _context.getContentResolver(), myBitmap))
                    return (false);
                // assign new bitmap and set scale type
                destImageView.setImageBitmap(myBitmap.Value);
//                Picasso.with(context).load(uri).resize(512, 512).into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getPageHeaderImage", e.getMessage());
        }
        return (false);
    }
    
    // Returns: true(worked)/false(failed)
    private boolean ScaleKeepingAspectRatio(Point currPoint, Point idealPoint, Point retPoint)
    {
        try
        {
            if (currPoint.x > currPoint.y)
            {
                // x = width, y = height
                // picture is wider than height
                // make the width the ideal width and calculate height
                retPoint.x = idealPoint.x;
                retPoint.y = (((currPoint.y * 10000) / currPoint.x) * idealPoint.x) / 10000;
            } else
            {
                retPoint.y = idealPoint.y;
                retPoint.x = (((currPoint.x * 10000) / currPoint.y) * idealPoint.y) / 10000;
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("ScaleKeepingAspectRatio", e.getMessage());
        }
        return (false);
    }
    
    // Returns: true(worked)/false(failed)
    public boolean ScaleBitmapFromUrl(Uri imageUri, ContentResolver cr, MyBitmap retBitmap)
    {
        try
        {
            _context.grantUriPermission("com.example.des.hp", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream imageStream = cr.openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            
            int orientation = myApiSpecific().GetImageOrientation(imageUri);
            
            Bitmap finalImage;
            
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    finalImage = rotateImage(selectedImage, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    finalImage = rotateImage(selectedImage, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    finalImage = rotateImage(selectedImage, 270);
                    break;
                default:
                    finalImage = selectedImage;
            }
            
            Point lCurrPoint = new Point(finalImage.getWidth(), finalImage.getHeight());
            Point lIdealPoint = new Point(512, 512);
            Point lNewPoint = new Point(0, 0);
            if (ScaleKeepingAspectRatio(lCurrPoint, lIdealPoint, lNewPoint) == false)
                return (false);
            retBitmap.Value = Bitmap.createScaledBitmap(finalImage, lNewPoint.x, lNewPoint.y, false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("ScaleBitmapFromUrl", e.getMessage());
        }
        return (false);
    }
    
    public boolean ScaleBitmapFromFile(String lfile, ContentResolver cr, MyBitmap retBitmap)
    {
        try
        {
            Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + lfile));
            
            return(ScaleBitmapFromUrl(uri, cr, retBitmap));
        }
        catch (Exception e)
        {
            ShowError("ScaleBitmapFromFile", e.getMessage());
        }
        return (false);
    }

    public boolean getGridIcon(Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        if (validFilename(argFilename, lValid) == false)
            return (false);
        try
        {
            if (lValid.Value == true)
            {
                Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + argFilename));

                com.squareup.picasso.Picasso.with(context).load(uri).resize(150, 150)
                        //.transform(new CircleTransform())
                        .into(destImageView);
            } else
            {
                com.squareup.picasso.Picasso.with(context).load(R.drawable.book).resize(150, 150)
                        //.transform(new CircleTransform())
                        .into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getListIcon", e.getMessage());
        }
        return (false);

    }


    private static Bitmap rotateImage(Bitmap img, int degree)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    
    
}
