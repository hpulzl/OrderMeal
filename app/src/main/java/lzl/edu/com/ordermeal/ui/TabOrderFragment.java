package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.View.OrderMealAdapter;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.javabean.ordermeal;


/**
 * Created by admin on 2015/11/8.
 */
public class TabOrderFragment extends Fragment implements View.OnClickListener{
    private final String ORDER_TAG ="OrderMealsFragment...";
    private ListView orderMeal_lv;
    private Button mealReturn_btn;
    private TextView meal_name,orderTotal_tv;
    private Button orderPay_btn;
    private List<ordermeal> list_order;
    private OrderMealAdapter omAdapter;
    private ProgressBar order_pb;
    private final int SELECT_A = 1;
    private final int SELECT_B = 2;
    private Context mContext;
    private Activity activity;
    private View view;
    String userId = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SELECT_A:
                    list_order = (List<ordermeal>) msg.obj;
                    Log.i(ORDER_TAG,"执行了？？？"+list_order.size());
                    omAdapter = new OrderMealAdapter(activity
                            , (ArrayList<ordermeal>)list_order);
                    orderMeal_lv.setAdapter(omAdapter);
                    order_pb.setVisibility(View.GONE);
                    break;
                case SELECT_B:
                    String str = (String) msg.obj;
                    Log.i(ORDER_TAG,"执行了？？？"+str);
                    String info[] = str.split(" ");
                    orderTotal_tv.setText("一共点了"+info[1]+"道美食，共"+info[0]+"元");
                    order_pb.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getActivity().getApplicationContext();
        activity = this.getActivity();
        view = inflater.inflate(R.layout.activity_order_meals,container,false);
        Customers customer = BmobUser.getCurrentUser(mContext, Customers.class);
        userId = customer.getObjectId();
        init();
        findOrderMeal(userId);
        return view;
    }
    public void init(){
        meal_name = (TextView) view.findViewById(R.id.meal_name);
        orderMeal_lv = (ListView) view.findViewById(R.id.orderMeal_lv);
        mealReturn_btn = (Button) view.findViewById(R.id.mealReturn_btn);
        orderPay_btn = (Button) view.findViewById(R.id.orderPay_btn);
        orderTotal_tv = (TextView) view.findViewById(R.id.orderTotal_tv);
        order_pb = (ProgressBar) view.findViewById(R.id.order_pb);

        order_pb.setVisibility(View.VISIBLE);
        mealReturn_btn.setVisibility(View.GONE);
        meal_name.setText("订餐");
    }
    public void findOrderMeal(String id){
        if(list_order!=null&&list_order.size()>0)
            list_order.clear();
        BmobQuery<ordermeal> bq = new BmobQuery<>();
        Customers customers = new Customers();
        customers.setObjectId(id);
        bq.addWhereEqualTo("o_flag", false);
        bq.order("-createdAt");   //按时间 从晚到早排序
        bq.addWhereEqualTo("o_customer", new BmobPointer(customers));
        bq.include("o_menu,o_customer");
        bq.findObjects(mContext, new FindListener<ordermeal>() {
            @Override
            public void onSuccess(List<ordermeal> list) {
                double totalPrice = 0;
                double num = 0;
                Message msg = new Message();
                if (list.size() > 0) {
                    msg.obj = list;
                    msg.what = SELECT_A;   //查询所有信息
                    mHandler.sendMessage(msg);
                    String info =null;
                    for (ordermeal om : list) {
                        num = (double) om.getO_number();
                        menu m = om.getO_menu();
                        totalPrice += num * Double.parseDouble(m.getM_price());
                        info = totalPrice+" "+list.size();
                    }
                    sendMessage(info);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(ORDER_TAG, s + "查询失败。。" + "错误码。。" + i);
            }
        });
    }
    public void sendMessage(String info){
        Message msg = new Message();
        msg.what = SELECT_B;
        msg.obj = info;
        mHandler.sendMessage(msg);
    }
    @Override
    public void onClick(View v) {

    }

  /*  @Override
    protected void onStop() {
        super.onStop();
        finish();
    }*/
}
