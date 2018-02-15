package ir.madresse.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.Volley.request_json;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    @BindView(R.id.Home_Img_Library)protected ImageView Img_Library;
    @BindView(R.id.Home_Img_SampleQuestion)protected ImageView Img_SampleQuestion;
    @BindView(R.id.Home_Img_EducationEvents)protected ImageView Img_EducationEvents;



    @BindView(R.id.Home_Txt_Library)protected TextView Txt_Library;
    @BindView(R.id.Home_Txt_SampleQuestion)protected TextView Txt_SampleQuestion;
    @BindView(R.id.Home_Txt_EducationEvents)protected TextView Txt_EducationEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        SharedPreferencesConnector.writeBoolean(Home.this,"Login",true);

        Update_accessToken();





        Txt_Library.setTypeface(App.FONT_iran_sana_light);
        Txt_SampleQuestion.setTypeface(App.FONT_iran_sana_light);
        Txt_EducationEvents.setTypeface(App.FONT_iran_sana_light);



        Img_Library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(1);
            }
        });

        Txt_Library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(1);
            }
        });


        Img_SampleQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(2);
            }
        });

      Txt_SampleQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(2);
            }
        });


        Img_EducationEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(3);
            }
        });

        Txt_EducationEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(3);
            }
        });











        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }

    public void StartActivity(int activityCode){

        switch (activityCode){

            case 1 :

                startActivity(new Intent(Home.this,Books.class));

                break;

            case 2 :
                startActivity(new Intent(Home.this,SampleQuestion.class));
                break;

            case 3:
                startActivity(new Intent(Home.this,EducationalEvents.class));
                break;

        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Library) {

            StartActivity(1);

        } else if (id == R.id.nav_SampleQuestion) {

            StartActivity(2);

        } else if (id == R.id.nav_EducationalEvents) {

            StartActivity(3);

        } else if (id == R.id.nav_Help) {

        } else if (id == R.id.nav_Info) {

        } else if (id == R.id.nav_Exit) {


            AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
            builder1.setMessage("آیا به برنامه امتیاز می دهید .");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "بله حتما",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "نه فعلا",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        Update_accessToken();
        super.onResume();
    }

    void Update_accessToken(){


         ArrayList<String> Keys = new ArrayList<String>();
         ArrayList<String> Values = new ArrayList<String>();

         String accessToken = SharedPreferencesConnector.readString(Home.this,"accessToken","");


         Keys.add("accessToken");
         Values.add(accessToken);

         request_json.sendrequest(Home.this, App.SERVER_ADDRESS + "user/login", Keys, Values, Request.Method.POST, new request_json.GetJson() {
             @Override
             public void onSuccess(String result) {

                 try {
                     JSONObject j = new JSONObject(result);
                     if(j.has("accessToken")){
                         SharedPreferencesConnector.writeString(Home.this,"accessToken",j.getString("accessToken"));
                     }else {
                         SharedPreferencesConnector.delete(Home.this, "Login");
                         startActivity(new Intent(Home.this,Login.class));
                         finish();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }

             @Override
             public void onError(String result, int errCode) {

             }

         });
     }




}
