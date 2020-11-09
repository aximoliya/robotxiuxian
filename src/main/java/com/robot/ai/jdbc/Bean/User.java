package com.robot.ai.jdbc.Bean;

public class User {
    private String id;
    private String username;
    private int checkexp;
    private int next;
    private int hp;
    private int atk;
    private int def;
    private int speed;
    private int zizhi;
    private int level;
    private int isdazuo;
    private String startdazuo;
    private String weizhi;

    public String getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getIsdazuo() {
        return isdazuo;
    }

    public void setIsdazuo(int isdazuo) {
        this.isdazuo = isdazuo;
    }

    public String getStartdazuo() {
        return startdazuo;
    }

    public void setStartdazuo(String startdazuo) {
        this.startdazuo = startdazuo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCheckexp() {
        return checkexp;
    }

    public void setCheckexp(int checkexp) {
        this.checkexp = checkexp;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getZizhi() {
        return zizhi;
    }

    public void setZizhi(int zizhi) {
        this.zizhi = zizhi;
    }
}
