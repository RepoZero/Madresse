package ir.madresse.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ir.madresse.Messages.CNotification;
import ir.madresse.R;
import ir.madresse.Tools.JalaliCalendar;
import ir.madresse.UI.EducationalEvents;

import static ir.madresse.App.DB;

/**
 * Created by cloner on 8/1/17.
 */

public class Events extends Service {



    Timer timer = new Timer();
    int UPDATE_INTERVAL = 1000;


    int Year ;
    int Month ;
    int Day ;
    int Hour ;
    int MINUTE ;
    int Second ;


    ArrayList<Integer> DB_ID = new ArrayList<Integer>();
    ArrayList<Integer> DB_Hour = new ArrayList<Integer>();
    ArrayList<Integer> DB_Minute = new ArrayList<Integer>();
    ArrayList<String> DB_Message = new ArrayList<String>();
    ArrayList<Integer> DB_Show = new ArrayList<Integer>();






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SystemClock();


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private void SystemClock() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {


                Calendar c = Calendar.getInstance();


                 Year = c.get(Calendar.YEAR);
                 Month =c.get(Calendar.MONTH)+1;
                 Day = c.get(Calendar.DAY_OF_MONTH);
                 Hour = c.get(Calendar.HOUR_OF_DAY);
                 MINUTE = c.get(Calendar.MINUTE);
                 Second = c.get(Calendar.SECOND);

                JalaliCalendar.YearMonthDate Date = new JalaliCalendar.YearMonthDate(Year,Month,Day);



                String SysClock = Year+"/"+Month+"/"+Day+"  ||| Shamsi : "+JalaliCalendar.gregorianToJalali(Date)+" |||+ "+Hour+":"+MINUTE+":"+Second;
                Log.i("AdapterEvent System Clock : ","||||||"+ SysClock);



                if(ReadAlarmsfromDB()){




                    for (int i=0 ; i< DB_ID.size() ; i++){



                      if(DB_Hour.get(i)==Hour && DB_Minute.get(i)==MINUTE && DB_Show.get(i)==0){

                          Intent intent = new Intent(Events.this, EducationalEvents.class);
                          PendingIntent pIntent = PendingIntent.getActivity(Events.this, 0, intent, 0);


                          CNotification n = new CNotification();
                          n.Normal(intent, pIntent, Events.this, "XCourse", DB_Message.get(i), R.mipmap.ic_launcher_round);
                          SetAlarmShow(DB_ID.get(i));

                          Log.v("Events","Notficarions Show Wow!");






                      }

                  }


                }




            }

        }, 0, UPDATE_INTERVAL);
    }


    boolean ReadAlarmsfromDB(){


        String SelectQuery = "SELECT * FROM Events Where Year="+Year+" AND Month="+Month+" AND Day="+Day;
        Cursor cursor= DB.rawQuery(SelectQuery,null);

        if(cursor!=null && cursor.getCount()>0){



            DB_ID.clear();
            DB_Hour.clear();
            DB_Minute.clear();
            DB_Message.clear();
            DB_Show.clear();


                if (cursor.moveToFirst()) {
                    do {
                        int ID = cursor.getInt(cursor.getColumnIndex("ID"));
                        int Hour = cursor.getInt(cursor.getColumnIndex("Hour"));
                        int Minute = cursor.getInt(cursor.getColumnIndex("Minute"));
                        String Message = cursor.getString(cursor.getColumnIndex("Message"));
                        int Show = cursor.getInt(cursor.getColumnIndex("Show"));

                        DB_ID.add(ID);
                        DB_Hour.add(Hour);
                        DB_Minute.add(Minute);
                        DB_Message.add(Message);
                        DB_Show.add(Show);






                    } while (cursor.moveToNext());
                }


                cursor.close();


            return true;


        }else
        return false;


    }

    void SetAlarmShow(int ID){


        String SelectQuery = "UPDATE Events SET Show=1 WHERE ID="+ID;
        Cursor cursor= DB.rawQuery(SelectQuery,null);

        cursor.moveToFirst();


        cursor.close();



    }

}
