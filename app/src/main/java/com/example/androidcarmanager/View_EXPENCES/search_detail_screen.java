package com.example.androidcarmanager.View_EXPENCES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.androidcarmanager.Database.Expence_DB;
import com.example.androidcarmanager.R;
import com.example.androidcarmanager.capture.Image_View_Screen;
import com.example.androidcarmanager.user_info.Login_Screen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class search_detail_screen extends AppCompatActivity {
    TextView searchTitle, searchDate, searchTime, searchOdometer,searchPrice;
    ImageView imageViewExpence;
    LinearLayout imageContainer;
    private DatabaseReference databaseReference1, databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    String vehicleId;

    String category;
    String index;
    ArrayList<String> indexList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail_screen);
        setTitle(Html.fromHtml("<font color='#3477e3'> Expence Detail</font>"));
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(search_detail_screen.this, Login_Screen.class));
        }
        vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key", "-1");

        Bundle intent=getIntent().getExtras();
        index = intent.getString("index");

        Log.d("bundle", index);


        searchTitle=(TextView) findViewById(R.id.searchtilte);
        searchDate=(TextView) findViewById(R.id.searchdate);
        searchTime=(TextView) findViewById(R.id.searchtime);
        searchOdometer=(TextView) findViewById(R.id.searchodometer);
        searchPrice=(TextView) findViewById(R.id.searchprice);
        imageContainer=(LinearLayout) findViewById(R.id.imagesParent);

        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/" + category);
        getData();
    }
    public void getData(){
        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        indexList.add(ds.child("Key").getValue(String.class));
                    }

                    String dbIndex=indexList.get(Integer.parseInt(index));
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/" + category);

                    databaseReference2.child(dbIndex).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot  dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Expence_DB expensesDB=dataSnapshot.getValue(Expence_DB.class);
                                searchTitle.setText(expensesDB.getExpenseTitle());
                                searchOdometer.setText(expensesDB.getOdometer().toString());
                                searchPrice.setText(expensesDB.getPrice().toString());

//                                searchTitle.setText(dataSnapshot.child("expenseTitle").getValue(String.class));
//                                  searchOdometer.setText(dataSnapshot.child("odometer").getValue(String.class));
//                                searchPrice.setText(dataSnapshot.child("price").getValue(String.class));

                                final Calendar date = Calendar.getInstance();
//                                date.setTimeInMillis(dataSnapshot.child("date").getValue(Long.class));
                                date.setTimeInMillis(expensesDB.getDate());
                                searchDate.setText(date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.MONTH)+"/"+date.get(Calendar.YEAR));

                                final Calendar time = Calendar.getInstance();
//                                time.setTimeInMillis(dataSnapshot.child("time").getValue(Long.class));
                                time.setTimeInMillis(expensesDB.getDate());
                                searchTime.setText(time.get(Calendar.HOUR_OF_DAY)+":"+time.get(Calendar.MINUTE)+":"+time.get(Calendar.MILLISECOND));

                                if(dataSnapshot.child("images").exists()){
                                    for (final DataSnapshot img : dataSnapshot.child("images").getChildren()){
                                        imageViewExpence=new ImageView(search_detail_screen.this);
                                        imageViewExpence.setLayoutParams(new ViewGroup.LayoutParams(250,350));
                                        imageViewExpence.setMaxHeight(350);
                                        imageViewExpence.setMaxWidth(250);
                                        imageViewExpence.setPadding(10,0,10,0);
                                        Glide.with(search_detail_screen.this)
                                                .load(img.getValue(String.class))
                                                .transition(DrawableTransitionOptions.withCrossFade())
                                                .transform(new RoundedCorners(20))
                                                .into(imageViewExpence);
                                        imageContainer.addView(imageViewExpence);
                                        imageViewExpence.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i=new Intent(search_detail_screen.this, Image_View_Screen.class);
                                                i.putExtra("imageurl", img.getValue(String.class));
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("category child","doesnot exist");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("category","doesnot exist 1");
            }
        });
    }

}
