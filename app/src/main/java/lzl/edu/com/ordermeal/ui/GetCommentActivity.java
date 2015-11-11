package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.View.CommentAdapter;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.javabean.comment;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.util.ToastUtil;

public class GetCommentActivity extends Activity implements View.OnClickListener{
    private static final String GET_TAG = "GetCommentActivity...";
    private EditText mItemComment_et;
    private Button mItemSubmit_btn,mMealReturn_btn;
    private ListView mItemInfo_lv;
    private  String meal_id;
    private String meal_name;
    private TextView mMeal_name_tv;
    private ProgressBar mComment_pb;
    private List<comment> mList_comment = new ArrayList<>();
    private CommentAdapter comAdapter;
    private static final int FIRST_DATA = 1;
    private static final int SECOND_DATA =2;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FIRST_DATA:
                    mList_comment = (List<comment>) msg.obj;
                    comAdapter = new CommentAdapter(GetCommentActivity.this,(ArrayList<comment>)mList_comment);
                    mItemInfo_lv.setAdapter(comAdapter);
                    mItemComment_et.setText(null);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_comment);
        //获取传输进来的数据
         meal_id = getIntent().getStringExtra(MenuItemActivity.DATA_ID);
        meal_name = getIntent().getStringExtra(MenuItemActivity.DATA_MEAL_NAME);

        init();
    }

    /**
     * 初始化布局
     */
    public void init(){
        mItemComment_et = (EditText) findViewById(R.id.item_comment_et);
        mItemSubmit_btn = (Button) findViewById(R.id.item_submit_btn);
        mItemInfo_lv = (ListView) findViewById(R.id.item_comment_lv);
        mMeal_name_tv = (TextView) findViewById(R.id.meal_name);
        mMealReturn_btn = (Button) findViewById(R.id.mealReturn_btn);
        mComment_pb = (ProgressBar) findViewById(R.id.comment_pb);

        if(meal_name!=null){
            mMeal_name_tv.setText(meal_name+"的评论");
            mMeal_name_tv.setTextSize(20);
        }
        //获取评论信息
        getComment();
        mItemSubmit_btn.setOnClickListener(this);
        mMealReturn_btn.setOnClickListener(this);
    }
    /**
     * 添加用户的评论，需要获取用户对象，以及菜品的对象
     * @param comment
     */
    private void setComment(String comment){
        Customers customers = BmobUser.getCurrentUser(this,Customers.class);
        menu m = new menu();
        m.setObjectId(meal_id);
        comment comm = new comment();
        comm.setP_customer(customers);
        comm.setP_menu(m);
        comm.setP_content(comment);
        comm.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(GET_TAG, "添加成功");
                ToastUtil.toast(GetCommentActivity.this, "评论成功");
                mComment_pb.setVisibility(View.GONE);
                getComment();  //再次获取评论
            }
            @Override
            public void onFailure(int i, String s) {
                Log.i(GET_TAG, "错误码：" + i + " 错误信息：" + s);
            }
        });
    }

    /**
     * 获取评论内容.
     * 查询评论该菜的信息和评论该菜的用户的信息
     */
    private void getComment(){
        if(mList_comment.size()>0){
            mList_comment.clear();
        }
        BmobQuery<comment> bq = new BmobQuery<>();
        menu m = new menu();
        m.setObjectId(meal_id);
        bq.addWhereEqualTo("p_menu", new BmobPointer(m));
        bq.include("p_customer,p_menu");
        bq.findObjects(this, new FindListener<comment>() {
            @Override
            public void onSuccess(List<comment> list) {
                if(list.size()>0) {
                    mList_comment.addAll(list);
                    Message msg = new Message();
                        msg.obj = mList_comment;
                        msg.what = FIRST_DATA;
                        mHandler.sendMessage(msg);
                }
            }
            @Override
            public void onError(int i, String s) {
            Log.i(GET_TAG,s+"查询失败："+" 错误码："+i);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_submit_btn:
            String commentInfo = mItemComment_et.getText().toString();
            mComment_pb.setVisibility(View.VISIBLE);
            if(commentInfo.isEmpty()){  //如果信息为空，给出提示
                ToastUtil.toast(this,"内容不能为空!");
                return ;
            }
            setComment(commentInfo);
                break;
            case R.id.mealReturn_btn:
               finish();
                break;
        }
    }
}
