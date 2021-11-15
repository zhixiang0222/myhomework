package com.offcn.dao;

import com.offcn.bean.Erole;
import com.offcn.bean.EroleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EroleMapper {
    long countByExample(EroleExample example);

    int deleteByExample(EroleExample example);

    int deleteByPrimaryKey(Integer rid);

    int insert(Erole record);

    int insertSelective(Erole record);

    List<Erole> selectByExample(EroleExample example);

    Erole selectByPrimaryKey(Integer rid);

    int updateByExampleSelective(@Param("record") Erole record, @Param("example") EroleExample example);

    int updateByExample(@Param("record") Erole record, @Param("example") EroleExample example);

    int updateByPrimaryKeySelective(Erole record);

    int updateByPrimaryKey(Erole record);

    /**
     * 根据用户id获取角色信息
     * @param eid
     * @return
     */
    List<Erole> findRolesByEid(long eid);

    /**
     * 根据条件获取角色列表数据
     * @param erole
     * @return
     */
    List<Erole> findRolesByCondition(Erole erole);

    /**
     * 根据条件统计总的条数
     * @param erole
     * @return
     */
    int countRolesByCondition(Erole erole);
}