package com.mynotes.prajyot.moonshot.Login;

import android.util.Log;

import com.mynotes.prajyot.moonshot.beans.Values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class CustomHttpClient {
    public static final int HTTP_TIMEOUT = 2 * 60 * 1000; // milliseconds
    private static HttpClient mHttpClient;
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }
    public static String executeHttpPost(String url,ArrayList<NameValuePair> postParameters,ArrayList<NameValuePair> header)throws Exception{
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);

            if (header!=null&&header.size()>0)
            {
                for (int i=0;i<header.size();i++)
                {
                    request.addHeader(header.get(i).getName(),header.get(i).getValue());
                }
            }
           // request.addHeader("Referer","http://107.23.14.180:204/_layouts/omsca_app/GeoLogin.aspx");
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(""+e);
                }
            }
        }
    }
    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            result=result.trim();
            result= result.replaceAll("\\s+","");
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(""+e);
                }
            }
        }
    }
    public static String giveResponse(String url,ArrayList<NameValuePair> parameters,ArrayList<NameValuePair> headres){
        try{
            String response=executeHttpPost(url, parameters,headres);
            Log.e("response", response);
            return response;
        }catch(Exception e){
            System.out.println(""+e);
            return "error_con "+e;
        }
    }
}