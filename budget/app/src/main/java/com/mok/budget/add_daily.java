package com.mok.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class add_daily extends AppCompatActivity {

    private String id, flag, date;
    private EditText add_name_edit, add_price_edit, add_date_edit, add_time_edit;
    private ToggleButton in_ex_btn;
    boolean chose, sel_pic;
    private Calendar calendar;
    private ImageView add_pic;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        date = intent.getStringExtra("date");
        sel_pic = false;

        add_name_edit = findViewById(R.id.add_name_edit);
        add_price_edit = findViewById(R.id.add_price_edit);
        add_date_edit = findViewById(R.id.add_date_edit);
        add_time_edit = findViewById(R.id.add_time_edit);
        in_ex_btn = findViewById(R.id.in_ex_btn);
        add_pic = findViewById(R.id.add_pic);



        //put date & time
        add_date_edit.setText(date);

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        add_time_edit.setText(dateFormat.format(today));


        calendar = Calendar.getInstance();

        add_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        add_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        chose = false;
        in_ex_btn.setChecked(chose);

        in_ex_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chose = isChecked;
            }
        });

    }

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(add_daily.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                add_time_edit.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = sdf.format(calendar.getTime());
                        add_date_edit.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void onaddclick (View view){

        if (add_name_edit.getText().toString().equals("") || add_price_edit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Insert Name & Price!!", Toast.LENGTH_SHORT).show();
            return;
        } else if ((add_date_edit.getText().toString().equals("") && !add_time_edit.getText().toString().equals("")) || (!add_date_edit.getText().toString().equals("") && add_time_edit.getText().toString().equals(""))){
            Toast.makeText(getApplicationContext(), "Insert Time or Date!!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Calendar calendar = Calendar.getInstance();
            int second = calendar.get(Calendar.SECOND);
            String date = add_date_edit.getText().toString() + " " + add_time_edit.getText().toString() + ":" + String.format(Locale.getDefault(), "%02d", second);//sdf.format(now);
            String name = add_name_edit.getText().toString();
            final String Filename;
            if (sel_pic){
                Filename = id + "_" + name + "_" + date;
            }else{
                Filename = "nul";
            }
            int price = Integer.parseInt(add_price_edit.getText().toString());

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                            if (sel_pic){
                                send_pic(Filename);

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

            addpriceRequest addpriceRequest = new addpriceRequest(id, date, name, chose, price, Filename, responseListener);

            int socketTimeout = 10000; // 10 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            addpriceRequest.setRetryPolicy(policy);

            RequestQueue queue = Volley.newRequestQueue(add_daily.this);
            queue.add(addpriceRequest);
        }
    }

    public void add_pic_click (View view){
        if (checkCameraPermission()) {
            dispatchTakePictureIntent();
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
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
            add_pic.setImageBitmap(imageBitmap);
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
        Drawable drawable = add_pic.getDrawable();
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
                            Toast.makeText(add_daily.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(add_daily.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
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