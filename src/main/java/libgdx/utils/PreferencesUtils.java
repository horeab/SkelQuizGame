package libgdx.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import libgdx.preferences.PreferencesService;

public class PreferencesUtils {

    public static void saveMap(Map<Integer, Integer> map, String mapName, PreferencesService preferencesService) {
        preferencesService.putInteger(mapName + "_size", map.size());
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            preferencesService.putInteger(mapName + "_key_" + i, entry.getKey());
            preferencesService.putInteger(mapName + "_value_" + i, entry.getValue());
            i++;
        }
    }

    public static Map<Integer, Integer> loadMap(String mapName, PreferencesService preferencesService) {
        int size = preferencesService.getPreferences().getInteger(mapName + "_size", 0);
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(preferencesService.getPreferences().getInteger(mapName + "_key_" + i, 0),
                    preferencesService.getPreferences().getInteger(mapName + "_value_" + i, 0));
        }
        return map;
    }

    public static List<Integer> loadList(String listName, PreferencesService preferencesService) {
        int size = preferencesService.getPreferences().getInteger(listName + "_size", 0);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++)
            list.add(preferencesService.getPreferences().getInteger(listName + "_" + i, 0));
        return list;
    }

    public static void saveList(List<Integer> list, String listName, PreferencesService preferencesService) {
        preferencesService.putInteger(listName + "_size", list.size());
        for (int i = 0; i < list.size(); i++) {
            preferencesService.putInteger(listName + "_" + i, list.get(i));
        }
    }
}
