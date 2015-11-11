package lzl.edu.com.ordermeal.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.Customers;
import lzl.edu.com.ordermeal.javabean.comment;

/**
 * Created by admin on 2015/11/6.
 */
public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<comment> list_comment;
    private LayoutInflater inflater;
    public CommentAdapter(Context context,ArrayList<comment> list){
        this.mContext = context;
        this.list_comment = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list_comment.size();
    }

    @Override
    public Object getItem(int position) {
        return list_comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CommentHolder comHolder;
        if(view ==null){
            view = inflater.inflate(R.layout.getcontent_item,null);
            comHolder = new CommentHolder();
            comHolder.userImage = (ImageView) view.findViewById(R.id.c_userImage_iv);
            comHolder.userPhone = (TextView) view.findViewById(R.id.c_userPhone_tv);
            comHolder.content = (TextView) view.findViewById(R.id.c_content_tv);
            comHolder.com_time = (TextView) view.findViewById(R.id.c_time_tv);

            view.setTag(comHolder);
        }else{
           comHolder = (CommentHolder) view.getTag();
        }
        Customers cus = list_comment.get(position).getP_customer();  //获取用户对象
        comHolder.userPhone.setText(cus.getUsername());
        cus.getU_nick().loadImage(mContext, comHolder.userImage);
        comHolder.content.setText(list_comment.get(position).getP_content());
        String time = list_comment.get(position).getUpdatedAt().substring(0,10);
        comHolder.com_time.setText(time);
        return view;
    }
    class CommentHolder{
        ImageView userImage;
        TextView userPhone,content,com_time;
    }
}
