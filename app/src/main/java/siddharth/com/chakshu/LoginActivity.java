package siddharth.com.chakshu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by siddharthsingh on 12/06/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private TextView mSignupBtn;
    private Button mLoginBtn;
    private EditText mPhoneView, mPasswordView;
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
//            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(i);
//            finish();
            new RequestTask().execute("http://192.168.1.35:8001/loguser/?phone="+mPhoneView.getText()+"&password="+mPasswordView.getText());
        }
        else if(v.getId()==R.id.link_signup)
        {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
            finish();
        }
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
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = "lolz";
            Log.d("url", uri[0]);
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                Log.d("serversentdata", statusLine.toString());
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);
            //Do anything with response..
            Log.d("serever data", result);
        }
    }
}
