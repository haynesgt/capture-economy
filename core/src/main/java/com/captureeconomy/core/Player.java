package com.captureeconomy.core;

import java.time.Instant;
import java.util.Collection;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

@Data
public class Player {

    private Instant positionInstant;

    private Vector2D position;

    private Vector2D velocity;

    private String name;

    private String key;

    private Collection<Resource> resources;

}
