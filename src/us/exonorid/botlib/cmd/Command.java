package us.exonorid.botlib.cmd;

import net.dv8tion.jda.api.entities.Message;

public abstract class Command {
	public abstract void run(Message msg, String[] args);
}
