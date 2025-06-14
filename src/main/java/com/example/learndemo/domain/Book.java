package com.example.learndemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 
 * @TableName book
 */
@TableName(value ="book")
public class Book {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer bookId;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 分类
     */
    private String category;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 出版年份
     */
    private Object publishYear;

    /**
     * 国际标准书号
     */
    private String isbn;

    /**
     * 总副本数
     */
    private Integer totalCopies;

    /**
     * 可用副本数
     */
    private Integer availableCopies;

    /**
     * 书籍状态
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
     * 书名
     */
    public String getTitle() {
        return title;
    }

    /**
     * 书名
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 分类
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 出版社
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 出版社
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * 出版年份
     */
    public Object getPublishYear() {
        return publishYear;
    }

    /**
     * 出版年份
     */
    public void setPublishYear(Object publishYear) {
        this.publishYear = publishYear;
    }

    /**
     * 国际标准书号
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * 国际标准书号
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * 总副本数
     */
    public Integer getTotalCopies() {
        return totalCopies;
    }

    /**
     * 总副本数
     */
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * 可用副本数
     */
    public Integer getAvailableCopies() {
        return availableCopies;
    }

    /**
     * 可用副本数
     */
    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    /**
     * 书籍状态
     */
    public Object getStatus() {
        return status;
    }

    /**
     * 书籍状态
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
        Book other = (Book) that;
        return (this.getBookId() == null ? other.getBookId() == null : this.getBookId().equals(other.getBookId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getAuthor() == null ? other.getAuthor() == null : this.getAuthor().equals(other.getAuthor()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getPublisher() == null ? other.getPublisher() == null : this.getPublisher().equals(other.getPublisher()))
            && (this.getPublishYear() == null ? other.getPublishYear() == null : this.getPublishYear().equals(other.getPublishYear()))
            && (this.getIsbn() == null ? other.getIsbn() == null : this.getIsbn().equals(other.getIsbn()))
            && (this.getTotalCopies() == null ? other.getTotalCopies() == null : this.getTotalCopies().equals(other.getTotalCopies()))
            && (this.getAvailableCopies() == null ? other.getAvailableCopies() == null : this.getAvailableCopies().equals(other.getAvailableCopies()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBookId() == null) ? 0 : getBookId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getAuthor() == null) ? 0 : getAuthor().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getPublisher() == null) ? 0 : getPublisher().hashCode());
        result = prime * result + ((getPublishYear() == null) ? 0 : getPublishYear().hashCode());
        result = prime * result + ((getIsbn() == null) ? 0 : getIsbn().hashCode());
        result = prime * result + ((getTotalCopies() == null) ? 0 : getTotalCopies().hashCode());
        result = prime * result + ((getAvailableCopies() == null) ? 0 : getAvailableCopies().hashCode());
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
        sb.append(", bookId=").append(bookId);
        sb.append(", title=").append(title);
        sb.append(", author=").append(author);
        sb.append(", category=").append(category);
        sb.append(", publisher=").append(publisher);
        sb.append(", publishYear=").append(publishYear);
        sb.append(", isbn=").append(isbn);
        sb.append(", totalCopies=").append(totalCopies);
        sb.append(", availableCopies=").append(availableCopies);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}