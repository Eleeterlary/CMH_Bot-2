package eleeter.Cmhbot.loaders;

import net.dv8tion.jda.api.EmbedBuilder;
import java.awt.Color;
import java.util.Map;

public class EmbedLoader {

    private static final Color DEFAULT_COLOR = Color.decode("#6726DE");

    public static EmbedBuilder createEmbed(String title, String description) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(DEFAULT_COLOR);
    }

    public static EmbedBuilder createEmbedWithImage(String title, String description, String imageUrl) {
        return createEmbed(title, description)
                .setImage(imageUrl);
    }


/** Custom Emoji.... Nothing more **/
    public static EmbedBuilder createWelcomeEmbed(String username, String serverName, String gifUrl) {
        String emoji = "<:model:1451381746007277569>"; // your custom emoji

        return new EmbedBuilder()
                .setTitle("WELCOME " + username.toUpperCase() + "!")
                .setDescription("Glad to have you here in **" + serverName + "** " + emoji)
                .setColor(Color.decode("#6726DE"))
                .setImage(gifUrl);
    }

    public static EmbedBuilder createFAQEmbed(String title, String description) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.decode("#6726DE"));
    }

    public static EmbedBuilder createFAQEmbed(String title, Map<String, String> faq, String entry) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.decode("#6726DE"));

        if ("list".equals(entry)) {embed.setTitle(title);embed.setDescription(String.join("\n", faq.keySet()));
        } else if (faq.containsKey(entry)) {
            embed.setTitle(title);
            embed.setDescription(faq.get(entry));
        } else {
            embed.setTitle("FAQ entry not found");
            embed.setDescription("No such entry: " + entry);
        }

        return embed;
    }

}
