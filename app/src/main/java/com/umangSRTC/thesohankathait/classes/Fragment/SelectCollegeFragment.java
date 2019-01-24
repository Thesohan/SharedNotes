package com.umangSRTC.thesohankathait.classes.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umangSRTC.thesohankathait.classes.Activity.Functionality;
import com.umangSRTC.thesohankathait.classes.Activity.Login;
import com.umangSRTC.thesohankathait.classes.Utill.Initialisation;
import com.umangSRTC.thesohankathait.classes.ViewHolders.CollegeViewHolder;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.umang.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;

public class SelectCollegeFragment extends Fragment implements View.OnClickListener {
    private Button nextButton;
    private FloatingActionButton addNewCollegeFloatingActionButton;
    private ListView listView;
    private DatabaseReference databaseref;
    private ArrayAdapter adapter;
    private ArrayList<String> collegeList;
    private Context context;
    private String selectedCollege=null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.select_collage,container,false);
        context=getContext();
        collegeList=new ArrayList<>();

        fetchCollegesFromFirebase();
        nextButton=view.findViewById(R.id.nextButton);
        listView=view.findViewById(R.id.college_list);
        adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice,collegeList);
        listView.setAdapter(adapter);
        addNewCollegeFloatingActionButton=view.findViewById(R.id.addCollegeFloatingActionButton);
        nextButton.setOnClickListener(this);
        addNewCollegeFloatingActionButton.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCollege=collegeList.get(position);
                view.setSelected(true);
            }
        });

        return view;
    }

    private void moveToFunctionality() {

        //first we will move to the previous activity then we will finish that activity after moving to  functionality activity
        Intent intent=new Intent(context,Functionality.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    private void fetchCollegesFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Colleges").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!collegeList.contains(dataSnapshot.getValue().toString())){
                    collegeList.add(dataSnapshot.getValue().toString());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static SelectCollegeFragment newInstance() {

        Bundle args = new Bundle();

        SelectCollegeFragment fragment = new SelectCollegeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addCollegeFloatingActionButton:

                if(getFragmentManager().getBackStackEntryCount()!=0) {
                    getFragmentManager().popBackStack();
                }

                FragmentManager fragmentManager = getFragmentManager();
                RequestCollegeName requestCollegeName = RequestCollegeName.newInstance();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(android.R.id.content, requestCollegeName).addToBackStack("faf").commit();



            break;

            case R.id.nextButton:

                if(selectedCollege!=null){
                    Toast.makeText(context, ""+selectedCollege, Toast.LENGTH_SHORT).show();
                    sharedPreferences = context.getSharedPreferences("COLLEGE", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("SELECTEDCOLLEGE",selectedCollege);
                    editor.apply();
                    Initialisation.selectedCollege=selectedCollege;//for the first time when user select the college
                    moveToFunctionality();

                }

                else{
                    Toast.makeText(context, "Please select a college first!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
