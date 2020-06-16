package com.xiaowen.method;

import com.xiaowen.Methods;
import com.xiaowen.Ui;

public class MethodAttack extends Methods {

        public MethodAttack(Ui ui){
            super(ui);
        }

        public void doCmd(String word){
            ui.attack(word);
        }

}
