package com.noticol.thesohankathait.notices.javas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.noticol.thesohankathait.notices.R;
import com.noticol.thesohankathait.notices.javas.Activity.Features;

import java.util.ArrayList;

public class SchoolsArrayAdapter extends ArrayAdapter {
    private ArrayList<String> schoolArrayList=new ArrayList<>();
    private Context context;
    private TextView schoolNameTextView;
    private ImageView schoolImageView;
    private TextDrawable textDrawable;
    private ColorGenerator colorGenerator;
    private ImageView pdfNoticeImageView;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.schools_custom_row_layout,parent,false);
        schoolNameTextView=view.findViewById(R.id.schoolNamesTextView);
        schoolImageView=view.findViewById(R.id.schoolsImages);
        pdfNoticeImageView=view.findViewById(R.id.noticeWithPdf);
        pdfNoticeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "pdfClicked", Toast.LENGTH_SHORT).show();
                Intent pdfFragmentIntent=new Intent(context,Features.class);
                pdfFragmentIntent.putExtra("FRAGMENT_NAME","PdfNotice");
                pdfFragmentIntent.putExtra("SCHOOL_NAME",schoolArrayList.get(position));
                context.startActivity(pdfFragmentIntent);
            }
        });



        colorGenerator = ColorGenerator.MATERIAL;//to generate random colors

        textDrawable = TextDrawable.builder()
                .buildRound("" + schoolArrayList.get(position).charAt(0), colorGenerator.getRandomColor());//setting first letter of the user name

        schoolImageView.setImageDrawable(textDrawable);
        schoolNameTextView.setText(schoolArrayList.get(position));

        return view;
    }

    @Override
    public int getCount() {
        return schoolArrayList.size();
    }

    public SchoolsArrayAdapter(Context context, ArrayList<String> schoolArrayList) {
        super(context,R.layout.schools_custom_row_layout,schoolArrayList);
        this.schoolArrayList=schoolArrayList;
        this.context=context;
    }
}
