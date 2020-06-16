package com.xiaowen.method;

import com.xiaowen.Methods;
import com.xiaowen.Ui;

public class MethodGo extends Methods {
    public  MethodGo(Ui ui){
        super(ui);
    }
    public void doCmd(String word){ui.goPlace(word);}
}
