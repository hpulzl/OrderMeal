package lzl.edu.com.ordermeal.javabean;

import java.io.Serializable;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by admin on 2015/10/29.
 * 收藏菜
 */
public class collect extends BmobObject implements Serializable{
    private static final long serialVersionUID = -6919461967497580385L;

    private Date c_time; //收藏的时间
    private menu c_menu; // 收藏的菜的所有信息，这里是一对一的关系
    private Customers customer_menu;//用户收藏菜单。这里是一对多的关系
    private Boolean c_flag; //是否收藏。也可以取消收藏

    public collect(){
        this.setTableName("collect");
    }

    public Date getC_time() {
        return c_time;
    }

    public void setC_time(Date c_time) {
        this.c_time = c_time;
    }

    public menu getC_menu() {
        return c_menu;
    }

    public void setC_menu(menu c_menu) {
        this.c_menu = c_menu;
    }

    public Customers getCustomer_menu() {
        return customer_menu;
    }

    public void setCustomer_menu(Customers customer_menu) {
        this.customer_menu = customer_menu;
    }

    public Boolean getC_flag() {
        return c_flag;
    }

    public void setC_flag(Boolean c_flag) {
        this.c_flag = c_flag;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "c_time=" + c_time +
                ", c_menu=" + c_menu +
                ", customer_menu=" + customer_menu +
                ", c_flag=" + c_flag +
                '}';
    }
}
