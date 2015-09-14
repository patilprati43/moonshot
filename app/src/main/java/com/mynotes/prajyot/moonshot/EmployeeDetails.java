package com.mynotes.prajyot.moonshot;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mynotes.prajyot.moonshot.Login.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class EmployeeDetails extends ActionBarActivity {
    LinearLayout ll_employee_approval_list;
    Context context;
    Button approve;
    static String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emloyee_approval_request_layout);
        context=EmployeeDetails.this;

        ll_employee_approval_list= (LinearLayout) findViewById(R.id.ll_employee_approval_list);
        for (int i=0;i<EmployeeListActivity.approvalLists.size();i++)
        {
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CardView ll_approval_layout = (CardView) inflater1.inflate(R.layout.emloyee_approval_request, null);
            TextView assign_nm= (TextView) ll_approval_layout.findViewById(R.id.assign_nm);
            TextView date= (TextView) ll_approval_layout.findViewById(R.id.date);
            TextView task= (TextView) ll_approval_layout.findViewById(R.id.task);
            TextView intime= (TextView) ll_approval_layout.findViewById(R.id.intime);
            TextView outtime= (TextView) ll_approval_layout.findViewById(R.id.outtime);
            approve= (Button) ll_approval_layout.findViewById(R.id.approve);
            ll_employee_approval_list.addView(ll_approval_layout);
            assign_nm.setText(EmployeeListActivity.approvalLists.get(i).getAssignment() + "");
            date.setText(EmployeeListActivity.approvalLists.get(i).getShowDate()+"");
            task.setText(EmployeeListActivity.approvalLists.get(i).getTask()+"");
            intime.setText(EmployeeListActivity.approvalLists.get(i).getIn_x0020_Time()+"");
            outtime.setText(EmployeeListActivity.approvalLists.get(i).getOut_x0020_Time() + "");
            approve.setTag(i);
            final int finalI = i;
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ID",EmployeeListActivity.approvalLists.get(finalI).getID()+"");
                    ID=EmployeeListActivity.approvalLists.get(finalI).getID();
                    Log.e("name", EmployeeListActivity.approvalLists.get(finalI).getAssignment() + "");
                    int val= (int) v.getTag();
                    ((Button)v).setText("Approved ");
                    ((Button)v).setEnabled(false);
                    new getEmployeeData().execute();
                }
            });

        }
    }
//    public void SubmitResponse(View view)
//    {
//
//    }
    public  class getEmployeeData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String connectedOrNot="success";
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EmployeeDetails.this, "", "");
            progressDialog.setCancelable(true);
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                String url="http://107.23.14.180:204/_layouts/omsca_app/FenceEntry.aspx";
                ArrayList<NameValuePair> postParam = new ArrayList<NameValuePair>();
                postParam.add(new BasicNameValuePair("ID",ID));
                response = CustomHttpClient.giveResponse(url,postParam,null);
                response = response.toString().trim();
                Log.d("ItemList Response",response);
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
                    if (response.equalsIgnoreCase("1"))
                    {
                        Log.d("success", response);

                        Toast.makeText(getApplicationContext(), "Successfully approved", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.d("failure",response);
                        Toast.makeText(getApplicationContext(), "N done", Toast.LENGTH_LONG).show();
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
