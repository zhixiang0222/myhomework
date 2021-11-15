package com.offcn.service;

import com.offcn.bean.*;
import com.offcn.dao.EmployeeRoleMapper;
import com.offcn.dao.EroleMapper;
import com.offcn.dao.RoleFunctionMapper;
import com.offcn.util.BaseResult;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleService.class);
    @Autowired
    private EroleMapper eroleMapper;
    @Autowired
    private RoleFunctionMapper roleFunctionMapper;
    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;
    @Override
    public List<Erole> findRolesByCondition(Erole erole) {
        erole.setLimitStart((erole.getPage()-1)*erole.getRows());
        List<Erole> eroles = eroleMapper.findRolesByCondition(erole);
        return eroles;
    }

    @Override
    public int countRolesByCondition(Erole erole) {
        return eroleMapper.countRolesByCondition(erole);
    }

    @Override
    @Transactional
    public BaseResult addRole(Erole erole) {
        BaseResult result = new BaseResult();
        logger.info("新增角色入参方法开始，入参："+erole);
        EroleExample eroleExample = new EroleExample();
        EroleExample.Criteria criteria = eroleExample.createCriteria();
        criteria.andRcodeEqualTo(erole.getRcode());
        List<Erole> eroles = eroleMapper.selectByExample(eroleExample);;
        if (eroles!=null&&eroles.size()>0){
            result.setSuccess(true);
            result.setMessage("角色编码重复！");
            return result;
        }
        try {
            //新增角色表
            eroleMapper.insert(erole);
            //新增角色功能关系表
            String[] fidArray = erole.getFids().split(",");
            for (String fid:fidArray){
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setRid(erole.getRid());
                roleFunction.setFid((int) Long.parseLong(fid));
                roleFunctionMapper.insert(roleFunction);
            }
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");

        }
        logger.info("新增方法结束，出参:"+result);
        return result;
    }

    @Override
    public Erole findEroleFunctionByRid(long rid) {
        Erole erole = roleFunctionMapper.fidEroleFunctionByRid(rid);
        return erole;
    }

    @Override
    @Transactional
    public BaseResult updateRole(Erole erole) {
        logger.info("修改角色功能开始，入参："+erole);
        BaseResult result = new BaseResult();
        try {
            EroleExample eroleExample = new EroleExample();
            EroleExample.Criteria criteria= eroleExample.createCriteria();
            criteria.andRcodeEqualTo(erole.getRcode());
            criteria.andRidNotEqualTo(erole.getRid());
            List<Erole> eroles = eroleMapper.selectByExample(eroleExample);
            if (eroles!=null&&eroles.size()>0){
                result.setSuccess(false);
                result.setMessage("角色编码重复");
                return result;
            }
            //修改角色表基本信息
            eroleMapper.updateByPrimaryKey(erole);
            //删除以前的角色关系表数据
            RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria1 = roleFunctionExample.createCriteria();
            criteria1.andRidEqualTo(erole.getRid());
            roleFunctionMapper.deleteByExample(roleFunctionExample);
            // 新增修改之后的角色关系表数据
            String[] fidArray = erole.getFids().split(",");
            for (String fid:fidArray){
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setRid(erole.getRid());
                roleFunction.setFid((int) Long.parseLong(fid));
                roleFunctionMapper.insert(roleFunction);
            }
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch (Exception e){
          e.printStackTrace();
          result.setSuccess(false);
          result.setMessage("操作失败");
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        logger.info("修改方法结束，出参："+result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult deleteRoleByRid(long rid) {
        BaseResult result = new BaseResult();
        logger.info("删除角色方法开始，入参"+rid);
        try{
            //删除角色关系表数据
            RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria = roleFunctionExample.createCriteria();
            criteria.andRidEqualTo((int) rid);
            roleFunctionMapper.deleteByExample(roleFunctionExample);
            //删除角色员工关系表数据
            EmployeeRoleExample employeeRoleExample = new EmployeeRoleExample();
            EmployeeRoleExample.Criteria criteria1 = employeeRoleExample.createCriteria();
            criteria1.andRidEqualTo((int) rid);
            employeeRoleMapper.deleteByExample(employeeRoleExample);
            //删除角色表数据
            eroleMapper.deleteByPrimaryKey((int) rid);
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch(Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        logger.info("删除角色方法结束，出参："+result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult batchDelete(String rids) {
        BaseResult result = new BaseResult();
        logger.info("批量删除操作方法开始，入参："+rids);
        try{
            String[] fidArray=rids.split(",");
            for (String rid:fidArray){
                //循环调用删除方法
                BaseResult result1= deleteRoleByRid(Long.parseLong(rid));
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

    @Override
    public List<Erole> getAllRoles() {
        EroleExample eroleExample = new EroleExample();
        List<Erole> eroles = eroleMapper.selectByExample(eroleExample);
        return eroles;
    }


}
