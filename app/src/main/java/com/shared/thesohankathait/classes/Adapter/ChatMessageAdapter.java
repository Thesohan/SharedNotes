package com.shared.thesohankathait.classes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shared.thesohankathait.classes.model.ChatMessage;
import com.shared.thesohankathait.notices.R;

import java.net.URL;
import java.util.List;


public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private  Context context;
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.my_messages, data);
        this.context=context;
    }
    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);
        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return MY_IMAGE;
        else return OTHER_IMAGE;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_messages, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_messages, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == MY_IMAGE) {

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_image, parent, false);
            WebView webView=convertView.findViewById(R.id.myWebView);
            showWebView(webView,getItem(position).getUrl());
           // Log.d("url",getItem(position).getUrl().toString());
            TextView textView=convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }
        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView(WebView webView, URL url) {

        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.loadUrl(String.valueOf(url));
            //progressBar.setVisibility(View.GONE);
            //to stop firing intent to chrome
//            webView.setWebViewClient(new WebViewClient() {
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }});
        }
        catch (Exception e){
            Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
