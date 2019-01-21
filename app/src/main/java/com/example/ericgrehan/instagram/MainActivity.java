package com.example.ericgrehan.instagram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.ericgrehan.instagram.Interfaces.AuthenticationListener;
import com.example.ericgrehan.instagram.Views.AuthenticationDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AuthenticationListener {

    Button button;
    ImageView imagview;
    TextView textView;
    private AuthenticationDialog aDialog;
    SharedPreferences sharedPreferences = null;
    String token = null;
    private RequestQueue mRequestQue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.myButton);
        imagview = findViewById(R.id.myImageView);
        textView = findViewById(R.id.myTextview);
        //check for token
        sharedPreferences = getSharedPreferences(InstaConsts.PreferenceName,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        if(token != null){
            button.setText("Logout");
        }else{
            button.setText("Login");
        }
        mRequestQue = Volley.newRequestQueue(this);
        getUserInfo(token);
        //getPics(token);
    }

    private void getUserInfo(String token) { new RequestInstagramApi().parseJsonUser();
    }



    public class RequestInstagramApi {

    private void parseJsonUser() {
        String url=InstaConsts.GET_USER_INFO + token;
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("data");
                    String userName = object.getString("username");
                    String profilePic = object.getString("profile_picture");

                    JSONObject objectCounts = object.getJSONObject("counts");
                    int mediaCount = objectCounts.getInt("media");
                    int follows = objectCounts.getInt("follows");
                    int followers = objectCounts.getInt("followed_by");

                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("profilepic", profilePic);
                    intent.putExtra("media",mediaCount);
                    intent.putExtra("follows",follows);
                    intent.putExtra("followed_by",followers);
                    intent.putExtra("token",token);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQue.add(request);
    }
    }


    @Override
    public void onCodeReceived(String authToken) {
        if (authToken == null)
            return;
            //Token saved in Shared prefs
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", authToken);
            editor.apply();
            token = authToken;
            button.setText("Logout");
            getUserInfo(token);
        }



    public void loginButton(View view) {
        if(token != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            button.setText("Login");
            token=null;
        }else {
            aDialog = new AuthenticationDialog(this, this);
            aDialog.setCancelable(true);
            aDialog.show();
        }
    }
}

