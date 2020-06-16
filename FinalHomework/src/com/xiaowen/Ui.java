package com.xiaowen;

import com.xiaowen.creature.*;
import com.xiaowen.method.*;
import com.xiaowen.prop.Potion;
import com.xiaowen.prop.PotionSet;
import com.xiaowen.prop.Weapon;
import com.xiaowen.prop.Weaponset;
import com.xiaowen.skill.Skill;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Ui {
    private Place currentPlace;
    private HashMap<String,Methods> methods = new HashMap<String, Methods>();
    private Devil devil=new Devil();
    private Archmage archmage = new Archmage();
    private MonsterSet monsters=new MonsterSet();
    private PotionSet dropPotion = new PotionSet();
    private Weaponset dropWeapon=new Weaponset();
    boolean newcity=true;
    boolean newcrown=true;
    boolean newtrunk=true;

    public PotionSet getPotionDrop(){return dropPotion;}
    public Weaponset getWeponDrop(){return dropWeapon;}

    public MonsterSet getMonsters(){return monsters;}
    public String rollAttackMethod(){
        String Id="";
        int ran=rollAttacknum();
        if(ran>0 && ran<=1){
            Id="手指光弹";
        }
        else if(ran>1 && ran<=3){
            Id="死之彗星";
        }
        else if(ran>3 && ran<=6){
            Id="死亡光束";
        }
        else if(ran>6 && ran<=10){
            Id="超新星";
        }

        return Id;
    }

    public int rollAttacknum(){
        int max=10,min=1;
        int ran=(int)(Math.random()*(max-min)+min);

        return ran;
    }

    public void createCrown(Place place){
        Place immortalCrown;
        immortalCrown=new Place("飞船降落地");

        immortalCrown.setExit("down",place);
        place.setExit("up",immortalCrown);

    }

    public void createTrunk(Place place){
        Place immortalTrunk;
        immortalTrunk=new Place("娜美克星村庄a");

        immortalTrunk.setExit("west",place);
        place.setExit("east",immortalTrunk);

    }

    public void createCity(Place place){
        Place anCity;
        anCity=new Place("湖畔");


        anCity.setExit("south",place);
        place.setExit("north",anCity);
    }

    public void createNewScence(){
        int river=0,city=0,trunk=0;
        for(Monster item:monsters.getMonsters()){
            if(item.getCurrentPName().equals("湖畔")){
                city+=1;
            }
            else if(item.getCurrentPName().equals("娜美克星村庄b")){
                river+=1;
            }
            else if(item.getCurrentPName().equals("娜美克星村庄a")){
                trunk+=1;
            }
        }
        if(newcity &&river==0) {
            createCity(currentPlace);
            newcity=false;
        }
        else if (newtrunk && city==0) {
            createTrunk(currentPlace);
            newtrunk=false;
        }
        else if (newcrown && trunk==0) {
            createCrown(currentPlace);
            newcrown=false;
        }
    }


    public int roll(){
        int max=100;
        int min=1;
        int ran=(int)(Math.random()*(max-min)+min);
        return ran;
    }

    public void createDropThings(){
        Potion hpPill=new Potion();
        Potion mpPill=new Potion();
        Potion immortalPill=new Potion();
        hpPill.setHPrecovery(50);
        hpPill.setNum(1);
        hpPill.setId("hppill");
        hpPill.setDescription("小红瓶");
        mpPill.setMPrecovery(50);
        mpPill.setNum(1);
        mpPill.setId("mppill");
        mpPill.setDescription("小蓝瓶");
        immortalPill.setNum(1);
        immortalPill.setId("immortalpill");
        this.getPotionDrop().getPotions().add(hpPill);
        this.getPotionDrop().getPotions().add(mpPill);
        this.getPotionDrop().getPotions().add(immortalPill);

    }

    public void drop(Creature creature){
        int num=roll();
        createDropThings();
        if(creature.getName().equals("小喽啰")){
            if(num>0 &&num<=30){
                System.out.println("掉落 乌丸补气散（恢复MP50）");
                Bag.getInstance().getPotions().getPotions().add(this.getPotionDrop().get("mppill"));
            }
            else if(num>30 &&num<=60){
                System.out.println("掉落 红丸补血丹（恢复HP50）");
                Bag.getInstance().getPotions().getPotions().add(this.getPotionDrop().get("hppill"));
            }
            else{
                System.out.println("掉落，无");
            }
        }
        else {
            if(num==1 || num==100){
                System.out.println("掉落 仙豆儿");
                Bag.getInstance().getPotions().getPotions().add(this.getPotionDrop().get("immortalpill"));
            }
        }

    }

    public void upgrade(){
        if(Level.getInstance().getLevel()>Level.getInstance().getBeforeLevel()){
            int difference=Level.getInstance().getLevel()-Level.getInstance().getBeforeLevel();
            Role.getInstance().setHPmax(Role.getInstance().getHPmax()+Role.getInstance().getHPadd()*difference);
            Role.getInstance().setHP(Role.getInstance().getHPmax());
            Role.getInstance().setMPmax(Role.getInstance().getMPmax()+Role.getInstance().getMPadd()*difference);
            Role.getInstance().setMP(Role.getInstance().getMPmax());
            Level.getInstance().setBeforeLevel(Level.getInstance().getLevel());
        }
    }


    public void getSuperWeapon(){
        if(currentPlace.isHasSuperWeapon() && Level.getInstance().getLevel()>=15){
            System.out.println("*你获得了你的信念");
            Weapon sp=new Weapon();
            sp.setDamagepoints(-75);
            sp.setDescription("信念之力");
            sp.setId("divineTroops");
            sp.setOccupation("所有人");
            Bag.getInstance().getWeapons().getWeapons().add(sp);
            currentPlace.setHasSuperWeapon(false);
        }
    }

    public void attack(String enemyId){
            Role.getInstance().attack(this.getMonsters().get(enemyId));
            monsters.get(enemyId).attack();
            monsterDie(enemyId);
    }

    public void monsterDie(String enemyId){
        if(monsters.get(enemyId).getHP()<=0){
            drop(monsters.get(enemyId));
            Level.getInstance().addExperience(monsters.get(enemyId).getExperience());
            System.out.println("敌人 "+monsters.get(enemyId).getId()+" 击败！");
            Iterator<Monster> iterator=monsters.getMonsters().iterator();
            while(iterator.hasNext()){
                Monster it=iterator.next();
                if(it.getId().equals(enemyId)){
                    iterator.remove();
                }
            }
        }

    }

    public void MonsterDisplay(Place place){
        int numbers=0;
        System.out.println("这里的怪物有:");
        for(Monster item:monsters.getMonsters()){
            if(item.getCurrentPName().equals(currentPlace.getName())){
                System.out.println(item.getName()+" id:"+item.getId()+" HP:"+item.getHP());
                numbers+=1;
            }
        }
        if(numbers==0){
            System.out.println("此地无怪物出没");
        }
    }



    public Ui(){
        methods.put("go",new MethodGo(this));
        methods.put("attack",new MethodAttack(this));
        methods.put("exit",new MethodExit(this));
        methods.put("chat",new MethodChat(this));
        methods.put("use",new MethodUse(this));
        methods.put("change",new MethodChange(this));
        methods.put("check",new MethodCheck(this));
        methods.put("skill",new MethodSkill(this));
        createPlace();
    }

    public void printHelp(){
        System.out.println("可用指令 change，attack，go，use,exit,skill");
        System.out.println("go: 例如 go east");
        System.out.println("attack: attack  敌人id");
        System.out.println("use（释放技能）: use 技能");
        System.out.println("change weaponid 改变当前武器");
        System.out.println("skill 技能Id 释放技能");
        System.out.println("exit 退出");

    }

    public void printBag(){
        System.out.println("武器：");
        for(Weapon item:Bag.getInstance().getWeapons().getWeapons()){
            System.out.println("名字:"+item.getDescription()+" Id:"+item.getId()+" 伤害数值:"+item.getDamagepoints());
        }
        System.out.println("药品:");
        if(Bag.getInstance().getPotions().getPotions().size()!=0)
            for(Potion item:Bag.getInstance().getPotions().getPotions())
                System.out.println(item.getDescription()+" Id:"+item.getId()+" num:"+item.getNum());
        else{
            System.out.println("暂无药品");
        }

    }

    public void printSkill(){
        for(Skill item:Role.getInstance().getSkills().getSkills()){
            if(item.getOccupation().equals(Role.getInstance().getOccupation())) {
                if (!item.getAttackRange().equals("treatment")) {
                    System.out.println("Id:" + item.getId() + " occupation:" + item.getOccupation() + " manacost:" + item.getManaCost() + " damage:" + item.getDamagepoints() + " attackRange:" + item.getAttackRange());
                }
                else {
                    System.out.println("Id:" + item.getId() + " occupation:" + item.getOccupation() + " manacost:" + item.getManaCost() + " hpTreat:" + item.getHpTreat());
                }
            }
        }
    }

    public void goPlace(String direction){
        Place nextPlace =currentPlace.getExit(direction);

        if(nextPlace==null){
            System.out.println("那里不能前进！");
        }
        else{
            currentPlace=nextPlace;
        }
    }

    public void printExit(){
        System.out.println("你现在在："+currentPlace);
        System.out.println("你可以去的地方有：");
        System.out.println(currentPlace.getExitDirt());
    }

    public void Scenario_1(){
        System.out.println("玩家" + Role.getInstance().getId() + "为了拯救地球的伙伴" );
    }

    public void roleCondition(){
        System.out.println("-------角-色-状-态-----------------");
        System.out.println("name:"+Role.getInstance().getId());
        System.out.println("level:"+Level.getInstance().getLevel());
        System.out.println("HP:"+Role.getInstance().getHP());
        System.out.println("MP:"+Role.getInstance().getMP());
        System.out.println("damagepoints:"+(Role.getInstance().getDamagepoints()+Role.getInstance().getCurrentWeapon().getDamagepoints()));
    }

    public void createPlace(){
        Place hupan,huzhong,liegu,Volcano;


        hupan=new Place("湖畔");
        huzhong=new Place("湖中");
        liegu=new Place("裂谷");
        Volcano=new Place("火山");

        Volcano.setHasSuperWeapon(true);

        hupan.setExit("south",huzhong);
        huzhong.setExit("north",hupan);
        huzhong.setExit("south",liegu);
        liegu.setExit("north",huzhong);
        liegu.setExit("west",Volcano);
        Volcano.setExit("east",liegu);
        currentPlace=liegu;

    }

    public void bossSkillAttackPerRound(){
        String method=rollAttackMethod();
        System.out.println("弗利沙对你使出了 "+method);
        for(Skill item:archmage.getSkills().getSkills()){
            if(method.equals(item.getId())){
                Role.getInstance().setHP(Role.getInstance().getHP()+item.getDamagepoints());
            }
        }

    }

    public void game(){
        System.out.println("请输入玩家id：");
        Scanner input = new Scanner(System.in);
        String name=input.nextLine();
        Role.getInstance().setName(name);
        System.out.println("飞船降落中~~~\n\n");
        Scenario_1();

        while(true){
            createNewScence();
            upgrade();
            roleCondition();
            printExit();
            MonsterDisplay(currentPlace);
            getSuperWeapon();
            String sentence=input.nextLine();
            String[] words=sentence.split(" ");
            String value="";
            Methods way=methods.get(words[0]);
            if(sentence.length()>1)
                value=words[1];
            if(words[0].equals("help")){
                printHelp();
            }
            if(way!=null) {
                way.doCmd(value);
                if(way.isExit()){
                    break;
                }
            }
            if(currentPlace.getName().equals("飞船降落地"))
                bossSkillAttackPerRound();
            if(Role.getInstance().getHP()<=0){
                System.out.println("玩家（"+Role.getInstance().getId()+"）游戏结束，菜~");
                break;
            }
        }

    }

    public void createRole(String line){
        if (line.equals("1")){
            Role.getInstance().setOccupation("孙悟饭");
            Role.getInstance().setHP(75);
            Role.getInstance().setHPmax(75);
            Role.getInstance().setMP(200);
            Role.getInstance().setMPmax(200);
            Role.getInstance().setHPadd(25);
            Role.getInstance().setMPadd(100);
            Role.getInstance().setDamagepoints(-75);
            Weapon intialWeapon=new Weapon();
            intialWeapon.setId("iW");

            intialWeapon.setDamagepoints(-5);
            intialWeapon.setOccupation("孙悟饭");
            Role.getInstance().setCurrentWeapon(intialWeapon);
            Bag.getInstance().getWeapons().getWeapons().add(intialWeapon);
        }
        else if(line.equals("2")){
            Role.getInstance().setOccupation("克林");
            Role.getInstance().setHP(200);
            Role.getInstance().setHPmax(200);
            Role.getInstance().setMP(100);
            Role.getInstance().setMPmax(100);
            Role.getInstance().setHPadd(75);
            Role.getInstance().setMPadd(50);
            Role.getInstance().setDamagepoints(-30);
            Weapon intialWeapon=new Weapon();
            intialWeapon.setId("iW");

            intialWeapon.setDamagepoints(-5);
            intialWeapon.setOccupation("克林");
            Role.getInstance().setCurrentWeapon(intialWeapon);
            Bag.getInstance().getWeapons().getWeapons().add(intialWeapon);
        }
        else{
            System.out.println("无此选项，请重新选择：");
            Scanner it = new Scanner(System.in);
            String choose=it.nextLine();
            createRole(choose);
        }

    }

    public void openingI() {
        System.out.println("【~~~~~~~~~~~~~~~~~~~~娜美克星篇~~~~~~~~~~~~~~~~~~】");
        System.out.println("【孙悟饭乘坐神仙当年来到地球所乘宇宙飞船前往娜美克星】");
        System.out.println("【为了使用那美克星上的龙珠复活在贝吉塔一役中战死的战士们】");
        System.out.println("【孙悟饭与克林（光头）背离家乡数万光年来到那美克星】");
        System.out.println("【前路灰暗，不知要遇到怎样的挑战.....】");
        System.out.println("【孙悟饭】：比克叔叔、天净饭叔叔、饺子,我会集齐龙珠复活你们的");
        System.out.println("【克林】：真不知道娜美克星是什么样子");
        System.out.println("【孙悟饭】：。。。。。");
        System.out.println("【克林】：。。。。。");
        System.out.println("请选择你的角色：");
        System.out.println("1.孙悟饭  HP:75 MP:200 Damage:75");
        System.out.println("2.克林  HP:200 MP:100 Damage:30");


    }

    public int rollnum(){
        int max=10;
        int min=5;
        int ran=(int)(Math.random()*(max-min)+min);
        return ran;
    }

    public void createMonster(){
        archmage.setId("archmage");
        archmage.setName("弗利沙");
        archmage.setExperience(20000);
        archmage.setHP(2000);
        archmage.setMP(500000);
        archmage.setDamagepoints(-50);
        archmage.setCurrentPName("飞船降落地");
        monsters.getMonsters().add(archmage);
        devil.setId("devil");
        devil.setName("多多利亚");
        devil.setExperience(10000);
        devil.setHP(5000);
        devil.setMP(5000);
        devil.setDamagepoints(-30);
        devil.setCurrentPName("娜美克星村落a");
        monsters.getMonsters().add(devil);
        Evilgeneral red=new Evilgeneral();
        Evilgeneral green=new Evilgeneral();
        Evilgeneral purple=new Evilgeneral();
        Evilgeneral blue=new Evilgeneral();
        red.setId("js");
        red.setName("吉斯");
        red.setExperience(500);
        red.setHP(400);
        red.setMP(2000);
        red.setCurrentPName("湖畔");
        red.setDamagepoints(-10);
        green.setId("lkm");
        green.setName("利库姆");
        green.setExperience(500);
        green.setHP(500);
        green.setMP(1000);
        green.setCurrentPName("湖畔");
        green.setDamagepoints(-10);
        purple.setId("bt");
        purple.setName("巴特");
        purple.setExperience(500);
        purple.setHP(500);
        purple.setMP(1000);
        purple.setCurrentPName("湖畔");
        purple.setDamagepoints(-10);
        blue.setId("jn");
        blue.setName("基纽");
        blue.setExperience(1000);
        blue.setHP(2000);
        blue.setMP(500);
        blue.setCurrentPName("湖畔");
        blue.setDamagepoints(-15);
        monsters.getMonsters().add(blue);
        monsters.getMonsters().add(purple);
        monsters.getMonsters().add(red);
        monsters.getMonsters().add(green);
        for(int i=1;i<rollnum();i++){
            Evilsoilder soilder=new Evilsoilder();
            soilder.setId("soilder"+i);
            soilder.setName("杂兵");
            soilder.setExperience(20);
            soilder.setHP(200);
            soilder.setMP(50);
            soilder.setCurrentPName("湖畔");
            soilder.setDamagepoints(-3);
            monsters.getMonsters().add(soilder);
        }
        for(int i=1;i<rollnum();i++){
            Centipede centipede=new Centipede();
            centipede.setId("centipde"+i);
            centipede.setName("小喽啰");
            centipede.setExperience(20);
            centipede.setHP(150);
            centipede.setMP(0);
            centipede.setCurrentPName("娜美克星村落");
            centipede.setDamagepoints(-5);
            monsters.getMonsters().add(centipede);
        }
    }

    public void change(String word){
        boolean exist=true;
        for(Weapon item:Bag.getInstance().getWeapons().getWeapons()){
            if (item.getId().equals(word)){
                if(Role.getInstance().getOccupation().equals(item.getOccupation())) {
                    Role.getInstance().setCurrentWeapon(item);
                }
                else if(item.getOccupation().equals("全职业")){
                    Role.getInstance().setCurrentWeapon(item);
                }
                else{
                    System.out.println("职业不符");
                }
                exist=false;
                break;
            }
        }
        if (exist){
            System.out.println("无此武器");
        }
    }


    public void usePotion(String word){
        boolean exist=true;
        for(Potion item:Bag.getInstance().getPotions().getPotions()){
            if(item.getId().equals(word)){
                if(item.getHPrecovery()+Role.getInstance().getHP()>Role.getInstance().getHPmax())
                    Role.getInstance().setHP(Role.getInstance().getHPmax());
                else
                    Role.getInstance().setHP(Role.getInstance().getHP() + item.getHPrecovery());
                if(item.getMPrecovery()+Role.getInstance().getMP()>Role.getInstance().getMPmax())
                    Role.getInstance().setMP(Role.getInstance().getMPmax());
                else
                    Role.getInstance().setMP(Role.getInstance().getMP() + item.getMPrecovery());
                exist=false;
                break;
            }
        }
        Iterator<Potion> iterator=Bag.getInstance().getPotions().getPotions().iterator();
        while(iterator.hasNext()){
            Potion it=iterator.next();
            if(it.getId().equals(word)){
                iterator.remove();
                break;
            }
        }
        if(exist){
            System.out.println("无此药品");
        }
    }

    public void useSkill(String word){
        boolean exist=false;
        for(Skill item:Role.getInstance().getSkills().getSkills()){
            if(word.equals(item.getId())){
                if(Role.getInstance().getOccupation().equals(item.getOccupation())) {
                    if (Role.getInstance().getMP() + item.getManaCost() >= 0) {
                        System.out.println("你使出了"+item.getId());
                        Role.getInstance().setMP(Role.getInstance().getMP() + item.getManaCost());
                        if ("groupAttack".equals(item.getAttackRange())) {
                            for (Monster object : monsters.getMonsters()) {
                                if (currentPlace.getName().equals(object.getCurrentPName())) {
                                    object.setHP(object.getHP() + item.getDamagepoints());
                                }
                            }
                            for (int i = 0; i < monsters.getMonsters().size(); i++) {
                                for (Monster nObject : monsters.getMonsters()) {
                                    if (nObject.getHP() <= 0) {
                                        monsterDie(nObject.getId());
                                        break;
                                    }
                                }
                            }
                        } else if ("singleAttack".equals(item.getAttackRange())) {
                            System.out.println("输入敌人ID");
                            Scanner id = new Scanner(System.in);
                            String enemyId = id.nextLine();
                            for (Monster object : monsters.getMonsters()) {
                                if (enemyId.equals(object.getId())) {
                                    object.setHP(object.getHP() + item.getDamagepoints());
                                    if(object.getHP()>0){
                                        Role.getInstance().setHP(Role.getInstance().getHP()+object.getDamagepoints());
                                    }
                                }
                            }
                            for (int i = 0; i < monsters.getMonsters().size(); i++) {
                                for (Monster nObject : monsters.getMonsters()) {
                                    if (nObject.getHP() <= 0) {
                                        monsterDie(nObject.getId());
                                        break;
                                    }
                                }
                            }
                        } else if ("treatment".equals(item.getAttackRange())) {
                            if (item.getHpTreat() + Role.getInstance().getHP() > Role.getInstance().getHPmax()) {
                                Role.getInstance().setHP(Role.getInstance().getHPmax());
                            } else {
                                Role.getInstance().setHP(item.getHpTreat() + Role.getInstance().getHP());
                            }
                        }

                    }
                    else{
                        System.out.println("法力值不足,无法释放!");
                    }
                }
                else {
                    exist = true;
                }
            }
            else{
                exist=true;
            }
        }
        if(!exist){
            System.out.println("此技能不存在!");
        }

    }

    public void createSkill(){
        Skill skill1=new Skill(-500,"龙拳",-60,"孙悟饭","groupAttack");
        Skill skill2=new Skill(-300,"龟派气功",-20,"孙悟饭","singleAttack");
        Skill skill3=new Skill("治疗",-20,"克林","treatment",100);
        Skill skill4=new Skill("紧急治疗",-80,"克林","treatment",200);
        Skill skill5=new Skill(-80,"太阳拳",-100,"克林","groupAttack");
        Role.getInstance().getSkills().getSkills().add(skill1);
        Role.getInstance().getSkills().getSkills().add(skill2);
        Role.getInstance().getSkills().getSkills().add(skill3);
        Role.getInstance().getSkills().getSkills().add(skill4);
        Role.getInstance().getSkills().getSkills().add(skill5);
        Skill skill9=new Skill("手指光弹",-100,-50);
        Skill skill10=new Skill("死之彗星",-200,-50);
        Skill skill11=new Skill("死亡光束",-300,-100);
        Skill skill12=new Skill("超新星",-400,-100);
        archmage.getSkills().getSkills().add(skill9);
        archmage.getSkills().getSkills().add(skill10);
        archmage.getSkills().getSkills().add(skill11);
        archmage.getSkills().getSkills().add(skill12);
    }

    public void play() {
        createMonster();
        createSkill();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        createRole(line);
        game();
        in.close();
    }

}
