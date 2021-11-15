package com.offcn.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.offcn.bean.Efunction;
import com.offcn.bean.EfunctionExample;
import com.offcn.bean.RoleFunctionExample;
import com.offcn.dao.EfunctionMapper;
import com.offcn.dao.RoleFunctionMapper;
import com.offcn.util.BaseResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService{
    private static Logger logger = Logger.getLogger(FunctionServiceImpl.class);
    @Autowired
    private EfunctionMapper efunctionMapper;
    @Autowired
    private RoleFunctionMapper roleFunctionMapper;
    @Override
    public List<Efunction> findFunctionByEid(long eid) {
        List<Efunction> efunctions = efunctionMapper.findFunctionByEid(eid);
        return efunctions;
    }

    @Override
    public JSONArray convert(List<Efunction> efunctions) {
        JSONArray jsonArray = new JSONArray();
        //获取当前用户所以一级功能
        for (Efunction efunction:efunctions){
            if(efunction.getParentid()==0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",efunction.getFid());
                jsonObject.put("text",efunction.getFname());
                jsonObject.put("state","closed");
                //获取一级功能的子功能
                JSONArray children=new JSONArray();
                for (Efunction efunction1:efunctions){
                    if(efunction1.getParentid()==efunction.getFid()){
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id",efunction.getFid());
                        jsonObject1.put("text",efunction.getFname());
                        jsonObject1.put("state","open");
                        jsonObject1.put("url",efunction1.getFurl());
                        children.add(jsonObject1);
                    }
                }
                jsonObject.put("children",children);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray convert2(List<Efunction> efunctions, long parentId) {
        JSONArray jsonArray = new JSONArray();

        //获取当前用户的一级功能,遍历当前功能的所有子功能
        for(Efunction efunction :efunctions){
            if(efunction.getParentid()==parentId){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",efunction.getFid());
                jsonObject.put("text",efunction.getFname());
                if(efunction.getRemark1().equals("YES")){
                    //表示当前功能是最后一级功能叶子节点
                    jsonObject.put("state","open");
                    jsonObject.put("furl",efunction.getFurl());
                }else{
                    JSONArray children = convert2(efunctions,efunction.getFid());
                    jsonObject.put("children",children);
                    jsonObject.put("state","closed");
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray convert3(List<Efunction> allFunctions, List<Efunction> roleFunctions, long parentId) {
        JSONArray jsonArray = new JSONArray();
        //获取当前用户的一级功能,遍历当前功能的所有子功能
        for(Efunction efunction :allFunctions){
            if(efunction.getParentid()==parentId){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",efunction.getFid());
                jsonObject.put("text",efunction.getFname());

                if(efunction.getRemark1().equals("YES")){
                    //表示当前功能是最后一级功能叶子节点
                    jsonObject.put("state","open");
                    jsonObject.put("furl",efunction.getFurl());
                    //判断功能是否被选中
                    for (Efunction efunction1:roleFunctions){
                        if (efunction1.getFid()==efunction.getFid()){
                            jsonObject.put("checked",true);
                        }
                    }
                }else{
                    JSONArray children = convert3(allFunctions,roleFunctions,efunction.getFid());
                    jsonObject.put("children",children);
                    jsonObject.put("state","open");
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    @Override
    public List<Efunction> findFunctionByCondition(Efunction efunction) {
        efunction.setLimitStart((efunction.getPage()-1)*efunction.getRows());
        List<Efunction> efunctions = efunctionMapper.findFunctionByCondition(efunction);
        return efunctions;
    }

    @Override
    public int countFunctionByCondition(Efunction efunction) {

        return efunctionMapper.countFunctionsByCondition(efunction);
    }

    @Override
    public List<Efunction> getFirstFunctions() {
        EfunctionExample efunctionExample = new EfunctionExample();
        EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
        criteria.andParentidEqualTo(01);
        List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
        return efunctions;
    }

    @Override
    @Transactional
    public BaseResult addFunction(Efunction efunction) {
        logger.info("新增功能权限方法开始，入参："+efunction);
        BaseResult result = new BaseResult();

        try {
            //校验功能编码唯一
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
            criteria.andFcodeEqualTo(efunction.getFcode());
            List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
            if (efunctions!=null&&efunctions.size()>0){
                result.setSuccess(false);
                result.setMessage("功能编码重复");
                return result;
            }
            if (efunction.getFlevel()==1){
                //新增一级功能
                efunction.setParentid(01);
                efunction.setRemark1("NO");
                efunction.setRemark2("一级功能");
            }else{
                //新增二级功能
                efunction.setRemark1("YES");
                //获取二级功能的一级功能的名称
                Efunction efunction1 = efunctionMapper.selectByPrimaryKey(efunction.getParentid());

                efunction.setRemark1(efunction1.getFname());

            }
            //新增操作
            efunctionMapper.insert(efunction);
            result.setSuccess(true);
            result.setMessage("操作成功");

        }catch (Exception e){
            //手动调用spring的事务进行回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setSuccess(false);
            result.setMessage("操作失败！");

        }

        logger.info("新增入参结束，出参："+result);
        return result;
    }



    @Override
    public Efunction findFunctionByFid(long fid) {

        return efunctionMapper.selectByPrimaryKey((int) fid);
    }

    @Override
    @Transactional
    public BaseResult updateFunction(Efunction efunction) {
        logger.info("修改功能权限开始，入参："+efunction);
        BaseResult result = new BaseResult();
        try {
            //功能编码唯一检验
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
            criteria.andFcodeEqualTo(efunction.getFcode());
            criteria.andFidNotEqualTo(efunction.getFid());
            List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
            if (efunctions!=null&&efunctions.size()>0){
                result.setSuccess(false);
                result.setMessage("功能编码重复!");
                return result;
            }
            if(efunction.getFlevel()==1){
                //修改之后功能为一级功能
                efunction.setParentid(01);
                efunction.setRemark1("NO");
                efunction.setRemark2("一级功能");
            }else{
                //修改之后的功能为二级功能
                //删除该功能的子功能
                EfunctionExample efunctionExample1 = new EfunctionExample();
                EfunctionExample.Criteria criteria1 = efunctionExample1.createCriteria();
                criteria1.andParentidEqualTo(efunction.getFid());
                efunctionMapper.deleteByExample(efunctionExample1);
                efunction.setRemark1("YES");
                //获取二级功能的父级功能信息
                Efunction efunction1 = efunctionMapper.selectByPrimaryKey(efunction.getParentid());
                efunction.setRemark2(efunction1.getFname());
            }
          //修改功能信息
            efunctionMapper.updateByPrimaryKey(efunction);
            //修改该功能的子功能的父级功能名称
            Efunction efunction1 = new Efunction();
            efunction1.setRemark2(efunction.getFname());
            efunction1.setParentid(efunction.getFid());
            efunctionMapper.updateRemark2ByParentId(efunction1);
            result.setSuccess(true);
            result.setMessage("操作成功");
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
        }
        logger.info("修改权限方法结束，出参："+result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult deleteFunctionByFid(long fid) {
        BaseResult result = new BaseResult();
        logger.info("删除功能权限方法开始，入参"+fid);
        try {
            //获取该功能的子功能
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
            criteria.andParentidEqualTo((int) fid);
            List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
            if (efunctions!=null&&efunctions.size()>0) {
                //删除子功能的功能角色关系表数据
                for (Efunction efunction : efunctions) {
                    RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
                    RoleFunctionExample.Criteria criteria1 = roleFunctionExample.createCriteria();
                    criteria1.andFidEqualTo(efunction.getFid());
                    roleFunctionMapper.deleteByExample(roleFunctionExample);
                    //删除子功能本身
                    efunctionMapper.deleteByPrimaryKey(efunction.getFid());
                }

            }

            //删除该功能的功能角色关系表数据
            RoleFunctionExample roleFunctionExample = new RoleFunctionExample();
            RoleFunctionExample.Criteria criteria1=roleFunctionExample.createCriteria();
            criteria1.andFidEqualTo((int) fid);
            roleFunctionMapper.deleteByExample(roleFunctionExample);
            //删除该功能本身
            efunctionMapper.deleteByPrimaryKey((int) fid);
            result.setSuccess(true);
            result.setMessage("操作成功");

        }catch (Exception e){
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("操作失败");
          }
        logger.info("删除权限方法结束，出参："+result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult batchDelete(String fids) {
        BaseResult result = new BaseResult();
        logger.info("批量删除操作方法开始，入参："+fids);
        try{
            String[] fidArray=fids.split(",");
            for (String fid:fidArray){
                //循环调用删除方法
               BaseResult result1= deleteFunctionByFid(Long.parseLong(fid));
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
    public List<Efunction> findFunctionsByParentid(long parentid) {
        EfunctionExample efunctionExample = new EfunctionExample();
        EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
        criteria.andParentidEqualTo((int) parentid);
        List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
        return efunctions;
    }

    @Override
    public List<Efunction> getAllFunctions() {
        EfunctionExample efunctionExample = new EfunctionExample();
        List<Efunction> efunctions = efunctionMapper.selectByExample(efunctionExample);
        return efunctions;
    }


}
