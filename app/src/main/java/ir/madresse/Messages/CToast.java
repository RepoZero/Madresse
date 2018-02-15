package ir.madresse.Messages;


import android.content.Context;
import android.os.Handler;

public class CToast {

     static public void CustomTimeTosat(String text, int duration, Context context){

         final android.widget.Toast toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT);
         toast.show();
         Handler handler = new Handler();
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 toast.cancel();
             }
         }, duration);
     }
}
