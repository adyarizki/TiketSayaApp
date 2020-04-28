package com.tukangtani.tiketsaya;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {

    Button btn_sign_in, btn_create_account;
    Animation ttb, btt;
    ImageView emble_app;
    TextView intro_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load animation
        ttb = AnimationUtils.loadAnimation(this,R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_create_account = findViewById(R.id.btn_create_account);

        emble_app = findViewById(R.id.emblem_app);
        intro_app = findViewById(R.id.intro_app);

        //run animation
        emble_app.startAnimation(ttb);
        intro_app.startAnimation(ttb);
        btn_sign_in.startAnimation(btt);
        btn_create_account.startAnimation(btt);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign= new Intent(GetStartedAct.this,SigninAct4.class);
                startActivity(gotosign);
            }
        });

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotregisterone = new Intent(GetStartedAct.this,RegisterAct.class);
                startActivity(gotregisterone);
            }
        });

    }
}
