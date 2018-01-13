package com.donno.nj.domain;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumOrdinalTypeHandler extends BaseTypeHandler<IEnum>
{

    private Class<IEnum> type;
    private final IEnum[] enums;

    public EnumOrdinalTypeHandler(Class<IEnum> type)
    {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null)
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
    }



    @Override
    public IEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if(rs.wasNull()) {
            return null;
        } else {
            try {
                return this.enums[i];
            } catch (Exception var5) {
                throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", var5);
            }
        }
    }





    public IEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if(rs.wasNull()) {
            return null;
        } else {
            try {
                return this.enums[i];
            } catch (Exception var5) {
                throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", var5);
            }
        }
    }

    public IEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if(cs.wasNull()) {
            return null;
        } else {
            try {
                return this.enums[i];
            } catch (Exception var5) {
                throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", var5);
            }
        }
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getIndex());
    }
}
