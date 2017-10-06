package hu.bme.aut.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity
{
    private TextView tv;
    private ImageView iv;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv) ;
        tvName = (TextView) findViewById(R.id.tvMyName);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        tvName.startAnimation(myanim);

        final Intent intent = new Intent(this, MainActivity.class) ;

        Thread timer = new Thread()
        {
          public void run()
          {
              try
              {
                  sleep(3000);
              }
              catch(InterruptedException e)
              {
                  e.printStackTrace();
              }
              finally
              {
                  startActivity(intent);
                  finish() ;
              }
          }
        };

        timer.start();
    }
}
