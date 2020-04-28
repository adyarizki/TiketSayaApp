package com.tukangtani.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_pay_now, btnmines, btnplus;
    TextView textjumlahtiket, texttotalharga, textmybalance, nama_wisata, lokasi, ketentuan;
    LinearLayout btn_back_buy;
    Integer valueJumlahTiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView notice_uang;
    Integer sisa_balance = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    Integer nomor_transaksi = new Random().nextInt();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();
        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru= bundle.getString("jenis_tiket");

        btnplus = findViewById(R.id.btnplus);
        btn_pay_now = findViewById(R.id.btn_pay_now);
        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.texttotalharga);
        textmybalance = findViewById(R.id.textmybalance);
        notice_uang = findViewById(R.id.notice_uang);

        nama_wisata = findViewById(R.id.xnama_wisata);
        lokasi = findViewById(R.id.xlokasi);
        ketentuan = findViewById(R.id.ketentuan);


        //setting value baru untuk beberapa komponen
        textjumlahtiket.setText(valueJumlahTiket.toString());
        btnmines = findViewById(R.id.btnmines);




        //secara awal hide btn mines
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);
        notice_uang.setVisibility(View.GONE);

        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("US$" + mybalance+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //mengambil data dari intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menimpa data yang ada
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata =dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata =dataSnapshot.child("time_wisata").getValue().toString();
                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                valuetotalharga = valuehargatiket* valueJumlahTiket;
                texttotalharga.setText("US$ "+ valuetotalharga+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket+=1;
                textjumlahtiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket > 1){
                    btnmines.animate().alpha(1).setDuration(300).start();
                    btnmines.setEnabled(true);
                }
                valuetotalharga = valuehargatiket* valueJumlahTiket;
                texttotalharga.setText("US$ "+ valuetotalharga+"");
                if (valuetotalharga > mybalance){
                    btn_pay_now.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_pay_now.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueJumlahTiket-=1;
                textjumlahtiket.setText(valueJumlahTiket.toString());
                if (valueJumlahTiket < 2){
                    btnmines.animate().alpha(0).setDuration(300).start();
                    btnmines.setEnabled(false);

                }
                valuetotalharga = valuehargatiket* valueJumlahTiket;
                texttotalharga.setText("US$ "+ valuetotalharga+"");
                if (valuetotalharga < mybalance){
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_now.setEnabled(true);
                    textmybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_pay_now = findViewById(R.id.btn_pay_now);
        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan data user ke firebase dan membuat tabel
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).child(nama_wisata.getText().toString()+ nomor_transaksi );
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString()+ nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        reference3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());

                        Intent gotosuccess = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccess);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //mengambil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sisa_balance= mybalance- valuetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        btn_back_buy = findViewById(R.id.btn_back_buy);
        btn_back_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoback = new Intent(TicketCheckoutAct.this, DetailTicketAct.class);
                startActivity(gotoback);
            }
        });

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new =sharedPreferences.getString(username_key, "");
    }
}
