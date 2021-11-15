package com.offcn.service;

import com.alibaba.fastjson.JSONArray;
import com.offcn.bean.Efunction;
import com.offcn.util.BaseResult;

import java.util.List;

public interface FunctionService {
    /**
     * 根据用户获取id
     * @param eid
     * @return
     */
    public List<Efunction> findFunctionByEid(long eid);

    /**
     * 将功能列表转换为easyui识别的菜单格式json字符串
     * @param efunctions
     * @return
     */
    public JSONArray convert(List<Efunction> efunctions);

    /**
     * 将功能列表转换为easyui识别的菜单格式json字符串
     * @param efunctions
     * @param parentId
     * @return
     */
    public JSONArray convert2(List<Efunction> efunctions,long parentId);



    public JSONArray convert3(List<Efunction> allFunctions,List<Efunction> roleFunctions,long parentId);

    /**
     *根据条件分页获取权限列表数据
     */
    public  List<Efunction> findFunctionByCondition(Efunction efunction);

    /**
     * 根据条件统计权限列表总的条数
     */
    public int countFunctionByCondition(Efunction efunction);

    /**
     * 获取系统所有一级功能
     * @return
     */
    public  List<Efunction> getFirstFunctions();

    /**
     * 新增功能权限
     * @param efunction
     * @return
     */
    BaseResult addFunction(Efunction efunction);

    /**
     * 根据主键id获取功能信息
     * @param fid
     * @return
     */
    Efunction findFunctionByFid(long fid);

    /**
     * 修改功能权限
     * @param efunction
     * @return
     */
    BaseResult updateFunction(Efunction efunction);

    /**
     * 根据功能id删除功能信息
     * @param fid
     * @return
     */
    BaseResult deleteFunctionByFid(long fid);

    /**
     * 批量删除
     * @param fids
     * @return
     */
    BaseResult batchDelete(String fids);

    /**
     * 根据父功能id获取子功能信息
     * @param parentid
     * @return
     */
    List<Efunction> findFunctionsByParentid(long parentid);

    /**
     * 获取系统所有功能
     * @return
     */
    List<Efunction> getAllFunctions();
}

