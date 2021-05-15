package com.core;

import java.util.List;

public class Solution {
    private List<Route> routes;

    public Solution(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getSchedules() {
        return routes;
    }
}
