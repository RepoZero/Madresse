package ir.madresse.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.madresse.Adapter.ExpandableListAdapter;
import ir.madresse.App;
import ir.madresse.R;
import ir.madresse.Storage.SharedPreferencesConnector;
import ir.madresse.Volley.request_json;

import static ir.madresse.App.DB;
import static ir.madresse.App.SERVER_ADDRESS;

public class SampleQuestion extends AppCompatActivity {


    @BindView(R.id.ExpandableListView)protected ExpandableListView expandableListView;

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader = new ArrayList<String>();;
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_question);
        this.setTitle("نمونه سوالات");
        ButterKnife.bind(this);


        getSampleQuestionFromServer();




        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String accessToken = SharedPreferencesConnector.readString(SampleQuestion.this,"accessToken","");
                String url = SERVER_ADDRESS+"book/exam/:"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)+"?accessToken=:"+accessToken;

                Intent intent =  new Intent(SampleQuestion.this, PDFReader.class);
                intent.putExtra("URL",url);
                startActivity(intent);

                return false;
            }
        });



    }




    void getSampleQuestionFromServer(){


        ArrayList<String> Keys = new ArrayList<String>();
        ArrayList<String> Values = new ArrayList<String>();


        String accessToken = SharedPreferencesConnector.readString(SampleQuestion.this,"accessToken","");
        String grade = SharedPreferencesConnector.readString(SampleQuestion.this,"grade","");

        Keys.add("accessToken");
        Values.add(accessToken);

        Keys.add("grade");
        Values.add(grade);

        request_json.sendrequest(SampleQuestion.this, SERVER_ADDRESS + "book/examByGrade",Keys,Values, Request.Method.POST, new request_json.GetJson() {
            @Override
            public void onSuccess(String result) {



                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayBooks = jsonObject.getJSONArray("books");

                    for(int i=0 ; i<jsonArrayBooks.length();i++){
                        listDataHeader.add(getBookNameFromDB((String) jsonArrayBooks.get(i)));
                    }

                    JSONObject jsonObjcetExams = jsonObject.getJSONObject("exams");


                    for (int i =0 ; i<jsonArrayBooks.length();i++){
                        JSONArray jsonArray = jsonObjcetExams.getJSONArray((String) jsonArrayBooks.get(i));

                        List<String> list = new ArrayList<String>();
                        for(int j=0 ; j<jsonArray.length();j++ ){
                            list.add((String) jsonArray.get(j));
                        }

                        listDataChild.put(getBookNameFromDB((String) jsonArrayBooks.get(i)), list);



                    }


                    listAdapter = new ExpandableListAdapter(SampleQuestion.this, listDataHeader, listDataChild);

                    // setting list adapter
                    expandableListView.setAdapter(listAdapter);






                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String result, int errCode) {

                Toast.makeText(SampleQuestion.this, "Error : "+errCode, Toast.LENGTH_SHORT).show();

            }
        });



        }

    String getBookNameFromDB(String bookID){


        String DeleteQuery = "Select * FROM Books WHERE BookId=\""+bookID+"\"";
        Cursor cursor = DB.rawQuery(DeleteQuery, null);
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndex("BookName"));
    }

}
