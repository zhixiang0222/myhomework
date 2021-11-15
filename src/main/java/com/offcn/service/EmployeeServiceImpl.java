package com.offcn.service;

import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeExample;
import com.offcn.bean.EmployeeRole;
import com.offcn.bean.EmployeeRoleExample;
import com.offcn.dao.EmployeeMapper;
import com.offcn.dao.EmployeeRoleMapper;
import com.offcn.util.BaseResult;
import com.offcn.util.EmployeeResult;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private static Logger logger = Logger.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;

    @Override
    public EmployeeResult loginCheck(String jobnumber, String password) {
        logger.info("登录校验开始，入参:"+jobnumber);
        EmployeeResult result = new EmployeeResult();

        try{
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andJobnumberEqualTo(jobnumber);
            criteria.andPasswordEqualTo(password);
            List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
            if(employeeList!=null&&employeeList.size()>0){
                result.setLoginSuccess(true);
                result.setMessage("登录成功");
                result.setEmployee(employeeList.get(0));
            }else{
                result.setLoginSuccess(false);
                result.setMessage("用户名或密码错误");
            }
            result.setSuccess(true);
        }catch (Exception e){
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setMessage("操作失败");
        }

        logger.info("登录校验方法结束，出参"+result);
        return result;
    }

    @Override
    public EmployeeResult shiroLogin(String jobnumber, String password) {
        EmployeeResult result = new EmployeeResult();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(jobnumber,password);
        try{
            subject.login(token);
            result.setSuccess(true);
            result.setLoginSuccess(true);
            result.setMessage("登录成功");
            //获取Shiro的Session
            Session session = subject.getSession();
            //将登录成功的用户存放到session中
            Employee employee = (Employee) subject.getPrincipal();
            session.setAttribute("employee",employee);
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setLoginSuccess(false);
            result.setMessage("登录失败");
        }

        return result;
    }

    @Override
    public List<Employee> findEmployeesByCondition(Employee employee) {
        employee.setLimitStart((employee.getPage()-1)*employee.getRows());
        List<Employee> employees = employeeMapper.findEmployeesByCondition(employee);
        //遍历设置性别和日期
        for (Employee employee1:employees){
            if(employee1.getEsex()==0){
                employee1.setEsexStr("男");
            }else{
                employee1.setEsexStr("女");
            }
            //使用hireDateStr来显示时间
           // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
           // employee1.setHireDateStr((simpleDateFormat.format(employee1.getHireDate())));
        }
        return employees;
    }

    @Override
    public int countEmployeesByCondition(Employee employee) {
        return employeeMapper.countEmployeesByCondition(employee);
    }

    @Override
    @Transactional
    public BaseResult addEmployee(Employee employee) {
        BaseResult result = new BaseResult();
        logger.info("新增员工方法开始，入参："+employee);
        try{
            //校验工号唯一
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andJobnumberEqualTo(employee.getJobnumber());
            List<Employee> employees = employeeMapper.selectByExample(employeeExample);
            if (employee!=null&&employees.size()>0){
                result.setSuccess(false);
                result.setMessage("工号已存在");
                return result;
            }
            //生成加密盐（随机数）
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            employee.setRemark1(salt);
            //密码加密
            String newpassword = new Md5Hash(employee.getPassword(),salt,3).toString();
            employee.setPassword(newpassword);
            //新增员工
            employeeMapper.insert(employee);
            //新增员工角色关系表
            String[] ridArray=employee.getRids().split(",");
            for (String rid:ridArray){
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEid(employee.getEid());
                employeeRole.setRid((int) Long.parseLong(rid));
                employeeRoleMapper.insert(employeeRole);
            }
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }
        logger.info("新增员工方法结束，出参"+result);
        return result;
    }

    @Override
    public Employee findEmployeeByEid(long eid) {
        Employee employee = employeeMapper.selectByPrimaryKey((int) eid);
        return employee;
    }

    @Override
    public Employee findEmployeeRoles(long eid) {
        Employee employee = employeeRoleMapper.findEmployeeRolesByEid(eid);
        return employee;
    }

    @Override
    @Transactional
    public BaseResult updateEmployee(Employee employee) {
        logger.info("修改员工方法开始，入参："+employee);
        BaseResult result = new BaseResult();
        try{
            //校验工号唯一
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria=employeeExample.createCriteria();
            criteria.andJobnumberEqualTo(employee.getJobnumber());
            criteria.andEidNotEqualTo(employee.getEid());
            List<Employee> employees = employeeMapper.selectByExample(employeeExample);
            if (employee!=null&&employees.size()>0){
                result.setSuccess(false);
                result.setMessage("该工号已存在！");
                return result;
            }
            //修改员工基本信息
            employeeMapper.updateByPrimaryKeySelective(employee);
            //删除以前的员工角色关系表数据
            EmployeeRoleExample employeeRoleExample = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria1 = employeeRoleExample.createCriteria();
            criteria1.andEidEqualTo(employee.getEid());
            employeeRoleMapper.deleteByExample(employeeRoleExample);
            //新增修改之后的员工角色关系表数据
            String[] ridArray=employee.getRids().split(",");
            for (String rid:ridArray){
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEid(employee.getEid());
                employeeRole.setRid((int) Long.parseLong(rid));
                employeeRoleMapper.insert(employeeRole);
            }

            result.setSuccess(true);
            result.setMessage("操作成功");

        }catch (Exception e){
           e.printStackTrace();
           result.setSuccess(false);
           result.setMessage("操作失败");
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult deleteEmployeeByEid(long eid) {
        logger.info("删除员工方法开始，入参："+eid);
        BaseResult result = new BaseResult();
        try{
            //删除员工角色关系表数据
            EmployeeRoleExample employeeRoleExample = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria = employeeRoleExample.createCriteria();
            criteria.andEidEqualTo((int) eid);
            employeeRoleMapper.deleteByExample(employeeRoleExample);
            //删除员工信息
            employeeMapper.deleteByPrimaryKey((int) eid);
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch (Exception e){
           e.printStackTrace();
           result.setSuccess(false);
           result.setMessage("操作失败");
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult batchDelete(String eids) {
        BaseResult result = new BaseResult();
        logger.info("批量删除操作方法开始，入参："+eids);
        try{
            String[] eidArray=eids.split(",");
            for (String eid:eidArray){
                //循环调用删除方法
                BaseResult result1= deleteEmployeeByEid(Long.parseLong(eid));
                //如果结果为false抛出异常
                if (!result1.isSuccess()){
                    throw new Exception();
                }
            }
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch (Exception e){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setSuccess(false);
            result.setMessage("操作失败");
        }
        logger.info("批量删除操作结束，出参："+result);
        return result;
    }
}
