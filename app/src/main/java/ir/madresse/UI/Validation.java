package ir.madresse.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.SQLiteDB.GetDataFromServer;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.Tools.Internet;
import ir.madresse.Volley.request_json;

public class Validation extends AppCompatActivity {

    @BindView(R.id.Validation_Edt_Code)protected EditText Edt_Code;
    @BindView(R.id.Validation_Btn_ok)protected Button Btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        ButterKnife.bind(this);

        Edt_Code.setTypeface(App.FONT_iran_sana_light);
        Btn_ok.setTypeface(App.FONT_iran_sana_light);






        Btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validation();
            }
        });


        ArrayList<String> Keys = new ArrayList<String>();
        ArrayList<String> Values = new ArrayList<String>();

        Keys.add("");
        Values.add("");

        request_json.sendrequest(Validation.this, App.SERVER_ADDRESS + "user/getCode", Keys, Values, Request.Method.POST, new request_json.GetJson() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(Validation.this, ""+result, Toast.LENGTH_SHORT).show();

                Log.e("VerficationCode",result);

            }

            @Override
            public void onError(String result, int errCode) {

                Toast.makeText(Validation.this, ""+result+errCode, Toast.LENGTH_SHORT).show();

            }
        });



    }

    void validation(){


        if(!Internet.isOnline(Validation.this)){
            Toast.makeText(this, "دسترسی به اینترنت خود را چک کنید", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(Edt_Code.getText().toString())) {
            Edt_Code.setError("کد را وارد کنید");
            return;
        }


         ArrayList<String> Keys = new ArrayList<String>();
         ArrayList<String> Values = new ArrayList<String>();


        Keys.add("code");
        Values.add(Edt_Code.getText().toString());

        final ProgressDialog loading = ProgressDialog.show(Validation.this, "در حال ارتباط با سرور", "لطفا صبر کنید", false, false);

        request_json.sendrequest(Validation.this, App.SERVER_ADDRESS + "user/login", Keys, Values, Request.Method.POST, new request_json.GetJson() {
            @Override
            public void onSuccess(String result) {




                try {
                    JSONObject j = new JSONObject(result);
                    if(j.has("isNewMember")){
                        Intent i =new Intent(Validation.this,Register.class) ;
                        i.putExtra("ID",j.getString("id"));

                        SharedPreferencesConnector.writeString(Validation.this,"accessToken",j.getString("accessToken"));

                        loading.dismiss();
                        startActivity(i);
                        finish();
                    }else{
                            SharedPreferencesConnector.writeString(Validation.this,"accessToken",j.getString("accessToken"));
                            GetDataFromServer.GetBooks(Validation.this,j.getString("grade"));

                            SharedPreferencesConnector.writeString(Validation.this,"grade",j.getString("grade"));

                            loading.dismiss();
                            startActivity(new Intent(Validation.this,Home.class));
                            finish();


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }



            }

            @Override
            public void onError(String result, int errCode) {

                loading.dismiss();
                Toast.makeText(Validation.this, "Erorr"+errCode, Toast.LENGTH_SHORT).show();

            }
        });





        }







}
