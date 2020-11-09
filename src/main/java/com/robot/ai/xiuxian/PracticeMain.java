package com.robot.ai.xiuxian;


import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.robot.ai.jdbc.Bean.User;
import com.robot.ai.xiuxian.bean.XiaoGuai;
import com.simplerobot.modules.utils.KQCodeUtils;

import java.util.ArrayList;
import java.util.Random;

@Beans
public class PracticeMain {
    @Listen(MsgGetTypes.groupMsg)
    @Filter("注册.*")
    public void zhuce(GroupMsg msg, MsgSender send) {
        //获取QQ号
        String qq = msg.getQQ();
        //获取发送的消息
        String msgMsg = msg.getMsg();
        System.out.println(msgMsg);
        //对消息切割
        String[] split = msgMsg.split(" ");
        //判断消息格式正确不正确
        if (split.length == 2 && split[1].length() < 10) {
            //获取姓名
            String name = split[1];
            //判断用户是否存在
            if (XiuXianUtil.isUser(qq) == 0) {
                //资质0为倍数1为多少年
                String[] strings = suiJiZiZhi();
                int role = XiuXianUtil.isRole(qq, name, Integer.parseInt(strings[0]));
                //判断添加用户信息是否成功
                if (role == 1) {

                    String at = KQCodeUtils.INSTANCE.toCq("at", "qq=" + qq);
                    send.SENDER.sendGroupMsg(msg, at + "\r\n注册成功\r\n你是一个" + strings[1] + "\r\n" +
                            "欢迎游玩机器人修仙\r\n" +
                            "当前版本：内测删档");
                } else {
                    send.SENDER.sendGroupMsg(msg, "名字格式不正确");
                }
            } else {
                send.SENDER.sendGroupMsg(msg, "你已注册");
            }
        } else {
            //send.SENDER.sendGroupMsg(msg, "格式不正确");
            send.SENDER.sendGroupMsg(msg, "格式不正确\r\n注册格式如下\r\n注册 姓名\r\n(注意空格)");
        }

    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter("角色信息")
    public void xinxi(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        if (XiuXianUtil.isUser(qq) > 0) {
            User user = XiuXianUtil.getUser(qq);
            String info = "名字：" + user.getUsername() + "\r\n" +
                    "QQ：" + qq + "\r\n" +
                    "等级：" + user.getLevel() + "\r\n" +
                    "灵气：" + user.getCheckexp() + "/" + user.getNext() + "\r\n" +
                    "生命：" + user.getHp() + "\r\n" +
                    "攻击：" + user.getAtk() + "\r\n" +
                    "防御：" + user.getDef() + "\r\n" +
                    "速度：" + user.getSpeed();
            String at = KQCodeUtils.INSTANCE.toCq("at", "qq=" + qq);
            send.SENDER.sendGroupMsg(msg, at + "\r\n" + info);
        } else {
            send.SENDER.sendGroupMsg(msg, "未注册");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter("打坐")
    public void dazuo(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        //判断是否注册
        if (XiuXianUtil.isUser(qq) > 0) {
            User user = XiuXianUtil.getUser(qq);
            //是否可以打坐1为可以0为不可以
            if (user.getIsdazuo() == 1) {
                int i = XiuXianUtil.startDaZuo(qq);
                if (i == 1) {
                    send.SENDER.sendGroupMsg(msg, "已开始打坐\r\n" +
                            "打坐每10分钟凝聚一点灵气");
                } else {
                    send.SENDER.sendGroupMsg(msg, "打坐失败");
                }
            } else {
                send.SENDER.sendGroupMsg(msg, "打坐中");
            }
        } else {
            send.SENDER.sendGroupMsg(msg, "未注册");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter("结束打坐")
    public void stopdazuo(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        //判断是否注册
        if (XiuXianUtil.isUser(qq) > 0) {
            User user = XiuXianUtil.getUser(qq);
            long l = Long.parseLong(user.getStartdazuo());
            //如果大于0就可以结束打坐
            if (l > 0) {
                int linqi = XiuXianUtil.stopDaZuo(qq, l, user.getCheckexp());
                send.SENDER.sendGroupMsg(msg, "已结束打坐\r\n" +
                        "凝聚了" + linqi + "点灵气");
            } else {
                send.SENDER.sendGroupMsg(msg, "未在打坐");
            }
        } else {
            send.SENDER.sendGroupMsg(msg, "未注册");
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter("突破")
    public void level(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        //判断是否注册
        if (XiuXianUtil.isUser(qq) > 0) {
            //获取用户灵气
            User user = XiuXianUtil.getUser(qq);
            int checkexp = user.getCheckexp();
            int next = user.getNext();
            int level = user.getLevel();
            //判读是否满足升级条件
            if (checkexp >= next) {
                //判断等级是否满足条件
                if (level < 10) {
                    if (XiuXianUtil.shenji(qq)) {
                        send.SENDER.sendGroupMsg(msg, "突破成功");
                    } else {
                        send.SENDER.sendGroupMsg(msg, "突破失败");
                    }
                } else {
                    send.SENDER.sendGroupMsg(msg, "你已是最高等级");
                }

            } else {
                send.SENDER.sendGroupMsg(msg, "灵气不够");
            }
        } else {
            send.SENDER.sendGroupMsg(msg, "未注册");
        }
    }


    @Listen(MsgGetTypes.groupMsg)
    @Filter("改名.*")
    public void rename(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        //判断是否注册
        if (XiuXianUtil.isUser(qq) > 0) {
            //判断格式
            String msgMsg = msg.getMsg();
            String[] s = msgMsg.split(" ");
            if (s.length == 2) {
                String name = s[1];
                //名字格式
                if (s[1].length() < 10) {
                    //改名
                    int rename = XiuXianUtil.rename(qq, name);
                    if (rename == 1) {
                        send.SENDER.sendGroupMsg(msg, "改名成功");
                    } else {
                        send.SENDER.sendGroupMsg(msg, "改名失败");
                    }
                } else {
                    send.SENDER.sendGroupMsg(msg, "名字格式不正确");
                }
            } else {
                send.SENDER.sendGroupMsg(msg, "格式不正确\r\n" +
                        "改名 新名字\r\n" +
                        "(注意空格)");
            }
        } else {
            send.SENDER.sendGroupMsg(msg, "未注册");
        }
    }


    /**
     * 历练打怪
     * 逻辑
     * 1.判断是否注册
     * 2.判断是否在打坐
     * 3.根据所选地图历练
     * 4.获取属性
     * 5.根据玩家资质和等级随机生成小怪属性
     */
    @Listen(MsgGetTypes.groupMsg)
    @Filter("地图")
    public void groupMap(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        send.SENDER.sendGroupMsg(msg, atqq(qq) + "\r\n" +
                "请选择地图\r\n" +
                "1.百岁山\r\n" +
                "例如：前往 百岁山\r\n" +
                "注意空格");
    }

   /* @Listen(MsgGetTypes.privateMsg)
    @Filter("历练.*")
    public void privatLilian(PrivateMsg msg, MsgSender send){
        String qq = msg.getQQ();
        //判断用户是否注册
        if (XiuXianUtil.isUser(qq) > 0){
            String msgMsg = msg.getMsg();
            //历练提示
            if (msgMsg.equals("历练")){
                send.SENDER.sendPrivateMsg(msg,atqq(qq)+"\r\n" +
                        "请选择地图历练\r\n" +
                        "1.百岁山(推荐等级1-10)\r\n" +
                        "例如：历练 1\r\n" +
                        "注意空格");
            }else {
                String[] s = msgMsg.split(" ");
                //关键字格式
                if (s.length == 2){
                    if (s[1].equals("1")) {
                        XiaoGuai xiaoGuai = suijiXiaoGuai(qq);
                        int dengji = xiaoGuai.getDengji();
                        int shengming = xiaoGuai.getShengming();
                        int gongji = xiaoGuai.getGongji();
                        int sudu = xiaoGuai.getSudu();
                        send.SENDER.sendPrivateMsg(msg,"你遭遇了.....\r\n" +
                                "等级：" + dengji + "\r\n" +
                                "生命：" + shengming + "\r\n" +
                                "攻击：" + gongji + "\r\n" +
                                "速度：" + sudu);
                        User user = XiuXianUtil.getUser(qq);
                        int hp = user.getHp();
                        int atk = user.getAtk();
                        int speed = user.getSpeed();
                        StringBuilder builder = new StringBuilder();
                        //此变量为谁攻击
                        //true为玩家
                        //false为小怪
                        boolean sheilai = true;
                        //判断谁的速度快
                        if (sudu > speed){
                            //小怪快
                            builder.append("....以迅雷不及掩耳之势对你造成了"+gongji+"点伤害\r\n");
                            hp = hp - gongji;
                        }else {
                            //人快
                            builder.append("你以迅雷不及掩耳之势对。。。造成了"+atk+"点伤害\r\n");
                            shengming = shengming - atk;
                            sheilai = false;
                        }

                        while (true){
                            if (hp <= 0){
                                builder.append("你身负重伤而逃\r\n" +
                                        "战败");
                                send.SENDER.sendPrivateMsg(msg, builder.toString());
                                return;
                            }
                            if (shengming <= 0){
                                builder.append("你战胜了。。。\r\n" +
                                        "胜利");
                                send.SENDER.sendPrivateMsg(msg, builder.toString());
                                return;
                            }
                            if (sheilai){
                                //人攻击
                                builder.append("你对。。。造成了"+atk+"点伤害\r\n");
                                shengming = shengming -atk;
                                sheilai = false;
                            }else {
                                //怪攻击
                                builder.append("...对你造成了"+gongji+"点伤害\r\n");
                                hp = hp - gongji;
                                sheilai = true;
                            }

                        }
                    }
                }else {
                    send.SENDER.sendPrivateMsg(msg,"格式不正确");
                }
            }
        }else {
            send.SENDER.sendPrivateMsg(msg,"未注册");
        }

    }*/

    @Listen(MsgGetTypes.groupMsg)
    @Filter("前往.*")
    public void qianWangDiTu(GroupMsg msg, MsgSender send){
        String qq = msg.getQQ();
        String msgMsg = msg.getMsg();
        String[] split = msgMsg.split(" ");
        if (split.length == 2){
            if (XiuXianUtil.isUser(qq) > 0){//判断是否注册
                if (XiuXianUtil.getUser(qq).getIsdazuo() > 0){//判断是否在打坐
                    for (String s : map()) {
                        if (s.equals(split[1])){//判断地图是否存在
                            if (XiuXianUtil.position(qq,split[1])) {//判断是否前往成功
                                send.SENDER.sendGroupMsg(msg,"前往成功");
                                return;
                            }else {
                                send.SENDER.sendGroupMsg(msg,"前往失败");
                                return;
                            }
                        }
                    }
                    send.SENDER.sendGroupMsg(msg,"地图不存在");
                }else {
                    send.SENDER.sendGroupMsg(msg,"请先结束打坐");
                }
            }else {
                send.SENDER.sendGroupMsg(msg,"未注册");
            }
        }else {
            send.SENDER.sendGroupMsg(msg,"格式不正确");
        }


    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter("调查附近")
    public void chakanFuJin(GroupMsg msg, MsgSender send){
        String qq = msg.getQQ();
        String msgMsg = msg.getMsg();
        if (XiuXianUtil.isUser(qq) > 0) {//判断是否注册
            if (XiuXianUtil.getUser(qq).getIsdazuo() > 0) {//判断是否在打坐
                User user = XiuXianUtil.getUser(qq);//获取用户全部属性
                String weizhi = user.getWeizhi();//获取角色位置

                switch (weizhi){
                    case "百岁山":
                        XiaoGuai maochong = XiuXianUtil.getXiaoGuaiShuXin("毛虫");//获取小怪信息
                        int gnumber = maochong.getGnumber();//获取数量
                        send.SENDER.sendGroupMsg(msg,"你附近有\n" +
                                gnumber+"只毛虫");
                        break;
                    default:
                        send.SENDER.sendGroupMsg(msg,"你附近什么都没有");
                        break;

                }
            }else {
                send.SENDER.sendGroupMsg(msg,"请先结束打坐");
            }
        }else {
            send.SENDER.sendGroupMsg(msg,"未注册");
        }
    }


    @Listen(MsgGetTypes.groupMsg)
    @Filter("攻击.*")
    public void gongji(GroupMsg msg, MsgSender send){
        String qq = msg.getQQ();
        String msgMsg = msg.getMsg();
        String[] split = msgMsg.split(" ");
        if (split.length == 2){
            String msgXiaoGuaiName = split[1];
            if (XiuXianUtil.isUser(qq) > 0) {//判断是否注册
                if (XiuXianUtil.getUser(qq).getIsdazuo() > 0) {//判断是否在打坐
                    User user = XiuXianUtil.getUser(qq);
                    String weizhi = user.getWeizhi();
                    Random random = new Random();
                    switch (weizhi){//判断位置
                        case "百岁山":
                            switch (msgXiaoGuaiName){//判断小怪是否存在
                                case "毛虫":
                                    if (daJia(XiuXianUtil.getXiaoGuaiShuXin(msgXiaoGuaiName), qq)) {
                                        String s = "你战胜了"+msgXiaoGuaiName+"\n" +
                                                "战利品：\n" +
                                                "";
                                        send.SENDER.sendGroupMsg(msg,s);
                                    }else {
                                        send.SENDER.sendGroupMsg(msg,"你被"+msgXiaoGuaiName+"干碎了");
                                    }
                                    break;
                                case "巨型兵蚁":
                                    break;
                                default:
                                    send.SENDER.sendGroupMsg(msg,"当前区域没有这个小怪");
                            }
                            break;
                        default:
                            send.SENDER.sendGroupMsg(msg,"你不在所在区域");
                            break;
                    }
                }else {
                    send.SENDER.sendGroupMsg(msg,"请先结束打坐");
                }
            }else {
                send.SENDER.sendGroupMsg(msg,"未注册");
            }
        }else {
            send.SENDER.sendGroupMsg(msg,"格式不正确");
        }
    }


    @Listen(MsgGetTypes.groupMsg)
    @Filter("注销.*")
    public void logOut(GroupMsg msg, MsgSender send) {
        String qq = msg.getQQ();
        String msgMsg = msg.getMsg();
        String[] s = msgMsg.split(" ");
        //判断格式和验证码
        if (s.length == 2 && "BBQ2".equals(s[1])) {
            //判断注册
            if (XiuXianUtil.isUser(qq) > 0) {
                //判断是否注销成功
                if (XiuXianUtil.logOut(qq)) {
                    send.SENDER.sendGroupMsg(msg, "注销成功");
                } else {
                    send.SENDER.sendGroupMsg(msg, "注销失败");
                }
            } else {
                send.SENDER.sendGroupMsg(msg, "未注册");
            }
        } else {
            send.SENDER.sendGroupMsg(msg, "格式不正确");
        }
    }


    //管理员
    private boolean admin(GroupMsg msg, MsgSender send) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1509643183");
        for (String s : list) {
            if (s.equals(msg.getQQ())) {
                return true;
            }
        }
        send.SENDER.sendGroupMsg(msg, "权限不够");
        return false;
    }

    //at
    private String atqq(String qq) {
        return KQCodeUtils.INSTANCE.toCq("at", "qq=" + qq);
    }

    //生成小怪属性
    /*private XiaoGuai suijiXiaoGuai(User user) {
        int ptrHp = 10;
        int ptrAtk = 3;
        int ptrDef = 1;
        int ptrSpeed = 1;
        Random random = new Random();
        //User user = XiuXianUtil.getUser(qq);
        XiaoGuai xiaoGuai = new XiaoGuai();
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("俄狼");
        strings.add("狂兔");
        int level = user.getLevel();
        String[] stringsZiZhi = suiJiZiZhi();//0为倍数1为文字
        int zizhi = Integer.parseInt(stringsZiZhi[0]);
        int sm = 10;
        int gj = 3;
        int fy = 1;
        int sd = 1;
        for (int i = 0; i < level; i++) {
            sm = sm + (ptrHp * zizhi) + random.nextInt(11);
        }

        xiaoGuai.setDengji(level);
        xiaoGuai.setGongji(gj);
        xiaoGuai.setFangYu(fy);
        xiaoGuai.setShengming(sm);
        xiaoGuai.setSudu(sd);
        xiaoGuai.setName(strings.get(new Random().nextInt(strings.size())));
        return xiaoGuai;
    }*/

    //资质生成
    private String[] suiJiZiZhi() {
        Random random = new Random();
        int nextInt = random.nextInt(1000000) + 1;
        System.out.println("随机资质为" + nextInt);
        if (nextInt >= 1 && nextInt <= 100000) {
            return new String[]{"2", "十年一遇天才"};
        }
        if (nextInt > 100000 && nextInt <= 110000) {
            return new String[]{"4", "百年一遇天才"};
        }
        if (nextInt > 110000 && nextInt <= 111000) {
            return new String[]{"8", "千年一遇天才"};
        }
        if (nextInt > 111000 && nextInt <= 111100) {
            return new String[]{"16", "万年一遇天才"};
        }
        if (nextInt > 111100 && nextInt <= 111110) {
            return new String[]{"32", "十万年一遇天才"};
        }
        if (nextInt == 111111) {
            return new String[]{"64", "百万年一遇天才"};
        }
        return new String[]{"1", "普通人"};
    }

    //打架系统f
    //返回值true为人赢false为怪赢
    private boolean daJia(XiaoGuai xiaoGuai, String... qqs) {
        //判断人与人pk还是人与怪
        //有两个qq代表人与人
        if (qqs.length == 2) {
            return true;
            //没有两个qq代表人与怪
        } else {
            String qq = qqs[0];//获取qq
            User user = XiuXianUtil.getUser(qq);//拿到角色属性
            int hp = user.getHp();
            int atk = user.getAtk();
            int def = user.getDef();
            int speed = user.getSpeed();
            int shengming = xiaoGuai.getGsm();
            int gongji = xiaoGuai.getGdengji();
            int fangYu = xiaoGuai.getGdef();
            int sudu = xiaoGuai.getGspeed();

            int wjshanghai = atk - fangYu;
            int xgshanghai = gongji - def;
            if (wjshanghai <= 0) {
                wjshanghai = 1;
            }
            if (xgshanghai <= 0) {
                xgshanghai = 1;
            }

            while (true) {
                //判断谁的速度快
                if (speed > sudu) {
                    shengming -= wjshanghai;
                    speed = 0;
                    sudu = 1;
                } else {
                    hp -= xgshanghai;
                    sudu = 0;
                    speed = 1;
                }

                if (hp <= 0) {
                    return false;
                }
                if (shengming <= 0) {
                    return true;
                }
            }

        }
    }

    //地图
    private String[] map(){
        return new String[]{"百岁山"};
    }

    //战利品
    private ArrayList trophy(String xiaoGuaiName){
        Random random = new Random();
        ArrayList<String> zlp = new ArrayList<>();
        switch (xiaoGuaiName){
            case "毛虫":
                String[] strings= {"新手剑","新手衣","新手裤","新手鞋"};
                int nextInt = random.nextInt(2);
                zlp.add(nextInt+"");
                int i = random.nextInt(100) + 1;
                if (i>=1 && i<=5){
                    int nextInt1 = random.nextInt(4);
                    zlp.add(strings[nextInt1]);
                    return zlp;
                }
                break;
            case "巨型兵蚁":
                break;
        }
        return zlp;
    }
}
