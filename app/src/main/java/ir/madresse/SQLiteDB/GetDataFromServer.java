package ir.madresse.SQLiteDB;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.madresse.App;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.UI.Login;
import ir.madresse.Volley.request_json;

import static ir.madresse.App.DB;

/**
 * Created by cloner on 8/13/17.
 */

public class GetDataFromServer {





   static public void GetBooks(final Context context,String grade){




        ArrayList<String> Keys = new ArrayList<String>();
        ArrayList<String> Values = new ArrayList<String>();

       String accessToken = SharedPreferencesConnector.readString(context,"accessToken","");


       Keys.add("accessToken");
       Values.add(accessToken);

       Keys.add("grade");
       Values.add(grade);



            final ProgressDialog loading = ProgressDialog.show(context, "درحال دریافت اطلاعات", "لطفا صبر کنید", false, false);

            request_json.sendrequest(context, App.SERVER_ADDRESS + "book/byGrade",Keys,Values, Request.Method.POST, new request_json.GetJson() {
            @Override
            public void onSuccess(String result) {



                ArrayList<String> BookId = new ArrayList<String>();
                ArrayList<String> BookName = new ArrayList<String>();
                ArrayList<String> BookImg = new ArrayList<String>();
                ArrayList<String> BookPdf = new ArrayList<String>();


                try {
                    JSONArray json = new JSONArray(result);
                    for (int i = 0; i < json.length(); i++) {

                        JSONObject e = json.getJSONObject(i);


                        BookId.add(e.getString("id"));
                        BookName.add(e.getString("name"));
                        BookImg.add(e.getString("cover"));
                        BookPdf.add(e.getString("file"));







                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String DeleteQuery = "DELETE FROM Books";
                Cursor cursor = DB.rawQuery(DeleteQuery, null);
                cursor.moveToFirst();

                for (int i = 0; i < BookName.size(); i++) {


                    String InsertQuery = "INSERT INTO Books(BookId,BookName,BookImage,BookPdf) VALUES (\""+BookId.get(i)+"\" ,'" + BookName.get(i) + "','" + BookImg.get(i) + "','" + BookPdf.get(i) + "')";
                    Cursor cursorAddBook = DB.rawQuery(InsertQuery, null);
                    cursorAddBook.moveToFirst();


                }

                loading.dismiss();

            }



            @Override
            public void onError(String result, int errCode) {


                loading.dismiss();

                Toast.makeText(context, "Get Book Error : "+errCode, Toast.LENGTH_SHORT).show();
            }
        });






    }



}
