package libgdx.implementations.skelgame.gameservice;

import java.util.HashMap;
import java.util.Map;

public abstract class CreatorDependenciesContainer {

    private static Map<Class<? extends CreatorDependencies>, CreatorDependencies> creatorDependencies = new HashMap<>();

    public static CreatorDependencies getCreator(Class<? extends CreatorDependencies> creatorDependenciesClass) {
        CreatorDependencies instance = creatorDependencies.get(creatorDependenciesClass);
        if (instance == null) {
            try {
                instance = creatorDependenciesClass.newInstance();
                creatorDependencies.put(creatorDependenciesClass, instance);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return instance;
    }
}
