package com.ajit.appstreetdemo.data;

public class ImagesRequest {

    private String searchText;
    private int page;
    private int perPage;
    private int lastId;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public String getSearchText() {
        return searchText;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }
}
