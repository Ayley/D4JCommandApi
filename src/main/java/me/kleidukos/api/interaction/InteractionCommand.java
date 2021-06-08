package me.kleidukos.api.interaction;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.discordjson.json.*;
import discord4j.rest.RestClient;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;


public abstract class InteractionCommand extends ReactiveEventAdapter {

    private final RestClient restClient;
    private final long applicationId;
    private final long guildId;
    private final String name;
    private final InteractionCommandOptionValue optionValue;
    private final InteractionResponse response;

    public InteractionCommand(RestClient restClient, long applicationId, long guildId, String name) {
        this.restClient = restClient;
        this.applicationId = applicationId;
        this.guildId = guildId;
        this.name = name;
        this.optionValue = new InteractionCommandOptionValue();
        this.response = new InteractionResponse();

        registerCommand();
    }

    public InteractionCommand(RestClient restClient, long applicationId, String name) {
        this(restClient, applicationId, -1, name);
    }

    @Override
    public Publisher<?> onInteractionCreate(InteractionCreateEvent event){
        optionValue.setEvent(event);
        response.setEvent(event);

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

    public IInteractionResponse response() {
        return response;
    }
}
