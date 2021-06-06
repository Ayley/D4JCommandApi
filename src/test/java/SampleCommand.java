import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.util.ApplicationCommandOptionType;
import me.kleidukos.api.interaction.InteractionCommand;
import me.kleidukos.api.interaction.InteractionResponseType;
import org.reactivestreams.Publisher;

import java.time.Duration;

public class SampleCommand extends InteractionCommand {

    //This constructor for specific guild slash command
    public SampleCommand(RestClient restClient, long applicationId, long guildId) {
        super(restClient, applicationId, guildId, "NameOfCommand");
    }

    //This constructor for global slash command
    public SampleCommand(RestClient restClient, long applicationId) {
        super(restClient, applicationId, "NameOfCommand");
    }

    @Override
    public Publisher<?> onInteraction(InteractionCreateEvent event) {

        //For InteractionResponseType.Text
        String text = "Some result";

        //For InteractionResponseType.Embed
        EmbedCreateSpec embed = new EmbedCreateSpec();

        embed.setTitle("Some Title");

        embed.setDescription("Some Description");

        //For InteractionResponseType.Message
        MessageCreateSpec message = new MessageCreateSpec();

        message.setContent("Some Content");

        message.setEmbed(embedCreateSpec -> embedCreateSpec = embed);

        //Answer on command
        //For simple Text acknowledge(event, InteractionResponseType.Text, text);
        //For embed acknowledge(event, InteractionResponseType.Embed, embed.asRequest());
        //For message acknowledge(event, InteractionResponseType.Message, message.asRequest());

        //The message can be deleted, an example is below, the duration is freely selectable (The message "Some result" will delete after 10 Seconds)
        return acknowledgeWithDeleteTime(event, InteractionResponseType.Text, text, Duration.ofSeconds(10));
    }

    @Override
    public ApplicationCommandRequest applicationCommandRequest() {
        //Create the command
        return ApplicationCommandRequest.builder()
                .name("SameAsInSuperByConstructor")
                .description("SomeCommandDescription")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("OptionName")
                        .description("OptionDescription")
                        .type(ApplicationCommandOptionType.STRING.getValue())
                        .required(false)
                        .build()
                ).build();
    }
}
