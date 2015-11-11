package lzl.edu.com.ordermeal.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by admin on 2015/10/29.
 */
public class Customers extends BmobUser implements Serializable{
    private static final long serialVersionUID = -6919461967497580385L;

    private Integer u_age;
    private Boolean u_sex;
    private BmobFile u_nick;

    public Customers(){
        this.setTableName("User");
    }

    public Customers(Integer u_age, Boolean u_sex, BmobFile u_nick) {
        this.u_age = u_age;
        this.u_sex = u_sex;
        this.u_nick = u_nick;
    }

    public Integer getU_age() {
        return u_age;
    }

    public void setU_age(Integer u_age) {
        this.u_age = u_age;
    }

    public Boolean getU_sex() {
        return u_sex;
    }

    public void setU_sex(Boolean u_sex) {
        this.u_sex = u_sex;
    }

    public BmobFile getU_nick() {
        return u_nick;
    }

    public void setU_nick(BmobFile u_nick) {
        this.u_nick = u_nick;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "u_age=" + u_age +
                ", u_sex=" + u_sex +
                ", u_nick='" + u_nick + '\'' +
                '}';
    }
}