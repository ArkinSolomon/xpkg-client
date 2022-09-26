package net.xpkgclient.exceptions;

// This interface is for exceptions that can point to a single line
public interface ILineException<T extends XPkgException> {

    // Return a new exception with the line number
    T setLine(int line);
}