package ro.sapientia.ms.sapvertiser.Main.Interfaces;

public interface RetrieveDataListener<T> {
    void onSucces(T data);
    void onFailure(String message);
}
