package com.brighteststar.asiftamal.recipeapp;

public class foodData {
    private  String itemTitle;
    private  String itemCategory;
    private  String itemIngredientQuantity;
    private  String itemCookingSteps;
    private  String itemSpecialInstruction;
    private String itemImage;
    private boolean itemIsfavorite;

    public foodData(String itemTitle, String itemCategory, String itemIngredientQuantity, String itemCookingSteps, String itemSpecialInstruction, String itemImage, boolean itemIsfavorite) {
        this.itemTitle = itemTitle;
        this.itemCategory = itemCategory;
        this.itemIngredientQuantity = itemIngredientQuantity;
        this.itemCookingSteps = itemCookingSteps;
        this.itemSpecialInstruction = itemSpecialInstruction;
        this.itemImage = itemImage;
        this.itemIsfavorite = itemIsfavorite;
    }

    public foodData() {
    }

    public boolean isItemIsfavorite() {
        return itemIsfavorite;
    }

    public void setItemIsfavorite(boolean itemIsfavorite) {
        this.itemIsfavorite = itemIsfavorite;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemIngredientQuantity() {
        return itemIngredientQuantity;
    }

    public void setItemIngredientQuantity(String itemIngredientQuantity) {
        this.itemIngredientQuantity = itemIngredientQuantity;
    }

    public String getItemCookingSteps() {
        return itemCookingSteps;
    }

    public void setItemCookingSteps(String itemCookingSteps) {
        this.itemCookingSteps = itemCookingSteps;
    }

    public String getItemSpecialInstruction() {
        return itemSpecialInstruction;
    }

    public void setItemSpecialInstruction(String itemSpecialInstruction) {
        this.itemSpecialInstruction = itemSpecialInstruction;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
