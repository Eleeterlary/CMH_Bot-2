package eleeter.Cmhbot.loaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SlashCommandLoader {

    public static void registerCommands(Guild guild, String filePath) {
        if (guild == null || !new File(filePath).exists()) return;

        try (FileReader reader = new FileReader(filePath)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            List<CommandData> commands = new ArrayList<>();

            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                CommandData cmd = Commands.slash(obj.get("name").getAsString(), obj.get("description").getAsString());
                if (obj.has("options")) {
                    for (JsonElement optEl : obj.getAsJsonArray("options")) {
                        JsonObject o = optEl.getAsJsonObject();
                        ((net.dv8tion.jda.api.interactions.commands.build.SlashCommandData) cmd)
                                .addOptions(new OptionData(
                                        OptionType.valueOf(o.get("type").getAsString().toUpperCase()),
                                        o.get("name").getAsString(),
                                        o.get("description").getAsString(),
                                        o.get("required").getAsBoolean()
                                ));
                    }
                }
                commands.add(cmd);
            }

            guild.updateCommands().addCommands(commands).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
