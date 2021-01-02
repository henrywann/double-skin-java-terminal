package model;

import java.util.HashMap;
import java.util.Map;

public class CardMap {

    private Map<String, Integer> map;

    public CardMap() {
        this.map = new HashMap<>();
        this.map.put("killer", -3);
        this.map.put("killerMaster", -4);
        this.map.put("police", 3);
        this.map.put("silencer", -2);
        this.map.put("doctor", 1);
        this.map.put("gunSmith", 1);
        this.map.put("villager", 0);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
