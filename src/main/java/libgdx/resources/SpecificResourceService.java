package libgdx.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import libgdx.game.Game;
import libgdx.utils.EnumUtils;

public class SpecificResourceService {

    private static Set<Res> specificResourceValues = new HashSet<>();

    public Set<Res> getAllRes() {
        Set<Res> allRes = new HashSet<Res>(getSpecificResourceValues());
        allRes.addAll(new IncrementingResService(Game.getInstance().getSubGameDependencyManager().getIncrementResList()).createRes());
        return allRes;
    }

    private Set<Res> getSpecificResourceValues() {
        if (specificResourceValues.isEmpty()) {
            specificResourceValues.addAll(Arrays.asList(EnumUtils.getValues(Game.getInstance().getSubGameDependencyManager().getSpecificResourceTypeEnum())));
        }
        return specificResourceValues;
    }

}
