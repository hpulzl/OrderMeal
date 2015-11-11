package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.View.CollectSwipeAdapter;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.javabean.collect;
import lzl.edu.com.ordermeal.util.ToastUtil;

public class FindCollectActivity extends Activity implements View.OnClickListener{

    private Context mContext;
    private CollectSwipeAdapter colAdapter;
    private List<collect> list_collect = new ArrayList<>();
    private ListView collect_lv;
    private TextView titleName;
    private Button return_btn;
    private final int SEND_MSG = 1;
    private final int RECEIVE_MAG = 2;
    public static final int STATE_A = 1;
    public static final int STATE_B = 2;

    private ProgressBar collect_pb;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_MSG:
                    list_collect = (List<collect>) msg.obj;
                    colAdapter = new CollectSwipeAdapter(mContext,(ArrayList<collect>)list_collect);
                    collect_lv.setAdapter(colAdapter);
                    collect_pb.setVisibility(View.GONE);
                    break;
                case RECEIVE_MAG:
                   /* list_collect = (List<collect>) msg.obj;
                    colAdapter = new CollectSwipeAdapter(mContext,(ArrayList<collect>)list_collect);
                    collect_lv.setAdapter(colAdapter);*/
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_collect);
        mContext = FindCollectActivity.this;
        init();
        //查询收藏列表
        findCollect(mContext,STATE_A);
    }
        private void init(){
            collect_lv = (ListView) findViewById(R.id.collect_lv);
            titleName = (TextView) findViewById(R.id.meal_name);
            return_btn = (Button) findViewById(R.id.mealReturn_btn);
            collect_pb = (ProgressBar) findViewById(R.id.collect_pb);
            titleName.setText("我的收藏");
            collect_pb.setVisibility(View.VISIBLE);

            return_btn.setOnClickListener(this);
        }


    /**
     * 查看用户收藏的菜。已经所有信息
     * @param context
     */
    public void findCollect(final Context context,final int state){
        Customers cus = BmobUser.getCurrentUser(context, Customers.class);
        BmobQuery<collect> bqCollect = new BmobQuery<>();
        bqCollect.addWhereEqualTo("customer_menu", new BmobPointer(cus));
        bqCollect.include("c_menu");
        bqCollect.addWhereEqualTo("c_flag", true);
        if(!list_collect.isEmpty()){
            list_collect.clear();
        }
        bqCollect.findObjects(context, new FindListener<collect>() {
            @Override
            public void onSuccess(List<collect> list) {
                Message msg = new Message();
                if (list.size() > 0) {
                    list_collect.addAll(list);
                    msg.what =SEND_MSG;
               /* if(state == STATE_A) {
                    msg.what = SEND_MSG;
                }else if(state == STATE_B){
                    msg.what =RECEIVE_MAG;
                }*/
                    msg.obj = list_collect;
                    mHandler.sendMessage(msg);
                }
            }
            @Override
            public void onError(int i, String s) {
                ToastUtil.toast(context, "查询失败");
            }
        });
    }
    public void cancleCollect(final Context context,String collect_id){
        collect col = new collect();
        col.setObjectId(collect_id);
        col.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                findCollect(context,STATE_B);
                ToastUtil.toast(context, "您取消了收藏");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.toast(context, "取消收藏失败");
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mealReturn_btn:
                finish();
                break;
        }
    }
}
