package com.hzx.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("insertFill");
//        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.setFieldValByName("createtime", new Date(), metaObject);
        this.setFieldValByName("updatetime", new Date(), metaObject);
        // 如果还有其他字段需要自动填充，可以继续调用strictInsertFill方法
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("updateFill");
//        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.setFieldValByName("updatetime", LocalDateTime.now(), metaObject);
        // 如果需要更新时自动填充字段，可以在这里实现
    }
}
