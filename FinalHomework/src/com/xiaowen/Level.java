package com.xiaowen;

public class Level {
    private static volatile com.xiaowen.Level instance=null;
    public int level=1;
    public int experience=0;
    private int beforeLevel=1;

    public int getBeforeLevel() {
        return beforeLevel;
    }

    public void setBeforeLevel(int beforeLevel) {
        this.beforeLevel = beforeLevel;
    }

    private Level(){}

    public  static com.xiaowen.Level getInstance(){
        if(instance==null){
            synchronized (com.xiaowen.Level.class){
                if(instance==null){
                    instance=new com.xiaowen.Level();
                }
            }
        }
        return instance;
    }

    public void addExperience(int exp){
        this.experience+=exp;
        this.setLevel(this.getExperience());
    }

    public int getExperience(){return experience;}

    public int getLevel(){
        return level;
    }

    public void setLevel(int exp){
        this.level=1+exp/100;
    }

}
