package org.typist.exceptions;

public class SourceTextIsNullException extends Exception {
    public String toString() {
        return "Source String could not be determined.";
    }
}
