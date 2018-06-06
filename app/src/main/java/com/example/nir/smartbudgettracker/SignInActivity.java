package com.example.anu.smartbudgettracker;

import android.app.AlertDialog;
import android.content.Context;
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

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final EditText edUsername= (EditText)  findViewById(R.id.edUsername);
        final EditText edPassword= (EditText)  findViewById(R.id.edPassword);
        final Button bSignIn= (Button) findViewById(R.id.bSignIn);
        final TextView signInLink= (TextView) findViewById(R.id.tvNewUser);

       signInLink.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent signInIntent= new Intent(SignInActivity.this, SignUpActivity.class);
               SignInActivity.this.startActivity(signInIntent);

           }
       });
        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edUsername.getText().toString();
                final String password = edPassword.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String email = jsonObject.getString("email");
                                String age = jsonObject.getString("age");
                                int uid=jsonObject.getInt("uid");
                                Intent intent = new Intent(SignInActivity.this, UserMainActivity.class);
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();
                                Log.i("sign in", Integer.toString(uid));
                                editor.putInt("uid", uid);
                                editor.commit();
                                SignInActivity.this.startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                builder.setMessage("Signin unsucessfull!!").setNegativeButton("retry", null).create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignInRequest signInRequest= new SignInRequest(username,password,responseListener);
                RequestQueue requestQueue= Volley.newRequestQueue(SignInActivity.this);
                requestQueue.add(signInRequest);


            }
        });
    }
}
