package ir.madresse.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.SQLiteDB.GetDataFromServer;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.Tools.Internet;
import ir.madresse.Volley.request_json;

public class Register extends AppCompatActivity {


    @BindView(R.id.Register_Edt_Name) EditText Edt_Name ;
    @BindView(R.id.Register_Txt_Desc) TextView Txt_Desc ;
    @BindView(R.id.Register_Spn_1)protected Spinner Spn_1;
    @BindView(R.id.Register_Spn_2)protected Spinner Spn_2;
    @BindView(R.id.Register_Spn_3)protected Spinner Spn_3;
    @BindView(R.id.Register_Btn_Ok)protected Button Btn_Ok;

    String[] BaseSection = new String[]{"ابتدایی","متوسطه اول","متوسطه دوم نظری"};
    String[] BasicSections = new String[]{"پایه اول","پایه دوم","پایه سوم","پایه چهارم","پایه پنجم","پایه ششم"};
    String[] SecondarySectons = new String[]{"پایه هفتم","پایه هشتم","پایه نهم"};
    String[] TwoSecondaryField = new String[]{"ریاضی فیزیک","علوم تجربی","علوم انسانی"};
    String[] TwoSecondarySections = new String[]{"پایه دهم","پایه یازدهم","پیش دانشگاهی"};

    String grade ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);



        this.setTitle("تکمیل ثبت نام");


        Edt_Name.setTypeface(App.FONT_iran_sana_light);
        Txt_Desc.setTypeface(App.FONT_iran_sana_light);
        Btn_Ok.setTypeface(App.FONT_iran_sana_light);
        Edt_Name.requestFocus();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BaseSection);
        Spn_1.setAdapter(adapter);


        Spn_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                ArrayAdapter<String> adapter ;
                ArrayAdapter<String> adapterTwoSecondarySections ;
                adapterTwoSecondarySections = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, TwoSecondarySections);




                switch (i){

                    case 0 :
                        adapter  = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, BasicSections);
                        Spn_2.setAdapter(adapter);
                        Spn_2.setVisibility(View.VISIBLE);
                        Spn_3.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, SecondarySectons);
                        Spn_2.setAdapter(adapter);
                        Spn_2.setVisibility(View.VISIBLE);
                        Spn_3.setVisibility(View.INVISIBLE);
                        break;
                    case 2 :
                        adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, TwoSecondaryField);
                        Spn_2.setAdapter(adapter);
                        Spn_2.setVisibility(View.VISIBLE);
                        Spn_3.setAdapter(adapterTwoSecondarySections);
                        Spn_3.setVisibility(View.VISIBLE);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                switch (Spn_1.getSelectedItemPosition()){

                    case 0:
                        grade = String.valueOf(Spn_2.getSelectedItemPosition()+1);
                        break;
                    case 1:
                        grade = grade + String.valueOf(Spn_2.getSelectedItemPosition()+7);
                        break;
                    case 2:
                        grade = "1";
                        grade = grade + String.valueOf(Spn_2.getSelectedItemPosition()+1);
                        grade = grade + String.valueOf(Spn_3.getSelectedItemPosition());
                        break;

                }






                if(!Internet.isOnline(Register.this)){
                    Toast.makeText(Register.this, "دسترسی به اینترنت خود را چک کنید", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(Edt_Name.getText().toString())) {
                    Edt_Name.setError("نام خود را وارد کنید");
                    return;
                }


                ArrayList<String> Keys = new ArrayList<String>();
                ArrayList<String> Values = new ArrayList<String>();

                String id = null;


                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();

                if(bundle != null){
                    id = bundle.getString("ID");
                }


                String accessToken = SharedPreferencesConnector.readString(Register.this,"accessToken","");


                Keys.add("name");
                Keys.add("grade");
                Keys.add("accessToken");




                Values.add(Edt_Name.getText().toString());
                Values.add(grade);
                Values.add(accessToken);


                final ProgressDialog loading = ProgressDialog.show(Register.this, "در حال ارتباط با سرور", "لطفا صبر کنید", false, false);

                request_json.sendrequest(Register.this, App.SERVER_ADDRESS + "user/"+id, Keys, Values, Request.Method.PUT, new request_json.GetJson() {
                    @Override
                    public void onSuccess(String result) {




                        GetDataFromServer.GetBooks(Register.this,grade);

                        loading.dismiss();
                        SharedPreferencesConnector.writeString(Register.this,"grade",grade);
                        startActivity(new Intent(Register.this,Home.class));
                        finish();






                    }

                    @Override
                    public void onError(String result, int errCode) {

                        loading.dismiss();
                        Toast.makeText(Register.this, "Register Error : "+errCode, Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });




    }

}

