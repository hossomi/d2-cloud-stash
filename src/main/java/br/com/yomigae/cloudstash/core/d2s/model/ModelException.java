package br.com.yomigae.cloudstash.core.d2s.model;

import br.com.yomigae.cloudstash.core.d2s.model.progress.Progress;
import br.com.yomigae.cloudstash.core.d2s.model.progress.Quest;

import static java.lang.String.format;

public class ModelException extends IllegalArgumentException {

    public ModelException(String s) {
        super(s);
    }

    public static ModelException invalidQuest(Progress.Quests quests, Quest<?> quest) {
        return new ModelException(format("Invalid quest %s for %s", quest, findAct(quests.getClass())));
    }

    public static ModelException invalidQuest(Progress.Quests.Builder<?> quests, Quest<?> quest) {
        return new ModelException(format("Invalid quest %s for %s", quest, findAct(quests.getClass())));
    }
    public static ModelException invalidWaypoint(Progress.Waypoints waypoints, Area waypoint) {
        return new ModelException(format("Invalid waypoint %s for %s", waypoint, findAct(waypoints.getClass())));
    }

    public static ModelException invalidWaypoint(Progress.Waypoints.Builder<?> waypoints, Area waypoint) {
        return new ModelException(format("Invalid waypoint %s for %s", waypoint, findAct(waypoints.getClass())));
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Progress<?, ?>> findAct(Class<?> source) {
        while (source != null && !Progress.class.isAssignableFrom(source)) {
            source = source.getDeclaringClass();
        }
        // Safe cast because of the loop condition
        return (Class<? extends Progress<?, ?>>) source;
    }
}
