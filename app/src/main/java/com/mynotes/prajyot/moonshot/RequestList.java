package com.mynotes.prajyot.moonshot;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mynotes.prajyot.moonshot.Approval.Approval;
import com.mynotes.prajyot.moonshot.Approval.ApprovalList;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;
import com.mynotes.prajyot.moonshot.UserDetails.CheckConnection;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.math.BigDecimal;
import java.util.ArrayList;

public class RequestList extends AppCompatActivity {
    LinearLayout ll_employee_approval_list,result_container,oops_container;
    Context context;
    Button approve,resend;
    static String ID,resendDiaryId,resendDate,resendEmpID;
    String EMPID,DiaryMasterID;
    String empid,dairyId;
    public  ArrayList<ApprovalList> approvalLists=new ArrayList<>();
    Approval approval;
    int tag;
    ArrayList<Button> ApproveBtnList=new ArrayList<>();
    ArrayList<Button> ResendBtnList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emloyee_approval_request_layout);
        context=RequestList.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.left);
        result_container= (LinearLayout) findViewById(R.id.result_container);
        oops_container= (LinearLayout) findViewById(R.id.oops_container);
        empid=getIntent().getStringExtra("empID");
        dairyId=getIntent().getStringExtra("diryID");
        new getEmployeeData().execute();
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Pending approvals" + "</font>")));
        ll_employee_approval_list= (LinearLayout) findViewById(R.id.ll_employee_approval_list);
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
    public void approveAll(View view)
    {
        EMPID= approvalLists.get(0).getEmployee_x0020_ID();
        DiaryMasterID=approvalLists.get(0).getDiaryMasterID();
        new approveAllAsync().execute();
    }

    public void showData()
    {
        ll_employee_approval_list.removeAllViews();
        for (int i=0;i<approvalLists.size();i++)
        {
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView ll_approval_layout = (CardView) inflater1.inflate(R.layout.emloyee_approval_request, null);
            TextView assign_nm= (TextView) ll_approval_layout.findViewById(R.id.assign_nm);
            TextView date= (TextView) ll_approval_layout.findViewById(R.id.date);
            TextView task= (TextView) ll_approval_layout.findViewById(R.id.task);
            TextView intime= (TextView) ll_approval_layout.findViewById(R.id.intime);
            TextView outtime= (TextView) ll_approval_layout.findViewById(R.id.outtime);
            approve= (Button) ll_approval_layout.findViewById(R.id.approve);
            resend= (Button) ll_approval_layout.findViewById(R.id.resend);
            approve.setId(i);
            resend.setId(i);
            ApproveBtnList.add(approve);
            ResendBtnList.add(resend);
            ll_employee_approval_list.addView(ll_approval_layout);
            assign_nm.setText(approvalLists.get(i).getAssignment() + "");
            date.setText(approvalLists.get(i).getShowDate()+"");
            task.setText(approvalLists.get(i).getTask() + "");
            intime.setText(setTime(approvalLists.get(i).getIn_x0020_Time()) + "");
            outtime.setText(setTime(approvalLists.get(i).getOut_x0020_Time())+ "");

            final int finalI = i;

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ID=approvalLists.get(finalI).getID();
                    tag = (int) v.getId();
                    new getApproveEmployeeData().execute();
                }
            });
            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendDiaryId=approvalLists.get(finalI).getDiaryMasterID();
                    resendDate=approvalLists.get(finalI).getShowDate();
                    resendEmpID=approvalLists.get(finalI).getEmployee_x0020_ID();
                    ID=approvalLists.get(finalI).getID();
                    new resendEmployeeData().execute();
                }
            });

        }
    }

    public String setTime(String date)
    {
        String str = "";
        double time = 12;
        try {
            time = Double.parseDouble(date);
        } catch (Exception e) {
        }
        if (time < 12) {
            str = str +  String.format("%.2f",time) + " AM";
        } else if (time >= 12 && time < 13) {
            str = str +  String.format("%.2f",time) + " PM";
        } else if (time >= 13) {
            double t1 = Double.parseDouble((new BigDecimal(time + "").subtract(new BigDecimal("12")).toString()));
            str = str + String.format("%.2f",t1) + " PM";
        }
        return str;
    }
    public class approveAllAsync extends AsyncTask<String,Integer,String>
    {
        ProgressDialog progressDialog1;
        String connectedOrNot="";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog1 = ProgressDialog.show(RequestList.this, "", "");
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/AndroidAppAll.aspx?empid=" + EMPID + "&Diarymasterid=" + DiaryMasterID;
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    response = CustomHttpClient.giveResponse(url, postParam, null);
                    response = response.toString().trim();
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
                progressDialog1.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (response!=null) {
                        if (response.equalsIgnoreCase("1")) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Successfully approved", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        result_container.setVisibility(View.GONE);
                        oops_container.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Server is not responding!!", Toast.LENGTH_LONG).show();
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
    public  class getApproveEmployeeData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RequestList.this, "", "");
//            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/FenceEntry.aspx";
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    postParam.add(new BasicNameValuePair("ID", ID));
                    response = CustomHttpClient.giveResponse(url, postParam, null);
                    response = response.toString().trim();
                }else {
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
                    if (response.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Successfully approved", Toast.LENGTH_LONG).show();
                        ApproveBtnList.get(tag).setEnabled(false);
                        ApproveBtnList.get(tag).setText("Approved");
                        ResendBtnList.get(tag).setEnabled(false);
                        ApproveBtnList.get(tag).setBackgroundColor(Color.parseColor("#ffffff"));
                        ResendBtnList.get(tag).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_LONG).show();
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

    public  class resendEmployeeData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog2;
        String connectedOrNot="";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog2 = ProgressDialog.show(RequestList.this, "", "");
//            progressDialog2.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/AndroidResend.aspx";
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    postParam.add(new BasicNameValuePair("ID", ID));
                    postParam.add(new BasicNameValuePair("empid", resendEmpID));
                    postParam.add(new BasicNameValuePair("Diarymasteris", resendDiaryId));
                    postParam.add(new BasicNameValuePair("Date", resendDate));
                    response = CustomHttpClient.giveResponse(url, postParam, null);
                    response = response.toString().trim();
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
                progressDialog2.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (response.equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getApplicationContext(), "Successfully resend", Toast.LENGTH_LONG).show();
                        ApproveBtnList.get(tag).setEnabled(false);
                        ResendBtnList.get(tag).setText("sent for resend");
                        ResendBtnList.get(tag).setEnabled(false);
                        ApproveBtnList.get(tag).setBackgroundColor(Color.parseColor("#ffffff"));
                        ResendBtnList.get(tag).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_LONG).show();
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
    public void setdata()
    {
        for (int i=0;i<approval.getFence().size();i++)
        {
          approvalLists.add(approval.getFence().get(i));
        }
    }

    public  class getEmployeeData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RequestList.this, "", "");
            progressDialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    String url = "http://107.23.14.180:204/_layouts/omsca_app/GioTrackResults.aspx?EmpID=" + empid + "&Diarymasterid=" + dairyId;
                    ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                    response = CustomHttpClient.giveResponse(url, postParam, null);
                    response = response.toString();
                    approval = new Gson().fromJson(response, Approval.class);
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
                    setdata();
                    showData();
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
