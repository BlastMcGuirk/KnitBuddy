package com.apps.bguirks.knitbuddy.database.dataobjects;

public class Project implements Comparable<Project> {

    // Database ID - PRIMARY KEY
    private long _id;
    // Project Name - TEXT NON-NULL
    private String projectName;
    // Category ID - INT NON-NULL
    private long categoryId;
    // Needle Type - TEXT
    private String needleType;
    // Needle Size - TEXT
    private String needleSize;
    // Yarn Brand - TEXT
    private String yarnBrand;
    // Yarn Size - TEXT
    private String yarnSize;

    public Project() {}
    public Project(String projectName, long categoryId) {
        this.projectName = projectName;
        this.categoryId = categoryId;
    }
    public Project(String projectName, long categoryId, String needleType, String needleSize,
                   String yarnBrand, String yarnSize) {
        this.projectName = projectName;
        this.categoryId = categoryId;
        this.needleType = needleType;
        this.needleSize = needleSize;
        this.yarnBrand = yarnBrand;
        this.yarnSize = yarnSize;
    }
    public Project(long _id, String projectName, long categoryId, String needleType, String needleSize,
                   String yarnBrand, String yarnSize) {
        this._id = _id;
        this.projectName = projectName;
        this.categoryId = categoryId;
        this.needleType = needleType;
        this.needleSize = needleSize;
        this.yarnBrand = yarnBrand;
        this.yarnSize = yarnSize;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNeedleType() {
        return needleType;
    }

    public void setNeedleType(String needleType) {
        this.needleType = needleType;
    }

    public String getNeedleSize() {
        return needleSize;
    }

    public void setNeedleSize(String needleSize) {
        this.needleSize = needleSize;
    }

    public String getYarnBrand() {
        return yarnBrand;
    }

    public void setYarnBrand(String yarnBrand) {
        this.yarnBrand = yarnBrand;
    }

    public String getYarnSize() {
        return yarnSize;
    }

    public void setYarnSize(String yarnSize) {
        this.yarnSize = yarnSize;
    }

    @Override
    public int compareTo(Project o) {
        return projectName.compareTo(o.projectName);
    }

    @Override
    public String toString() {
        return projectName;
    }
}
