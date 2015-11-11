package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import lzl.edu.com.ordermeal.R;

public class WelcomeActivity extends Activity {
//    SharedPreferences preferences;
    final Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        //初始化bmob
        Bmob.initialize(this, "7541240bda6f2672346b4ba8458f8ac6");

            Timer time = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    if(isLogin()){   //如果已经登录过了，跳转至主界面
                        intent.setClass(WelcomeActivity.this,Main.class);
                        startActivity(intent);
                    }else {  // 未登录过，跳转至 登录界面
                        intent.setClass(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            };
            time.schedule(tt, 3000);
    }

    /**
     * 判断是否登录过。
     * @return
     */
    private boolean isLogin(){
        //判断文件中是否保存有 登录账号和密码
       /* preferences = getSharedPreferences("autoLogin",MODE_PRIVATE);
        String password = preferences.getString("password",null);
        String phoneNumber = preferences.getString("phoneNumber",null);
        Log.i("----welcome-----", password + "  "+phoneNumber);*/
        BmobUser bu = BmobUser.getCurrentUser(this);
        if(bu!=null) {
            return true;
        }
        return false;
    }
}
