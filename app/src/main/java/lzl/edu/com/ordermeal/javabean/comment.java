package lzl.edu.com.ordermeal.javabean;

import java.io.Serializable;
import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by admin on 2015/10/29.
 * 关于菜品的一些评论
 */
public class comment extends BmobObject implements Serializable{
    private static final long serialVersionUID = -6919461967497580385L;

    private menu p_menu;   //菜的评论，这里是一对多的关系
    private Customers p_customer; //用户对菜的评论，这里是一对多的关系
    private String p_content;  //评论的内容
    private Date p_time;  //评论的时间
    public comment(){
        this.setTableName("comment");
    }

    public menu getP_menu() {
        return p_menu;
    }

    public void setP_menu(menu p_menu) {
        this.p_menu = p_menu;
    }

    public Customers getP_customer() {
        return p_customer;
    }

    public void setP_customer(Customers p_customer) {
        this.p_customer = p_customer;
    }

    public String getP_content() {
        return p_content;
    }

    public void setP_content(String p_content) {
        this.p_content = p_content;
    }

    public Date getP_time() {
        return p_time;
    }

    public void setP_time(Date p_time) {
        this.p_time = p_time;
    }

    @Override
    public String toString() {
        return "comment{" +
                "p_menu=" + p_menu +
                ", p_customer=" + p_customer +
                ", p_content='" + p_content + '\'' +
                ", p_time=" + p_time +
                '}';
    }
}
