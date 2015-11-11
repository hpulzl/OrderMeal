package lzl.edu.com.ordermeal.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.collect;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.ui.FindCollectActivity;

/**
 * Created by admin on 2015/11/9.
 */
public class CollectSwipeAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<collect> list_collect;
    private LayoutInflater inflater;
    private SwipeLayout swipeLayout;
    private FindCollectActivity activity;

    public CollectSwipeAdapter(Context context,ArrayList<collect> list){
        Log.i("adapter.....",context.toString());
        this.mContext = context;
        this.activity = activity;
        this.list_collect = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.collect_swipe_layout;
    }

    @Override
    public View generateView(final int i, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.collect_item,null);
        swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(i));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);
        v.findViewById(R.id.collect_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String collect_id = list_collect.get(i).getObjectId();
                activity.cancleCollect(mContext,collect_id);
//                new FindCollectActivity().cancleCollect(mContext,collect_id);

            }
        });
        return v;
    }

    @Override
    public void fillValues(int i, View view) {
        CollectHolder colHolder = new CollectHolder();
        colHolder.itemMealName = (TextView) view.findViewById(R.id.item_mealName_tv);
        colHolder.itemDescribe = (TextView) view.findViewById(R.id.item_mealDescribe_tv);
        colHolder.itemPrice = (TextView) view.findViewById(R.id.item_mealPrice_tv);
        colHolder.itemTime = (TextView) view.findViewById(R.id.item_collectTime_tv);
        colHolder.itemPicture = (ImageView) view.findViewById(R.id.item_collect_iv);

        menu m = list_collect.get(i).getC_menu();
        String name = m.getM_name();
        String price = m.getM_price();
        String time = list_collect.get(i).getUpdatedAt().substring(0, 10);
        String describe = m.getM_describe();
        BmobFile image = m.getM_image();


        colHolder.itemMealName.setText(name);
        colHolder.itemDescribe.setText(describe);
        colHolder.itemPrice.setText("￥"+price);
        colHolder.itemTime.setText(time);
        image.loadImage(mContext,colHolder.itemPicture);
    }

    @Override
    public int getCount() {
        return list_collect.size();
    }

    @Override
    public Object getItem(int position) {
        return list_collect.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class CollectHolder{
        TextView itemMealName,itemDescribe,itemPrice,itemTime;
        ImageView itemPicture;
    }
}
