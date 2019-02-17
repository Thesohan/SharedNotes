package com.shared.thesohankathait.classes.Fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shared.thesohankathait.classes.Adapter.ChatMessageAdapter;
import com.shared.thesohankathait.classes.model.ChatMessage;
import com.shared.thesohankathait.notices.R;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;

public class ChatBot extends DialogFragment {

    private Context context;
    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    private ProgressBar chatBotProgressbar;

    //First let’s include the class Bot from our ALICE bot JAR package in this fragment
    public Bot bot;
    public static Chat chat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        View view = inflater.inflate(R.layout.chatbot, container, false);
        context = getContext();


        chatBotProgressbar=view.findViewById(R.id.chatBotProgressbar);
        mListView = view.findViewById(R.id.listView);
        mButtonSend = view.findViewById(R.id.btn_send);
        mEditTextMessage = view.findViewById(R.id.et_message);
        mImageView = view.findViewById(R.id.iv_image);
        mAdapter = new ChatMessageAdapter(context, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);


        mEditTextMessage.requestFocus();
//        //code for sending the message
//        mButtonSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = mEditTextMessage.getText().toString();
//                sendMessage(message);
//                mEditTextMessage.setText("");
//                mListView.setSelection(mAdapter.getCount() - 1);
//            }
//        });
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                //bot
                String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
                mimicOtherMessage(response);
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                checkSdCardAvailability();
            }
        };

        handler.post(runnable);
    }

    private void checkSdCardAvailability() {

        //checking SD card availablility
        boolean a = isSDCARDAvailable();
//receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/Sohan/bots/Sohan");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Sohan")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Sohan/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Sohan/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/Sohan";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();

        //Assign the AIML files to bot for processing
        bot = new Bot("Sohan", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        chatBotProgressbar.setVisibility(View.GONE);

        //  String[] args = null;
    //    mainFunction(args);

    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        //respond as Helloworld
        //mimicOtherMessage("HelloWorld");
    }

    private void mimicOtherMessage(String message) {
        URL url = null;
        String urlPrefix="https://";
        String urlPostfix="</search></oob>";
            try {
                if (message.contains(urlPrefix)) {
                    url = new URL(message.substring(message.indexOf(urlPrefix), message.indexOf("\"/>")));
                    message = message.substring(0, message.indexOf(urlPrefix));
                }
            }
            catch (Exception e) {
            }
            finally {
                try {
                    if(url==null){
                        urlPrefix="<oob><search>";
                    String str = message.substring(message.indexOf(urlPrefix)+urlPrefix.length(), message.indexOf(urlPostfix));
                    if (str.length() != 0) {
                        url = new URL("https://google.com/search?q=" + str);
                        message = message.substring(0, message.indexOf(urlPrefix));
                    }
                }
            } catch (Exception e) {

            } finally {
                    ChatMessage chatMessage;
                    if(url==null) {
                        chatMessage = new ChatMessage(message, false, false);
                    }else {
                        chatMessage = new ChatMessage(message, false, true);
                        chatMessage.setUrl(url);
                    }
                mAdapter.add(chatMessage);
                Toast.makeText(context, "" + url, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);
        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }


    public static ChatBot newInstance() {
        
        Bundle args = new Bundle();
        ChatBot fragment = new ChatBot();
        fragment.setArguments(args);
        return fragment;
    }


    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    //Request and response of user and the bot
//    public static void mainFunction (String[] args) {
//        MagicBooleans.trace_mode = false;
//        System.out.println("trace mode = " + MagicBooleans.trace_mode);
//        Graphmaster.enableShortCuts = true;
//        Timer timer = new Timer();
//        String request = "Hello.";
//        String response = chat.multisentenceRespond(request);
//
//        System.out.println("Human: "+request);
//        System.out.println("Robot: " + response);
  //  }

}