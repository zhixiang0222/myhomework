package com.offcn.service;

import com.offcn.bean.Erole;
import com.offcn.util.BaseResult;

import java.util.List;

public interface RoleService {
    /**
     * 根据条件获取角色列表
     * @param erole
     * @return
     */

 List<Erole> findRolesByCondition(Erole erole);

    /**
     * 根据条件获取角色列表条数
     * @param erole
     * @return
     */
 int countRolesByCondition(Erole erole);

    /**
     * 新增角色
     * @param erole
     * @return
     */
    BaseResult addRole(Erole erole);

    /**
     *根据角色id获取角色功能信息
      * @param rid
     * @return
     */
    Erole findEroleFunctionByRid(long rid);

    /**
     * 修改角色信息
     * @param erole
     * @return
     */
    BaseResult updateRole(Erole erole);

    /**
     * 根据角色id删除角色信息
     * @param rid
     * @return
     */
    BaseResult deleteRoleByRid(long rid);

    /**
     * 批量删除
     * @param rids
     * @return
     */
     BaseResult batchDelete(String rids);

    List<Erole> getAllRoles();
 }