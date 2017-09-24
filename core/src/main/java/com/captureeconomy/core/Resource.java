package com.captureeconomy.core;

import java.time.Instant;
import java.time.Duration;
import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A rectangular barrier with one corner at position and another at position + size
 */
@Data
public class Resource {

    private Vector2D position;

    private Instant created;

    private Duration duration;

}

