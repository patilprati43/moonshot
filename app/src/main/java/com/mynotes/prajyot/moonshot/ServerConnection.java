//package com.mynotes.prajyot.moonshot;
//import android.util.Log;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.HttpHostConnectException;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;
//import java.io.IOException;
//import java.io.InputStream;
//public class ServerConnection {
//
//    public static HttpClient httpClient;
//    public static HttpContext localContext;
//    public static String url = "";
//
//    public static HttpClient getHttpClient() {
//        if (httpClient == null) {
//            httpClient = new DefaultHttpClient();
//        }
//        return httpClient;
//    }
//
//    public static void setHttpClient(HttpClient httpClient) {
//        ServerConnection.httpClient = httpClient;
//    }
//
//    public static HttpContext getLocalContext() {
//        if (localContext == null)
//            localContext = new BasicHttpContext();
//        return localContext;
//    }
//
//    public static void setLocalContext(HttpContext localContext) {
//        ServerConnection.localContext = localContext;
//    }
//
//
//
//    public static String getASCIIContentFromEntity(HttpEntity entity) {
//
//        InputStream in = null;
//        StringBuffer out = null;
//        try {
//            in = entity.getContent();
//            out = new StringBuffer();
//            int n = 1;
//            while (n > 0) {
//                byte[] b = new byte[4096];
//                n = in.read(b);
//                if (n > 0)
//                    out.append(new String(b, 0, n));
//            }
//        } catch (IllegalStateException ie) {
//            ie.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } catch (OutOfMemoryError oom) {
//            oom.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                in.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NullPointerException npe) {
//                npe.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return out.toString();
//    }
//
//    public static String executePost1(String param, String urlPath) {
//        httpClient = getHttpClient();
//        localContext = getLocalContext();
//        HttpEntity entity;
//        String entityString = "";
//        HttpPost httpPost = new HttpPost("http://107.23.14.180:204"+ urlPath);
//        Log.e("urlpath",httpPost+"");
//        try {
//
//            StringEntity stringEntity = new StringEntity(param);
//            stringEntity.setContentType("application/json");
//
//            httpPost.setEntity(stringEntity);
//
//            HttpResponse response = httpClient.execute(httpPost, localContext);
//            entity = response.getEntity();
//            entityString = getASCIIContentFromEntity(entity);
//
//        } catch (HttpHostConnectException hhce) {
//            hhce.printStackTrace();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return entityString;
//
//    }
//
//
//
//
//}
//
