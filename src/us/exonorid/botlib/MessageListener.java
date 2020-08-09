package us.exonorid.botlib;

import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.exonorid.botlib.cmd.Commands;

/*
 * A class
 */
public class MessageListener extends ListenerAdapter {
	private Commands commands;
	private String prefix;
	private boolean listenToBots = false;
	private Consumer<Message> first = null, last = null;

	public MessageListener(String cp, String p) {
		commands = new Commands(cp, p);
		prefix = p;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getAuthor().isBot() && listenToBots == false)
			return;
		Message msg = e.getMessage();
		String content = msg.getContentRaw();
		if(first != null)
			first.accept(msg);
		if(content.startsWith(prefix)) {
			commands.handleCmd(msg);
		}
		if(last != null)
			last.accept(msg);
	}

	public void injectFirst(Consumer<Message> f) {
		first = f;
	}

	public void injectLast(Consumer<Message> f) {
		last = f;
	}

	public void uninjectFirst() {
		first = null;
	}

	public void uninjectLast() {
		last = null;
	}
}
