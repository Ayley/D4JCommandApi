package me.kleidukos.api.interaction;

import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.MessageData;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

final class InteractionResponse implements IInteractionResponse{

    private InteractionCreateEvent event;
    private final InteractionCommand command;

    public InteractionResponse(InteractionCommand command) {
        this.command = command;
    }

    @Override
    public final Mono<? extends MessageData> acknowledgeDelete(String message, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendText(event, message);

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    @Override
    public final Mono<? extends MessageData> acknowledgeDelete(EmbedCreateSpec embed, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendEmbed(command, event, embed.asRequest());

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    @Override
    public final Mono<? extends MessageData> acknowledgeDelete(MessageCreateSpec message, Duration duration) {

        Mono<? extends MessageData> data = InteractionResponseAnswer.sendMessage(command, event, message.asRequest());

        Mono<? extends MessageData> result = event.acknowledge().then(data);

        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return result;
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(String message) {
        return event.reply(spec -> spec.setContent(message).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.addEmbed(embed -> embed = embedCreateSpec).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(String message, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.setContent(Objects.requireNonNull(message)).addEmbed(embed -> embed = embedCreateSpec).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledge(String message) {
        return event.reply(spec -> spec.setContent(message));
    }

    @Override
    public final Mono<?> acknowledge(EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.addEmbed(embed -> embed = embedCreateSpec));
    }

    @Override
    public final Mono<?> acknowledge(String message, EmbedCreateSpec embedCreateSpec) {
        return event.reply(spec -> spec.setContent(Objects.requireNonNull(message)).addEmbed(embed -> embed = embedCreateSpec));
    }

    public final void setEvent(InteractionCreateEvent event){
        this.event = event;
    }
}
