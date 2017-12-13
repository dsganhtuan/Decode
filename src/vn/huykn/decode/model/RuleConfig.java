/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.huykn.decode.model;

/**
 *
 * @author huykn
 */
public class RuleConfig {
    public enum Type{
        All,
        Content,
        Abs
    }
    
    private Type type;
    private String key;
    private String value;

    public RuleConfig() {
    }

    public RuleConfig(Type type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
