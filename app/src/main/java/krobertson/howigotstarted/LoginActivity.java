/*
Source: https://www.androidtutorialpoint.com/androidwithphp/login-and-registration-form-in-android/

This class is responsible for handling the functionality of the login screen.
 */

package krobertson.howigotstarted;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://kevronr.netai.net/login.php";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnLinkSignup = (Button) findViewById(R.id.btn_link_signup);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(loginInputEmail.getText().toString(),
                        loginInputPassword.getText().toString());
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginUser(final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        //Creates new request using the login URL
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // Launch ChooseMajor.java
                        Intent intent = new Intent(
                                LoginActivity.this,
                                ChooseMajor.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Show error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}