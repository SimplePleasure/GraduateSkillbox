package com.graduate.core;

public class ComputePages {

    public static int computePage(int offset, int limit) {
        return offset / limit;
    }
}
