package com.xiaowen.method;

import com.xiaowen.Methods;
import com.xiaowen.Ui;

public class MethodChange extends Methods {
    public MethodChange(Ui ui){
        super(ui);
    }
    public void doCmd(String word){
        ui.change(word);
    }
}
