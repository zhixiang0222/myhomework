package com.offcn.bean;

import com.offcn.util.BasePage;

import java.util.List;

public class Erole extends BasePage {
    private Integer rid;

    private String rcode;

    private String rname;

    private List<Efunction> efunctionList;

    private String fids;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode == null ? null : rcode.trim();
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname == null ? null : rname.trim();
    }

    public String getFids() {
        return fids;
    }

    public void setFids(String fids) {
        this.fids = fids;
    }

    public List<Efunction> getEfunctionList() {
        return efunctionList;
    }

    public void setEfunctionList(List<Efunction> efunctionList) {
        this.efunctionList = efunctionList;
    }

    @Override
    public String toString() {
        return "Erole{" +
                "rid=" + rid +
                ", rcode='" + rcode + '\'' +
                ", rname='" + rname + '\'' +
                ", efunctionList=" + efunctionList +
                ", fids='" + fids + '\'' +
                '}';
    }
}