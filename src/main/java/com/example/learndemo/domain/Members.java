package com.example.learndemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

/**
 * 
 * @TableName members
 */
@TableName(value ="members")
public class Members {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer memberId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 可借书数量
     */
    private Integer maxBooks;

    /**
     * 免费租赁天数
     */
    private Integer freeDays;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 租金
     */
    private BigDecimal rentPay;

    /**
     * 滞纳金
     */
    private BigDecimal overdueFine;

    /**
     * 
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * 
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 可借书数量
     */
    public Integer getMaxBooks() {
        return maxBooks;
    }

    /**
     * 可借书数量
     */
    public void setMaxBooks(Integer maxBooks) {
        this.maxBooks = maxBooks;
    }

    /**
     * 免费租赁天数
     */
    public Integer getFreeDays() {
        return freeDays;
    }

    /**
     * 免费租赁天数
     */
    public void setFreeDays(Integer freeDays) {
        this.freeDays = freeDays;
    }

    /**
     * 押金
     */
    public BigDecimal getDeposit() {
        return deposit;
    }

    /**
     * 押金
     */
    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    /**
     * 租金
     */
    public BigDecimal getRentPay() {
        return rentPay;
    }

    /**
     * 租金
     */
    public void setRentPay(BigDecimal rentPay) {
        this.rentPay = rentPay;
    }

    /**
     * 滞纳金
     */
    public BigDecimal getOverdueFine() {
        return overdueFine;
    }

    /**
     * 滞纳金
     */
    public void setOverdueFine(BigDecimal overdueFine) {
        this.overdueFine = overdueFine;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Members other = (Members) that;
        return (this.getMemberId() == null ? other.getMemberId() == null : this.getMemberId().equals(other.getMemberId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMaxBooks() == null ? other.getMaxBooks() == null : this.getMaxBooks().equals(other.getMaxBooks()))
            && (this.getFreeDays() == null ? other.getFreeDays() == null : this.getFreeDays().equals(other.getFreeDays()))
            && (this.getDeposit() == null ? other.getDeposit() == null : this.getDeposit().equals(other.getDeposit()))
            && (this.getRentPay() == null ? other.getRentPay() == null : this.getRentPay().equals(other.getRentPay()))
            && (this.getOverdueFine() == null ? other.getOverdueFine() == null : this.getOverdueFine().equals(other.getOverdueFine()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMemberId() == null) ? 0 : getMemberId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMaxBooks() == null) ? 0 : getMaxBooks().hashCode());
        result = prime * result + ((getFreeDays() == null) ? 0 : getFreeDays().hashCode());
        result = prime * result + ((getDeposit() == null) ? 0 : getDeposit().hashCode());
        result = prime * result + ((getRentPay() == null) ? 0 : getRentPay().hashCode());
        result = prime * result + ((getOverdueFine() == null) ? 0 : getOverdueFine().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", memberId=").append(memberId);
        sb.append(", userId=").append(userId);
        sb.append(", maxBooks=").append(maxBooks);
        sb.append(", freeDays=").append(freeDays);
        sb.append(", deposit=").append(deposit);
        sb.append(", rentPay=").append(rentPay);
        sb.append(", overdueFine=").append(overdueFine);
        sb.append("]");
        return sb.toString();
    }
}