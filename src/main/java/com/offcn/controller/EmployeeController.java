package com.offcn.controller;


import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Employee;

import com.offcn.service.EmployeeService;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.awt.image.BandedSampleModel;
import java.util.List;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录校验
     * @param jobnumber
     * @param password
     * @return
     */
    @RequestMapping("loginCheck")
    @ResponseBody
    public EmployeeResult loginCheck(String jobnumber, String password){
       EmployeeResult result = employeeService.shiroLogin(jobnumber,password);
       //将登录成功的用户存放到session中
        //session.setAttribute("employee",result.getEmployee());
       return result;
    }

    /**
     * 根据条件分页查询员工信息
     * @param employee
     * @return
     */
    @RequestMapping("getEmployeeList")
    @ResponseBody
    public JSONObject getEmployeeList(Employee employee){
       //获取分页列表数据
        ListDocument.List<Employee> employeeList = employeeService.findEmployeesByCondition(employee);
        //获取总的条数
        int total = employeeService.countEmployeesByCondition(employee);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",employeeList);
        jsonObject.put("total",total);
        return jsonObject;
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @RequestMapping("addEmployee")
    @ResponseBody
    public BaseResult addEmployee(Employee employee){
       BaseResult result = employeeService.addEmployee(employee);
       return  result;
    }

    /**
     * 获取要修改的员工信息存放到session中
     * @param eid
     * @return
     */
    @RequestMapping("getUpdateEmployee")
    @ResponseBody
    public BaseResult getUpdateEmployee(long eid){
        Employee employee = employeeService.findEmployeeRoles(eid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updateEmployee",employee);
        BaseResult result = new BaseResult();
        result.setSuccess(true);
        return  result;
    }

    /**
     * 获取要修改的员工信息
     * @return
     */
    @RequestMapping("findUpdateEmployee")
    @ResponseBody
    public Employee findUpdateEmployee(){
       Session session = SecurityUtils.getSubject().getSession();
       Employee employee = (Employee) session.getAttribute("updateEmployee");
       return employee;
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @RequestMapping("updateEmployee")
    @ResponseBody
    public BaseResult updateEmployee(Employee employee){
       BaseResult result = employeeService.updateEmployee(employee);
       return result;
    }

    /**
     * 删除员工信息
     * @param eid
     * @return
     */
    @RequestMapping("deleteEmployeeByEid")
    @ResponseBody
    public BaseResult deleteEmployeeByEid(long eid){
        BaseResult result = employeeService.deleteEmployeeByEid(eid);
        return result;
    }

    /**
     * 批量删除
     * @param eids
     * @return
     */
    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String eids){
      BaseResult result = employeeService.batchDelete(eids);
      return result;
    }

    @RequestMapping("getCurrentEmployee")
    @ResponseBody
    public Employee getCurrentEmployee(){
       Session session=SecurityUtils.getSubject().getSession();
       Employee employee = (Employee) session.getAttribute("employee");
       return employee;
    }

}
