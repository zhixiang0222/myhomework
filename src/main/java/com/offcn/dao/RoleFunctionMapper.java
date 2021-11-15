package com.offcn.dao;

import com.offcn.bean.Erole;
import com.offcn.bean.RoleFunction;
import com.offcn.bean.RoleFunctionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleFunctionMapper {
    long countByExample(RoleFunctionExample example);

    int deleteByExample(RoleFunctionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RoleFunction record);

    int insertSelective(RoleFunction record);

    List<RoleFunction> selectByExample(RoleFunctionExample example);

    RoleFunction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RoleFunction record, @Param("example") RoleFunctionExample example);

    int updateByExample(@Param("record") RoleFunction record, @Param("example") RoleFunctionExample example);

    int updateByPrimaryKeySelective(RoleFunction record);

    int updateByPrimaryKey(RoleFunction record);

    /**
     * 根据角色获取id
     * @param rid
     * @return
     */
    Erole fidEroleFunctionByRid(long rid);
}