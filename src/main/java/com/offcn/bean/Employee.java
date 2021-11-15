package com.offcn.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.offcn.util.BasePage;

import java.util.Date;
import java.util.List;

public class Employee extends BasePage {
    private Integer eid;

    private String ename;

    private Integer esex;

    private String esexStr;

    private String age;

    private String phonenumber;
//使用注解来显示时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "CMT+08")
    private Date hireDate;

    private String hireDateStr;

    private String jobnumber;

    private String password;

    private String remark1;

    private String remark2;

    private String rids;

    private String remark3;

    private List<Erole> eroleList;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public Integer getEsex() {
        return esex;
    }

    public void setEsex(Integer esex) {
        this.esex = esex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber == null ? null : phonenumber.trim();
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber == null ? null : jobnumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1 == null ? null : remark1.trim();
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2 == null ? null : remark2.trim();
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3 == null ? null : remark3.trim();
    }

    public String getEsexStr() {
        return esexStr;
    }

    public void setEsexStr(String esexStr) {
        this.esexStr = esexStr;
    }

    public String getHireDateStr() {
        return hireDateStr;
    }

    public void setHireDateStr(String hireDateStr) {
        this.hireDateStr = hireDateStr;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    public List<Erole> getEroleList() {
        return eroleList;
    }

    public void setEroleList(List<Erole> eroleList) {
        this.eroleList = eroleList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", ename='" + ename + '\'' +
                ", esex=" + esex +
                ", esexStr='" + esexStr + '\'' +
                ", age='" + age + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", hireDate=" + hireDate +
                ", hireDateStr='" + hireDateStr + '\'' +
                ", jobnumber='" + jobnumber + '\'' +
                ", password='" + password + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", rids='" + rids + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", eroleList=" + eroleList +
                '}';
    }
}