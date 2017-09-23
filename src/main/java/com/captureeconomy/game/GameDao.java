package com.captureeconomy.game;

import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private World world;

    public World getWorld() {
        return world;
    }

    public setWorld(World world) {
        this.world = world;
    }

}
