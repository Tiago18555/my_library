package com.TiagoSoftware.MyLibrary.Utils;

import org.springframework.beans.BeanUtils;

import java.util.List;

public class Utils<T> {

    public List copyAllTheseProperties(List<T> source, List<Object> target){
        for(Object objectItem : source){
            var targetItem = new Object();
            BeanUtils.copyProperties(objectItem, targetItem);
            target.add(targetItem);
        }
        return target;
    }
}
