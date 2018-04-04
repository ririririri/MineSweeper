package com.example.jianingsun.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setTitle();
        setSkip();
        setRotation();
    }

    private void setTitle() {
        TextView text=(TextView)findViewById(R.id.title);
        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        text.setAnimation(animation);
    }

    private void setRotation() {
        ImageView image=(ImageView)findViewById(R.id.aMine);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.welcomeanimation);
        image.setAnimation(animation);
       animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setSkip() {
        Button btn=(Button)findViewById(R.id.skipBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context,WelcomeActivity.class);
    }
}
