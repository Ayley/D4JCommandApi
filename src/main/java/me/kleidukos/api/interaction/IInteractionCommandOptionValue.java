package me.kleidukos.api.interaction;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import reactor.core.publisher.Mono;

public interface IInteractionCommandOptionValue {

    String getOptionRawValue(String name);
    String getOptionValueAsString(String name);
    boolean getOptionValueAsBoolean(String name);
    long getOptionValueAsLong(String name);
    Snowflake getOptionValueAsSnowflake(String name);
    Mono<User> getOptionValueAsUser(String name);
    Mono<Role> getOptionValueAsRole(String name);
    Mono<Channel> getOptionValueAsChannel(String name);
    Object getOptionValueAsObject(String name);
    boolean isOptionValuePresent(String name);
}
