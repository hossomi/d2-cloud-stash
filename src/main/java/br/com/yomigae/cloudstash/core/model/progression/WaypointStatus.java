package br.com.yomigae.cloudstash.core.model.progression;

import br.com.yomigae.cloudstash.core.model.Area;

import static br.com.yomigae.cloudstash.core.util.StringUtils.checkbox;

public record WaypointStatus(Area area, boolean active) {

    public WaypointStatus {
        if (!area.waypoint()) {
            throw new IllegalArgumentException("Area %s does not have a waypoint".formatted(area));
        }
    }

    @Override
    public String toString() {
        return checkbox(area().label(), active);
    }
}
