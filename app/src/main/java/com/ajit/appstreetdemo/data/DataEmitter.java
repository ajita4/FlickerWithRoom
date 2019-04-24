package com.ajit.appstreetdemo.data;

import com.ajit.appstreetdemo.data.models.Flicker;

public interface DataEmitter {

    void setData(Flicker flicker);

    void error();
}
