package com.offcn.realm;


import com.offcn.bean.Employee;
import com.offcn.bean.EmployeeExample;
import com.offcn.bean.Erole;
import com.offcn.dao.EmployeeMapper;
import com.offcn.dao.EroleMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmployeeRealm  extends AuthorizingRealm {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EroleMapper eroleMapper;

    /**
     * 获取数据库授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录用户
        Employee employee = (Employee) principalCollection.getPrimaryPrincipal();
        //获取当前用户角色信息
        List<Erole> eroles = eroleMapper.findRolesByEid(employee.getEid());
        //将用户角色信息返回给shiro
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Erole erole:eroles){
           simpleAuthorizationInfo.addRole(erole.getRcode());
        }



        return simpleAuthorizationInfo;
    }

    /**
     * 获取数据库认证信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取数据库要校验的用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取页面传入的用户名
        String jobnumber = token.getUsername();
        //根据用户名获取用户信息
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andJobnumberEqualTo(jobnumber);
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        if(employeeList!=null&&employeeList.size()>0){
            Employee employee = employeeList.get(0);

            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(employee,employee.getPassword(),token.getUsername());
            //设置加密盐
            simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(employee.getRemark1()));
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
