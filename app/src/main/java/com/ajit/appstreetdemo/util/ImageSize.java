package com.ajit.appstreetdemo.util;

public enum ImageSize {
    IMAGE_SIZE_SMALL_SQUARE("s"),//75
    IMAGE_SIZE_MEDIUM("z"),// 640
    IMAGE_SIZE_LARGE("b");//1600


    private String name;

    ImageSize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
