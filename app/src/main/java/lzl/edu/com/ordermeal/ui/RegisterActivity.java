package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.operate.CustOperate;
import lzl.edu.com.ordermeal.util.ToastUtil;

public class RegisterActivity extends Activity {

    private EditText mEt_Phone,mEt_Password,mEt_Code;
    private Button mBtn_Obtain,mBtn_register,mBtn_Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }
    private void init(){
        mEt_Phone = (EditText) findViewById(R.id.myPhoneNumber_et);
        mEt_Code = (EditText) findViewById(R.id.myCheckCode);
        mEt_Password = (EditText) findViewById(R.id.myPassword_et);
        mBtn_Obtain = (Button) findViewById(R.id.obtainChechCode_btn);
        mBtn_register = (Button) findViewById(R.id.register_btn);
        mBtn_Return = (Button) findViewById(R.id.return_btn);

        mBtn_register.setOnClickListener(new RegisterListener()); //注册监听，注册成功跳转至主界面。并保存数据
        mBtn_Obtain.setOnClickListener(new RegisterListener());  //获取验证码，获取后监听验证码验证超时时间
        mBtn_Return.setOnClickListener(new RegisterListener()); //返回按钮，返回到登录界面

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    class RegisterListener implements View.OnClickListener{
        String phoneNum,smsCode,password;
        Intent intent = new Intent();
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.register_btn: //注册监听，注册成功跳转至主界面。并保存数据
                    phoneNum = mEt_Phone.getText().toString();
                    password = mEt_Password.getText().toString();
                    smsCode = mEt_Code.getText().toString();
                    if(isNull(RegisterActivity.this,phoneNum,password,smsCode)) {//返回true可以进行注册了
                        CustOperate custOper = new CustOperate();
                        custOper.CustRegister(RegisterActivity.this,phoneNum, password, smsCode);
                    }
                    break;
                case R.id.obtainChechCode_btn:  //获取验证码，获取后监听验证码验证超时时间
                    phoneNum = mEt_Phone.getText().toString();
                    password = mEt_Password.getText().toString();
                    if(isNull(RegisterActivity.this,phoneNum,password)) { //返回true，可以进行获取验证码了
                        CustOperate custOper = new CustOperate();
                        //获取验证码
                        custOper.CustRequestSMSCode(RegisterActivity.this,phoneNum, mBtn_Obtain);
                    }
                    break;
                case R.id.return_btn:  //返回按钮，返回到登录界面
                    intent.setClass(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 点击获取验证码时，获取的验证信息。
     * @param context
     * @param phoneNum
     * @param password
     * @return
     */
    public boolean isNull(Context context,String phoneNum,String password) {
        if(phoneNum.isEmpty()){
            ToastUtil.toast(context,"手机号不能为空");
            return false;
        }else if(password.isEmpty()){
            ToastUtil.toast(context,"密码不能为空");
            return false;
        }
        return true;
    }
    /**
     * 点击登录时，获取的验证信息。
     * @param context
     * @param phoneNum
     * @param password
     * @param smsCode
     * @return
     */
    public boolean isNull(Context context,String phoneNum,String password,String smsCode){
        if(phoneNum.isEmpty()){
            ToastUtil.toast(context,"手机号不能为空");
            return false;
        }else if(password.isEmpty()){
            ToastUtil.toast(context,"密码不能为空");
            return false;
        }else if(smsCode.isEmpty()){
            ToastUtil.toast(context,"验证码不能为空");
            return false;
        }
        return true;
    }
}
