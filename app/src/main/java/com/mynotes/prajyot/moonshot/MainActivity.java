package com.mynotes.prajyot.moonshot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import com.mynotes.prajyot.moonshot.Login.LoginObject;
import com.mynotes.prajyot.moonshot.UserDetails.CheckConnection;
import com.mynotes.prajyot.moonshot.UserDetails.UserData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
    TextView usernm,password;
    LinearLayout banner,loginscreen;
    static String Username,Password;
    LoginObject employeeResult;
    ImageView ediary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=MainActivity.this;
        usernm= (TextView) findViewById(R.id.usernm);
        password= (TextView) findViewById(R.id.password);
        Button login= (Button) findViewById(R.id.login);
        ediary= (ImageView) findViewById(R.id.ediary);
        banner= (LinearLayout) findViewById(R.id.banner);
        loginscreen= (LinearLayout) findViewById(R.id.loginscreen);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (UserData.getUserName(getApplicationContext())==""&& UserData.getPassword(getApplicationContext())=="") {
                                    banner.setVisibility(View.GONE);
                                    loginscreen.setVisibility(View.VISIBLE);
                                }else
                                {
                                    banner.setVisibility(View.GONE);
                                    Intent welcomeActivity=new Intent(getApplicationContext(),WelcomeUser.class);
                                    startActivity(welcomeActivity);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();



    }
    public void LoginDone(View view)
    {
        if (usernm.getText().toString().trim().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            Username=usernm.getText().toString().trim();
        }
       if (password.getText().toString().trim().length()==0)
       {
           Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
           return;
       }else
       {
           Password=password.getText().toString().trim();
       }
        new getData().execute();
    }
    public  class getData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot;
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "");
//            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.e("connection",new CheckConnection(getApplicationContext()).isConnectingToInternet()+"");
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot="success";
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx?Username=" + Username + "&Password=" + Password;
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    ArrayList<NameValuePair> header = new ArrayList<NameValuePair>();
                    header.add(new BasicNameValuePair("Referer", "http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx"));
                    response = CustomHttpClient.giveResponse(url, postParam, header);
                    response = response.toString();
                    Log.d("ItemList Response", response);
                    employeeResult = new Gson().fromJson(response, LoginObject.class);
                }else
                {
                    connectedOrNot="error";
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("connectedOrNot",connectedOrNot+"");

            return connectedOrNot;
        }
        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
                progressDialog.dismiss();
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (employeeResult!=null) {
                        if (employeeResult.getResult().equalsIgnoreCase("valid")) {
                            UserData.setUserName(context,Username);
                            UserData.setPassword(context,Password);
                            Log.e("resultGetCategory", response);
                            Intent employeeActivity = new Intent(MainActivity.this, WelcomeUser.class);
                            startActivity(employeeActivity);
                        }else {
                            Toast.makeText(getApplicationContext(),"User does not exist or user name or password is wrong",Toast.LENGTH_LONG).show();
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

