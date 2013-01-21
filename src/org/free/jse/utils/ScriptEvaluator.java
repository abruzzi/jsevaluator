package org.free.jse.utils;

import java.io.File;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEvaluator {
    
    public static Object evalScript(String script){
        Object result = new Object();
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            result = engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Object evalScript(File file){
    	return evalScript(FileUtil.getContents(file));
    }
    
}
