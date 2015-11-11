package lzl.edu.com.ordermeal.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by admin on 2015/10/29.
 */
public class menu extends BmobObject implements Serializable {
    private static final long serialVersionUID = -6919461967497580385L;
    private BmobFile m_image;  //菜单中的图片
    private String m_name;    //菜名
    private String m_kind;   //菜的种类
    private String m_describe; //菜的描述
    private String m_price; //菜的价格
    private Integer m_commentNum; //评论总数
    public menu(){

        //this.setTableName("menu");
    }

    public Integer getM_commentNum() {
        return m_commentNum;
    }

    public void setM_commentNum(Integer m_commentNum) {
        this.m_commentNum = m_commentNum;
    }

    public BmobFile getM_image() {
        return m_image;
    }

    public void setM_image(BmobFile m_image) {
        this.m_image = m_image;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_kind() {
        return m_kind;
    }

    public void setM_kind(String m_kind) {
        this.m_kind = m_kind;
    }

    public String getM_describe() {
        return m_describe;
    }

    public void setM_describe(String m_describe) {
        this.m_describe = m_describe;
    }

    @Override
    public String toString() {
        return "menu{" +
                "m_image=" + m_image +
                ", m_name='" + m_name + '\'' +
                ", m_kind='" + m_kind + '\'' +
                ", m_describe='" + m_describe + '\'' +
                ",m_price="+m_price+
                '}';
    }
}
