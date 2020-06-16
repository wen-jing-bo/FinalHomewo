package com.xiaowen;

import com.xiaowen.prop.PotionSet;
import com.xiaowen.prop.Weaponset;

public class Bag {
    Weaponset weapons=new Weaponset();
    PotionSet potions=new PotionSet();

    private static volatile com.xiaowen.Bag instance=null;

    private Bag(){}

    public static com.xiaowen.Bag getInstance(){
        if(instance==null){
            synchronized (com.xiaowen.Bag.class){
                if(instance==null){
                    instance=new com.xiaowen.Bag();
                }
            }
        }
        return instance;
    }

    public Weaponset getWeapons() {
        return weapons;
    }

    public void setWeapons(Weaponset weapons) {
        this.weapons = weapons;
    }

    public PotionSet getPotions() {
        return potions;
    }

    public void setPotions(PotionSet potions) {
        this.potions = potions;
    }
}
