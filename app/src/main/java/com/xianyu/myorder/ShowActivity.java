package com.xianyu.myorder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class ShowActivity extends Activity {

    private List<Order> mOrder = new ArrayList<>();
    private static final int CHOOSE_PHOTO = 0;
    Base64Util base64Util = new Base64Util();
    Bitmap bitmap;
    ListView mListView;
    ImageView picture;
    Button add;
    Button find;
    Button Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mListView = findViewById(R.id.detail_view);
        add = findViewById(R.id.add);
        find = findViewById(R.id.find);
        Return = findViewById(R.id.Return);
        if (new OrderDao(getApplicationContext()).getAllDate() == null) {
            Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
        } else {
            mOrder = new OrderDao(getApplicationContext()).getAllDate();
            mListView.setAdapter(new OrderAdapter());
        }
        find();
        Return.setOnClickListener(v -> {
            startActivity(new Intent(ShowActivity.this, MainActivity.class));
            finish();
        });
        add.setOnClickListener(v -> add());
    }

    private int compare(Object selectedItem) {
        int type = 0;
        if (selectedItem.toString().equals("凉拌类")) {
            type = 1;
        }
        if (selectedItem.toString().equals("快餐类")) {
            type = 2;
        }
        if (selectedItem.toString().equals("盖饭类")) {
            type = 3;
        }
        if (selectedItem.toString().equals("炒菜类")) {
            type = 4;
        }
        if (selectedItem.toString().equals("炖汤类")) {
            type = 5;
        }
        if (selectedItem.toString().equals("卤酱类")) {
            type = 6;
        }
        if (selectedItem.toString().equals("西餐类")) {
            type = 7;
        }
        if (selectedItem.toString().equals("蒸菜类")) {
            type = 8;
        }
        if (selectedItem.toString().equals("油炸类")) {
            type = 9;
        }
        if (selectedItem.toString().equals("香煎类")) {
            type = 10;
        }
        if (selectedItem.toString().equals("腌菜类")) {
            type = 11;
        }
        if (selectedItem.toString().equals("涮汆类")) {
            type = 12;
        }
        return type;
    }

    private void add() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle("添加菜品");
        builder.setIcon(android.R.drawable.btn_star_big_on);
        final AlertDialog diaLog = builder.create();
        View dialogView = View.inflate(this, R.layout.layout_add, null);
        diaLog.setView(dialogView);
        diaLog.show();
        EditText dishName = dialogView.findViewById(R.id.Dish_name);
        EditText dishPrice = dialogView.findViewById(R.id.Dish_price);
        Spinner spinner = dialogView.findViewById(R.id.spinner);
        picture = dialogView.findViewById(R.id.pic);
        Button upload = dialogView.findViewById(R.id.upload);
        Button ok = dialogView.findViewById(R.id.ok);
        upload.setOnClickListener(upload());
        ok.setOnClickListener(v -> {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(ShowActivity.this);
            normalDialog.setIcon(R.mipmap.info);
            normalDialog.setTitle("确认信息");
            normalDialog.setMessage("确定提交吗?");
            normalDialog.setPositiveButton("取消", null);
            normalDialog.setNegativeButton("确定", (dialog, which) -> {
                List<String> allDishName = new OrderDao(getApplicationContext()).getAllDishName();
                if (dishName.getText().toString().equals("") || dishPrice.getText().toString().equals("") || bitmap == null) {
                    Toast.makeText(ShowActivity.this, "请填写菜品信息", Toast.LENGTH_SHORT).show();
                } else if (allDishName.contains(dishName.getText().toString())) {
                    Toast.makeText(this, "此菜名已存在，请重命名", Toast.LENGTH_SHORT).show();
                } else if (!RegexUtil.checkChinese(dishName.getText().toString()) || !RegexUtil.checkDigit(dishPrice.getText().toString())) {
                    Toast.makeText(ShowActivity.this, "输入的信息有误，请检查", Toast.LENGTH_SHORT).show();
                } else {
                    int type = compare(spinner.getSelectedItem());
                    String image = base64Util.encodeImage(bitmap);
                    String images = "data:image/jpg;base64," + image;
                    boolean b = new OrderDao(getApplicationContext()).insertDate(dishName.getText().toString(), Integer.parseInt(dishPrice.getText().toString()), images, type);
                    if (b) {
                        Toast.makeText(ShowActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        diaLog.dismiss();
                        mOrder = new OrderDao(getApplicationContext()).getAllDate();
                        mListView.setAdapter(new OrderAdapter());
                    } else {
                        Toast.makeText(ShowActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            normalDialog.show();
        });
    }

    private View.OnClickListener deleteOnClickListener(int id) {
        return v -> {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(ShowActivity.this);
            normalDialog.setIcon(R.mipmap.info);
            normalDialog.setTitle("确认信息");
            normalDialog.setMessage("确定删除吗?");
            normalDialog.setPositiveButton("取消", null);
            normalDialog.setNegativeButton("确定", (dialog, which) -> {
                boolean order = new OrderDao(ShowActivity.this).deleteOrder(id);
                if (order) {
                    Toast.makeText(ShowActivity.this, "删除成功 ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowActivity.this, ShowActivity.class));
                    this.finish();
                } else {
                    Toast.makeText(ShowActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            });
            normalDialog.show();
        };
    }

    private View.OnClickListener modifyOnClickListener(int id, String orderName, int orderPrice, String orderImage, int orderType) {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setTitle("修改信息");
            builder.setIcon(android.R.drawable.btn_star_big_off);
            final AlertDialog diaLog = builder.create();
            View dialogView = View.inflate(this, R.layout.layout_modify, null);
            diaLog.setView(dialogView);
            diaLog.show();
            EditText dishName = dialogView.findViewById(R.id.OrderName);
            EditText dishPrice = dialogView.findViewById(R.id.OrderPrice);
            Spinner spinner = dialogView.findViewById(R.id.Spinner);
            picture = dialogView.findViewById(R.id.Pic);
            Button modify = dialogView.findViewById(R.id.Modify);
            Button ok = dialogView.findViewById(R.id.Ok);
            Bitmap bitmap = base64Util.stringToBitmap(orderImage);
            dishName.setText(orderName);
            dishPrice.setText(orderPrice + "");
            spinner.setSelection(orderType - 1);
            picture.setImageBitmap(bitmap);
            modify.setOnClickListener(upload());
            ok.setOnClickListener(view -> {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(ShowActivity.this);
                normalDialog.setIcon(R.mipmap.info);
                normalDialog.setTitle("确认信息");
                normalDialog.setMessage("确定提交吗?");
                normalDialog.setPositiveButton("取消", null);
                normalDialog.setNegativeButton("确定", (dialog, which) -> {
                    Boolean b = new OrderDao(getApplicationContext()).getDataByIdAndDishName(id, dishName.getText().toString());
                    if (dishName.getText().toString().equals("") || dishPrice.getText().toString().equals("")) {
                        Toast.makeText(ShowActivity.this, "请填写菜品信息", Toast.LENGTH_SHORT).show();
                    } else if (b) {
                        Toast.makeText(this, "此菜名已存在，请重命名", Toast.LENGTH_SHORT).show();
                    } else if (!RegexUtil.checkChinese(dishName.getText().toString()) || !RegexUtil.checkDigit(dishPrice.getText().toString())) {
                        Toast.makeText(ShowActivity.this, "输入的信息有误，请检查", Toast.LENGTH_SHORT).show();
                    } else {
                        int type = compare(spinner.getSelectedItem());
                        Bitmap bm = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                        String image = base64Util.encodeImage(bm);
                        String images = "data:image/jpg;base64," + image;
                        boolean order = new OrderDao(getApplicationContext()).updateOrder(id, dishName.getText().toString(), Integer.parseInt(dishPrice.getText().toString()), images, type);
                        if (order) {
                            Toast.makeText(ShowActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            diaLog.dismiss();
                            mOrder = new OrderDao(getApplicationContext()).getAllDate();
                            mListView.setAdapter(new OrderAdapter());
                        } else {
                            Toast.makeText(ShowActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                normalDialog.show();
            });
        };
    }

    private void find() {
        find.setOnClickListener(v -> {
            final EditText inputServer = new EditText(ShowActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
            builder.setTitle("筛选菜品").setIcon(R.mipmap.info).setView(inputServer)
                    .setNeutralButton("回到菜品详情", (dialog, which) -> {
                        if (new OrderDao(getApplicationContext()).getAllDate() == null) {
                            Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
                        } else {
                            mOrder = new OrderDao(getApplicationContext()).getAllDate();
                            mListView.setAdapter(new OrderAdapter());
                        }
                    });
            builder.setNegativeButton("根据菜名筛选", (dialog, which) -> {
                if (inputServer.getText().toString().equals("")) {
                    Toast.makeText(ShowActivity.this, "请输入菜名!", Toast.LENGTH_SHORT).show();
                } else {
                    mOrder = new OrderDao(getApplicationContext()).getOrderByNameLike(inputServer.getText().toString());
                    if (mOrder != null && mOrder.size() != 0) {
                        mListView.setAdapter(new OrderAdapter());
                    } else {
                        mListView.setAdapter(null);
                    }
                }
            });
            builder.setPositiveButton("根据类型筛选", (dialog, which) -> {
                int type = compare(inputServer.getText());
                if (inputServer.getText().toString().equals("")) {
                    Toast.makeText(ShowActivity.this, "请输入类型!", Toast.LENGTH_SHORT).show();
                } else {
                    mOrder = new OrderDao(getApplicationContext()).getOrderByType(type);
                    if (mOrder != null && mOrder.size() != 0) {
                        mListView.setAdapter(new OrderAdapter());
                    } else {
                        mListView.setAdapter(null);
                    }
                }
            });
            builder.show();
        });
    }

    private View.OnClickListener upload() {
        return v -> {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent, CHOOSE_PHOTO);
        };
    }

    public class OrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mOrder.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrder.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Order order = mOrder.get(position);
            ViewHolder viewHolder = new ViewHolder();
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.layout_show, null);
                viewHolder.orderType = view.findViewById(R.id.orderType);
                viewHolder.orderName = view.findViewById(R.id.orderName);
                viewHolder.orderPrice = view.findViewById(R.id.orderPrice);
                viewHolder.orderImage = view.findViewById(R.id.orderImage);
                viewHolder.modify = view.findViewById(R.id.modify);
                viewHolder.delete = view.findViewById(R.id.delete);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            String type = "";
            switch (order.getOrderType()) {
                case 1:
                    type = "凉拌类";
                    break;
                case 2:
                    type = "快餐类";
                    break;
                case 3:
                    type = "盖饭类";
                    break;
                case 4:
                    type = "炒菜类";
                    break;
                case 5:
                    type = "炖汤类";
                    break;
                case 6:
                    type = "卤酱类";
                    break;
                case 7:
                    type = "西餐类";
                    break;
                case 8:
                    type = "蒸菜类";
                    break;
                case 9:
                    type = "油炸类";
                    break;
                case 10:
                    type = "香煎类";
                    break;
                case 11:
                    type = "腌菜类";
                    break;
                case 12:
                    type = "涮汆类";
                    break;
            }
            viewHolder.orderType.setText(type);
            viewHolder.orderName.setText(order.getOrderName());
            viewHolder.orderPrice.setText(order.getOrderPrice() + "元");
            Bitmap bitmap = base64Util.stringToBitmap(order.getOrderImage());
            viewHolder.orderImage.setImageBitmap(bitmap);
            viewHolder.delete.setOnClickListener(deleteOnClickListener(order.getId()));
            viewHolder.modify.setOnClickListener(modifyOnClickListener(order.getId(), order.getOrderName(), order.getOrderPrice(), order.getOrderImage(), order.getOrderType()));
            return view;
        }

        class ViewHolder {
            private TextView orderType;
            private TextView orderName;
            private TextView orderPrice;
            private ImageView orderImage;
            private Button modify;
            private Button delete;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                bitmap = ImgUtil.handleImageOnKitKat(this, data);
                picture.setImageBitmap(bitmap);
            }
        }
    }
}