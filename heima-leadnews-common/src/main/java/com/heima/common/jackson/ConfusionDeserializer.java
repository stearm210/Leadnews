package com.heima.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.heima.utils.common.IdsUtils;

import java.io.IOException;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-18 15:07:30
  * @Params:
  * @Return: null
  * @Description: 永续反序列化自增数字的混淆解密
  */
public class ConfusionDeserializer extends JsonDeserializer<Object> {

    JsonDeserializer<Object>  deserializer = null;
    JavaType type =null;

    public  ConfusionDeserializer(JsonDeserializer<Object> deserializer, JavaType type){
        this.deserializer = deserializer;
        this.type = type;
    }

    @Override
    public  Object deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException{
        try {
            if(type!=null){
                if(type.getTypeName().contains("Long")){
                    return Long.valueOf(p.getValueAsString());
                }
                if(type.getTypeName().contains("Integer")){
                    return Integer.valueOf(p.getValueAsString());
                }
            }
            return IdsUtils.decryptLong(p.getValueAsString());
        }catch (Exception e){
            if(deserializer!=null){
                return deserializer.deserialize(p,ctxt);
            }else {
                return p.getCurrentValue();
            }
        }
    }
}
