package com.captureeconomy.core;

import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private World world;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

}
