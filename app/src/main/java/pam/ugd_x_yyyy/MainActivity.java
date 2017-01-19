package pam.ugd_x_yyyy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout btnNext, btnProduk, btnLogOut;

    private SharedPreferences sharedpreferences;
    private final String name="myAccount";
    private static final int mode = Activity.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences=getSharedPreferences(name, mode);

        btnNext = (RelativeLayout) findViewById(R.id.buttonNext);
        btnProduk = (RelativeLayout) findViewById(R.id.buttonBeli);
        btnLogOut = (RelativeLayout) findViewById(R.id.buttonLogOut);

        btnNext.setOnClickListener(this);
        btnProduk.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnNext)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else if(v == btnProduk)
        {
            Intent intent = new Intent(this, ListUtama.class);
            startActivity(intent);
        }
        else if(v == btnLogOut)
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("status", "not logged in");
            editor.apply();
            finish();

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        }
    }
}
