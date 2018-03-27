package cn.hzstk.securities.financial.domain;
import cn.hzstk.securities.common.domain.BaseDomain;
import cn.hzstk.securities.sys.utils.json.Dict;

import javax.persistence.Column;
import javax.persistence.Table;


/**
* @description:
* @author: autoCode
* @history:
*/
@Table(name = "financial_auth_bank")
public class AuthBank extends BaseDomain {

    private static final long serialVersionUID = 1L;


    //用户编号
    @Column(name = "uid")
    private Integer uid;

    //用户名
    @Column(name = "username")
    private String username;

    //线下支付账号
    @Column(name = "bank_account")
    private String bankAccount;

    //银行名称
    @Column(name = "bank_name")
    private String bankName;

    //银行编号
    @Column(name = "bank_id")
    private Integer bankId;

    //开户行所在地
    @Column(name = "deposit_area")
    private String depositArea;

    //开户行名称
    @Column(name = "deposit_name")
    private String depositName;

    //打给用户的金额
    @Column(name = "pay_to_user_cash")
    private Integer payToUserCash;

    //收款金额
    @Column(name = "user_get_cash")
    private Integer userGetCash;

    //打款时间
    @Column(name = "pay_time")
    private java.util.Date payTime;

    //支付费用
    @Column(name = "cash")
    private Integer cash;

    //认证开始时间
    @Column(name = "start_time")
    private java.util.Date startTime;

    //认证结束时间
    @Column(name = "end_time")
    private java.util.Date endTime;

    //认证状态
    @Column(name = "auth_status")
    @Dict(key="auth_bank_status")
    private Integer authStatus;

    //开户行名称
    @Column(name = "bank_sname")
    private String bankSname;

    public void setUid(Integer uid){
        this.uid = uid;
    }

    public Integer getUid(){
        return uid;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setBankAccount(String bankAccount){
        this.bankAccount = bankAccount;
    }

    public String getBankAccount(){
        return bankAccount;
    }

    public void setBankName(String bankName){
        this.bankName = bankName;
    }

    public String getBankName(){
        return bankName;
    }

    public void setBankId(Integer bankId){
        this.bankId = bankId;
    }

    public Integer getBankId(){
        return bankId;
    }

    public void setDepositArea(String depositArea){
        this.depositArea = depositArea;
    }

    public String getDepositArea(){
        return depositArea;
    }

    public void setDepositName(String depositName){
        this.depositName = depositName;
    }

    public String getDepositName(){
        return depositName;
    }

    public void setPayToUserCash(Integer payToUserCash){
        this.payToUserCash = payToUserCash;
    }

    public Integer getPayToUserCash(){
        return payToUserCash;
    }

    public void setUserGetCash(Integer userGetCash){
        this.userGetCash = userGetCash;
    }

    public Integer getUserGetCash(){
        return userGetCash;
    }

    public void setPayTime(java.util.Date payTime){
        this.payTime = payTime;
    }

    public java.util.Date getPayTime(){
        return payTime;
    }

    public void setCash(Integer cash){
        this.cash = cash;
    }

    public Integer getCash(){
        return cash;
    }

    public void setStartTime(java.util.Date startTime){
        this.startTime = startTime;
    }

    public java.util.Date getStartTime(){
        return startTime;
    }

    public void setEndTime(java.util.Date endTime){
        this.endTime = endTime;
    }

    public java.util.Date getEndTime(){
        return endTime;
    }

    public void setAuthStatus(Integer authStatus){
        this.authStatus = authStatus;
    }

    public Integer getAuthStatus(){
        return authStatus;
    }

    public void setBankSname(String bankSname){
        this.bankSname = bankSname;
    }

    public String getBankSname(){
        return bankSname;
    }
}