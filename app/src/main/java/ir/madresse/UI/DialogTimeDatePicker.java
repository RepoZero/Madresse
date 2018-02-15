package ir.madresse.UI;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.Tools.JalaliCalendar;

import static ir.madresse.App.DB;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DialogTimeDatePicker extends AppCompatActivity {

    @BindView(R.id.DialogTimeDatePicker_Btn_Add)protected Button Btn_Add ;
    @BindView(R.id.DialogTimeDatePicker_Btn_DatePicker)protected Button Btn_DatePicke ;
    @BindView(R.id.DialogTimeDatePicker_Btn_TimePicker)protected Button Btn_TimePicker ;
    @BindView(R.id.DialogTimeDatePicker_Edt_Message)protected EditText Edt_Message;

    JalaliCalendar jalaliCalendar ;

    protected boolean SelectDate=false;
    protected boolean SelectTime=false;

    int Year;
    int Month;
    int Day;
    int Hour;
    int Minute;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_time_date_picker);
        ButterKnife.bind(this);




        Btn_Add.setTypeface(App.FONT_iran_sana_light);
        Btn_DatePicke.setTypeface(App.FONT_iran_sana_light);
        Btn_TimePicker.setTypeface(App.FONT_iran_sana_light);
        Edt_Message.setTypeface(App.FONT_iran_sana_light);


          jalaliCalendar = new JalaliCalendar();



        Btn_DatePicke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PersianDatePickerDialog picker ;

                picker = new PersianDatePickerDialog(DialogTimeDatePicker.this)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setMaxYear(1400)
                        .setMinYear(1396)
                        .setActionTextColor(Color.GRAY)
                        .setTypeFace(App.FONT_iran_sana_light)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {


                                JalaliCalendar.YearMonthDate ymd = new JalaliCalendar.YearMonthDate(persianCalendar.getPersianYear(),persianCalendar.getPersianMonth(),persianCalendar.getPersianDay());

                                JalaliCalendar.YearMonthDate JtoG = jalaliCalendar.jalaliToGregorian(ymd);


                                Year = JtoG.getYear();
                                Month = JtoG.getMonth();
                                Day = JtoG.getDate();


                                Btn_DatePicke.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());



                                SelectDate = true;



                            }

                            @Override
                            public void onDisimised() {

                                Toast.makeText(DialogTimeDatePicker.this, "هیچ روزی را انتخاب نکردید !", Toast.LENGTH_SHORT).show();

                            }
                        });

                picker.show();

            }
        });




        Btn_TimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);




                TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //time set stuff

                        Hour =hourOfDay;
                        Minute = minute;

                        SelectTime = true;

                        Btn_TimePicker.setText(hourOfDay+":"+minute);

                    }
                };



                TimePickerDialog myTPDialog = new TimePickerDialog(DialogTimeDatePicker.this,mTimeSetListener,hour,minute,true);


                myTPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {


                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(DialogTimeDatePicker.this, "هیچ زمانی را انتخاب نکردید !", Toast.LENGTH_SHORT).show();
                    }
                });
                myTPDialog.show();


            }
        });



        Btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(Edt_Message.getText().toString())){
                    Toast.makeText(DialogTimeDatePicker.this, "لطفا عنوان رویداد را وارد کنید", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!SelectDate){
                    Toast.makeText(DialogTimeDatePicker.this, "تاریخ را انتخاب کنید", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!SelectTime){
                    Toast.makeText(DialogTimeDatePicker.this, "زمان را وارد کنید", Toast.LENGTH_SHORT).show();
                    return;
                }


                String InsertQuery = "INSERT INTO Events (Year,Month,Day,Hour,Minute,Message,Show) VALUES("+Year+","+Month+","+Day+","+Hour+","+Minute+","+"'"+Edt_Message.getText().toString()+"'"+","+0+")";
                Cursor cursor = DB.rawQuery(InsertQuery, null);
                cursor.moveToFirst();

                finish();



            }
        });






    }






}
