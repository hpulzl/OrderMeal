package lzl.edu.com.ordermeal.View;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by admin on 2015/11/2.
 */
public class ImagePageAdapter extends PagerAdapter {
    private List<ImageView> listImages;
    private ImageView image ;

    public ImagePageAdapter(List<ImageView> list){
        listImages = list;

    }
    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    /**
     * 将选中的页面设置到view布局中
     * @param container 选中的那个页面
     * @param position 当前选中的id
     * @return view布局
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= listImages.size();  //取出view列表中要显示的项
        if(position<0)
            position = listImages.size()+position;
        ImageView image = listImages.get(position);
        ViewParent vp = image.getParent();
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        if(vp!=null){
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(image);
        }
        container.addView(image);
        return image;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager)container).removeView((View)object);
    }
}
