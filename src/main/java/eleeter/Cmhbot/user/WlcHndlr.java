package eleeter.Cmhbot.user;

import eleeter.Cmhbot.loaders.EmbedLoader;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class WlcHndlr extends ListenerAdapter {
    private final String welcomeChannelId;

    public WlcHndlr(String welcomeChannelId) {
        this.welcomeChannelId = welcomeChannelId;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        GuildMessageChannel channel = event.getGuild().getTextChannelById(welcomeChannelId);
        if (channel != null) {
            EmbedBuilder embed = EmbedLoader.createWelcomeEmbed(
                    event.getUser().getName(),
                    event.getGuild().getName(),
                    "https://cdn.discordapp.com/attachments/1450948147768721428/1451358755315908690/model.png?ex=6945e2ac&is=6944912c&hm=3977c8497fee5ca645630bcaec1cfbc33e070805368e11b6ad0d1cb923f723ab&"
            );
            channel.sendMessageEmbeds(embed.build()).queue();

        }
    }
}