package com.example.anu.smartbudgettracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText edFName= (EditText)  findViewById(R.id.edFName);
        final EditText edLName= (EditText)  findViewById(R.id.edLName);
        final EditText edEmail= (EditText)  findViewById(R.id.edLname);
        final EditText edAge= (EditText)  findViewById(R.id.edAge);
        final EditText edUsername= (EditText)  findViewById(R.id.edUsername);
        final EditText edPassword= (EditText)  findViewById(R.id.edPassword);
        final Button bSignUp= (Button) findViewById(R.id.bSignUp);

        final TextView signInLink= (TextView) findViewById(R.id.btnBackToSignIn);

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent= new Intent(SignUpActivity.this, SignInActivity.class);
                SignUpActivity.this.startActivity(signInIntent);

            }
        });

        if (bSignUp != null) {
            bSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String fName= edFName.getText().toString();
                    final String lName= edLName.getText().toString();
                    final String email= edEmail.getText().toString();
                    final int age= Integer.parseInt(edAge.getText().toString());
                    final String username= edUsername.getText().toString();
                    final String password= edPassword.getText().toString();
                    Response.Listener<String> responseListener= new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject= new JSONObject(response);
                                boolean success= jsonObject.getBoolean("success");
                                Log.i("SignUpActivity", "request string on server =" + jsonObject.getString("request"));
                                if (success) {

                                    Log.i("SignUpActivity", "in success");
                                    int uid=jsonObject.getInt("uid");
                                    Intent intent = new Intent(SignUpActivity.this, UserMainActivity.class);
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = settings.edit();
                                    Log.i("sign in", Integer.toString(uid));
                                    editor.putInt("uid", uid);
                                    editor.commit();
                                    SignUpActivity.this.startActivity(intent);
                                }else {
                                    Log.e("SignUpActivity", "in fail");
                                    AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
                                    builder.setMessage("SignUp unsuccessfull!!") .setNegativeButton("retry",null) .create() .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    Log.d("SignupActivity", fName);
                    Log.d("SignupActivity", lName);
                    Log.d("SignupActivity", email);

                    SignUpRequest signUpRequest=new SignUpRequest(fName,lName,email,age,username,password,responseListener);
                    RequestQueue requestQueue= Volley.newRequestQueue(SignUpActivity.this);
                    requestQueue.add(signUpRequest);

                }
            });
        }else{
            Log.e("SignupActivity", "Button bsignup is null");
        }
    }
}
