package us.exonorid.botlib;

import java.util.Arrays;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/*
 * A way to interface with the Discord API while abstracting away details such as message listening, command handling, etc.
 */
public class Bot {
	JDA jda = null;
	JDABuilder builder;
	MessageListener defaultListener;
	MessageListener listener;

	/**
	 * Creates a new Bot object with a token, a package for commands, and a prefix to use with said commands
	 *
	 * @param	token
	 * 			The token to use when logging into Discord.
	 *
	 * @param	commandPackage
	 * 			The fully qualified name of the package containing all commands (e.g. us.exonorid.examplebot.cmd)
	 *
	 * @param	prefix
	 * 			The prefix to use when searching for commands.
	 */
	public Bot(String token, String commandPackage, String prefix) {
		builder = JDABuilder.createDefault(token);
		defaultListener = new MessageListener(commandPackage, prefix);
		listener = defaultListener;
		builder.addEventListeners(defaultListener);
	}

	/**
	 * Adds a list of {@link net.dv8tion.jda.api.requests.GatewayIntent} instances to the bot
	 * <br><b>This MUST be called before building the bot using {@link #build()}!</b>
	 *
	 * @param	intents
	 * 			The list of GatewayIntents to be requested.
	 *
	 * @return	The new Bot instance.
	 */
	public Bot addIntents(GatewayIntent[] intents)  {
		builder.enableIntents(Arrays.asList(intents));
		return this;
	}

	public Bot addCacheFlags(CacheFlag[] flags) {
		builder.enableCache(Arrays.asList(flags));
		return this;
	}

	public Bot useCustomListener(ListenerAdapter l) {
		if(jda == null) {
			builder.removeEventListeners(listener);
			builder.addEventListeners(l);
		} else {
			jda.removeEventListener(listener);
			jda.addEventListener(l);
		}
		return this;
	}

	public Bot useDefaultListener() {
		if(jda == null) {
			builder.removeEventListeners(listener);
			builder.addEventListeners(defaultListener);
		} else {
			jda.removeEventListener(listener);
			jda.addEventListener(defaultListener);
		}
		return this;
	}

	/*
	 * Builds
	 */
	public Bot build() throws LoginException {
		jda = builder.build();
		return this;
	}

	public void injectFirst(Consumer<Message> f) {
		defaultListener.injectFirst(f);
	}

	public void injectLast(Consumer<Message> f) {
		defaultListener.injectLast(f);
	}

	public void uninjectFirst() {
		defaultListener.uninjectFirst();
	}

	public void uninjectLast() {
		defaultListener.uninjectLast();
	}
}
