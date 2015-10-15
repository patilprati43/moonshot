package com.mynotes.prajyot.moonshot;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Approval.Approval;
import com.mynotes.prajyot.moonshot.Approval.ApprovalList;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import com.mynotes.prajyot.moonshot.Login.LoginObject;
import com.mynotes.prajyot.moonshot.Login.ResultLogin;
import com.mynotes.prajyot.moonshot.UserDetails.CheckConnection;
import com.mynotes.prajyot.moonshot.UserDetails.UserData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class EmployeeListActivity extends AppCompatActivity {
    LinearLayout ll_approval_list,result_container,oops_container;
    Context context;
    static  String diryID,empID;
    public  ArrayList<ResultLogin> employeeList=new ArrayList<>();
    public  ArrayList<ApprovalList> approvalLists=new ArrayList<>();
    Approval approval;
    LoginObject employeeResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_list);
        context=EmployeeListActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.left);
        ll_approval_list= (LinearLayout) findViewById(R.id.ll_approval_list);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Employee list" + "</font>")));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.left);
        oops_container= (LinearLayout) findViewById(R.id.oops_container);
        result_container= (LinearLayout) findViewById(R.id.result_container);


    }

    @Override
    protected void onResume() {
        super.onResume();
        new getData().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return true;
    }
    public void setShow()
    {
        ll_approval_list.removeAllViews();
        Log.e("size",employeeList.size()+"");
        for (int i=0;i<employeeList.size();i++)
        {
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView ll_approval_layout = (CardView) inflater1.inflate(R.layout.approval_item, null);
            TextView employee_nm= (TextView)ll_approval_layout.findViewById(R.id.employee_nm);
            TextView startDate= (TextView) ll_approval_layout.findViewById(R.id.startDate);
            TextView endDate= (TextView) ll_approval_layout.findViewById(R.id.endDate);
            employee_nm.setText(employeeList.get(i).getEmployeeName()+"");
            startDate.setText(employeeList.get(i).getWeekstartdate()+"");
            endDate.setText(employeeList.get(i).getWeekenddate()+"");
            ll_approval_list.addView(ll_approval_layout);
            final int finalI = i;
            ll_approval_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    empID=employeeList.get(finalI).getEmployeeID();
                    diryID=employeeList.get(finalI).getDiaryMasterID();
                    //name=name.replaceAll(" ", "%20");
                    Intent employeeDetails=new Intent(EmployeeListActivity.this,RequestList.class);
                    employeeDetails.putExtra("empID",empID);
                    employeeDetails.putExtra("diryID",diryID);
                    startActivity(employeeDetails);
                    //new getEmployeeData().execute();
                }
            });
        }
    }
    public void setEmpdata()
    {
        employeeList.clear();
        for (int i=0;i<employeeResult.getFence().size();i++)
            employeeList.add(employeeResult.getFence().get(i));
    }

    public  class getData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="";
        String response = null;
        String Username,Password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EmployeeListActivity.this, "", "");
//            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    Username = UserData.getUserName(getApplicationContext());
                    Password = UserData.getPassword(getApplicationContext());
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/GeoHome.aspx";
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    ArrayList<NameValuePair> header = new ArrayList<NameValuePair>();
                    //header.add(new BasicNameValuePair("Referer","http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx"));
                    response = CustomHttpClient.giveResponse(url, postParam, null);
                    response = response.toString();
                    Log.d("ItemList Response", response);
                    employeeResult = new Gson().fromJson(response, LoginObject.class);
                }else
                {
                    connectedOrNot = "error";
                }
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
                            setEmpdata();
                            setShow();

                        }else {
                            result_container.setVisibility(View.GONE);
                            oops_container.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Server is not responding",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        result_container.setVisibility(View.GONE);
                        oops_container.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Server is not responding",Toast.LENGTH_LONG).show();
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
