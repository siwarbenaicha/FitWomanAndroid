package com.sim.fitwoman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sim.fitwoman.R;
import com.sim.fitwoman.model.User;
import com.sim.fitwoman.service.MySingleton;
import com.sim.fitwoman.utils.WSadressIP;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
// EditText txtEmail, txtPwd;
 //Button btnLogin;
 //TextView txtSignUp;


 //logeD User



/*private LoginButton loginButton;
private CallbackManager callbackManager;
     String FBEmail = "email";
    String FBName = "email";
    String FBId = "id";*/





    EditText txtUserName,txtWeight, txtHeight ,txtAge ;
    ImageView imgNext;

   // String userEmail , userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }




        txtUserName = findViewById(R.id.MeditText900);
        txtWeight =findViewById(R.id.MeditText);
        txtHeight =findViewById(R.id.MeditText2);
        txtAge =findViewById(R.id.MeditText3);
        imgNext = findViewById(R.id.MimageView);


        imgNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!txtUserName.getText().toString().equals("") &&
                                !txtWeight.getText().toString().equals("") &&
                                !txtHeight.getText().toString().equals("") &&
                                !txtAge.getText().toString().equals("")) {
                            Float TFWeight = Float.valueOf(txtWeight.getText().toString());
                            Float TFHeight = Float.valueOf(txtHeight.getText().toString());
                            if(TFWeight>=25 && TFWeight<= 200 && TFHeight>=50 && TFHeight<=250){
                                final int random = new Random().nextInt(999);
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                                String formattedDate = df.format(c);
                                final String userEmail = Integer.toString(random)+txtUserName.getText().toString()+formattedDate;
                                goToImcResult(userEmail);
                            }else{
                                Toast.makeText(getApplicationContext(), "Your Body Data Is not Logic", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                        }
                    }});
    }

  /*

    public void InsertUserInDB(final String UserName, final String UserEmail , final String loginType, final String idid){
        final String   URL = "http://"+ WSadressIP.WSIP+"/FitWomanServices/addUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")) {

                    ////////////////////////////////////
                    Intent intent = new Intent("com.sim.fitwoman.IMC");
                    intent.putExtra("userName", UserName); //this 2 lines for pass the values to the next activity
                    intent.putExtra("userEmail", UserEmail);
                    intent.putExtra("photo", idid);
                    startActivity(intent);
                    ////////////////////
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"failed to login",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("name", UserName);
                params.put("email",UserEmail);
                params.put("logintype", loginType);
                params.put("photo", idid);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }*/

    void goToImcResult( final String Umail){

        final String   URL = "http://"+ WSadressIP.WSIP+"/FitWomanServices/AddFirst.php";





        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")) {




                    //SHARED PREF
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name",txtUserName.getText().toString());
                    editor.putString("email",Umail);
                    editor.putString("weight",txtWeight.getText().toString());
                    editor.putString("height",txtHeight.getText().toString());
                    editor.putString("age",txtAge.getText().toString());
                    //editor.putString("photo",userPhoto);
                    String StringBMI = getBMI(txtWeight.getText().toString(),txtHeight.getText().toString());

                    editor.putString("bmi",StringBMI);

                    editor.commit();
                    ////////
                    SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("activity_executed", true);
                    ed.commit();
                    //////////
                    Intent intent = new Intent("com.sim.fitwoman.Home");
                    intent.putExtra("userName", txtUserName.getText().toString()); //this 2 lines for pass the values to the next activity
                    intent.putExtra("userEmail", Umail);
                    startActivity(intent);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"You need Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");

                params.put("nom", txtUserName.getText().toString());




                params.put("email", Umail);

                params.put("lastWeight", txtWeight.getText().toString());
                params.put("height", txtHeight.getText().toString());
                params.put("age", txtAge.getText().toString());
                params.put("logintype", "AndroidApp");
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }
    public String getBMI(String TWeight,String THeight){
        Float FWeight = Float.valueOf(TWeight);
        Float FHeight = Float.valueOf(THeight);
        Float IMC = 10000 *(FWeight/(FHeight * FHeight));
        String Res="";
        if (IMC < 18.5){
            Res = "1";
        } else if(IMC > 18.5 && IMC < 25){
            Res = "2";
        } else if(IMC > 25 ){
            Res = "3";
        }
        return Res;
    }
}
