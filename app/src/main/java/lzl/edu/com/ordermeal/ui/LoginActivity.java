package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.operate.CustOperate;
import lzl.edu.com.ordermeal.util.ToastUtil;

public class LoginActivity extends Activity {
    private Button mBtn_Login;    //登录按钮
    private TextView mTv_ForgetPass,mTv_Register;  //注册、忘记密码文本
    private EditText mEtv_PhoneNumber,mEtv_Password; //手机号，密码编辑框
    private Intent intent = new Intent();
    private String phoneNumber;
    private String passWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        mBtn_Login = (Button) findViewById(R.id.login_btn);
        mEtv_Password = (EditText) findViewById(R.id.myPassword_et);
        mEtv_PhoneNumber = (EditText) findViewById(R.id.myPhoneNumber_et);
        mTv_ForgetPass = (TextView) findViewById(R.id.forgetPass_tv);
        mTv_Register = (TextView) findViewById(R.id.regist_tv);


        //设置监听
        mTv_ForgetPass.setOnClickListener(new myListener());   //忘记密码监听，跳转到找回密码界面
        mBtn_Login.setOnClickListener(new myListener());     //登录按钮监听，登录成功后跳转到主界面
        mTv_Register.setOnClickListener(new myListener());  //注册监听，跳转到注册界面
    }
    class myListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_btn: //登录按钮监听，登录成功后跳转到主界面
                    passWord = mEtv_Password.getText().toString();
                    phoneNumber = mEtv_PhoneNumber.getText().toString();
                    if(passWord.isEmpty()&&phoneNumber.isEmpty()){
                        ToastUtil.toast(LoginActivity.this,"用户名或密码不能为空");
                        return;
                    }
                    CustOperate co = new CustOperate();
                     co.CustLogin(LoginActivity.this,phoneNumber, passWord);
                    break;
                case R.id.regist_tv: //注册监听，跳转到注册界面
                    intent.setClass(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.forgetPass_tv: //忘记密码监听，跳转到找回密码界面
                    ToastUtil.toast(LoginActivity.this,"忘记密码了？");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
