package com.umangSRTC.thesohankathait.classes.Utill;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.widget.Toast;

public class PdfDownloadTask {

    private DownloadManager downloadManager;
    private Context context;
    public static long downloadReference;
    private String url, title, school;

    public PdfDownloadTask(Context context, String url, String title, String school) {
        this.context = context;
        this.url = url;
        this.title = title;
        this.school = school;
        downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    }
    //Download Data from URL using Android Download Manager
    //DownloadData() function will be used to download data from internet.

    public void DownloadData() {

        if(CheckNetwork.isNetworkAvailable(context)) {

            Toast.makeText(context, "Downloadinig started...", Toast.LENGTH_LONG).show();

//            new CountDownTimer(5000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
//
//                    Toast.makeText(context, "File Location will be:\n Downloads/umang/"+school+"/"+title, Toast.LENGTH_SHORT).show();
//                }
//            }.start();

            //since download manager takes a uri not a url
            Uri uri = Uri.parse(url);

            //converting url into uri
//        String mimeType = mContext.getContentResolver().getType(uri);
            // Log.i("mimeType:",mimeType);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            // configure notification
            request.setDescription(title + " -" + school)
                    .setTitle(title);


            // set the download path
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/Umang/" + school +
                    "/", title + ".pdf");
            //In the above code we can make some changes. Instead of appending .pdf with title we should not append anything.

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


            // enqueue for download execute in a separate thread
            downloadReference = downloadManager.enqueue(request);

        }
        else{
            Toast.makeText(context, "Please check your internet Connection and try again", Toast.LENGTH_LONG).show();
        }
    }


}
