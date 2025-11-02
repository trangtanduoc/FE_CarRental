package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Represents the pagination payload for list endpoints.
 */
public class PagedResponse<T> {

    @SerializedName("items")
    private List<T> items;

    @SerializedName("pageNumber")
    private Integer pageNumber;

    @SerializedName("totalPages")
    private Integer totalPages;

    @SerializedName("totalCount")
    private int totalCount;

    public List<T> getItems() {
        return items == null ? Collections.emptyList() : items;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
