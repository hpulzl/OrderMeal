package lzl.edu.com.ordermeal.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import cn.bmob.v3.Bmob;
import lzl.edu.com.ordermeal.R;

/**
 * Created by admin on 2015/11/8.
 * 将Fragment布局加载到主activity中。
 */
public class Main extends Activity implements View.OnClickListener{
    /**
     * 四个Fragment
     */
    private TabHomeFragment homeFragment;
    private TabOrderFragment orderFragment;
    private TabMineFragment mineFragment;
    private TabFoundFragment foundFragment;

    private Toolbar toolbar;
    /**
     * 底部四个按钮
     */
    private LinearLayout linearBarFound;
    private LinearLayout linearBarHome;
    private LinearLayout linearBarOrder;
    private LinearLayout linearBarMine;
    /**
     * 对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    //获取资源文件中的两种颜色
    int reColor = 0;
    int nowColor =0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Bmob.initialize(this, "7541240bda6f2672346b4ba8458f8ac6");
        initView();
        fragmentManager = getFragmentManager();
        reColor = getResources().getColor(R.color.title_main_teal);
        nowColor = getResources().getColor(R.color.title_change_rteal);
        tabSelector(0);
    }
    private void initView(){

        linearBarFound = (LinearLayout) findViewById(R.id.linearBarFound);
        linearBarHome = (LinearLayout) findViewById(R.id.linearBarHome);
        linearBarOrder = (LinearLayout) findViewById(R.id.linearBarOrder);
        linearBarMine = (LinearLayout) findViewById(R.id.linearBarMine);

        linearBarFound.setOnClickListener(this);
        linearBarHome.setOnClickListener(this);
        linearBarOrder.setOnClickListener(this);
        linearBarMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearBarHome: //主页
                tabSelector(0);
                break;
            case R.id.linearBarOrder://订餐
                tabSelector(1);
                break;
            case R.id.linearBarFound: //发现
                tabSelector(2);
                break;
            case R.id.linearBarMine:  //我的
                tabSelector(3);
                break;
        }
    }

    /**
     * 根据传过来的值，来设置选中哪一个Fragment
     * @param index
     */
    private void tabSelector(int index){
        //重置底部布局的颜色
        resetColor();
        //开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index){
            case 0:
                linearBarHome.setBackgroundColor(nowColor);  //改变颜色
                if(homeFragment == null){
                    homeFragment = new TabHomeFragment();
                    transaction.add(R.id.id_content,homeFragment);
                }else{
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                linearBarOrder.setBackgroundColor(nowColor);  //改变颜色
                if(orderFragment == null){
                    orderFragment = new TabOrderFragment();
                    transaction.add(R.id.id_content,orderFragment);
                }else{
                    transaction.show(orderFragment);
                }
                break;
            case 2:
                linearBarFound.setBackgroundColor(nowColor);  //改变颜色
                if(foundFragment == null){
                    foundFragment = new TabFoundFragment();
                    transaction.add(R.id.id_content,foundFragment);
                }else{
                    transaction.show(foundFragment);
                }
                break;
            case 3:
                linearBarMine.setBackgroundColor(nowColor);  //改变颜色
                if(mineFragment == null){
                    mineFragment = new TabMineFragment();
                    transaction.add(R.id.id_content,mineFragment);
                }else{
                    transaction.show(mineFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 重置底部布局的颜色
     */
    private void resetColor(){
        linearBarFound.setBackgroundColor(reColor);
        linearBarHome.setBackgroundColor(reColor);
        linearBarOrder.setBackgroundColor(reColor);
        linearBarMine.setBackgroundColor(reColor);
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction)
    {
        if (foundFragment != null)
        {
            transaction.hide(foundFragment);
        }
        if (orderFragment != null)
        {
            transaction.hide(orderFragment);
        }
        if (mineFragment != null)
        {
            transaction.hide(mineFragment);
        }
        if (homeFragment != null)
        {
            transaction.hide(homeFragment);
        }

    }

}
