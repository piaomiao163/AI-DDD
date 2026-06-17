package com.piaomiao.rest;

import com.piaomiao.response.Response;

import java.io.Serializable;

public abstract class AbstractTemplateRest implements Serializable {

    private static final long serialVersionUID = 1L;

    public <T> Response<T> request(CallbackRest<T> callback) {
        return (Response<T>) callback.execute();
    }

}
