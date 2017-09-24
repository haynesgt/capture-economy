package com.captureeconomy.core;

import java.time.Clock;
import java.time.Duration;
import java.util.Random;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GameService {

  private Clock clock = Clock.systemUTC();

  private Random random = new Random();

  public void addPlayer(World world, Player player) {
    world.getPlayers().add(player);
  }

  public void updateToNow() {
  }

  public void update(World world) {
    updatePlayers(world);
    updateBarriers(world);
    updateResources(world);
  }

  private void updatePlayers(World world) {
    for (Player player : world.getPlayers()) {
      Vector2D desiredPosition = player.getPosition().add(
          player.getVelocity().scalarMultiply(
            Duration.between(
              player.getPositionInstant(), world.getInstant()
              ).getNano() / 1.0e9
            )
          );
      boolean canMove = true;
      for (Barrier barrier : world.getBarriers()) {
        if (isPointInRectangle(desiredPosition, barrier.getPosition(), barrier.getSize()) &&
            barrier.isEnabled()
           ) {
          canMove = false;
           }
        if (isPointInRectangle(player.getPosition(), barrier.getPosition(), barrier.getSize()) &&
            barrier.isEnabled()
           ) {
          canMove = true;
           }
      }
      if (canMove) {
        player.setPosition(desiredPosition);
      }
      if (player.getPosition().getX() >= world.getSize().getX()) {
        player.setPosition(player.getPosition().subtract(new Vector2D(world.getSize().getX(), 0)));
      }
      if (player.getPosition().getX() < 0) {
        player.setPosition(player.getPosition().add(new Vector2D(world.getSize().getX(), 0)));
      }
      if (player.getPosition().getY() >= world.getSize().getY()) {
        player.setPosition(player.getPosition().subtract(new Vector2D(world.getSize().getY(), 0)));
      }
      if (player.getPosition().getY() >= world.getSize().getY()) {
        player.setPosition(player.getPosition().add(new Vector2D(world.getSize().getY(), 0)));
      }
      player.setPositionInstant(world.getInstant());
    }
  }

  private void updateBarriers(World world) {
    for (Barrier barrier : world.getBarriers()) {
      if (!barrier.isEnabled()) {
        for (Player player : world.getPlayers()) {
          if (isPointInRectangle(player.getPosition(), barrier.getPosition(), barrier.getSize())) {
            barrier.setEnabled(true);
          }
        }
      }
    }
  }

  private void updateResources(World world) {
    for (Resource resource : world.getResources()) {
          if (resource.getCreated().plus(resource.getDuration()).isAfter(world.getInstant())) {
            world.getResources().remove(resource);
          }
    }
    boolean isFirstCreated = true;
    while (world.getResources().size() < world.getDesiredResourceNumber()) {
      Resource resource = new Resource();
      resource.setPosition(
          new Vector2D(
            random.nextDouble() * world.getSize().getX(),
            random.nextDouble() * world.getSize().getY()
            )
          );
      resource.setCreated(world.getInstant());
      if (isFirstCreated) {
        resource.setDuration(world.getDesiredResourceDuration());
        isFirstCreated = false;
      } else {
        resource.setDuration(Duration.ofSeconds((long) random.nextDouble() * world.getDesiredResourceDuration().getSeconds()));
      }
      world.getResources().add(resource);
    }
  }

  private boolean isPointInRectangle(Vector2D point, Vector2D rectanglePosition, Vector2D rectangleSize) {
    return (rectanglePosition.getX() <= point.getX() &&
        rectanglePosition.getX() + rectangleSize.getX() >= point.getX() &&
        rectanglePosition.getY() <= point.getY() &&
        rectanglePosition.getY() + rectangleSize.getY() >= point.getY());
  }

}
