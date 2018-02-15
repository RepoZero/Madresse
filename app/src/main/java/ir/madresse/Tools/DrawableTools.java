package ir.madresse.Tools;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class DrawableTools {

    public static android.graphics.drawable.Drawable setTint(android.graphics.drawable.Drawable d, int color) {
        android.graphics.drawable.Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static String getFileToByte(String path){
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        byte[] b = null;
        String encodeString = null;
        try{
            bm = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
            encodeString = Base64.encodeToString(b, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }
}
