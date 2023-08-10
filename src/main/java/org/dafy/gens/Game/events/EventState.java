package org.dafy.gens.Game.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EventState {
    INACTIVE,
    SELL_EVENT,
    DOUBLE_DROP,
    UPGRADE_DROP;
    private static final List<EventState> ACTIVE_MODES = Arrays.asList(DOUBLE_DROP, SELL_EVENT, UPGRADE_DROP);
    public static EventState getRandomMode() {
        Collections.shuffle(ACTIVE_MODES);
        return ACTIVE_MODES.get(0);
    }
}
