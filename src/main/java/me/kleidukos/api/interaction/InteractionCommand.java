package me.kleidukos.api.interaction;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.*;
import discord4j.rest.RestClient;
import discord4j.rest.util.Color;
import discord4j.rest.util.MultipartRequest;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

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

    public final Mono<? extends MessageData> acknowledgeDelete(InteractionCreateEvent event, String message, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendText(event, message);

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    public final Mono<? extends MessageData> acknowledgeDelete(InteractionCreateEvent event, EmbedCreateSpec embed, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendEmbed(this, event, embed.asRequest());

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    public final Mono<? extends MessageData> acknowledgeDelete(InteractionCreateEvent event, MessageCreateSpec message, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendMessage(this, event, message.asRequest());

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    public final Mono<?> acknowledgeEphemeral(InteractionCreateEvent event, String message) {
        return event.reply(spec -> spec.setContent(message).setEphemeral(true));
    }

    public final Mono<?> acknowledgeEphemeral(InteractionCreateEvent event, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.addEmbed(embed -> embed = embedCreateSpec).setEphemeral(true));
    }

    public final Mono<?> acknowledgeEphemeral(InteractionCreateEvent event, String message, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.setContent(Objects.requireNonNull(message)).addEmbed(embed -> embed = embedCreateSpec).setEphemeral(true));
    }

    public final Mono<?> acknowledge(InteractionCreateEvent event, String message) {
        return event.reply(spec -> spec.setContent(message));
    }

    public final Mono<?> acknowledge(InteractionCreateEvent event, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.addEmbed(embed -> embed = embedCreateSpec));
    }

    public final Mono<?> acknowledge(InteractionCreateEvent event, String message, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.setContent(Objects.requireNonNull(message)).addEmbed(embed -> embed = embedCreateSpec));
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
