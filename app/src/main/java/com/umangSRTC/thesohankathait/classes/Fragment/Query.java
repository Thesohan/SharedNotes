package com.umangSRTC.thesohankathait.classes.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umangSRTC.thesohankathait.umang.R;
import com.umangSRTC.thesohankathait.classes.Utill.Admin;
import com.umangSRTC.thesohankathait.classes.Utill.Equals;
import com.umangSRTC.thesohankathait.classes.ViewHolders.QueryViewHolder;
import com.umangSRTC.thesohankathait.classes.model.Query_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Query extends Fragment {
    private RecyclerView queryRecyclerView;
    private ProgressBar queryProgressbar;
    private FirebaseRecyclerAdapter<Query_model,QueryViewHolder> firebaseRecyclerAdapter;
    private FloatingActionButton askQueryFloatingActionButton;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_query,container,false);

        context=getContext();
        queryRecyclerView=view.findViewById(R.id.queryRecyclerView);
        askQueryFloatingActionButton=view.findViewById(R.id.askQueryFloatingActionButton);

        queryProgressbar=view.findViewById(R.id.queryProgressbar);

        askQueryFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForQuestion();

            }
        });


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);//it will set the recycler view to show the elements in bottom up manner.
        linearLayoutManager.setStackFromEnd(true);//it will show the last element first.
        queryRecyclerView.setLayoutManager(linearLayoutManager);
        queryRecyclerView.setHasFixedSize(true);

        fetchQueryFromFirebase();



        //for displaying ads
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        return view;
    }


    private void fetchQueryFromFirebase() {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Query_model, QueryViewHolder>(Query_model.class,R.layout.query_row,QueryViewHolder.class,FirebaseDatabase.getInstance().getReference("Query")) {
            @Override
            protected void populateViewHolder(QueryViewHolder queryViewHolder, final Query_model query_model, int i) {


                queryProgressbar.setVisibility(View.GONE);

                if(Admin.CheckAdmin(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    queryViewHolder.replyButton.setVisibility(View.VISIBLE);

                    queryViewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showDeleteQueryDialog(query_model);
                            return true;
                        }
                    });

                }
                queryViewHolder.questionTextView.setText(query_model.getQuestion());
                queryViewHolder.answerTextView.setText(query_model.getAnswer());
                queryViewHolder.replyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showReplyDialog(query_model);
                    }
                });

            }
        };
        queryRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void showDeleteQueryDialog(final Query_model query_model) {

        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Delete Query")
                .setCancelable(false)
                .setMessage("Do you really want to delete this query?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteQueryFromFirebase(query_model);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    private void deleteQueryFromFirebase(final Query_model query_model) {

        FirebaseDatabase.getInstance().getReference("Query").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(Equals.BothEqual(query_model,dataSnapshot.getValue(Query_model.class))){
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(getContext(), "Query Deleted", Toast.LENGTH_SHORT).show();
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

    private void showDialogForQuestion() {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.question_layout,null,false);
        final EditText questionEditText=view.findViewById(R.id.questionEditText);
        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Ask Question")
                .setCancelable(false)
                .setMessage("Enter your question here....")
                .setView(view)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query_model query=new Query_model(questionEditText.getText().toString().trim(),"...");
                        pushQueryIntoFirebase(query);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",null)
                .show();


    }

    private void pushQueryIntoFirebase(Query_model query) {
        FirebaseDatabase.getInstance().getReference("Query").push().setValue(query).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showReplyDialog(final Query_model query_model) {

        View view=LayoutInflater.from(context).inflate(R.layout.question_reply_layout,null,false);
        final TextView questionTextView=view.findViewById(R.id.questionTextView);
        final EditText ansEditText=view.findViewById(R.id.ansEditText);
        questionTextView.setText(query_model.getQuestion());
        ansEditText.setText(query_model.getAnswer());
        AlertDialog builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Reply")
                .setCancelable(false)
                .setMessage("If you press submit your ans will be publish")
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query_model new_query=new Query_model(query_model.getQuestion(),ansEditText.getText().toString().trim());
                        updateQueryWithAns(query_model,new_query);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Back",null)
                .show();
    }

    private void updateQueryWithAns(final Query_model old_query_model, final Query_model new_query) {
    FirebaseDatabase.getInstance().getReference("Query").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot finalDataSnapshot:dataSnapshot.getChildren()){
                Query_model currentQuery=finalDataSnapshot.getValue(Query_model.class);
                if(Equals.BothEqual(currentQuery,old_query_model)){
                    finalDataSnapshot.getRef().setValue(new_query);
                    break;
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }


    public static Query newInstance() {
        
        Bundle args = new Bundle();
        
        Query fragment = new Query();
        fragment.setArguments(args);
        return fragment;
    }
}
