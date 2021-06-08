package me.kleidukos.api.interaction;

import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.MessageData;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface IInteractionResponse {

    Mono<? extends MessageData> acknowledgeDelete(String message, Duration duration);

    Mono<? extends MessageData> acknowledgeDelete(EmbedCreateSpec embed, Duration duration);

    Mono<? extends MessageData> acknowledgeDelete(MessageCreateSpec message, Duration duration);

    Mono<?> acknowledgeEphemeral(String message);

    Mono<?> acknowledgeEphemeral(EmbedCreateSpec embedCreateSpec);

    Mono<?> acknowledgeEphemeral(String message, EmbedCreateSpec embedCreateSpec);

    Mono<?> acknowledge(String message);

    Mono<?> acknowledge(EmbedCreateSpec embedCreateSpec);

    Mono<?> acknowledge(String message, EmbedCreateSpec embedCreateSpec)    ;
}
