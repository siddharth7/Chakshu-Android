package siddharth.com.chakshu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
public class SignUpActivity extends Activity implements View.OnClickListener {

    private Button mSignupBtn;
    private EditText mPhoneView, mPasswordView, mEmailView, mNameview, mConfirmPasswordView;
    private String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSignupBtn = (Button)findViewById(R.id.btn_signup);
        mPhoneView = (EditText)findViewById(R.id.input_phone);
        mPasswordView = (EditText)findViewById(R.id.input_password);
        mConfirmPasswordView = (EditText)findViewById(R.id.input_confpassword);
        mEmailView = (EditText)findViewById(R.id.input_email);
        mNameview = (EditText)findViewById(R.id.input_name);
        mSignupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_signup)
        {
            if(mPhoneView.getText().toString().length()!=10)
            {
                    new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("Error")
                    .setMessage("Enter a valid phone number")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            }
            else if(mPasswordView.getText().toString().length()<5)
            {
                    new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("Error")
                    .setMessage("Minimum password length is 5")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            }
            else if(!mPasswordView.getText().toString().equals(mConfirmPasswordView.getText().toString()))
            {
                    new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("Error")
                    .setMessage("Password do not match")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                    Log.d("pas", mPasswordView.getText().toString());
                    Log.d("pas2", mConfirmPasswordView.getText().toString());

            }
            else if(mNameview.getText().toString().length()==0)
            {
                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Error")
                        .setMessage("Please enter name")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
            else if(mEmailView.getText().toString().length()==0)
            {
                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Error")
                        .setMessage("Please enter email")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else
            {
                new RequestTask().execute("http://192.168.56.74:8001/signupuser/");
            }
        }
    }
    public String POST(String url) {
        InputStream is = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", mPhoneView.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password", mPasswordView.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("email",mEmailView.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("first_name",mNameview.getText().toString()));
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

        ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
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
