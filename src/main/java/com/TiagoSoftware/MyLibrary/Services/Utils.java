package com.TiagoSoftware.MyLibrary.Services;

import org.springframework.beans.BeanUtils;

import java.util.List;

public class Utils<T> {
    //BeanUtils.copyProperties(authDTO, auth);

    public List copyAllTheseProperties(List<T> source, List<Object> target){
        for(Object objectItem : source){
            var targetItem = new Object();
            BeanUtils.copyProperties(objectItem, targetItem);
            target.add(targetItem);
        }
        return target;
    }
}
