package com.mok.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class edit_daily extends AppCompatActivity {

    private String id, date, name, price, chose, re_name, Filename;
    private EditText edit_name_edit, edit_price_edit;
    private ToggleButton edit_in_ex_btn;
    private ImageView edit_pic;
    private boolean sel_pic;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        chose = intent.getStringExtra("chose");
        date = intent.getStringExtra("date");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        sel_pic = false;

        edit_name_edit = findViewById(R.id.edit_name_edit);
        edit_price_edit = findViewById(R.id.edit_price_edit);
        edit_in_ex_btn = findViewById(R.id.edit_in_ex_btn);
        edit_pic = findViewById(R.id.edit_pic);

        edit_name_edit.setText(name);
        price = price.replace("$","");
        edit_price_edit.setText(price);
        if (chose.equals("1")){
            edit_in_ex_btn.setChecked(true);
        }else{
            edit_in_ex_btn.setChecked(false);
        }

        get_picture();

        edit_in_ex_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    chose = "1";
                }else{
                    chose = "0";
                }
            }
        });

    }

    public void get_picture() {
        Filename = id + "_" + name + "_" + date;
        String imageUrl = "http://sgm1991.dothome.co.kr/edit_picture.php?filename=" + Filename;

        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // load success
                edit_pic.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // fail
                        edit_pic.setImageResource(R.drawable.no_image);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);
    }

    public void click_edit(View view){

        re_name = edit_name_edit.getText().toString();
        price = edit_price_edit.getText().toString();
        Drawable currentImage = edit_pic.getDrawable();

        final String re_Filename;

        if (currentImage.equals(getResources().getDrawable(R.drawable.no_image))){
            re_Filename = "nul";
        }else{
            re_Filename = id + "_" + re_name + "_" + date;
        }

        if (edit_name_edit.getText().toString().equals("") || edit_price_edit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                        if (sel_pic){
                            delete_File(Filename);
                            send_pic(re_Filename);

                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            };
                            int delayMillis = 5000;
                            handler.postDelayed(runnable, delayMillis);

                        }else{
                            change_Filename(re_Filename);

                            finish();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        editpriceRequest editpriceRequest = new editpriceRequest(id, Integer.parseInt(chose) , date, re_name, Integer.parseInt(price), name, re_Filename, responseListener);

        int socketTimeout = 10000; // 10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        editpriceRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(edit_daily.this);
        queue.add(editpriceRequest);
    }

    public void click_delete(View view){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                        delete_File(Filename);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        deletepriceRequest deletepriceRequest = new deletepriceRequest(id, date, name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(edit_daily.this);
        queue.add(deletepriceRequest);
    }

    public void change_Filename (String re_Filename){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        filenamechgRequest filenamechgRequest = new filenamechgRequest(Filename, re_Filename, responseListener);
        RequestQueue queue = Volley.newRequestQueue(edit_daily.this);
        queue.add(filenamechgRequest);
    }

    public void delete_File (String Filename){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        deletefileRequest deletefileRequest = new deletefileRequest(Filename, responseListener);
        RequestQueue queue = Volley.newRequestQueue(edit_daily.this);
        queue.add(deletefileRequest);
    }

    public void edit_pic_click (View view){
        if (checkCameraPermission()) {
            dispatchTakePictureIntent();
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            edit_pic.setImageBitmap(imageBitmap);
            sel_pic = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // refuse camera permission
            }
        }
    }

    public void send_pic (String Filename){
        Drawable drawable = edit_pic.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // compress picture File
            int maxWidth = 800;
            int maxHeight = 800;
            float scale = Math.min(((float) maxWidth / bitmap.getWidth()), ((float) maxHeight / bitmap.getHeight()));
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos); // image compress 10%
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            String url = "http://sgm1991.dothome.co.kr/add_picture.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(edit_daily.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(edit_daily.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", encodedImage);
                    params.put("filename", Filename);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}