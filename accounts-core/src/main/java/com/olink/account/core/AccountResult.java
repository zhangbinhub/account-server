package com.olink.account.core;

import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.enumration.AmontTypeEnum;
import com.olink.account.enumration.ChangeAccountTypeEnum;
import com.olink.account.enumration.ResultStatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 * 记账结果
 */
public class AccountResult {

    public ResultStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ResultStatusEnum status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBeforbalance() {
        return beforbalance;
    }

    public void setBeforbalance(double beforbalance) {
        this.beforbalance = beforbalance;
    }

    public double getAfterbalance() {
        return afterbalance;
    }

    public void setAfterbalance(double afterbalance) {
        this.afterbalance = afterbalance;
    }

    public double getBeformoney() {
        return beformoney;
    }

    public void setBeformoney(double beformoney) {
        this.beformoney = beformoney;
    }

    public double getAftermoney() {
        return aftermoney;
    }

    public void setAftermoney(double aftermoney) {
        this.aftermoney = aftermoney;
    }

    public Double getResultBeforBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return beforbalanceMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultBeforBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, double beforbalance) {
        beforbalanceMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), beforbalance);
    }

    public Double getResultAfterBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return afterbalanceMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultAfterBalance(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, double afterbalance) {
        afterbalanceMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), afterbalance);
    }

    public Double getResultAmontMap(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum) {
        return amontMap.get(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue());
    }

    public void setResultAmontMap(ChangeAccountTypeEnum changeAccountTypeEnum, AccountTypeEnum accountTypeEnum, AmontTypeEnum amontTypeEnum, double amont) {
        amontMap.put(changeAccountTypeEnum.getValue() + accountTypeEnum.getType() + amontTypeEnum.getValue(), amont);
    }

    private ResultStatusEnum status;

    private String message;

    private double beforbalance;

    private double afterbalance;

    private double beformoney;

    private double aftermoney;

    private Map<String, Double> beforbalanceMap = new HashMap<>();

    private Map<String, Double> afterbalanceMap = new HashMap<>();

    private Map<String, Double> amontMap = new HashMap<>();

}
