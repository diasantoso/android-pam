package pam.ugd_x_yyyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dias on 11/11/2016.
 */
public class CustomList extends ArrayAdapter {

    private final Context context;
    private final String[] values;

    public CustomList(Context context, String[] values) {
        super(context, R.layout.activity_list_produk, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_list_produk, parent, false);
        TextView tv = (TextView) rowView.findViewById(R.id.label);
        ImageView iv = (ImageView) rowView.findViewById(R.id.logo);

        String item_value = values[position];
        if (item_value.equals("Samsung Grand Duos")) {
            iv.setImageResource(R.drawable.hp1);
            tv.setText(item_value);
        } else if (item_value.equals("Iphone 6S 64GH")) {
            iv.setImageResource(R.drawable.hp2);
            tv.setText(item_value);
        } else if (item_value.equals("Xiaomi Redmi Note 3")) {
            iv.setImageResource(R.drawable.hp3);
            tv.setText(item_value);
        } else if (item_value.equals("Lenovo A7000")) {
            iv.setImageResource(R.drawable.hp4);
            tv.setText(item_value);
        } else if (item_value.equals("Huawei P9")) {
            iv.setImageResource(R.drawable.hp5);
            tv.setText(item_value);
        } else if (item_value.equals("LG G5")) {
            iv.setImageResource(R.drawable.hp6);
            tv.setText(item_value);
        } else if (item_value.equals("HTC One X")) {
            iv.setImageResource(R.drawable.hp7);
            tv.setText(item_value);
        } else if (item_value.equals("Zenfone 3 Max")) {
            iv.setImageResource(R.drawable.hp8);
            tv.setText(item_value);
        } else if (item_value.equals("Andromax E2")) {
            iv.setImageResource(R.drawable.hp9);
            tv.setText(item_value);
        } else if (item_value.equals("Homtom 7000")) {
            iv.setImageResource(R.drawable.hp10);
            tv.setText(item_value);
        } else if (item_value.equals("ZUK Z1")) {
            iv.setImageResource(R.drawable.hp8);
            tv.setText(item_value);
        } else if (item_value.equals("Vivo V3 Max")) {
            iv.setImageResource(R.drawable.hp7);
            tv.setText(item_value);
        }

        return rowView;
    }
}
