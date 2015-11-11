package lzl.edu.com.ordermeal.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lzl.edu.com.ordermeal.R;
import lzl.edu.com.ordermeal.View.ImagePageAdapter;
import lzl.edu.com.ordermeal.View.ListViewAdapter;
import lzl.edu.com.ordermeal.javabean.menu;
import lzl.edu.com.ordermeal.util.ToastUtil;


/**
 * Created by admin on 2015/11/8.
 */
public class TabHomeFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener{
    private static final String HOME_TAG = "TabHomeFragment....";
    private Spinner mSp_search;
    public ViewPager viewPager;
    private List<ImageView> list_Images;
    private LayoutInflater layout;
    private ImageView image;
    private ImagePageAdapter pageAdapter;
    private int arr[] = {R.mipmap.image1,R.mipmap.image2,R.mipmap.image3};
    private final int IMAGE_CHANGE = 1;
    private final long DELAY_TIME=2000;
    private int i=0;
    private ListView mSwipe_lv;
    private ListViewAdapter listViewAdapter;
    private SwipeRefreshLayout mFresh_swipe;
    private List<menu> list_menu = new ArrayList<>();
    private List<menu> list_KindMenu = new ArrayList<>();
    private final int limit = 10;
    private int currentPage = 0;
    private String str_name;
    private Intent intent = new Intent();
    public static final String KEY_MENU = "key_menu";
    private long exitTime=0;
    private Context mContext;
    private Activity activity;
    private View view;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IMAGE_CHANGE:
                    if(i>=3)
                        i %=3;
//                    Log.i("TabHomePage",i+"------");
                    viewPager.setCurrentItem(i);
                    handler.sendEmptyMessageDelayed(IMAGE_CHANGE, DELAY_TIME);
                    i++;
                    break;
                default:
                    break;
            }
        }
    };
 /*   public TabHomeFragment(){
        Log.i(HOME_TAG,"创建实例");
       onCreateView(null,null,null);
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getActivity().getApplicationContext();    //获取上下文实例
        activity = this.getActivity();
        view = inflater.inflate(R.layout.activity_tab_home_page,container,false);
         init(view);
        return view;
    }
    private void init(View view){
        mSp_search = (Spinner)view.findViewById(R.id.meals_sp);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mSwipe_lv = (ListView) view.findViewById(R.id.swipe_lv);
        mFresh_swipe = (SwipeRefreshLayout) view.findViewById(R.id.fresh_swipe);

        //设置图片轮播
        list_Images = getList(mContext);
        pageAdapter = new ImagePageAdapter(list_Images);
        viewPager.setAdapter(pageAdapter);
        handler.sendEmptyMessageDelayed(IMAGE_CHANGE, DELAY_TIME);
        //Spinner选择口味
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,R.array.meals_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_search.setAdapter(adapter);

        //设置监听
        mSp_search.setOnItemSelectedListener(new MySpinnerListener());
        mSwipe_lv.setOnScrollListener(this);
        mSwipe_lv.setOnItemClickListener(this);
        mFresh_swipe.setOnRefreshListener(this);
    }

    /**
     * 设置图片并存放到list集合中
     */
    private List<ImageView> getList(Context context){
        List<ImageView> list = new ArrayList<>();
        layout = LayoutInflater.from(context);
        for(int i=0;i<3;i++){
            LinearLayout linearLayout = (LinearLayout) layout.inflate(R.layout.imageview_item,null);
            image = (ImageView) linearLayout.findViewById(R.id.viewpager_iv);
            image.setImageResource(arr[i]);
            list.add(image);
        }
        return list;
    }

    /**
     * 通过种类获取菜
     */
    public void findMealByKind(String kind){
        list_menu.clear();
        BmobQuery<menu> bq = new BmobQuery<>();
        bq.addWhereEqualTo("m_kind", kind);
        if(list_KindMenu.size()>0) {
            list_KindMenu.clear();
        }
        bq.findObjects(getActivity(), new FindListener<menu>() {
            @Override
            public void onSuccess(List<menu> list) {
                if (list.size() > 0) {
                    list_KindMenu.addAll(list);
                } else {
                    mSwipe_lv.setEmptyView(getActivity().findViewById(R.id.empty_view));
                    return;
                }
                //将所有数据显示到listView中
                listViewAdapter = new ListViewAdapter(activity, (ArrayList<menu>) list_KindMenu);
                mSwipe_lv.setAdapter(listViewAdapter);
                currentPage++;
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.toast(activity, "加载数据失败...");
            }
        });
    }

    /**
     * 监听下拉刷新
     */
    @Override
    public void onRefresh() {
        mFresh_swipe.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFresh_swipe.setRefreshing(false);
                findMeals(currentPage);
            }
        }, 1000);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    /**
     * 获得所用的菜品
     */
    public void findMeals( final int page) {
        list_KindMenu.clear();
        final BmobQuery<menu> menu_all = new BmobQuery<menu>();
        menu_all.setLimit(limit);
        menu_all.setSkip(page * limit); //设置下次从第几个开始
        menu_all.findObjects(getActivity(), new FindListener<menu>() {
            boolean flag = false;

            @Override
            public void onSuccess(List<menu> list) {
                if (list.size() > 0) {
                        list_menu.addAll(list);
                } else {
                    ToastUtil.toast(getActivity(), "没有更多数据了");
                    flag = true;
                }
                if (currentPage > 0) {
                    listViewAdapter.notifyDataSetChanged();
                }
                //将所有数据显示到listView中
                if (!flag) {
                    listViewAdapter = new ListViewAdapter(activity, (ArrayList<menu>) list_menu);
                    mSwipe_lv.setAdapter(listViewAdapter);
                    currentPage++;
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(HOME_TAG, "访问失败：：" + i + " 错误原因：：" + s);
            }
        });
    }

    /**
     * 此处用来解决ListView下拉刷新与SwipeRefreshLayout下拉加载数据之间的监听冲突的
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem == 0 && (str_name==null||"口味".equals(str_name))) {  //如果listview滑动到最上端，那么设置可以下拉刷新
            mFresh_swipe.setEnabled(true);
        }
        else {   //否则不可以下拉刷新
            mFresh_swipe.setEnabled(false);
        }
    }

    /**
     * 设置listView中每个item的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        menu menu ;
        if("口味".equals(str_name)||str_name.isEmpty()) {
            menu= list_menu.get(position);
        }else{
            menu = list_KindMenu.get(position);
        }
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("key_menu", menu);  //将menu对象存储到bundle中
        intent.putExtras(mBundle);   //放入到intent中，发送给另一个activity
        intent.setClass(this.getActivity(), MenuItemActivity.class);
        startActivity(intent);
    }

    class MySpinnerListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView)view;
            tv.setTextColor(Color.WHITE);  //设置字体颜色
            tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);  //设置居中
            tv.setTextSize(16);
            str_name = tv.getText().toString();
            if(!str_name.equals("口味")&&!str_name.isEmpty()) {
                findMealByKind(str_name);
            }else if(str_name.equals("口味")){  //str_name不为空并且选择“口味”。。
                currentPage = 0;
                findMeals(currentPage);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
