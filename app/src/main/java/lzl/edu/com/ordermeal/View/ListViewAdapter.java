package lzl.edu.com.ordermeal.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.ui.MenuItemActivity;

/**
 * Created by admin on 2015/11/3.
 */
public class ListViewAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private ArrayList<menu> mList_menu;
    private SwipeLayout swipeLayout;
    private LayoutInflater layout;
    private Integer num =1;
    public ListViewAdapter(Context context,ArrayList<menu> list){
        this.mContext = context;
        this.mList_menu = list;
        layout = LayoutInflater.from(context);
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        //返回SwipeLayout资源id
        return R.id.swipe_layout;
    }
    /**
     * 返一个新的item layout布局
     * @param position
     * @param viewGroup
     * @return
     */
    @Override
    public View generateView(final int position, ViewGroup viewGroup) {   //此处有些bug。position
        View view = null;
        view  =  layout.inflate(R.layout.listview_swipe_item,null);
         swipeLayout = (SwipeLayout)view.findViewById(getSwipeLayoutResourceId(position));
         swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
         swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
        final String object_id = mList_menu.get(position).getObjectId();
        view.findViewById(R.id.collect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MenuItemActivity().collectMenu(mContext,object_id);
            }
        });
        view.findViewById(R.id.order_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MenuItemActivity().addOrderMeal(mContext, num,object_id);
            }
        });
        return view;
    }

    @Override
    public void fillValues( int position, View view) {
        MenuHolder mh ;
            mh = new MenuHolder();
            mh.m_name = (TextView) view.findViewById(R.id.mealName_tv);
            mh.m_describe = (TextView) view.findViewById(R.id.mealDescribe_tv);
            mh.m_price = (TextView) view.findViewById(R.id.mealPrice_tv);
            mh.m_image = (ImageView) view.findViewById(R.id.picture_iv);
            view.setTag(mh);
        mh.m_name.setText(mList_menu.get(position).getM_name());
        mh.m_price.setText("￥" + mList_menu.get(position).getM_price());
        mh.m_describe.setText(mList_menu.get(position).getM_describe());
        mList_menu.get(position).getM_image().loadImage(mContext, mh.m_image, 100, 80);
    }
    @Override
    public int getCount() {
        return mList_menu.size();
    }

    @Override
    public Object getItem(int position) {
        return mList_menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MenuHolder {
        TextView m_name;
        TextView m_describe;
        TextView m_price;
        ImageView m_image;
    }
}
