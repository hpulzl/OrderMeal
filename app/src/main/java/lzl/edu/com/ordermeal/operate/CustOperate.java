package lzl.edu.com.ordermeal.operate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.ui.Main;
import lzl.edu.com.ordermeal.util.ToastUtil;

/**
 * Created by admin on 2015/10/30.
 * 该类展示用户的操作。
 * 例如：登录、注册、
 */
public class CustOperate {
    SharedPreferences preferences;
    Intent intent = new Intent();
    public final String SMSNAME = "sendMsg";
     MyCountTimer timer;
    public CustOperate(){}


    /**
     * 用户登录验证
     * @param phoneNumber  手机号
     * @param password  密码
     */
    public void CustLogin(final Context context ,final String phoneNumber, final String password){

        BmobUser.loginByAccount(context, phoneNumber, password, new LogInListener<Customers>() {
            @Override
            public void done(Customers customers, BmobException e) {
                if (customers != null) {
                    ToastUtil.toast(context, "登录成功");
                   /* //登录成功，用数据存储技术保存密码，下次进入时直接登录
                    preferences = context.getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("phoneNumber", phoneNumber);
                    editor.putString("password", password);
                    editor.commit();*/
                    intent.setClass(context, Main.class);
                    context.startActivity(intent);
                } else {
                    ToastUtil.toast(context, "用户名或密码不正确");
                }
            }
        });
    }

    /**
     * 手机发送验证码的流程：
     * 1、请求获取验证码，
     * 2、查询验证码发送状态  -->因为技术原因获取不到smsId的值，所以暂时不获取状态。
     * @param phoneNumber
     */
    public void CustRequestSMSCode(final Context context,String phoneNumber,Button button){
        timer = new MyCountTimer(60000,1000,button);
        timer.start();
        BmobSMS.requestSMSCode(context, phoneNumber, SMSNAME, new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) { // 验证码发送成功
                    ToastUtil.toast(context, "验证码发送成功");
                } else {
                    ToastUtil.toast(context, "请十分钟后再发验证码...");
                    timer.cancel();
                }
            }
        });
    }

    /**
     * 完成注册用户信息:
     * 步骤：
     * 1、判断验证码是否正确
     * 2、若通过 注册信息
     * 未通过 停止注册信息
     * @param phoneNum
     * @param password
     * @param smsCode
     */
    public void CustRegister(final Context context,final String phoneNum, final String password,String smsCode){
        BmobSMS.verifySmsCode(context, phoneNum, smsCode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) { //短息验证码验证成功
                    //进行注册信息的操作
                    new CustOperate().Register(context,phoneNum, password);
                } else {  //验证错误
                    ToastUtil.toast(context, "验证码输入错误");
                }
            }
        });
    }

    /**
     * 用户具体信息的注册
     * @param phoneNum
     * @param password
     */
    public void Register(final Context cont,String phoneNum,String password){
        Log.i("register",cont.toString()+"-----");
        Customers cust = new Customers(18,true,null);

        cust.setUsername(phoneNum);
        cust.setMobilePhoneNumber(phoneNum);
        cust.setPassword(password);
        cust.signUp(cont, new SaveListener() {
            @Override
            public void onSuccess() {
                //跳转至 主界面
                intent.setClass(cont, Main.class);
                cont.startActivity(intent);
                ToastUtil.toast(cont,"注册成功");
            }
            @Override
            public void onFailure(int i, String s) {
                ToastUtil.toast(cont, "您的账号已注册" + s + "错误码：" + i);
                if(i==9015){
                    intent.setClass(cont, Main.class);
                    cont.startActivity(intent);
                    ToastUtil.toast(cont,"注册成功");
                }
                Log.i("cont","注册失败"+s+"错误码："+i);
            }
        });
    }
    /**
     * 定义一个内部类用了获取 时间倒计时
     */
    class MyCountTimer extends CountDownTimer {
        Button myButton;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountTimer(long millisInFuture, long countDownInterval,Button button) {
            super(millisInFuture, countDownInterval);
            myButton =button;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            myButton.setText((millisUntilFinished/1000)+"秒后重新发送");
            myButton.setTextSize(15);
            myButton.setEnabled(false); //设置按钮不能点击
        }
        @Override
        public void onFinish() {
            myButton.setText("获取验证码");
        }
    }
}
