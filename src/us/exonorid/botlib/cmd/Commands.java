package us.exonorid.botlib.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.entities.Message;

public class Commands {
	List<Command> cmds;
	List<CommandInfo> cmdInfos;
	String prefix;
	public Commands(String cp, String p) {
		List<Class<Command>> classes;
		cmds = new ArrayList<Command>();
		cmdInfos = new ArrayList<CommandInfo>();
		try(ScanResult sr = new ClassGraph().whitelistPackages(cp).enableClassInfo().scan()) {
			classes = sr.getSubclasses(Command.class.getName()).loadClasses(Command.class);
		}
		for(Class<Command> c : classes) {
			try {
				CommandInfo info = c.getAnnotation(CommandInfo.class);
				if(info == null) {
					System.err.println("ERROR: Command class " + c.getSimpleName() + " does not have a @ClassInfo annotation");
					System.exit(-1);
				}
				cmds.add(c.getConstructor().newInstance());
				cmdInfos.add(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		prefix = p;
	}

	public boolean hasAlias(int i, String name) {
		for(String alias : cmdInfos.get(i).aliases())
			if(alias.contentEquals(name))
				return true;
		return false;
	}

	public Command getCmd(String name) {
		for(int i = 0; i < cmds.size(); i++) {
			if(hasAlias(i, name))
				return cmds.get(i);
		}
		return null;
	}

	public void handleCmd(Message msg) {
		String[] split = msg.getContentRaw().split(" ");
		String name = split[0].substring(prefix.length());
		String[] args = null;
		if(split.length > 1)
			args = Arrays.copyOfRange(split, 1, split.length);
		Command c = getCmd(name);
		if(c != null) {
			c.run(msg, args);
		}
	}
}
