package com.offcn.service;

import com.offcn.bean.Employee;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;

import java.util.List;

public interface EmployeeService {

    public EmployeeResult loginCheck(String jobnumber,String password);

    /**
     * 使用shiro做登录认证校验
     * @param jobnumber
     * @param password
     * @return
     */
    public EmployeeResult shiroLogin(String jobnumber,String password);

    /**
     * 根据id分列展示数据
     * @param employee
     * @return
     */
    public List<Employee> findEmployeesByCondition(Employee employee);

    /**
     * 统计条数
     * @param employee
     * @return
     */
    public int countEmployeesByCondition(Employee employee);

    BaseResult addEmployee(Employee employee);

    public Employee findEmployeeByEid(long eid);

    /**
     * 根据员工id获取员工角色信息
     * @param eid
     * @return
     */
    public Employee findEmployeeRoles(long eid);

    BaseResult updateEmployee(Employee employee);

    BaseResult deleteEmployeeByEid(long eid);

    BaseResult batchDelete(String eids);

}
