package org.zxs.test.step040.entity;

import java.sql.ResultSet;

public abstract class AbstractEntityHelper {
    /**
     * 把数据集换成对象
     * @param rs
     * @return
     * @throws Exception
     */
    public abstract Object create(ResultSet rs) throws Exception;
}
