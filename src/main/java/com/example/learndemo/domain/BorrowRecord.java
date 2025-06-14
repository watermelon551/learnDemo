package com.example.learndemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 
 * @TableName borrow_record
 */
@TableName(value ="borrow_record")
public class BorrowRecord {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer bookId;

    /**
     * 
     */
    private LocalDate borrowDate;

    /**
     * 
     */
    private LocalDate dueDate;

    /**
     * 
     */
    private LocalDate returnDate;

    /**
     * 
     */
    private Object type;

    /**
     * 
     */
    private Object status;

    /**
     * 
     */
    private LocalDateTime createdAt;

    /**
     * 
     */
    private LocalDateTime updatedAt;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * 
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * 
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * 
     */
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * 
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * 
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * 
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * 
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * 
     */
    public Object getType() {
        return type;
    }

    /**
     * 
     */
    public void setType(Object type) {
        this.type = type;
    }

    /**
     * 
     */
    public Object getStatus() {
        return status;
    }

    /**
     * 
     */
    public void setStatus(Object status) {
        this.status = status;
    }

    /**
     * 
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
        BorrowRecord other = (BorrowRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBookId() == null ? other.getBookId() == null : this.getBookId().equals(other.getBookId()))
            && (this.getBorrowDate() == null ? other.getBorrowDate() == null : this.getBorrowDate().equals(other.getBorrowDate()))
            && (this.getDueDate() == null ? other.getDueDate() == null : this.getDueDate().equals(other.getDueDate()))
            && (this.getReturnDate() == null ? other.getReturnDate() == null : this.getReturnDate().equals(other.getReturnDate()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBookId() == null) ? 0 : getBookId().hashCode());
        result = prime * result + ((getBorrowDate() == null) ? 0 : getBorrowDate().hashCode());
        result = prime * result + ((getDueDate() == null) ? 0 : getDueDate().hashCode());
        result = prime * result + ((getReturnDate() == null) ? 0 : getReturnDate().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", bookId=").append(bookId);
        sb.append(", borrowDate=").append(borrowDate);
        sb.append(", dueDate=").append(dueDate);
        sb.append(", returnDate=").append(returnDate);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}