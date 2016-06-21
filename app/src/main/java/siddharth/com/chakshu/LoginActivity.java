package siddharth.com.chakshu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by siddharthsingh on 12/06/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private TextView mSignupBtn;
    private Button mLoginBtn;
    private EditText mPhoneView, mPasswordView;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSignupBtn = (TextView)findViewById(R.id.link_signup);
        mLoginBtn = (Button)findViewById(R.id.btn_login);
        mPhoneView = (EditText)findViewById(R.id.input_phone);
        mPasswordView = (EditText)findViewById(R.id.input_password);
        mSignupBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_login)
        {
            if(mPhoneView.getText().toString().length()==10 && mPasswordView.getText().toString().length()>=5)
                new RequestTask().execute("http://192.168.56.74:8001/loguser/");
            else if(mPhoneView.getText().toString().length()!=10)
            {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error")
                        .setMessage("Enter a valid phone number")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(mPasswordView.getText().toString().length()<=5)
            {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error")
                        .setMessage("Minimum password length is 5")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }
        else if(v.getId()==R.id.link_signup)
        {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
            finish();
        }
    }
    public String POST(String url) {
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", mPhoneView.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password", mPasswordView.getText().toString()));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.e("HTTP", "Error in http connection " + e.toString());
        }
        return result;

    }
    class RequestTask extends AsyncTask<String, String, String> {

        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            // Show Progress dialog
            dialog.setMessage("Checking Server..");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... uri)
        {

            return POST(uri[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);
            //Do anything with response..
            Log.d("serever data", result);
        }
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
