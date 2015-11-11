package lzl.edu.com.ordermeal.javabean;

import java.io.Serializable;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by admin on 2015/10/29.
 */
public class ordermeal extends BmobObject implements Serializable{
    private static final long serialVersionUID = -6919461967497580385L;

    private menu o_menu;//用户点餐，//一个用户可以点多种类型的菜。一对多
    private Customers o_customer;  //用户点餐，一个用户可以
    private Boolean o_flag; // 是否订餐
    private Date o_time;  //订餐时间
    private Integer o_number; //订餐数量
    private String o_price;//总金额

    public ordermeal(){
        this.setTableName("ordermeal");
    }

    public Integer getO_number() {
        return o_number;
    }

    public void setO_number(Integer o_number) {
        this.o_number = o_number;
    }

    public String getO_price() {
        return o_price;
    }

    public void setO_price(String o_price) {
        this.o_price = o_price;
    }

    public Customers getO_customer() {
        return o_customer;
    }

    public void setO_customer(Customers o_customer) {
        this.o_customer = o_customer;
    }

    public Boolean getO_flag() {
        return o_flag;
    }

    public void setO_flag(Boolean o_flag) {
        this.o_flag = o_flag;
    }

    public Date getO_time() {
        return o_time;
    }

    public void setO_time(Date o_time) {
        this.o_time = o_time;
    }

    public menu getO_menu() {
        return o_menu;
    }

    public void setO_menu(menu o_menu) {
        this.o_menu = o_menu;
    }

    @Override
    public String toString() {
        return "OrderMeal{" +
                "o_menu=" + o_menu +
                ", o_customer=" + o_customer +
                ", o_seat='" + o_flag + '\'' +
                ", o_time=" + o_time +
                '}';
    }
}
