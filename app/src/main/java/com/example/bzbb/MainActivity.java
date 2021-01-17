package com.example.bzbb;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Adapters.CouponAdapter;
import com.example.bzbb.Adapters.GoodDisplayAdapter;
import com.example.bzbb.Adapters.OrderListAdapter;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Coupon;
import com.example.bzbb.Model.Good;
import com.example.bzbb.Model.Order;
import com.example.bzbb.StaticValues.FastMailKV;
import com.example.bzbb.StaticValues.GoodBrandKV;
import com.example.bzbb.StaticValues.GoodTypeKV;
import com.example.bzbb.Tasks.MyPayTask;
import com.example.bzbb.Utils.CartListUtils;
import com.example.bzbb.Utils.HttpUtils;
import com.example.bzbb.Utils.LocalCacheUtils;
import com.example.bzbb.custom.InfoDialog;
import com.example.bzbb.custom.VerificationCodeDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View selectedInclude;
    LinearLayout loginLinearLayout;
    TextView loginTx1;
    TextView loginTx2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedInclude = findViewById(R.id.include_index);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //先绑定headview
        View headerView = navigationView.getHeaderView(0);
        HttpUtils.view = headerView;
        loginLinearLayout = headerView.findViewById(R.id.loginlinearLayout);
        loginTx1 = headerView.findViewById(R.id.loginTx1);
        loginTx2 = headerView.findViewById(R.id.loginTx2);
        //初始化加载文件
        GoodBrandKV.init();
        GoodTypeKV.init();
        FastMailKV.init();
        HttpUtils.loginCache = getSharedPreferences("LoginCache", Context.MODE_PRIVATE);
        HttpUtils.initLoginInfo();
        CartListUtils.init(getSharedPreferences("CartListCache", Context.MODE_PRIVATE));

        //登录展示框绑定监听事件
        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                if (HttpUtils.isLogin) {

                } else {
                    startActivityForResult(intent, 1);
                }
            }
        });


        //绑定商品列表
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoodListActivity.class);
                startActivity(intent);
            }
        });


        loadIndexInclude();

    }

    class LoadImageByStatic extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource(Integer.parseInt(path.toString()));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            changeInclude(R.id.include_index);
            loadIndexInclude();
        } else if (id == R.id.nav_shoppinglist) {
            if (checkLogin()){
                changeInclude(R.id.include_shoppinglist);
                loadShoppingListInclude();
            }
        } else if (id == R.id.nav_order_query) {
            if (checkLogin()){
                changeInclude(R.id.include_orderlist);
                loadOrderListInclude();
            }
        } else if (id == R.id.nav_couponlist) {
            if (checkLogin()){
                changeInclude(R.id.include_couponlist);
                loadCouponListInclude();
            }
        } else if (id == R.id.nav_aftersale) {


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeInclude(int view) {
        View v = findViewById(view);
        selectedInclude.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
        selectedInclude = v;
    }

    private void loadIndexInclude() {
        //首页广告
        Banner banner = findViewById(R.id.goodbanner);
        List<Integer> imgs = new ArrayList<>();
        imgs.add(R.drawable.t1);
        imgs.add(R.drawable.t2);
        imgs.add(R.drawable.t3);

        List<String> title = new ArrayList<>();
        title.add("包包");
        title.add("包包");
        title.add("包包");
        banner.setImages(imgs);
        banner.setImageLoader(new LoadImageByStatic());
        banner.setBannerTitles(title);
        banner.setDelayTime(500);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.start();

        //最新上架
        final GridView listView = findViewById(R.id.list1);
        final Handler recentlyReleaseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    List<Good> goods = (List<Good>) msg.obj;
                    GoodDisplayAdapter goodDisplayAdapter = new GoodDisplayAdapter(MainActivity.this, R.layout.item, goods);
                    listView.setAdapter(goodDisplayAdapter);
                    listView.measure(0, 0);
                    ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                    int c = goods.size() % 2 == 0 ? goods.size()/2 : goods.size()/2+1;
                    layoutParams.height = listView.getMeasuredHeight() * c;
                }
            }
        };
        (new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Response response = HttpUtils.get("/app/index");
                try {
                    message.what = 0;
                    String res = response.body().string();
                    AjaxResult ajaxResult = JSON.parseObject(res, AjaxResult.class);
                    if (ajaxResult.getSuccess()) {
                        JSONObject jsonObject = JSON.parseObject(ajaxResult.getObject());
                        message.obj = JSON.parseArray(jsonObject.getString("recently")).toJavaList(Good.class);
                        recentlyReleaseHandler.sendMessage(message);

                    }
                } catch (IOException e) {
                    message.what = -1;
                    e.printStackTrace();
                }
            }
        })).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Good good = (Good) listView.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, GoodActivity.class);
                intent.putExtra("Good", good);
                startActivity(intent);
            }
        });

    }

    private void loadShoppingListInclude() {

        final GridView gridView = findViewById(R.id.orderlist);
        final Handler getWaitingForPayOrdersHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()) {
                        final List<Order> orders = JSON.parseArray(ajaxResult.getObject(), Order.class);
                        final OrderListAdapter orderListAdapter = new OrderListAdapter(MainActivity.this, R.layout.orderlistitem, orders);
                        orderListAdapter.setContinuePayHandler(new OrderListAdapter.continuePayHandler() {
                            @Override
                            public void continuePayAction(String orderInfo) {
                                MyPayTask.payTask(MainActivity.this,orderInfo);
                            }
                        });
                        orderListAdapter.setCancelPayHandler(new OrderListAdapter.cancelPayHandler() {
                            @Override
                            public void cancelPayAction(int position) {
                                orders.remove(position);
                                orderListAdapter.notifyDataSetChanged();
                            }
                        });
                        gridView.setAdapter(orderListAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "出现异常", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/getWaitingForPayOrders?isStatus0=true");
                HttpUtils.processHttpResponse(response, getWaitingForPayOrdersHandler, AjaxResult.class);
            }
        }).start();

    }

    private void loadOrderListInclude(){
        final GridView gridView = findViewById(R.id.orderlist1);
        final Handler getPayedOrdersHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        final List<Order> orders = JSON.parseArray(ajaxResult.getObject()).toJavaList(Order.class);
                        final OrderListAdapter orderListAdapter = new OrderListAdapter(MainActivity.this, R.layout.orderlistitem, orders);
                        orderListAdapter.copyString = new OrderListAdapter.copyString() {
                            @Override
                            public void copyStringAction(String s) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("Label", s);
                                cm.setPrimaryClip(mClipData);
                                Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                            }
                        };
                        orderListAdapter.setConfirmOrderHandler(new OrderListAdapter.confirmOrderProcess() {
                            @Override
                            public void confirmOrderAction(int position, Order order, AjaxResult result) {
                                if (result.getSuccess()){
                                    orders.set(position,order);
                                    orderListAdapter.notifyDataSetChanged();
                                }
                                Toast.makeText(MainActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        gridView.setAdapter(orderListAdapter);
                    }else {
                        Toast.makeText(MainActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "出现异常", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/getWaitingForPayOrders?isStatus0=false");
                HttpUtils.processHttpResponse(response,getPayedOrdersHandler,AjaxResult.class);
            }
        }).start();
    }

    private void loadCouponListInclude(){
        final GridView gridView = findViewById(R.id.couponListGV);
        final Handler getCouponListHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        List<Coupon> coupons = JSON.parseArray(ajaxResult.getObject()).toJavaList(Coupon.class);
                        CouponAdapter couponAdapter = new CouponAdapter(MainActivity.this,R.layout.coupon_item,coupons);
                        gridView.setAdapter(couponAdapter);
                    }else {
                        Toast.makeText(MainActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/myCoupons?status=0");
                HttpUtils.processHttpResponse(response,getCouponListHandler,AjaxResult.class);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 1) {
        }
    }

    private boolean checkLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        if (HttpUtils.isLogin){
            return true;
        }else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);
            return false;
        }
    }

}
