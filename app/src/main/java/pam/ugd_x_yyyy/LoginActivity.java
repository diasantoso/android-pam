package pam.ugd_x_yyyy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin,btnDaftar;
    TextView txtUsername,txtPassword;

    private static final String URL_LOGIN="http://modulpushnotif.esy.es/pushnotif/pengguna_controller/read.php";
    private static final String URL_SIGNUP="http://modulpushnotif.esy.es/pushnotif/pengguna_controller/create.php";
    private static final String TAG_PESAN="message";
    private static final String TAG_HASIL="result";
    JSONParser jParser = new JSONParser();
    JSONArray jsonarray = null;

    private SharedPreferences sharedpreferences;
    private final String name="myAccount";
    private static final int mode = Activity.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences=getSharedPreferences(name, mode);

        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnDaftar=(Button) findViewById(R.id.btnDaftar);
        txtUsername=(TextView) findViewById(R.id.txtUsername);
        txtPassword=(TextView) findViewById(R.id.txtPassword);

        if(sharedpreferences.getString("status","").equals("logged in")){
            finish();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsername.getText().toString().isEmpty()&&!txtPassword.getText().toString().isEmpty()){
                    new LoginJSON(txtUsername.getText().toString(),txtPassword.getText().toString()).execute();
                }
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsername.getText().toString().isEmpty()&&!txtPassword.getText().toString().isEmpty()){
                    new SignUpJSON(txtUsername.getText().toString(),txtPassword.getText().toString()).execute();
                }
            }
        });
    }

    class SignUpJSON extends AsyncTask<String,String,String> {
        String sukses = "";
        String username,password;
        ProgressDialog dialog;

        public SignUpJSON(String username, String password) {
            this.username=username;
            this.password=password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Mohon Tunggu...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            try {
                JSONObject json = jParser.makeHttpRequest(URL_SIGNUP, "GET",params);
                Log.d("","--->"+json.toString());

                if (json != null) {
                    sukses = json.getString(TAG_PESAN);
                    if (sukses.equalsIgnoreCase("berhasil")) {
                        Log.d("", json.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            if(sukses.equalsIgnoreCase("berhasil")){
                Toast.makeText(getApplicationContext(),"Daftar berhasil XD", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(),"Daftar gagal :(", Toast.LENGTH_LONG).show();
        }
    }

    class LoginJSON extends AsyncTask<String,String,String> {
        String sukses = "";
        String username,password;

        public LoginJSON(String username, String password) {
            this.username=username;
            this.password=password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            try {
                JSONObject json = jParser.makeHttpRequest(URL_LOGIN, "GET",params);
                Log.d("","--->"+json.toString());

                if (json != null) {
                    sukses = json.getString(TAG_PESAN);
                    if (sukses.equalsIgnoreCase("berhasil")) {
                        Log.d("", json.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(sukses.equalsIgnoreCase("berhasil")){
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("status", "logged in");
                editor.putString("username", username);
                editor.putString("password",password);
                editor.apply();
                finish();

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        }
    }
}
