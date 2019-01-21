package com.example.ericgrehan.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ericgrehan.instagram.Models.Images;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity  {

    private MyAdapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;

    ImageView imageView;
    TextView textView;
    //ListView listView;
    TextView textViewMcount;
    TextView textViewFollows;
    TextView textViewFollowedBy;
    //ArrayList<String> imageUrl = new ArrayList<>();
    RecyclerView recyclerView;
    private ArrayList<Images> imageList;

    private RequestQueue mRequestQue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageList = new ArrayList<>();

        imageView = findViewById(R.id.myImageView2);
        textView = findViewById(R.id.myTextview2);
        //listView = findViewById(R.id.listView);
        textViewMcount = findViewById(R.id.myTextviewMediaCount);
        textViewFollows = findViewById(R.id.myTextviewFollows);
        textViewFollowedBy = findViewById(R.id.myTextviewFollowedBy);
        //RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRequestQue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        String myUserName = intent.getStringExtra("username");
        String proFilePic = intent.getStringExtra("profilepic");
        int mediaCount = intent.getIntExtra("media",0);
        String mediaCountString = Integer.toString(mediaCount);
        int follows = intent.getIntExtra("follows",0);
        String followsString = Integer.toString(follows);
        int followers = intent.getIntExtra("followed_by",0);
        String followersString = Integer.toString(followers);
        textView.setText(myUserName);
        Picasso.get().load(proFilePic).into(imageView);
        textViewMcount.setText(mediaCountString + " Posts");
        textViewFollows.setText(followsString + " Followers");
        textViewFollowedBy.setText(followersString + " Following");

        String token = intent.getStringExtra("token");

        parseJsonPic(token);
    }

    //private void getPics(String token) { new MainActivity.RequestInstagramApi().parseJsonPic(); }


    private void parseJsonPic(String token) {
        String url=InstaConsts.GET_USER_RECENTS + token;
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    for(int i = 0; i< jsonArray.length(); i++){
                        JSONObject link =  jsonArray.getJSONObject(i);

                        JSONObject images =  link.getJSONObject("images");
                        JSONObject res = images.getJSONObject("standard_resolution");
                        String url = res.getString("url");


                        imageList.add(new Images(url));
                    }

                    mAdapter = new MyAdapter(ProfileActivity.this,imageList);
                    recyclerView.setAdapter(mAdapter);

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

    public void logOutButton(View view) {
    }
}
