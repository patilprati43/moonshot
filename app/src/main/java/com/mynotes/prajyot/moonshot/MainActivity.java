package com.mynotes.prajyot.moonshot;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import com.mynotes.prajyot.moonshot.Login.LoginObject;
import com.mynotes.prajyot.moonshot.Login.ResultLogin;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
   // String Username,Password;
    TextView usernm,password;
    static String Username,Password;
    public static ArrayList<ResultLogin> employeeList=new ArrayList<>();
    LoginObject employeeResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=MainActivity.this;
        usernm= (TextView) findViewById(R.id.usernm);
        password= (TextView) findViewById(R.id.password);
        Button login= (Button) findViewById(R.id.login);

    }
    public void LoginDone(View view)
    {
        Username=usernm.getText().toString().trim();
        Password=password.getText().toString().trim();
      //  new getData().execute();
        Intent employeeActivity = new Intent(MainActivity.this, WelcomeUser.class);
        startActivity(employeeActivity);

    }

    public void setdata()
    {
       for (int i=0;i<employeeResult.getFence().size();i++)
           employeeList.add(employeeResult.getFence().get(i));

    }

    public  class getData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="success";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "");
            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String url="http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx?Username="+Username+"&Password="+Password;
                ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                ArrayList<NameValuePair> header = new ArrayList<NameValuePair>();
                //ArrayList<ContentValues>
                header.add(new BasicNameValuePair("Referer","http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx"));
//                postParam.add(new BasicNameValuePair("Username",Username));
//                postParam.add(new BasicNameValuePair("Password",Password));
//                postParam.add(new BasicNameValuePair("Referer","http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx"));
              //  postParam.add(new BasicNameValuePair("ClientID","PGB"));
                response = CustomHttpClient.giveResponse(url,postParam,header);
                response = response.toString();
                Log.d("ItemList Response",response);
                employeeResult= new Gson().fromJson(response, LoginObject.class);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }
        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
                progressDialog.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (employeeResult!=null) {
                        if (employeeResult.getResult().equalsIgnoreCase("valid")) {
                            Log.e("resultGetCategory", response);
                            setdata();
                            Intent employeeActivity = new Intent(MainActivity.this, WelcomeUser.class);
                            startActivity(employeeActivity);
                        }else {
                            Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
