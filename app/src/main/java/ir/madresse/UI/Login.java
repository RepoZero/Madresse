package ir.madresse.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.Tools.Internet;
import ir.madresse.Volley.request_json;

public class Login extends AppCompatActivity {


    @BindView(R.id.Login_Edt_PhoneNumber) EditText Edt_PhoneNumber;
    @BindView(R.id.Login_Btn_Login) Button Btn_Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        boolean login = SharedPreferencesConnector.exist(Login.this,"Login");

        if(login) {
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }


        this.setTitle("ورود");



        Edt_PhoneNumber.setTypeface(App.FONT_iran_sana_light);
        Btn_Login.setTypeface(App.FONT_iran_sana_light);


        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();

            }
        });


        }


    void login(){



        if(!Internet.isOnline(Login.this)){
            Toast.makeText(this, "دسترسی به اینترنت خود را چک کنید", Toast.LENGTH_SHORT).show();
            return;
        }



        if (TextUtils.isEmpty(Edt_PhoneNumber.getText().toString())) {
            Edt_PhoneNumber.setError("لطفا شماره تلفن را وارد کنید");
            return;
        }

        ir.madresse.Tools.Validation validation = new ir.madresse.Tools.Validation();
        if (validation.ValidPhoneNumber(Edt_PhoneNumber.getText().toString())){

            ArrayList<String> Keys = new ArrayList<String>();
            ArrayList<String> Values = new ArrayList<String>();

            Keys.add("phone");
            Values.add(Edt_PhoneNumber.getText().toString());

            final ProgressDialog loading = ProgressDialog.show(Login.this, "در حال ارتباط با سرور", "لطفا صبر کنید", false, false);

            request_json.sendrequest(Login.this, App.SERVER_ADDRESS + "user/sendCode", Keys, Values, Request.Method.POST, new request_json.GetJson() {
                @Override
                public void onSuccess(String result) {

                    loading.dismiss();
                    Toast.makeText(Login.this, "کد تایید برای شما از طریق اس ام اس ارسال شد", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Validation.class));
                    finish();



                }

                @Override
                public void onError(String result, int errCode) {

                    loading.dismiss();
                    Toast.makeText(Login.this, "Erorr"+errCode, Toast.LENGTH_SHORT).show();

                }

            });
        }else {
            Edt_PhoneNumber.setError("شماره تلفن اشتباه است");
        }

    }




}



