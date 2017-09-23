package com.captureeconomy.game;

import java.time.Clock;
import java.time.Duration;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GameService {

    private Clock clock = Clock.systemUTC();

    private Random random = new Random();

    public addPlayer(World world, Player player) {
        world.getPlayers().add(player);
    }

    public updateToNow() {
    }

    public update(World world) {
        updatePlayers(world);
        updateBarriers(world);
        updateResources(world);
    }

    private void updatePlayers(World world) {
        world.getPlayers().forEach(
            (Player player) -> {
                Vector2D desiredPosition = player.getPosition() +
                                           player.getVelocity().scalarMultiply(
                                               Duration.between(
                                                   player.getPositionInstant(), world.getInstant()
                                               ).getNano() / 1000000000.0
                                           );
                boolean canMove = true;
                world.getBarriers().forEach(
                    (Barrier barrier) -> {
                        if (isPointInRectangle(desiredPosition, barrier.getPosition(), barrier.getSize()) &&
                            barrier.getEnabled()
                        ) {
                            canMove = false;
                        }
                        if (isPointInRectangle(player.getPosition(), barrier.getPosition(), barrier.getSize()) &&
                            barrier.getEnabled()
                        ) {
                            canMove = true;
                        }
                    }
                );
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
        );
    }

    private void updateBarriers(World world) {
        world.getBarriers().forEach(
            (Barrier barrier) -> {
                if (!barrier.getEnabled()) {
                    world.players.forEach(
                        (Player player) -> {
                            if (isPointInRectangle(player.getPosition(), barrier.getPosition(), barrier.getSize())) {
                                barrier.setEnabled(true);
                            }
                        }
                    );
                }
            }
        );
    }

    private void updateResources(World world) {
        world.getResources().forEach(
            (Resource resource) -> {
                if (resource.getCreated().plus(resource.getDuration()).isAfter(world.getInstant())) {
                    world.getResources().remove(resource);
                }
            }
        );
        boolean isFirstCreated = true;
        while (world.getResources().size() < world.desiredResourceNumber()) {
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
                resource.setDuration(random.Random() * Duration.ofSeconds(world.getDesiredResourceDuration().getSeconds()));
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
