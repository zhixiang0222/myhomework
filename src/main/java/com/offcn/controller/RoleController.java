package com.offcn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Efunction;
import com.offcn.bean.Erole;
import com.offcn.service.FunctionService;
import com.offcn.service.RoleService;
import com.offcn.util.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private FunctionService functionService;
    /**
     * 分页获取角色列表数据
     * @return
     */
    @RequestMapping("getRoleList")
    @ResponseBody
    public JSONObject getRoleList(Erole erole){
        JSONObject jsonObject = new JSONObject();
      //获取角色分页列表展示数据
        List<Erole> eroleList = roleService.findRolesByCondition(erole);
        //获取角色列表总的条数
        int total = roleService.countRolesByCondition(erole);
        jsonObject.put("rows",eroleList);
        jsonObject.put("total",total);
        return jsonObject;
    }

    /**
     *新增角色
     * @param erole
     * @return
     */
    @RequestMapping("addRole")
    @ResponseBody
    public BaseResult addRole(Erole erole){
        BaseResult result = roleService.addRole(erole);
        return result;
    }

    /**
     * 获取要修改的角色功能信息，并存到session中
     * @param rid
     * @return
     */
    @RequestMapping("getUpdateRoleFunction")
    @ResponseBody
    public BaseResult getUpdateRoleFunction(long rid){
        BaseResult result = new BaseResult();
        //获取要修改的角色功能信息
        Erole erole = roleService.findEroleFunctionByRid(rid);
        //将要修改的信息存到session中
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updateRoleFunction",erole);
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取session中要修改的角色信息
     * @return
     */
    @RequestMapping("findRole")
    @ResponseBody
    public Erole findRole(){
        Session session = SecurityUtils.getSubject().getSession();
        Erole erole = (Erole) session.getAttribute("updateRoleFunction");
        return erole;
    }

    /**
     * 获取角色功能信息
     * @return
     */
    @RequestMapping("roleFunctions")
    @ResponseBody
    public JSONArray roleFunctions(){
        //获取角色功能
        Session session = SecurityUtils.getSubject().getSession();
        Erole erole = (Erole) session.getAttribute("updateRoleFunction");
        List<Efunction> efunctions = erole.getEfunctionList();
        //获取系统所有功能
        List<Efunction> allFunctions = functionService.getAllFunctions();
        JSONArray jsonArray = functionService.convert3(allFunctions,efunctions,0L);
        return jsonArray;
}

    /**
     * 修改角色信息
     * @param erole
     * @return
     */
    @RequestMapping("updateRole")
    @ResponseBody
    public BaseResult updateRole(Erole erole){
     BaseResult result = roleService.updateRole(erole);
     return result;
    }

    /**
     * 根据角色id删除角色信息
     * @param rid
     * @return
     */
    @RequestMapping("deleteRoleByRid")
    @ResponseBody
    public BaseResult deleteRoleByRid(long rid){
          BaseResult result = roleService.deleteRoleByRid(rid);
          return result;
    }

    /**
     * 批量删除
     * @return
     */
    @RequestMapping("batchDelete")
    @ResponseBody
    public  BaseResult batchDelete(String rids){
        BaseResult result = roleService.batchDelete(rids);
        return result;
    }

    /**
     * 获取系统所有角色信息
     * @return
     */
    @RequestMapping("getAllRoles")
    @ResponseBody
    public List<Erole> getAllRoles(){
       List<Erole> eroles = roleService.getAllRoles();
       return eroles;

    }
}
