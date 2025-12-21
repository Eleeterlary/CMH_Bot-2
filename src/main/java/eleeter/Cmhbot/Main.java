package eleeter.Cmhbot;

import eleeter.Cmhbot.loaders.EmbedLoader;
import eleeter.Cmhbot.loaders.LevelLoader;
import eleeter.Cmhbot.loaders.ResponseLoader;
import eleeter.Cmhbot.user.UserBot;
import eleeter.Cmhbot.user.WlcHndlr;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.concurrent.ConcurrentHashMap;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Main extends ListenerAdapter {

    private static final String RESPONSES_FILE = "responses.json";
    private static final String FAQ_FILE = "faq.json";
    private static final String LEVEL_EMOJI = "<:model:1451381746007277569>";
    private static final String LEVEL_UP_CHANNEL_ID = "1452403480755966174";


    static Map<String, Long> leaderboardCooldown = new ConcurrentHashMap<>();
    static Map<String, Long> levelCooldown = new ConcurrentHashMap<>();
    static Map<String, Long> responseCooldown = new ConcurrentHashMap<>();

    private static final ResponseLoader dataLoader = new ResponseLoader(RESPONSES_FILE, FAQ_FILE);
    private static final Map<String, String> responses = dataLoader.getResponses();
    private static final Map<String, String> faq = dataLoader.getFAQ();



    public static void main(String[] args) throws Exception {
        String token = System.getenv("DISCORD_TOKEN");

        if (token == null || token.isEmpty()) {throw new IllegalStateException("DISCORD_TOKEN is not set");}

        UserBot bot = new UserBot();

        JDA jda = JDABuilder.createDefault(bot.getToken()).enableIntents(net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT, net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS).setStatus(bot.getStatus()).setActivity(bot.getActivity()).addEventListeners(new Main(), new WlcHndlr("1416063061004914708")).build();jda.awaitReady();}


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        TextChannel levelChannel = event.getGuild().getTextChannelById(LEVEL_UP_CHANNEL_ID);
        if (levelChannel == null) return;

        String msg = event.getMessage().getContentRaw().trim().toLowerCase();
        String userId = event.getAuthor().getId();

        long now = System.currentTimeMillis();

        LevelLoader.UserLevel userLevel = LevelLoader.get(userId);
        userLevel.xp += 1;

        int newLevel = userLevel.xp / 10;

        long lastLevelMsg = levelCooldown.getOrDefault(userId, 0L);

        if (newLevel > userLevel.level && now - lastLevelMsg >= 10_000) {userLevel.level = newLevel;levelCooldown.put(userId, now);

            levelChannel.sendMessage(event.getAuthor().getAsMention() + " leveled up to " + newLevel + " You’ve shown your loyalty and Eleeter won't betray you You’re becoming a real one in this server Keep the grind going " + "<:model:1451381746007277569>").queue();}

        LevelLoader.update(userId, userLevel.xp, userLevel.level);

        if (msg.startsWith("!faq")) {String entry = msg.contains(" ") ? msg.split("\\s+", 2)[1] : "list";event.getChannel().sendMessageEmbeds(EmbedLoader.createFAQEmbed("FAQ", faq, entry).build()).queue();
            return;
        }

        if (responses.containsKey(msg)){long last = responseCooldown.getOrDefault(userId, 0L);

            if (now - last < 2_000) return;

            responseCooldown.put(userId, now);event.getChannel().sendMessage(responses.get(msg)).queue();return;
        }
        if (msg.equals("!leaderboard")) {String channelId = event.getChannel().getId();long last = leaderboardCooldown.getOrDefault(channelId, 0L);

            if (now - last < 15_000) {
                event.getChannel().sendMessage("⏳ Leaderboard cooldown. Try again soon.").queue();
                return;
            }

            leaderboardCooldown.put(channelId, now);

            Map<String, LevelLoader.UserLevel> allUsers = LevelLoader.getAllUsers();List<Map.Entry<String, LevelLoader.UserLevel>> sorted = new ArrayList<>(allUsers.entrySet());

            sorted.sort((a, b) -> Integer.compare(b.getValue().level, a.getValue().level));

            StringBuilder lbText = new StringBuilder();
            int rank = 1;

            for (Map.Entry<String, LevelLoader.UserLevel> entry : sorted) {if (rank > 10) break;lbText.append("**#").append(rank).append("** <@").append(entry.getKey()).append("> - Level ").append(entry.getValue().level).append("\n");rank++;}

            event.getChannel().sendMessageEmbeds(new net.dv8tion.jda.api.EmbedBuilder().setTitle("🏆 Leaderboard - Top 10").setDescription(lbText.toString()).setColor(java.awt.Color.decode("#6726DE")).build()).queue();
        }
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!"faq".equals(event.getName())) return;

        String entry = event.getOption("entry") != null
                ? event.getOption("entry").getAsString().toLowerCase()
                : "list";

        event.replyEmbeds(
                EmbedLoader.createFAQEmbed("FAQ", faq, entry).build()
        ).queue();
    }
}
