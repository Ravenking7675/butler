package com.avinash.butler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public ImageView girlMenu;
    Animation animFadein;
    public Button letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letsGo = findViewById(R.id.letsGo);
        girlMenu = findViewById(R.id.girl_menu);


        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        //Animations
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.hover);
        girlMenu.startAnimation(animFadein);

    }
}
