package com.example.nagyjahel.sapiads.Main.Interfaces;

public interface RetrieveDataListener<T> {
    void onSucces(T data);
    void onFailure(String message);
}
