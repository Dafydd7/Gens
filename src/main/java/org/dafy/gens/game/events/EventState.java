package org.dafy.gens.game.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EventState {
    INACTIVE("Event Inactive"),
    SELL_EVENT("2x Sell Event"),
    DOUBLE_DROP("Double Drops Event"),
    UPGRADE_DROP("Upgraded Drops Event");
    final String event;
     EventState(String event) {
         this.event = event;
     }
    private static final List<EventState> ACTIVE_MODES = Arrays.asList(DOUBLE_DROP, SELL_EVENT, UPGRADE_DROP);
    public static EventState getRandomEventMode() {
        Collections.shuffle(ACTIVE_MODES);
        return ACTIVE_MODES.get(0);
    }
    @Override
    public String toString(){
        return event;
    }
}
