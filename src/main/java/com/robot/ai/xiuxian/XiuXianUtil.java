package com.robot.ai.xiuxian;



import com.robot.ai.jdbc.Bean.User;
import com.robot.ai.jdbc.BeanHandler;
import com.robot.ai.jdbc.BeanListHandler;
import com.robot.ai.jdbc.CRUDTemplate;
import com.robot.ai.xiuxian.bean.BackPack;
import com.robot.ai.xiuxian.bean.XiaoGuai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XiuXianUtil {

    //查询用户是否存在
    public static int isUser(String qq){
        //查询用户是否存在的sql
        String sql = "select id from user where id = ?";
        //调用工具类查询用户id是否存在
        return CRUDTemplate.executeQueryID(sql, qq);
    }

    //用户注册
    public static int isRole(String qq,String name,int zizhi){
        //注册用户sql
        String sql = "insert into user(id,username,zizhi) values (?,?,?)";
        System.out.println("资质为"+zizhi);
        //调用工具类添加数据返回0为失败1为成功
        return CRUDTemplate.executeUpdate(sql, qq, name, zizhi);
    }

    //获取角色信息
    public static User getUser(String qq){
        //获取用户信息sql
        String sql = "select * from user where id = ?";
        //执行sql语句
        return CRUDTemplate.executeQuery(sql, new BeanHandler<User>(User.class), qq);
    }

    //获取小怪属性
    public static XiaoGuai getXiaoGuaiShuXin(String name){
        String sql = "select * from xiaoguai where guainame = ?";//获取小怪属性sql语句
        return CRUDTemplate.executeQuery(sql,new BeanHandler<>(XiaoGuai.class),name);
    }

    //开始打坐
    public static int startDaZuo(String qq){
        //添加开始时间sql
        String sql = "update user set startdazuo = ?, isdazuo = ? where id = ?";
        //执行sql
        return CRUDTemplate.executeUpdate(sql,System.currentTimeMillis(),0,qq);
    }

    //结束打坐
    public static int stopDaZuo(String qq,long start,int dqexp){
        long linqi = System.currentTimeMillis() - start;
        linqi = linqi / 1000 / 600;
        //添加开始时间sql
        String sql = "update user set startdazuo = ?, isdazuo = ?,checkexp = ? where id = ?";
        //执行sql
        CRUDTemplate.executeUpdate(sql,0,1,(dqexp+linqi),qq);
        return (int) linqi;
    }


    //升级
    public static boolean shenji(String qq){
        int ptrHp = 10;
        int ptrAtk = 3;
        int ptrDef = 1;
        int ptrSpeed = 1;
        Random random = new Random();
        User user = getUser(qq);
        int checkexp = user.getCheckexp();
        int next = user.getNext();
        if (checkexp >= next){
            int level = user.getLevel();
            int zizhi = user.getZizhi();
            int hp = user.getHp();
            int atk = user.getAtk();
            int def = user.getDef();
            int speed = user.getSpeed();
            checkexp = checkexp - next;
            next = (int)(next * 1.5) + 10;
            level = level + 1;
            hp = hp + (ptrHp*zizhi) + random.nextInt(11);
            atk = atk + (ptrAtk*zizhi) + random.nextInt(4);
            def = def + (ptrDef*zizhi) + random.nextInt(2);
            speed = speed + (ptrSpeed*zizhi) + random.nextInt(2);
            String sql = "update user set level = ?, checkexp = ?,next = ?,hp = ?,atk = ?,def = ?,speed = ? where id = ?";
            return CRUDTemplate.executeUpdate(sql, level, checkexp, next, hp, atk, def, speed, qq) == 1;
        }else {
            return false;
        }

    }

    //改名
    public static int rename(String qq,String name){
        //改名sql
        String sql = "update user set username = ? where id = ?";
        return CRUDTemplate.executeUpdate(sql,name,qq);
    }

    //单独加属性
    public static void jiashuxing(String qq,String nage,int duoshao){
        User user = getUser(qq);//获取用户
        switch (nage){
            case "checkexp":
                int checkexp = user.getCheckexp()+duoshao;//添加后的经验
                String sql = "update user set checkexp = ? where id = ?";
                CRUDTemplate.executeUpdate(sql,checkexp,qq);
                break;
        }

//        if ("checkexp".equals(nage)){
//            if (duoshao < 0){
//                duoshao = 0;
//            }
//            String sql = "update user set checkexp = ? where id = ?";
//            CRUDTemplate.executeUpdate(sql,duoshao,qq);
//        }

    }

    //注销
    public static boolean logOut(String id){
        //删除语句
        String sql = "delete from user where id = ?";
        return CRUDTemplate.executeUpdate(sql, id) == 1;
    }

    //位置
    public static boolean position(String id,String weizhi){
        String sql = "update user set weizhi = ? where id = ?";
        return CRUDTemplate.executeUpdate(sql, weizhi,id) == 1;
    }

    //获取所有物品列表
    public static List<BackPack> getItems(String qq){
        String sql = "select * from backpack where qq = ?";
        return CRUDTemplate.executeQuery(sql,new BeanListHandler<>(BackPack.class),qq);
    }

    //获取单个物品
    public static BackPack queryItems(String qq,String itemName){
        String sql = "select * from backpack where qq = ? and szname = ?";//查询sql语句
        return CRUDTemplate.executeQuery(sql,new BeanHandler<>(BackPack.class),qq,itemName);
    }

    //添加物品
    public static boolean acquisition(String id,String wupingname){
        BackPack backPack = queryItems(id, wupingname);
        if (backPack != null){//判断物品是否存在如果存在则添加数量不存在则添加物品
            int number = backPack.getNumber()+1;
            String sql = "update backpack set number = ? where qq = ?";
            return CRUDTemplate.executeUpdate(sql,number,id) == 1;
        }else {
            String sql = "insert into backpack (qq,szname,number) values (?,?,?)";//添加物品sql
            return CRUDTemplate.executeUpdate(sql,id,wupingname,1) == 1;//添加成功返回true
        }
    }
}
