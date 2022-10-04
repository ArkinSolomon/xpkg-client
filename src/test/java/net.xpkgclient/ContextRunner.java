package net.xpkgclient;

import net.xpkgclient.exceptions.XPkgException;

/**
 * This interface allows creating lambda expressions with a single execution context as the parameter.
 */
public interface ContextRunner {

    /**
     * Run something in an execution context.
     *
     * @param context The execution context that the method will be run in.
     */
    void run(ExecutionContext context) throws XPkgException;
}
