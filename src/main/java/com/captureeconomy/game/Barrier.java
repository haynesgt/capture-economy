package com.captureeconomy.game;

import lombok.Data;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A rectangular barrier with one corner at position and another at position + size
 */
@Data
public class Barrier {

    private Vector2D position;

    private Vector2D size;

    private boolean enabled;

}
