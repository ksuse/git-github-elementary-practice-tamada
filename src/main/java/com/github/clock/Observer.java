package com.github.clock;

@FunctionalInterface
public interface Observer {
    void update(Clock clock);
}
