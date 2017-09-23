package com.captureeconomy.game;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

@Data
public class World {

    private Instant instant;

    private Collection<Player> players;

    private long desiredResourceNumber = 100;

    private Duration desiredResourceDuration = Duration.ofSeconds(60);

    private Collection<Resource> resources;

    private Collection<Barrier> barriers;

    private Vector2D size;

}
