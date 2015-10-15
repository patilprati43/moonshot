package com.mynotes.prajyot.moonshot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import com.mynotes.prajyot.moonshot.Login.LoginObject;
import com.mynotes.prajyot.moonshot.UserDetails.CheckConnection;
import com.mynotes.prajyot.moonshot.UserDetails.UserData;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class WelcomeUser extends AppCompatActivity {
   Toolbar toolbar;
    RelativeLayout rllogout;
    TextView usernm;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usernm= (TextView) findViewById(R.id.usernm);
        DrawerFragment drawerFragment = (DrawerFragment)getSupportFragmentManager().findFragmentById(R.id.drawer);
        drawerFragment.setUp((DrawerLayout)findViewById(R.id.drawer_layout),toolbar);
        rllogout= (RelativeLayout) findViewById(R.id.rllogout);
        rllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });
        new loginAsync().execute();
    }
    public void showList(View v)
    {
        Intent employeeActivity=new Intent(WelcomeUser.this,EmployeeListActivity.class);
        startActivity(employeeActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_welcome_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.action_logout:
            {
                logOutUser();
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
    public void logOutUser()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(WelcomeUser.this).setMessage("Are you sure to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserData.setUserName(getApplicationContext(),"");
                        UserData.setPassword(getApplicationContext(),"");
                        Intent exitApp=new Intent(getApplicationContext(), MainActivity.class);
                        exitApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        exitApp.putExtra("EXIT", "yes");
                        startActivity(exitApp);
                        //code for app exit
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    public  class loginAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="";
        String response = null,Username,Password;
        LoginObject employeeResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(WelcomeUser.this, "", "");
//            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    Username = UserData.getUserName(getApplicationContext());
                    Password = UserData.getPassword(getApplicationContext());
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx?Username=" + Username + "&Password=" + Password;
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    ArrayList<NameValuePair> header = new ArrayList<NameValuePair>();
                    header.add(new BasicNameValuePair("Referer", "http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx"));
                    response = CustomHttpClient.giveResponse(url, postParam, header);
                    response = response.toString();
                    employeeResult = new Gson().fromJson(response, LoginObject.class);
                }else
                {
                    connectedOrNot="error";
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }
        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
                //progressDialog.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (employeeResult!=null) {
                        if (employeeResult.getResult().equalsIgnoreCase("valid")) {
                            JSONObject obj=new JSONObject(response);
                            JSONArray usrDetails=obj.getJSONArray("fence");
                            JSONObject usrname = usrDetails.getJSONObject(0);
                            String username = usrname.getString("Full_x0020_Name");
                            usernm.setText(username);
                            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                            TextView area = (TextView) drawerLayout.findViewById(R.id.user);
                            area.setText(username);
                           // new getUserDetail().execute();

                        }else {
                            Intent employeeActivity = new Intent(WelcomeUser.this, MainActivity.class);
                            startActivity(employeeActivity);
                            UserData.setUserName(getApplicationContext(), "");
                            UserData.setPassword(getApplicationContext(),"");
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
