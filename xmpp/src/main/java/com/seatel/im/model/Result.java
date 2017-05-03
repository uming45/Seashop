package com.seatel.im.model;

/**
 * Created by eldorado on 17-5-3.
 *
 * 通用的http返回结果对象
 */
public class Result<T> {

    T data;
    int resultCode;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", resultCode=" + resultCode +
                '}';
    }
}
