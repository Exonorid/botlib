package us.exonorid.botlib.cmd;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface CommandInfo {
	public String[] aliases();
	public String usage();
}