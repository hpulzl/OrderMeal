package lzl.edu.com.ordermeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.util.IPTimeStamp;
import lzl.edu.com.ordermeal.util.ToastUtil;

import static lzl.edu.com.ordermeal.R.id.camera_btn;

/**
 * Created by admin on 2015/11/8.
 */
public class TabMineFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Toolbar toolbar;
    private Activity activity;
    private Context mContext;
    private LinearLayout mLinCollect,mLinMsg,mLinShop,mLinBack,mRLOrder;
    private RelativeLayout mRLGoods,mRLIntegral;
    private CircleImageView mIVHead;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Button mealReturn_btn;
    private TextView meal_name;
    private Intent mIntent;
    private Customers customers ;
     String fileName = null;
    private static final int SET_NUM = 1;
    private ProgressBar mine_pb;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SET_NUM:
                    BmobFile images = (BmobFile) msg.obj;
                    images.loadImage(mContext,mIVHead);
                    mine_pb.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mine, container, false);
        activity = this.getActivity();
        mContext = this.getActivity().getApplicationContext();
        mIntent = new Intent();
        customers = Customers.getCurrentUser(mContext,Customers.class);
        initViews();
        return view;
    }
    private void initViews(){
        mLinCollect = (LinearLayout) view.findViewById(R.id.lin_collect);
        mLinMsg = (LinearLayout) view.findViewById(R.id.lin_msg);
        mLinShop = (LinearLayout) view.findViewById(R.id.lin_shop);
        mLinBack = (LinearLayout) view.findViewById(R.id.lin_back);
        mRLGoods = (RelativeLayout) view.findViewById(R.id.rl_goods);
        mRLIntegral = (RelativeLayout) view.findViewById(R.id.rl_integral);
        mRLOrder = (LinearLayout) view.findViewById(R.id.lin_order);
        mIVHead = (CircleImageView) view.findViewById(R.id.iv_head);
        mealReturn_btn = (Button) view.findViewById(R.id.mealReturn_btn);
        meal_name = (TextView) view.findViewById(R.id.meal_name);
        mine_pb = (ProgressBar) view.findViewById(R.id.mine_pb);

        //获取user的信息
        findUserInfo();

        mealReturn_btn.setVisibility(View.GONE);
        meal_name.setText("个人中心");
        mLinBack.setOnClickListener(this); //点击返回
        mRLOrder.setOnClickListener(this);  //点击查看订餐情况
        mLinCollect.setOnClickListener(this); //点击查看收藏
        mIVHead.setOnClickListener(this);  //点击更换头像
    }
    /**
     * 获取user的所有信息
     */
    public void findUserInfo(){
        if (customers.getU_nick()!=null){
            customers.getU_nick().loadImage(mContext,mIVHead);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:  //点击返回
                mIntent.setClass(activity,LoginActivity.class);
                BmobUser.logOut(mContext);   //清除缓存用户对象
                startActivity(mIntent);
                activity.finish();
                break;
            case R.id.lin_order:  //点击查看订餐情况
                break;
            case R.id.lin_collect:  //点击查看收藏
                mIntent.setClass(mContext,FindCollectActivity.class);
                startActivity(mIntent);
                break;
            case R.id.iv_head:  //点击更换头像
                new PopupWindows(mContext,v);
                break;
        }
    }

    /**
     * 将Uri格式转化成Sting类型
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:  //表示通过访问图库获取图片
                String fileAName = getRealPathFromURI(data.getData());
                if(data!=null) {
                    File file = new File(getRealPathFromURI(data.getData()));
                    if (file != null) {
                        upLoadImage(mContext, file);
                    }
                }
                break;
            case 2://表示通过拍照获取图片
                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();

                File pfile = new File(baseDir+File.separator + fileName+".jpg");
                if(pfile!=null){
                    upLoadImage(mContext,pfile);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void upLoadImage(final Context context,File file){
        mine_pb.setVisibility(View.VISIBLE);  //设置进度条
        final BmobFile image = new BmobFile(file);
        image.upload(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Customers cus = new Customers();
                cus.setU_nick(image);
                BmobUser bmobCus = cus.getCurrentUser(context);
                cus.update(context, bmobCus.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Message msg = new Message();
                        msg.obj = image;
                        msg.what = SET_NUM;
                        mHandler.sendMessage(msg);
                    }

                   @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.toast(context,"更换头像失败");
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.toast(context, "更换头像失败");
            }
        });
    }
    class PopupWindows extends PopupWindow implements View.OnClickListener{
        Button mCamera,mFromPicture,mCancel;
        LinearLayout ll_popWindow;
        public PopupWindows(Context context,View parent){
        View view = View.inflate(context,R.layout.popwindow,null);
            view.startAnimation(AnimationUtils.loadAnimation(context,
                     R.anim.push_bottom_in_2));
            ll_popWindow = (LinearLayout) view
                    .findViewById(R.id.ll_popWindow);
            ll_popWindow.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            mCamera = (Button) view.findViewById(camera_btn);
            mFromPicture = (Button) view.findViewById(R.id.fromPicture_btn);
            mCancel = (Button) view.findViewById(R.id.cancel_btn);

            mCamera.setOnClickListener(this);
            mFromPicture.setOnClickListener(this);
            mCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.camera_btn:  //调用手机拍照功能
                    takePhoto();
                    break;
                case R.id.fromPicture_btn:  //调用相册
                    getPictures();
                    break;
                case R.id.cancel_btn:  //取消
                    dismiss();
                    break;
            }
        }

        /**
         * 调用手机拍照功能
         */
        public void takePhoto(){
            fileName = new IPTimeStamp().getIPTimeRandName();
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(),
                            fileName+".jpg")));
            startActivityForResult(intent, 2);
            dismiss();     //关闭Pop窗口
        }

        /**
         * 调用相册
         */
        public void getPictures(){

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");// 调用android的图库
            startActivityForResult(intent, 1);
            dismiss();
        }
    }
}
