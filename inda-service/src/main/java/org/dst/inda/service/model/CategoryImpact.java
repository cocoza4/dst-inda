package org.dst.inda.service.model;

public class CategoryImpact {

    private String category;
    private float percentage;

    public CategoryImpact(String category, float percentage) {
        this.category = category;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
