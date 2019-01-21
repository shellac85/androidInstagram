package com.example.ericgrehan.instagram.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.ericgrehan.instagram.InstaConsts;
import com.example.ericgrehan.instagram.Interfaces.AuthenticationListener;
import com.example.ericgrehan.instagram.R;

public class AuthenticationDialog extends Dialog {

    private AuthenticationListener listener;
    private  Context context;
    private WebView webView;

    private final String url = InstaConsts.BASE_URL
            +"oauth/authorize/?client_id="
            +InstaConsts.INSTAGRAM_CLIENT_ID
            +"&redirect_uri="
            +InstaConsts.REDIRECT_URL
            +"&response_type=token"
            +"&display=touch&scope=public_content";

    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialogg);
        startWebview();
    }

    private void startWebview() {
        webView = findViewById(R.id.myWebView);

        webView.setWebViewClient(new WebViewClient(){

            String accessToken;
            boolean authComplete;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("#access_token=") && !authComplete){
                    Uri uri = Uri.parse(url);
                    accessToken = uri.getEncodedFragment();
                    accessToken = accessToken.substring(accessToken.lastIndexOf("=")+1);
                    Log.i("", "CODE : " + accessToken);
                    authComplete = true;
                    listener.onCodeReceived(accessToken);
                    dismiss();
                }else if(url.contains("?error")){
                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
                    //dismiss();
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


}
