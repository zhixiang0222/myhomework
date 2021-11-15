package com.offcn.dao;

import com.offcn.bean.Efunction;
import com.offcn.bean.EfunctionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EfunctionMapper {
    long countByExample(EfunctionExample example);

    int deleteByExample(EfunctionExample example);

    int deleteByPrimaryKey(Integer fid);

    int insert(Efunction record);

    int insertSelective(Efunction record);

    List<Efunction> selectByExample(EfunctionExample example);

    Efunction selectByPrimaryKey(Integer fid);

    int updateByExampleSelective(@Param("record") Efunction record, @Param("example") EfunctionExample example);

    int updateByExample(@Param("record") Efunction record, @Param("example") EfunctionExample example);

    int updateByPrimaryKeySelective(Efunction record);

    int updateByPrimaryKey(Efunction record);

    /**
     * 根据员工id获取功能信息
     * @param eid
     * @return
     */
    List<Efunction> findFunctionByEid(long eid);


    List<Efunction> findFunctionByCondition(Efunction efunction);

    int countFunctionsByCondition(Efunction efunction);

    /**
     * 根据父级功能id修改子功能的父级功能名称
     * @param efunction
     * @return
     */
    void updateRemark2ByParentId(Efunction efunction);
}