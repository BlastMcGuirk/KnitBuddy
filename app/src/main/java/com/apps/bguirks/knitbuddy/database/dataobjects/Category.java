package com.apps.bguirks.knitbuddy.database.dataobjects;

import java.util.Objects;

public class Category implements Comparable<Category> {

    // Database ID - PRIMARY KEY
    private long _id;
    // Category Name - TEXT NON-NULL
    private String category;

    public Category() {}
    public Category(String categoryName) {
        this.category = categoryName;
    }
    public Category(long id, String categoryName) {
        this._id = id;
        this.category = categoryName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(Category o) {
        return category.compareTo(o.category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return _id == category1._id &&
                Objects.equals(category, category1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, category);
    }

    @Override
    public String toString() {
        return category;
    }
}
