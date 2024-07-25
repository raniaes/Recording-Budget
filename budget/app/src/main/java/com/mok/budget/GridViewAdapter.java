package com.mok.budget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private String id, ymd_date;

    ArrayList<grid_item> items = new ArrayList<>();

    public GridViewAdapter (String id, String ymd_date){
        this.id = id;
        this.ymd_date = ymd_date;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(grid_item item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final grid_item gridItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_list_item, viewGroup, false);
        }

        TextView tvChoice = convertView.findViewById(R.id.tv_choice);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvPrice = convertView.findViewById(R.id.tv_price);
        TextView tvDate = convertView.findViewById(R.id.tv_date);
        ImageView ivIcon = convertView.findViewById(R.id.iv_icon);

        tvChoice.setText(gridItem.getChoice());
        tvName.setText(gridItem.getName());
        tvPrice.setText(gridItem.getPrice());
        tvDate.setText(gridItem.getDate());


        loadImage(context, gridItem.getimageName(), ivIcon);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_daily.class);
                intent.putExtra("id", id);
                if (gridItem.getChoice().equals("Income")) {
                    intent.putExtra("chose", "1");
                } else {
                    intent.putExtra("chose", "0");
                }
                intent.putExtra("date", ymd_date + " " + gridItem.getDate());
                intent.putExtra("name", gridItem.getName());
                intent.putExtra("price", gridItem.getPrice());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void loadImage(Context context, String filename, ImageView imageView) {
        String imageUrl = "http://sgm1991.dothome.co.kr/edit_picture.php?filename=" + filename;

        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.no_image);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(imageRequest);
    }
}

