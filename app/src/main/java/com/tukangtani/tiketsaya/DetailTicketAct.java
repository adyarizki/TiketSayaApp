package com.tukangtani.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailTicketAct extends AppCompatActivity {

    Button btn_buy_ticket;
    LinearLayout btn_back;
    TextView title_ticket, locatio_ticket,
            photo_spot_ticket, wifi_ticket,
            festival_ticket, short_desc_ticket;
    ImageView header_ticket_detail;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        header_ticket_detail = findViewById(R.id.header_ticket_detail);
        title_ticket = findViewById(R.id.title_ticket);
        locatio_ticket = findViewById(R.id.locatio_ticket);
        photo_spot_ticket = findViewById(R.id.xnama_wisata);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        short_desc_ticket = findViewById(R.id.short_desc_ticket);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru= bundle.getString("jenis_tiket");

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menimpa data yang ada
                title_ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                locatio_ticket.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot_ticket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_ticket.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festival_ticket.setText(dataSnapshot.child("is_festival").getValue().toString());
                short_desc_ticket.setText(dataSnapshot.child("short_desc").getValue().toString());
                Picasso.with(DetailTicketAct.this).load(dataSnapshot.child("url_thumbnail")
                        .getValue().toString()).centerCrop().fit().into(header_ticket_detail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocheckout = new Intent(DetailTicketAct.this, TicketCheckoutAct.class);
                gotocheckout.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(gotocheckout);
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoback = new Intent(DetailTicketAct.this, HomeAct.class);
                startActivity(gotoback);
            }
        });
    }
}
