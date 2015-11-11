package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.javabean.collect;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.javabean.ordermeal;
import lzl.edu.com.ordermeal.util.ToastUtil;

/**
 * 每个菜的详细说明及评论信息。
 */
public class MenuItemActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "MenuItemActivity";
    private ImageView mMeal_iv;
    private TextView mMealName_tv,mMealPrice_tv,mMealDescribe_tv,mMealName;
    private LinearLayout mLinear_comment;
    private ImageButton mOrder_btn,mCollect_btn;
    private Button mMealReturn;
    private Intent intent = new Intent();
    private menu m;
    public static final String DATA_ID = "meal_objectId";
    public static final String DATA_MEAL_NAME = "meal_name";
    private ImageButton orderPlus_ib,orderMinus_ib;
    private EditText orderNum_et;
    private String edit_Num;
    private Context mContext;
    int num=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        mContext = MenuItemActivity.this;
        if(getIntent().getSerializableExtra(TabHomeFragment.KEY_MENU) !=null) {
            m = (menu) getIntent().getSerializableExtra(TabHomeFragment.KEY_MENU);
        }
         init();
    }
    public void init(){
        mMeal_iv = (ImageView) findViewById(R.id.meal_iv);
        mMealDescribe_tv = (TextView) findViewById(R.id.item_describe_tv);
        mMealName_tv = (TextView) findViewById(R.id.item_mealName_tv);
        mMealName = (TextView) findViewById(R.id.meal_name);
        mMealPrice_tv = (TextView) findViewById(R.id.item_meal_price);
        mLinear_comment = (LinearLayout) findViewById(R.id.linear_comment);
        mCollect_btn = (ImageButton) findViewById(R.id.item_collect_btn);
        mOrder_btn = (ImageButton) findViewById(R.id.item_order_btn);
        mMealReturn = (Button) findViewById(R.id.mealReturn_btn);
        mLinear_comment = (LinearLayout) findViewById(R.id.linear_comment);
        orderMinus_ib = (ImageButton) findViewById(R.id.order_plus);
        orderPlus_ib = (ImageButton)findViewById(R.id.order_minus);
        orderNum_et = (EditText) findViewById(R.id.orderNum_et);
        edit_Num = orderNum_et.getText().toString();
        num = Integer.parseInt(edit_Num);

        setView();//动态设置布局中的内容
        mLinear_comment.setOnClickListener(this);
        mCollect_btn.setOnClickListener(this);
        mOrder_btn.setOnClickListener(this);
        mMealReturn.setOnClickListener(this);
        orderPlus_ib.setOnClickListener(this);
        orderMinus_ib.setOnClickListener(this);
    }
    private void setView(){
        mMealName_tv.setText(m.getM_name());
        mMealName.setText(m.getM_name());
        mMealDescribe_tv.setText(m.getM_describe());
        mMealPrice_tv.setText("优惠价 " + m.getM_price());
        m.getM_image().loadImage(this, mMeal_iv);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_comment:   //获取评论信息
                intent.setClass(this, GetCommentActivity.class);
                intent.putExtra(DATA_ID,m.getObjectId());
                intent.putExtra(DATA_MEAL_NAME,m.getM_name());
                startActivity(intent);
                break;
            case R.id.item_collect_btn:
                collectMenu(mContext,m.getObjectId());
                break;
            case R.id.item_order_btn:
                addOrderMeal(mContext,num,m.getObjectId());
                break;
            case R.id.order_plus:  //对编辑框进行++操作
                num++;
                orderNum_et.setText(num+"");
                break;
            case R.id.order_minus://对编辑框进行--操作
                num--;
                if(num<1){
                    num=1;
                    ToastUtil.toast(this,"不能减了。。。");
                }
                orderNum_et.setText(num+"");
                break;
            case R.id.mealReturn_btn:   //返回到原来的界面
               finish();
                break;
            default:
                break;
        }
    }

    /**
     * 用户收藏菜品。
     * @param context
     */
    public void collectMenu(final Context context,String menu_id){
        Customers cus = BmobUser.getCurrentUser(context,Customers.class);
        menu m = new menu();
        m.setObjectId(menu_id);
        collect cot = new collect();
        cot.setC_menu(m);
        cot.setCustomer_menu(cus);
        cot.setC_flag(true);  //表示已经被添加
        cot.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.toast(context, "收藏成功");
            }

            @Override
            public void onFailure(int i, String s) {
                if(i==401){
                    ToastUtil.toast(context, "您已经收藏过了");
                }else{
                    ToastUtil.toast(context, "收藏失败");
                }
                Log.i("collect","收藏失败。。"+i+"  原因。。"+s);
            }
        });
    }
    /**
     * 往order表添加数据。。
     * 要将用户的信息和菜的信息添加到该表中
     * 在添加之前首先要查询一下、这个菜是否已经被添加过了。
     * 若添加过 不予保存到数据库。
     */
    public void addOrderMeal(final Context context, int number,String menu_id){
        Customers cus = BmobUser.getCurrentUser(context, Customers.class);
        menu m = new menu();
        m.setObjectId(menu_id);
        ordermeal om = new ordermeal();
        om.setO_menu(m);
        om.setO_customer(cus);
        om.setO_number(number);
        om.setO_flag(false);
        om.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtil.toast(context, "订餐成功");
            }
            @Override
            public void onFailure(int i, String s) {
                if(i==401){
                    ToastUtil.toast(context, "您已点过该菜");
                }
            }
        });
    }
}
