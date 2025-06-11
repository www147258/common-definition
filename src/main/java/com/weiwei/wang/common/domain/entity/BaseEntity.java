package com.weiwei.wang.common.domain.entity;

public class BaseEntity {


    /**
     * 表主键
     */
    private Long id;

    /**
     * 创建人Id
     */
    private Long createById;

    /**
     * 创建人名字
     */
    private String createByName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人Id
     */
    private Long updateById;

    /**
     * 更新人名称
     */
    private String updateByName;

    /**
     * 更新时间
     */
    private Long updateTime;


    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createById=" + createById +
                ", createByName='" + createByName + '\'' +
                ", createTime=" + createTime +
                ", updateById=" + updateById +
                ", updateByName='" + updateByName + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
