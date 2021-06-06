import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.rest.RestClient;

public class SampleBot {

    private final GatewayDiscordClient discordClient;
    private final RestClient restClient;
    private final long applicationId;
    private final long guildId = 0L;

    public SampleBot(){
        //Create Discord bot
        discordClient = DiscordClient.create("token").login().block();

        //Get RestClient
        restClient = discordClient.getRestClient();

        //Get ApplicationId
        applicationId = restClient.getApplicationId().block();


        //Don't add a command both ways
        //Add specific guild slash command
        discordClient.getEventDispatcher().on(new SampleCommand(restClient, applicationId, guildId)).subscribe();

        //For global slash command
        discordClient.getEventDispatcher().on(new SampleCommand(restClient, applicationId)).subscribe();

        discordClient.onDisconnect().block();
    }

    public static void main(String[] args) {
        new SampleBot();
    }

}
