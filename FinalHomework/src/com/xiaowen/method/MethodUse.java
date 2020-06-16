package com.xiaowen.method;

import com.xiaowen.Methods;
import com.xiaowen.Ui;

public class MethodUse extends Methods {
    public MethodUse(Ui ui){
        super(ui);
    }

    public void doCmd(String word){
        ui.usePotion(word);
    }
}
