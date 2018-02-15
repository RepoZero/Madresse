package ir.madresse.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.Tools.DrawableTools;
import ir.madresse.Tools.JalaliCalendar;

import static ir.madresse.App.DB;

public class EducationalEvents extends AppCompatActivity {


    ArrayList<Integer> DB_ID = new ArrayList<Integer>();
    ArrayList<String> DB_Date = new ArrayList<String>();
    ArrayList<String> DB_Message = new ArrayList<String>();


    EducationalEvents.AdapterEvent adapterAdapterEvent;





    @BindView(R.id.EducationalEvents_Lst) protected ListView Lst;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educational_events_items, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ActionBarEducationalEventsAdd:
                // do whatever
                startActivity(new Intent(this,DialogTimeDatePicker.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_events);
        this.setTitle("رویداد های تحصیلی");
        ButterKnife.bind(this);

        Drawable myIcon = getResources().getDrawable( R.drawable.ic_add_black_24dp );
        getSupportActionBar().setIcon(DrawableTools.setTint(myIcon, Color.WHITE));








        
        if(GetEventListFromDB()) {


            adapterAdapterEvent = new AdapterEvent(DB_Message, DB_Date, DB_ID, this);
            Lst.setAdapter(adapterAdapterEvent);

        }else
            Toast.makeText(this, "هیچ رویدادی ثبت نشده است", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(GetEventListFromDB()) {


            adapterAdapterEvent = new AdapterEvent(DB_Message, DB_Date, DB_ID, this);
            Lst.setAdapter(adapterAdapterEvent);

        }else
            Toast.makeText(this, "هیچ رویدادی ثبت نشده است", Toast.LENGTH_SHORT).show();


    }



    public class AdapterEvent extends BaseAdapter {


        ArrayList<String> AdapterMessages = new ArrayList<String>();
        ArrayList<String> AdapterTimes = new ArrayList<String>();
        ArrayList<Integer> AdapterID = new ArrayList<Integer>();
        LayoutInflater layoutInflater;
        Context context;



        public AdapterEvent(ArrayList<String> Messages , ArrayList<String> Times , ArrayList<Integer> ID , Context context) {
            super();
            this.AdapterMessages = Messages;
            this.AdapterTimes = Times;
            this.context = context;
            this.AdapterID = ID;
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return AdapterMessages.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.row_lst_event, null);

            TextView Txt_Message=(TextView)view.findViewById(R.id.row_lst_event_Txt_Message);
            TextView Txt_Time=(TextView)view.findViewById(R.id.row_lst_event_Txt_Time);
            ImageView Del_img=(ImageView)view.findViewById(R.id.row_lst_event_Img_Delete);

            Txt_Message.setTypeface(App.FONT_iran_sana_light);
            Txt_Time.setTypeface(App.FONT_iran_sana_light);

            Del_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("واقعا میخواهید این رویداد رو حذف کنید ؟");
                    builder.setPositiveButton("آره مطمئنم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO



                            RemoveFromEventsDB(AdapterID.get(position));

                            DB_ID.remove(position);
                            DB_Date.remove(position);
                            DB_Message.remove(position);


                            adapterAdapterEvent.notifyDataSetChanged();


                            dialog.dismiss();


                        }
                    });
                    builder.setNegativeButton("نه نمیخوام", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();



                }
            });


            Txt_Message.setText(AdapterMessages.get(position));
            Txt_Time.setText(AdapterTimes.get(position));


            return view;
        }

        @Override
        public CharSequence[] getAutofillOptions() {
            return new CharSequence[0];


        }

    }




    boolean GetEventListFromDB() {

        String SelectQuery = "SELECT * FROM Events";
        Cursor cursor = DB.rawQuery(SelectQuery, null);


        if (cursor != null && cursor.getCount() > 0) {


            DB_ID.clear();
            DB_Date.clear();
            DB_Message.clear();



            if (cursor.moveToFirst()) {
                do {
                    int ID = cursor.getInt(cursor.getColumnIndex("ID"));
                    int Year = cursor.getInt(cursor.getColumnIndex("Year"));
                    int Month = cursor.getInt(cursor.getColumnIndex("Month"));
                    int Day = cursor.getInt(cursor.getColumnIndex("Day"));
                    int Hour = cursor.getInt(cursor.getColumnIndex("Hour"));
                    int Minute = cursor.getInt(cursor.getColumnIndex("Minute"));
                    String Message = cursor.getString(cursor.getColumnIndex("Message"));



                    DB_ID.add(ID);

                    JalaliCalendar.YearMonthDate Date = new JalaliCalendar.YearMonthDate(Year,Month,Day);
                    DB_Date.add(""+JalaliCalendar.gregorianToJalali(Date) + " | " + Hour + ":"+Minute);

                    DB_Message.add(Message);



                } while (cursor.moveToNext());
            }


            cursor.close();


            return true;


        } else
            return false;

    }




    void RemoveFromEventsDB(int ID){

        String SelectQuery = "DELETE FROM Events Where ID="+ID;
        Cursor cursor = DB.rawQuery(SelectQuery, null);
        cursor.moveToFirst();
        cursor.close();

    }


}
