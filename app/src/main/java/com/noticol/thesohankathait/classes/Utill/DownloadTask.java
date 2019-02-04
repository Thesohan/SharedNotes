package com.noticol.thesohankathait.classes.Utill;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class DownloadTask {

    private DownloadManager downloadManager;
    private Context context;
    public static long downloadReference;
    private String url, title, school,fileExtension;

    public DownloadTask(Context context, String url, String title, String school, String fileExtension) {
        this.context = context;
        this.url = url;
        this.title = title;
        this.school = school;
        this.fileExtension=fileExtension;
        downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }
    //when download is complete this on Receive method is called
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadReference == id && validDownload(downloadReference)) {
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Download failed!", Toast.LENGTH_SHORT).show();
            }
        }
    };


    //Download Data from URL using Android Download Manager
    //DownloadData() function will be used to download data from internet.

    public void DownloadData() {

        if (CheckNetwork.isNetworkAvailable(context) && url!=null) {

            Toast.makeText(context, "Downloading started...", Toast.LENGTH_LONG).show();

            //since download manager takes a uri not a url
            Uri uri = Uri.parse(url);

            //converting url into uri
//        String mimeType = mContext.getContentResolver().getType(uri);
            // Log.i("mimeType:",mimeType);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            // configure notification
            request.setDescription(title + " -" + school)
                    .setTitle(title);


            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/Umang/" + school +
                    "/", title + "." + fileExtension);
            //In the above code we can make some changes. Instead of appending .pdf with title we should not append anything.

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


            // enqueue for download execute in a separate thread
            downloadReference = downloadManager.enqueue(request);

        } else {
            if (url==null) {
                Toast.makeText(context, "This notice don't have any file", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please check your internet Connection and try again", Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean validDownload(long downloadId) {

  //      Log.d("downloadstatusid","Checking download status for id: " + downloadId);

        //Verify if download is a success
        Cursor cursor= downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));

        if(cursor.moveToFirst()){
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

            if(status == DownloadManager.STATUS_SUCCESSFUL){
                return true; //Download is valid, celebrate
            }else{
                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
//                Log.d("correctornot", "Download not correct, status [" + status + "] reason [" + reason + "]");
                return false;
            }
        }
        return false;
    }

}
