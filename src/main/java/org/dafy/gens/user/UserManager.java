package org.dafy.gens.user;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class UserManager {
    private final HashMap<UUID, User> userHashMap = new HashMap<>();
    public void cacheUser(User user){
        userHashMap.put(user.getUuid(),user);
    }
    public void removeUser(UUID uuid){
        userHashMap.remove(uuid);
    }
    public User getUser(UUID uuid){
        return userHashMap.get(uuid);
    }
    public Set<UUID> getUserKeys(){
        return userHashMap.keySet();
    }
}
