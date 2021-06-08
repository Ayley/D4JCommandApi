package me.kleidukos.api.interaction;

import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

final class InteractionResponse implements IInteractionResponse{

    private InteractionCreateEvent event;

    @Override
    public final Mono<?> acknowledgeDelete(String message, Duration duration) {
        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return  event.reply(r -> r.setContent(message));
    }

    @Override
    public final Mono<?> acknowledgeDelete(Consumer<EmbedCreateSpec> embed, Duration duration) {
        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return  event.reply(r -> r.addEmbed(embed));
    }

    @Override
    public final Mono<?> acknowledgeDelete(String message, Consumer<EmbedCreateSpec> embed, Duration duration) {
        Mono.delay(duration).then(event.getInteractionResponse().deleteInitialResponse()).subscribe();

        return event.reply(spec -> spec.setContent(message).addEmbed(embed).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(String message) {
        return event.reply(spec -> spec.setContent(message).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(Consumer<EmbedCreateSpec> embed) {
        return event.reply(spec -> spec.addEmbed(embed).setEphemeral(true));
    }

    @Override
    public final Mono<?> acknowledgeEphemeral(String message, Consumer<EmbedCreateSpec> embed) {
        return event.reply(spec -> spec.setContent(message).addEmbed(embed).setEphemeral(true));
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
