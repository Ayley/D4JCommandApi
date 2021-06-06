package me.kleidukos.api.interaction;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.rest.util.ApplicationCommandOptionType;
import reactor.core.publisher.Mono;

final class InteractionCommandOptionValue implements IInteractionCommandOptionValue{

    private InteractionCreateEvent event;

    @Override
    public String getOptionRawValue(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::getRaw).orElse("");
    }

    @Override
    public String getOptionValueAsString(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asString).orElse("");
    }

    @Override
    public boolean getOptionValueAsBoolean(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asBoolean).orElse(true);
    }

    @Override
    public long getOptionValueAsLong(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
    }

    @Override
    public Snowflake getOptionValueAsSnowflake(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asSnowflake).orElse(null);
    }

    @Override
    public Mono<User> getOptionValueAsUser(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asUser).orElse(null);
    }

    @Override
    public Mono<Role> getOptionValueAsRole(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asRole).orElse(null);
    }

    @Override
    public Mono<Channel> getOptionValueAsChannel(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).flatMap(ApplicationCommandInteractionOption::getValue).map(ApplicationCommandInteractionOptionValue::asChannel).orElse(null);
    }

    @Override
    public Object getOptionValueAsObject(String name){
        return getOptionRawValue(name);
    }

    @Override
    public boolean isOptionValuePresent(String name){
        return event.getInteraction().getCommandInteraction().getOption(name).isPresent();
    }

    public void setEvent(InteractionCreateEvent event){
        this.event = event;
    }

}
