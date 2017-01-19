package pam.ugd_x_yyyy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BeliProduk extends AppCompatActivity {

    ImageView imgProduk;
    TextView namaProduk, harga;
    EditText namaPro, alamat, jumlah;
    final int DIALOG1 = 1, DIALOG2 = 2;
    String nama;

    RelativeLayout buttonBeli, buttonBatal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_produk);

        imgProduk = (ImageView) findViewById(R.id.imageView);
        namaProduk = (TextView) findViewById(R.id.namaProduk);
        harga = (TextView) findViewById(R.id.harga);
        buttonBeli = (RelativeLayout) findViewById(R.id.buttonBeli);
        buttonBatal = (RelativeLayout) findViewById(R.id.buttonBatal);

        namaPro = (EditText) findViewById(R.id.editNama);
        alamat = (EditText) findViewById(R.id.editAlamat);
        jumlah = (EditText) findViewById(R.id.editJumlah);

        Bundle b = getIntent().getExtras();
        nama = b.getString("nama");

        namaProduk.setText(nama);

        if (nama.equalsIgnoreCase("Samsung Grand Duos")) {
            imgProduk.setImageResource(R.drawable.hp1);

        } else if (nama.equals("Iphone 6S 64GH")) {
            imgProduk.setImageResource(R.drawable.hp2);
            harga.setText("Rp 7.999.999,00");
        } else if (nama.equals("Xiaomi Redmi Note 3")) {
            imgProduk.setImageResource(R.drawable.hp3);
            harga.setText("Rp 2.300.000,00");
        } else if (nama.equals("Lenovo A7000")) {
            imgProduk.setImageResource(R.drawable.hp4);
            harga.setText("Rp 1.999.999,00");
        } else if (nama.equals("Huawei P9")) {
            imgProduk.setImageResource(R.drawable.hp5);
            harga.setText("Rp 9.999.999,00");
        } else if (nama.equals("LG G5")) {
            imgProduk.setImageResource(R.drawable.hp6);
            harga.setText("Rp 5.100.000,00");
        } else if (nama.equals("HTC One X")) {
            imgProduk.setImageResource(R.drawable.hp7);
            harga.setText("Rp 4.444.444,00");
        } else if (nama.equals("Zenfone 3 Max")) {
            imgProduk.setImageResource(R.drawable.hp8);
            harga.setText("Rp 2.100.000,00");
        } else if (nama.equals("Andromax E2")) {
            imgProduk.setImageResource(R.drawable.hp9);
            harga.setText("Rp 1.599.999,00");
        } else if (nama.equals("Homtom 7000")) {
            imgProduk.setImageResource(R.drawable.hp10);
            harga.setText("Rp 777.999,00");
        } else if (nama.equals("ZUK Z1")) {
            imgProduk.setImageResource(R.drawable.hp8);
            harga.setText("Rp 3.599.999,00");
        } else if (nama.equals("Vivo V3 Max")) {
            imgProduk.setImageResource(R.drawable.hp7);
            harga.setText("Rp 3.200.000,00");
        }

        buttonBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG1);
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG2);
            }
        });
    }

    protected Dialog onCreateDialog(int id){

        switch(id)
        {
            case DIALOG1 : return createDialog(BeliProduk.this);
            case DIALOG2 : return createDialog2(BeliProduk.this);
        }
        return null;
    }

    private Dialog createDialog(Context c)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setTitle("Apakah anda yakin membeli "+nama+" ini?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){
                if(namaPro.getText().toString().equalsIgnoreCase("") || alamat.getText().toString().equalsIgnoreCase("") || jumlah.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Silahkan lengkapi data diri anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Anda sudah membeli produk ini, Barang segera dikirim.. Terima kasih", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){

            }
        });
        return b.create();
    }

    private Dialog createDialog2(Context c)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setTitle("Apakah anda yakin ingin melakukan pembatalan?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int w){
                Toast.makeText(getApplicationContext(),"Pembatalan Berhasil...", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
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
