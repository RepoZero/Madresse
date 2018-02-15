package ir.madresse.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.madresse.R;

public class PDFReader extends AppCompatActivity  {

     PDFView pdfView ;

    ProgressDialog loading;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pdfreader);

        pdfView = (PDFView) findViewById(R.id.pdfView);

        String url = null;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            url = bundle.getString("URL");
        }

        new PdfFromUrl().execute(url);

        loading = ProgressDialog.show(PDFReader.this, "در حال بارگذاری از سرور", "لطفا صبر کنید", false, false);




    }




    class PdfFromUrl extends AsyncTask<String,Void,InputStream> implements OnLoadCompleteListener, OnPageChangeListener {
        @Override
        protected InputStream doInBackground(String... strings) {



            InputStream inputStream =null ;
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode()==200){

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            }
            catch (IOException e){

                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {



            pdfView.fromStream(inputStream).onLoad(this).onPageChange(this).load();


        }


        @Override
        public void loadComplete(int nbPages) {

            loading.dismiss();

        }

        @Override
        public void onPageChanged(int page, int pageCount) {

//            CToast.CustomTimeTosat(page+"", 200,PDFReader.this);

        }
    }







}
