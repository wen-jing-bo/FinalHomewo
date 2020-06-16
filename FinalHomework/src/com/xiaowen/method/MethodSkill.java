package com.xiaowen.method;

import com.xiaowen.Methods;
import com.xiaowen.Ui;

public class MethodSkill extends Methods {
    public MethodSkill(Ui ui) {
        super(ui);
    }

    public void doCmd(String word){
        ui.useSkill(word);
    }
}
