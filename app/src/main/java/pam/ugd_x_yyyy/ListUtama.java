package pam.ugd_x_yyyy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListUtama extends AppCompatActivity {

    ListView list;
    final int DIALOG1 = 1;
    String message;

    String[] mob = new String[] { "Samsung Grand Duos", "Iphone 6S 64GH", "Xiaomi Redmi Note 3", "Lenovo A7000", "Huawei P9"
            , "LG G5", "HTC One X", "Zenfone 3 Max", "Andromax E2", "Homtom 7000", "ZUK Z1", "Vivo V3 Max"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_utama);

        list = (ListView) findViewById(R.id.listView);
        CustomList cv = new CustomList(this, mob);
        list.setAdapter(cv);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                message = parent.getAdapter().getItem(position).toString();
                showDialog(DIALOG1);
            }
        });
    }

    protected Dialog onCreateDialog(int id){

        switch(id)
        {
            case DIALOG1 : return createDialog(ListUtama.this);
        }
        return null;
    }

    private Dialog createDialog(Context c)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setTitle("Apakah anda yakin membeli "+message+" ini?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){
                Intent i = new Intent(getApplicationContext(), BeliProduk.class);
                Bundle b = new Bundle();

                b.putString("nama", message);
                i.putExtras(b);
                startActivity(i);
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){

            }
        });
        return b.create();
    }
}
