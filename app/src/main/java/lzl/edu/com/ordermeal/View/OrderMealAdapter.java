package lzl.edu.com.ordermeal.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.javabean.ordermeal;

/**
 * Created by admin on 2015/11/7.
 */
public class OrderMealAdapter extends BaseAdapter{
    public Context mContext;
    public List<ordermeal> list_orderMeal;
    private LayoutInflater inflater;
    private OrderMealHolder omHolder;
    public OrderMealAdapter(Context context,ArrayList<ordermeal> list){
        this.mContext = context;
        this.list_orderMeal = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list_orderMeal.size();
    }

    @Override
    public Object getItem(int position) {
        return list_orderMeal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.i("orderAdapter",position+"------");
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.ordermeal_item, null);
            omHolder = new OrderMealHolder();
            omHolder.orderImage_iv = (ImageView) v.findViewById(R.id.orderImage_iv);
            omHolder.orderName_tv = (TextView) v.findViewById(R.id.orderName_tv);
            omHolder.orderTime_tv = (TextView) v.findViewById(R.id.orderTime_tv);
            omHolder.orderPrice_tv = (TextView) v.findViewById(R.id.orderPrice_tv);
            omHolder.orderNum_tv = (TextView) v.findViewById(R.id.order_num);
            v.setTag(omHolder);
        } else {
            omHolder = (OrderMealHolder) v.getTag();
        }
        menu m = list_orderMeal.get(position).getO_menu();
        m.getM_image().loadImage(mContext, omHolder.orderImage_iv);
        String price = m.getM_price();
        omHolder.orderPrice_tv.setText("￥" + price);
        omHolder.orderName_tv.setText(m.getM_name());
        omHolder.orderNum_tv.setText(list_orderMeal.get(position).getO_number()+"份" +
                "");
        String time = list_orderMeal.get(position).getCreatedAt();
        omHolder.orderTime_tv.setText(time.substring(0, 11));

        return v;
    }

    class OrderMealHolder{
        TextView orderPrice_tv,orderName_tv,orderTime_tv,orderNum_tv;
        ImageView orderImage_iv;

    }
}
