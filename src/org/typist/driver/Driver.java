package org.typist.driver;

import java.awt.EventQueue;

import org.typist.utils.*;
import org.typist.beans.*;
import org.typist.engine.*;

class Driver {
    public static void main(String[] args) {
        /* Firing up UI... steps:
         * Steps to fire up UI:
         * get source text from repos and populate SourceTextBean
         * pass the bean to the swing components
         */

        // get source text from repos and populate SourceTextBean
        SourceTextFetcher stf = new SourceTextFetcher();
        final SourceTextBean stb = stf.fetchSourceText();

        // pass bean to swing components
        EventQueue.invokeLater(new Runnable() {
        public void run() {
            TypistUI typistUI = new TypistUI(stb);
        }
        });
    }
}

