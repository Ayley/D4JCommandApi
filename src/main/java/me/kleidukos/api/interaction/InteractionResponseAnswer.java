package me.kleidukos.api.interaction;

import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.discordjson.json.MessageData;
import discord4j.discordjson.json.WebhookExecuteRequest;
import discord4j.rest.util.MultipartRequest;
import discord4j.rest.util.WebhookMultipartRequest;
import reactor.core.publisher.Mono;

final class InteractionResponseAnswer {

    public static Mono<MessageData> sendText(InteractionCommand command, InteractionCreateEvent event, String content) {
        return event.getInteractionResponse().createFollowupMessage(content);
    }

    public static Mono<MessageData> sendEmbed(InteractionCommand command, InteractionCreateEvent event, EmbedData embedData) {
        WebhookExecuteRequest body = WebhookExecuteRequest.builder().addEmbed(embedData).build();
        WebhookMultipartRequest request = new WebhookMultipartRequest(body);
        return command.getRestClient().getWebhookService().executeWebhook(command.getApplicationId(), event.getInteraction().getToken(), true, request);
    }

    public static Mono<MessageData> sendMessage(InteractionCommand command, InteractionCreateEvent event, MultipartRequest multipartRequest) {
        MessageCreateRequest createRequest = multipartRequest.getCreateRequest();
        assert createRequest != null;
        WebhookExecuteRequest body = WebhookExecuteRequest.builder().content(createRequest.content()).addEmbed(createRequest.embed().get()).build();
        WebhookMultipartRequest request = new WebhookMultipartRequest(body);
        return command.getRestClient().getWebhookService().executeWebhook(command.getApplicationId(), event.getInteraction().getToken(), true, request);
    }

}
