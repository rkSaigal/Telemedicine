package com.telemedicicne.telemedicicne.newVideoCall;



import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceee {
    private static final int MAX_TRIES = 10;
    private final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    private static final String[] adjs = {
            "autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark",
            "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter",
            "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue",
            "billowing", "broken", "cold", "damp", "falling", "frosty", "green",
            "long", "late", "lingering", "bold", "little", "morning", "muddy", "old",
            "red", "rough", "still", "small", "sparkling", "throbbing", "shy",
            "wandering", "withered", "wild", "black", "young", "holy", "solitary",
            "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine",
            "polished", "ancient", "purple", "lively", "nameless"
    };

    private static final String[] nouns = {
            "waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning",
            "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter",
            "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook",
            "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly",
            "feather", "grass", "haze", "mountain", "night", "pond", "darkness",
            "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder",
            "violet", "water", "wildflower", "wave", "resonance", "sun", "wood",
            "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"
    };

    public String createUser(WebSocketSession session) throws InterruptedException {
        String id = generateUniqueID();
        if (id != null) {
            users.put(id, session);
        }
        return id;
    }

    public WebSocketSession getUser(String id) {
        return users.get(id);
    }

    public void removeUser(String id) {
        users.remove(id);
    }

    private String generateUniqueID() throws InterruptedException {
        for (int i = 0; i < MAX_TRIES; i++) {
            String id = generateHaiku();
            if (!users.containsKey(id)) {
                return id;
            }
            Thread.sleep(10);
        }
        return null;
    }

    private String generateHaiku() {
        String adj = adjs[ThreadLocalRandom.current().nextInt(adjs.length)];
        String noun = nouns[ThreadLocalRandom.current().nextInt(nouns.length)];
        int num = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.format("%s-%s-%d", adj, noun, num);
    }
}
