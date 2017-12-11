package com.donno.nj.domain;

import org.apache.ibatis.type.BaseTypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
public class EnumKeyTypeHandler extends BaseTypeHandler<IEnum>
{

    private Class<IEnum> type;
    private final IEnum[] enums;

    public EnumKeyTypeHandler(Class<IEnum> type)
    {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null)
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
    }

    /**
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param key 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    private IEnum locateIEnum(int key) {
        for(IEnum status : enums) {
            if(status.getIndex()== key) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + key + ",请核对" + type.getSimpleName());
    }

    @Override
    public IEnum  getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return locateIEnum(rs.getInt(columnName));
    }

    @Override
    public IEnum  getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return locateIEnum(rs.getInt(columnIndex));
    }

    @Override
    public IEnum  getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return locateIEnum(cs.getInt(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    IEnum enumObj, JdbcType jdbcType) throws SQLException {
        // baseTypeHandler已经帮我们做了parameter的null判断
        ps.setInt(i, enumObj.getIndex());

    }
}
