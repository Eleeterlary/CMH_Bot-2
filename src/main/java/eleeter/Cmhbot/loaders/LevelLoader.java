package eleeter.Cmhbot.loaders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LevelLoader {

    private static final String LEVELS_FILE = "levels.json";
    private static final Gson gson = new Gson();

    public static class UserLevel {
        public int xp;
        public int level;

        public UserLevel(int xp, int level) {
            this.xp = xp;
            this.level = level;
        }
    }

    private static final Map<String, UserLevel> userLevels = loadLevels();

    // Load levels from JSON
    private static Map<String, UserLevel> loadLevels() {
        try {
            File file = new File(LEVELS_FILE);
            if (!file.exists()) return new HashMap<>();

            Type type = new TypeToken<Map<String, UserLevel>>() {}.getType();
            return gson.fromJson(new FileReader(file), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Save levels to JSON
    private static void saveLevels() {
        try (FileWriter writer = new FileWriter(LEVELS_FILE)) {
            gson.toJson(userLevels, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserLevel get(String userId) {
        return userLevels.computeIfAbsent(userId, id -> new UserLevel(0, 0));
    }

    public static void update(String userId, int xp, int level) {
        userLevels.put(userId, new UserLevel(xp, level));
        saveLevels();
    }

    public static Map<String, UserLevel> getAllUsers() {
        return new HashMap<>(userLevels);
    }

    public static int getLevel(int xp) {
        return xp / 10;
    }

}

