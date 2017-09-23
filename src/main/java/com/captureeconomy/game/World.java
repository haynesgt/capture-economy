package com.captureeconomy.game;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.TemporalAmount;
import java.util.Collection;
import lombok.Data;

@Data
public class World {

    private Instant instant;

    private Collection<Player> players;

    private long desiredResourceNumber = 100;

    private TemporalAmount desiredResourceDuration = Duration.ofSeconds(60);

    private Collection<Resource> resources;

    private Collection<Barrier> barriers;

    private Vector2D size;

}
