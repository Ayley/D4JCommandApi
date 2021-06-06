package me.kleidukos.api.interaction;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.MessageData;
import discord4j.rest.RestClient;
import discord4j.rest.util.MultipartRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.time.Duration;

public abstract class InteractionCommand extends ReactiveEventAdapter {

    private final RestClient restClient;
    private final long applicationId;
    private final long guildId;
    private final String name;
    private final InteractionCommandOptionValue optionValue;

    public InteractionCommand(RestClient restClient, long applicationId, long guildId, String name) {
        this.restClient = restClient;
        this.applicationId = applicationId;
        this.guildId = guildId;
        this.name = name;
        this.optionValue = new InteractionCommandOptionValue();

        registerCommand();
    }

    public InteractionCommand(RestClient restClient, long applicationId, String name) {
        this.restClient = restClient;
        this.applicationId = applicationId;
        this.guildId = -1;
        this.name = name;
        this.optionValue = new InteractionCommandOptionValue();

        registerCommand();
    }

    @Override
    public Publisher<?> onInteractionCreate(InteractionCreateEvent event){
        optionValue.setEvent(event);

        if(event.getInteraction().getCommandInteraction().getName().equalsIgnoreCase(name)) {
            return onInteraction(event);
        }else {
            return Mono.empty();
        }
    }

    public abstract Publisher<?> onInteraction(InteractionCreateEvent event);

    public abstract ApplicationCommandRequest applicationCommandRequest();

    private void registerCommand(){
        if(guildId > 0) {
            this.restClient.getApplicationService().createGuildApplicationCommand(applicationId, guildId, applicationCommandRequest()).block();
        }else {
            this.restClient.getApplicationService().createGlobalApplicationCommand(applicationId, applicationCommandRequest()).block();
        }
    }

    public final Mono<? extends MessageData> acknowledge(InteractionCreateEvent event, InteractionResponseType type, Object message) {
        Mono<? extends MessageData> data = switch (type) {
            case Embed -> InteractionResponseAnswer.sendEmbed(this, event, (EmbedData) message);
            case Text -> InteractionResponseAnswer.sendText(this, event, (String) message);
            case Message -> InteractionResponseAnswer.sendMessage(this, event, (MultipartRequest) message);
        };
        return event.acknowledge().then(data);
    }

    public final Mono<? extends MessageData> acknowledgeWithDeleteTime(InteractionCreateEvent event, InteractionResponseType type, Object message, Duration duration) {
        Mono<? extends MessageData> data = switch (type) {
            case Embed -> InteractionResponseAnswer.sendEmbed(this, event, (EmbedData) message);
            case Text -> InteractionResponseAnswer.sendText(this, event, (String) message);
            case Message -> InteractionResponseAnswer.sendMessage(this, event, (MultipartRequest) message);
        };
        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public long getGuildId() {
        return guildId;
    }

    public IInteractionCommandOptionValue getOptionValue() {
        return optionValue;
    }
}
