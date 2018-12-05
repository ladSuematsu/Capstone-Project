package com.ladsuematsu.capstoneproject.core.data.persistence;

import java.util.List;

public interface DataProvider<T, U> {
    interface ProviderListener<T> {
        void onSuccess(List<T> result);
        void onFailure();
    }



    void fetch(U searchKey, ProviderListener<T> listener);
    void create(T entity);
    void update(T entity);

}
