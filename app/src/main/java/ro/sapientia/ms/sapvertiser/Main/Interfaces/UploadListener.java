package ro.sapientia.ms.sapvertiser.Main.Interfaces;

public interface UploadListener<T> {
    void onSucces(T data);
    void onFailure(String message);
    void onProgress();
}
