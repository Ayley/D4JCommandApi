package me.kleidukos.api.interaction;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.MessageData;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;

public interface IInteractionResponse {

    Mono<?> acknowledgeDelete(String message, Duration duration);

    Mono<?> acknowledgeDelete(Consumer<EmbedCreateSpec> embed, Duration duration);

    Mono<?> acknowledgeDelete(String message, Consumer<EmbedCreateSpec> embed, Duration duration);

    Mono<?> acknowledgeEphemeral(String message);

    Mono<?> acknowledgeEphemeral(Consumer<EmbedCreateSpec> embed);

    Mono<?> acknowledgeEphemeral(String message, Consumer<EmbedCreateSpec> embed);

    Mono<?> acknowledge(String message);

    Mono<?> acknowledge(EmbedCreateSpec embedCreateSpec);

    Mono<?> acknowledge(String message, EmbedCreateSpec embedCreateSpec);
}
