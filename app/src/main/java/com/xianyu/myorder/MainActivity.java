package com.xianyu.myorder;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private OrderDao ordersDao;
    Base64Util base64Util = new Base64Util();
    Bitmap bitmap = null;

    TextView text;
    TextView detail;
    ToggleButton TB;
    ImageView Pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        detail = findViewById(R.id.Details);
        TB = findViewById(R.id.TB);
        Pic = findViewById(R.id.show_pic);

        ordersDao = new OrderDao(this);
        if (!ordersDao.isDataExist()) {
            ordersDao.initTable();
        } else {
            ordersDao.getAllDate();
        }

        TB.setOnClickListener(v -> new MyThread().start());
        detail.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ShowActivity.class));
            finish();
        });
        Pic.setOnClickListener(v -> {
            List<Order> orders = ordersDao.getOrderByName(text.getText().toString());
            if (bitmap == null) {
                Toast.makeText(this, "请先选择菜品！", Toast.LENGTH_SHORT).show();
            } else {
                for (Order order : orders) {
                    int orderPrice = order.getOrderPrice();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    AlertDialog alert = builder.setIcon(R.mipmap.ic_icon_fish).setMessage("价格：" + orderPrice).create();
                    WindowManager.LayoutParams wlp = alert.getWindow().getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    wlp.x = 0;
                    wlp.y = 0;
                    alert.show();
                    alert.getWindow().setLayout(200, 150);
                }
            }
        });
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!TB.isChecked()) {
                List<String> name = ordersDao.getAllDishName();
                Collections.shuffle(name);
                for (int i = 0; i < name.size(); i++) {
                    try {
                        Thread.sleep(20);
                        int finalI1 = i;
                        runOnUiThread(() -> text.setText(name.get(finalI1)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            runOnUiThread(() -> {
                List<String> imageList = ordersDao.getImageByDishName(text.getText().toString());
                bitmap = base64Util.stringToBitmap(imageList.toString());
                Pic.setImageBitmap(bitmap);
            });
        }
    }
            /*class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!TB.isChecked()) {
                List<Order> name = ordersDao.getAllDate();
                Collections.shuffle(name);
                for (Order order : name) {
                    String orderName = order.getOrderName();
                    String orderImage = order.getOrderImage();
                    Bitmap bitmap = base64Util.stringToBitmap(orderImage);
                    for (int i = 0; i < orderName.length(); i++) {
                        try {
                            Thread.sleep(20);
                            runOnUiThread(() -> {
                                text.setText(orderName);
                                Pic.setImageBitmap(bitmap);
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }*/
}