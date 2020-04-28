package com.tukangtani.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninAct4 extends AppCompatActivity {

    TextView btn_new_account;
    Button btn_signin;
    EditText xusername, xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_act4);

        btn_new_account = findViewById(R.id.btn_new_account);
        btn_signin = findViewById(R.id.btn_sign_in);

        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotregisterone = new Intent(SigninAct4.this,RegisterAct.class);
                startActivity(gotregisterone);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_signin.setEnabled(false);
                btn_signin.setText("Loading ...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()){
                Toast.makeText(getApplicationContext(), "Username Kosong", Toast.LENGTH_SHORT).show();
                btn_signin.setEnabled(true);
                btn_signin.setText("SIGN IN");
                }
                else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Password Kosong", Toast.LENGTH_SHORT).show();
                        btn_signin.setEnabled(true);
                        btn_signin.setText("SIGN IN");
                    }
                    else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    //                            Toast.makeText(getApplicationContext(), "Username Ada!", Toast.LENGTH_SHORT).show();
                                    //ambil data password
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();
                                    //validasi password dengan password firebase
                                    if (password.equals(passwordFromFirebase)){
                                        //simpan username key kepada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();
                                        //berpindah act
                                        Intent gotohome = new Intent(SigninAct4.this,HomeAct.class);
                                        startActivity(gotohome);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Password Salah!", Toast.LENGTH_SHORT).show();
                                        btn_signin.setEnabled(true);
                                        btn_signin.setText("SIGN IN");
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Username Tidak Ada!", Toast.LENGTH_SHORT).show();
                                    btn_signin.setEnabled(true);
                                    btn_signin.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error !", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            }
        });
        
    }
}
