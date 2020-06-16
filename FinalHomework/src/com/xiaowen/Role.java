package com.xiaowen;

import com.xiaowen.creature.Creature;
import com.xiaowen.prop.Weapon;
import com.xiaowen.skill.SkillSet;

public class Role {
    private static volatile com.xiaowen.Role instance=null;

    private Role(){}

    public static com.xiaowen.Role getInstance(){
        if(instance==null){
            synchronized (com.xiaowen.Role.class){
                if(instance==null){
                    instance=new com.xiaowen.Role();
                }
            }
        }
        return instance;
    }

    private int HP;
    private int MP;
    private int HPmax;
    private int MPmax;
    private int damagepoints;
    private int HPadd;
    private int MPadd;
    private String id;
    Weapon currentWeapon=new Weapon();
    private String occupation;
    SkillSet skills=new SkillSet();

    public SkillSet getSkills() {
        return skills;
    }

    public void setSkills(SkillSet skills) {
        this.skills = skills;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public void attack(Creature enemy){
        enemy.setHP(enemy.getHP()+this.damagepoints+currentWeapon.getDamagepoints());
    }

    public String getId() {
        return id
                ;
    }

    public void setName(String name) {
        this.id = id;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

    public int getHPmax() {
        return HPmax;
    }

    public void setHPmax(int HPmax) {
        this.HPmax = HPmax;
    }

    public int getMPmax() {
        return MPmax;
    }

    public void setMPmax(int MPmax) {
        this.MPmax = MPmax;
    }

    public int getDamagepoints() {
        return damagepoints;
    }

    public void setDamagepoints(int damagepoints) {
        this.damagepoints = damagepoints;
    }

    public int getHPadd() {
        return HPadd;
    }

    public void setHPadd(int HPadd) {
        this.HPadd = HPadd;
    }

    public int getMPadd() {
        return MPadd;
    }

    public void setMPadd(int MPadd) {
        this.MPadd = MPadd;
    }
}
