package com.mynotes.prajyot.moonshot;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Approval.Approval;
import com.mynotes.prajyot.moonshot.Approval.ApprovalList;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class EmployeeListActivity extends AppCompatActivity {
    LinearLayout ll_approval_list;
    Context context;
    static  String name,ID;
    public static ArrayList<ApprovalList> approvalLists=new ArrayList<>();
    Approval approval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_list);
        context=EmployeeListActivity.this;
        ll_approval_list= (LinearLayout) findViewById(R.id.ll_approval_list);
        ll_approval_list.removeAllViews();
        Log.e("size",MainActivity.employeeList.size()+"");
        for (int i=0;i<MainActivity.employeeList.size();i++)
        {
            Log.e("nm",MainActivity.employeeList.get(i).getEmpIDLookup_x003a_Full_x0020_Nam()+"");
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView ll_approval_layout = (CardView) inflater1.inflate(R.layout.approval_item, null);
            TextView employee_nm= (TextView)ll_approval_layout.findViewById(R.id.employee_nm);
            TextView startDate= (TextView) ll_approval_layout.findViewById(R.id.startDate);
            TextView endDate= (TextView) ll_approval_layout.findViewById(R.id.endDate);
            employee_nm.setText(MainActivity.employeeList.get(i).getEmpIDLookup_x003a_Full_x0020_Nam()+"");
            startDate.setText(MainActivity.employeeList.get(i).getWeekStartDateShow()+"");
            endDate.setText(MainActivity.employeeList.get(i).getWeekEndDateShow()+"");
            ll_approval_list.addView(ll_approval_layout);
            final int finalI = i;
            ll_approval_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ID=MainActivity.employeeList.get(finalI).getID();
                    name=MainActivity.employeeList.get(finalI).getEmpIDLookup_x003a_Full_x0020_Nam();
                   new getEmployeeData().execute();
                }
            });
        }
    }
    public void setdata()
    {
        for (int i=0;i<approval.getFence().size();i++)
        {
          approvalLists.add(approval.getFence().get(i));
        }
    }
    public  class getEmployeeData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="success";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EmployeeListActivity.this, "", "");
            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                String url="http://107.23.14.180:204/_layouts/omsca_app/GioTrackResults.aspx";
                ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                postParam.add(new BasicNameValuePair("EmpIDLookup_x003a_Full_x0020_Nam",name));
                postParam.add(new BasicNameValuePair("ID",ID));
                response = CustomHttpClient.giveResponse(url,postParam,null);
                response = response.toString();
                Log.d("ItemList Response",response);
                approval= new Gson().fromJson(response, Approval.class);
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
                    Log.e("resultGetCategory", response);
                    setdata();
                    Intent employeeDetails=new Intent(EmployeeListActivity.this,EmployeeDetails.class);
                    startActivity(employeeDetails);
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
