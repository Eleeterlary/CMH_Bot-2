package eleeter.Cmhbot.user;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class UserBot {

    private final String token;
    private final OnlineStatus status;
    private final Activity activity;

    public UserBot() {
      String envToken = System.getenv("DISCORD_TOKEN");
      if (envToken == null || envToken.isEmpty()){
          throw new IllegalStateException("DISCORD_TOKEN IS COOKED!!");
      }

    this.token = envToken;

    this.status = OnlineStatus.DO_NOT_DISTURB;
    this.activity = Activity.playing("Suffering from Bugs ");

}

    public String getToken() {
        return token;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public Activity getActivity() {
        return activity;
    }
}
