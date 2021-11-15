package com.offcn.dao;

import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeRole;
import com.offcn.bean.EmployeeRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmployeeRoleMapper {
    long countByExample(EmployeeRoleExample example);

    int deleteByExample(EmployeeRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeRole record);

    int insertSelective(EmployeeRole record);

    List<EmployeeRole> selectByExample(EmployeeRoleExample example);

    EmployeeRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EmployeeRole record, @Param("example") EmployeeRoleExample example);

    int updateByExample(@Param("record") EmployeeRole record, @Param("example") EmployeeRoleExample example);

    int updateByPrimaryKeySelective(EmployeeRole record);

    int updateByPrimaryKey(EmployeeRole record);

    /**
     * 根据员工id获取员工角色信息
     * @param eid
     * @return
     */
    Employee findEmployeeRolesByEid(long eid);
}