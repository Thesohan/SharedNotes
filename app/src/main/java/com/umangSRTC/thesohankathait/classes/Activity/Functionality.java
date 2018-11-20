package com.umangSRTC.thesohankathait.classes.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.umangSRTC.thesohankathait.classes.Services.RequestService;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Adapter.ViewPagerAdapter;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static com.umangSRTC.thesohankathait.classes.Utill.DownloadTask.downloadReference;

public class Functionality extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   private TabLayout tabLayout;
   private ViewPager viewPager;


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    showAlertMessage();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void showAlertMessage() {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please allow the permission to use this app")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Permission")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        askForPermissions();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality);


        //starting services for admin


        if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Intent serviceIntent=new Intent(Functionality.this,RequestService.class);
            startService(serviceIntent);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getActionBar().setTitle("Umang");
        getSupportActionBar().setTitle("Umang");  // provide compatibility to all the versions
  //      getActionBar().setIcon(R.drawable.ic_launcher);
//        getSupportActionBar().setLogo(R.drawable.ic_launcher);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        tabLayout=findViewById(R.id.tabLayout);

        viewPager=findViewById(R.id.subscriptionViewPager);

        //First we will set the adapter to the viewPager
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);




        //And then we set the viewPager on the tabLayout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(this.getResources().getColor(R.color.colorAccent));

        //This below one is used to set the custom textview on tabLayout items.
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv=(TextView)LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            tv.setTypeface(Typeface);null
            tv.setTextColor(this.getResources().getColor(R.color.colorAccent));
            tabLayout.getTabAt(i).setCustomView(tv);

        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                  Toast.makeText(Functionality.this, ""+position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        navigationView.setCheckedItem(R.id.srtcNotification);
                        break;
                    case 1:
                        navigationView.setCheckedItem(R.id.upload);
                        break;
                    case 2:
                        navigationView.setCheckedItem(R.id.schools);
                        break;
                    case 3:
                        navigationView.setCheckedItem(R.id.aboutUmang);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if(!hasAllPermissions())
            askForPermissions();

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    //when download is complete this on Receive method is called
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadReference == id) {
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };




    private void askForPermissions()
    {
        String permissions[]={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE};
        if(Build.VERSION.SDK_INT>=23)
            requestPermissions(permissions,1);
    }


    private boolean hasAllPermissions()
    {
        if(Build.VERSION.SDK_INT>=23)
            return ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    &&ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                    &&ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_GRANTED;

        return true;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitAlert();
          //  super.onBackPressed();
        }
    }

    private void exitAlert() {
        AlertDialog builder=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Exit")
                .setCancelable(false)
                .setMessage("Do you realy want to exit")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_setting) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.srtcNotification) {
            // Handle the srtcNotification
            tabLayout.setScrollPosition(0,0f,false);
            viewPager.setCurrentItem(0,true);
        } else if (id == R.id.schools) {
            tabLayout.setScrollPosition(2,0f,false);
            viewPager.setCurrentItem(2,true);

        } else if (id == R.id.upload) {
            tabLayout.setScrollPosition(1,0f,false);
            viewPager.setCurrentItem(1,true);
        }  else if (id == R.id.share) {
            shareMyApp("https://play.google.com/store/apps/details?id=com.umangSRTC.thesohankathait.umang");

        } else if (id == R.id.feedback) {
            sendFeedbackViaMail();

        } else if (id == R.id.aboutUmang) {

            tabLayout.setScrollPosition(3,0f,false);
            viewPager.setCurrentItem(3,true);


        } else if (id == R.id.query) {
            sendIntent("Query");

        }  else if (id == R.id.request) {

            if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                sendIntent("Request");
                }
            else{
                Toast.makeText(this, "You are not an Admin", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.privacyPolicy) {
            sendIntent("Policy");

        } else if (id == R.id.logout) {
            showAlertDialog();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareMyApp(String appLink) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appLink);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void sendFeedbackViaMail() {
        String [] address={"adarshbhatt91@gmail.com","sohan.kathait@gmail.com"};
//If you want to ensure that your intent is handled only by an email app (and not other text messaging or social apps),
// then use the ACTION_SENDTO action and include the "mailto:" data scheme. For example:
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL,address);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Umang app");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void sendIntent(String fragmentName) {
        Intent requestIntent = new Intent(Functionality.this, Features.class);
        requestIntent.putExtra("FRAGMENT_NAME",fragmentName);
        startActivity(requestIntent);

    }

    private void showAlertDialog() {
        AlertDialog builder=new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor   editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        LoginManager.getInstance().logOut();

                        finish();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}
