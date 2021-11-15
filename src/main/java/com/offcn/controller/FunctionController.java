package com.offcn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Efunction;
import com.offcn.bean.Employee;
import com.offcn.service.FunctionService;
import com.offcn.util.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.ls.LSInput;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;
    @RequestMapping("getCurrentEmployeeFunctions")
    @ResponseBody
    public JSONArray getCurrentEmployeeFunctions(HttpSession session){
        //获取当前登录用户
        Employee employee = (Employee) session.getAttribute("employee");
        List<Efunction> efunctionList = functionService.findFunctionByEid(employee.getEid());

        JSONArray jsonArray = functionService.convert2(efunctionList,0L);

       return jsonArray;
    }

    /**
     * 分页条件获取功能列表数据
     * @return
     */
    @RequestMapping("getFunctionList")
    @ResponseBody
    public JSONObject findFuntionsByCondition(Efunction efunction){
       //获取分页列表数据
        List<Efunction> efunctions = functionService.findFunctionByCondition(efunction);
        //获取总的条数
        int total = functionService.countFunctionByCondition(efunction);
        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",efunctions);
        jsonObject.put("total",total);

        return jsonObject;
    }

    /**
     * 获取系统所有一级功能
     * @return
     */
    @RequestMapping("getFirstFunctions")
    @ResponseBody
    public  List<Efunction> getFirstFunctions(){

        return functionService.getFirstFunctions();

    }

    /**
     * 新增功能
     * @param efunction
     * @return
     */
    @RequestMapping("addFunction")
    @ResponseBody
    public BaseResult addFunction(Efunction efunction){
      BaseResult result = functionService.addFunction(efunction);
      return result;
    }

    /**
     * 获取要修改的功能信息并存放到session中
     * @param fid
     * @return
     */
    @RequestMapping("getUpdateFunction")
    @ResponseBody
    public BaseResult getUpdateFunction(long fid){
        BaseResult result = new BaseResult();
        //获取要修改的功能信息
        Efunction efunction = functionService.findFunctionByFid(fid);
        //将修改的功能信息存放到session中
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updateFunction",efunction);
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取存放在session中的修改权限
     * @return
     */
    @RequestMapping("findUpdateFunction")
    @ResponseBody
    public Efunction findUpdateFunction(){
       //获取session
        Session session = SecurityUtils.getSubject().getSession();
        Efunction efunction = (Efunction) session.getAttribute("updateFunction");
        return efunction;
    }

    /**
     * 修改功能权限
     * @param efunction
     * @return
     */
    @RequestMapping("updateFunction")
    @ResponseBody
    public BaseResult updateFunction(Efunction efunction){
        BaseResult result = functionService.updateFunction(efunction);
        return result;
    }

    /**
     * 根据功能id删除功能信息
     * @param fid
     * @return
     */
    @RequestMapping("deleteFunctionByFid")
    @ResponseBody
    public BaseResult deleteFunctionByFid(long fid){
      BaseResult result = functionService.deleteFunctionByFid(fid);
        return result;
    }

    /**
     * 批量删除
     * @param fids
     * @return
     */
    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String fids){
      BaseResult result = functionService.batchDelete(fids);
      return result;
    }

    /**
     * 根据父功能id获取子功能列表
     * @param parentid
     * @return
     */
    @RequestMapping("findFunctionsByParentid")
    @ResponseBody
    public List<Efunction> findFunctionsByParentid(long parentid){
        List<Efunction> efunctions = functionService.findFunctionsByParentid(parentid);
        return efunctions;

    }
}
