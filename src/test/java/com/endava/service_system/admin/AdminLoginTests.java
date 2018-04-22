package com.endava.service_system.admin;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AdminLoginTests {
    private final static String HOST="http://localhost:8080";

    @Test
    public void testLoginWithoutBody() throws IOException {
        HttpClient instance =
                HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpResponse response = instance.execute(new HttpPost(HOST+"/admin/login"));

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        response.getEntity().toString().contains("class=\"alert alert-danger\"");
        assertTrue(bodyHas(response,"class=\"alert alert-danger\""));
    }

    @Test
    public void testLoginWithWrongCredentials() throws IOException {
        HttpClient instance =
                HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpPost post=new HttpPost(HOST+"/admin/login");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "John"));
        params.add(new BasicNameValuePair("password", "pass"));
        post.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = instance.execute(post);

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        assertTrue(bodyHas(response,"class=\"alert alert-danger\""));
    }

    @Test
    public void testLoginWithRightCredentials() throws IOException {
        HttpClient instance =
                HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpPost post=new HttpPost(HOST+"/admin/login");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "admin"));
        params.add(new BasicNameValuePair("password", "1qa2ws3ed"));
        post.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = instance.execute(post);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        //CHECKS if is admin panel view;
        assertTrue(bodyHas(response,"<title>Admin Panel</title>"));
//        response.getEntity().toString().contains("class=\"alert alert-danger\"");
    }
    @Test
    public void testLoginWithoutBody2() throws IOException {
        HttpClient instance =
                HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpResponse response = instance.execute(new HttpPost(HOST+"/admin/login"));

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        response.getEntity().toString().contains("class=\"alert alert-danger\"");
        assertTrue(bodyHas(response,"class=\"alert alert-danger\""));
    }

    @Test
    public void testLoginWithoutBody3() throws IOException {
        HttpClient instance =
                HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpResponse response = instance.execute(new HttpPost(HOST+"/admin/login"));

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        response.getEntity().toString().contains("class=\"alert alert-danger\"");
        assertTrue(bodyHas(response,"class=\"alert alert-danger\""));
    }

    private String getString(InputStream inputStream){
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textBuilder.toString();
    }

    private boolean bodyHas(HttpResponse response,String words) throws IOException {
        String htmlContent=getString(response.getEntity().getContent());
        return htmlContent.contains(words);
    }

}
