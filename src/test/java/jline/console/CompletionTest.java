/*
 * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package jline.console;

import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.SimpleCompleter;
import org.junit.Test;

import java.util.Iterator;

/**
 * Tests completion.
 *
 * @author <a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>
 */
public class CompletionTest
    extends ConsoleReaderTestSupport
{
    @Test
    public void testSimpleCompletor() throws Exception {
        // clear any current completors
        for (Iterator i = console.getCompletors().iterator(); i.hasNext(); console.removeCompletor((Completer) i.next())) {
            // empty
        }

        console.addCompletor(new SimpleCompleter(new String[]{"foo", "bar", "baz"}));

        assertBuffer("foo ", new Buffer("f").op(ConsoleReader.COMPLETE));
        // single tab completes to unabbiguous "ba"
        assertBuffer("ba", new Buffer("b").op(ConsoleReader.COMPLETE));
        assertBuffer("ba", new Buffer("ba").op(ConsoleReader.COMPLETE));
        assertBuffer("baz ", new Buffer("baz").op(ConsoleReader.COMPLETE));
    }

    @Test
    public void testArgumentCompletor() throws Exception {
        // clear any current completors
        for (Iterator i = console.getCompletors().iterator(); i.hasNext(); console.removeCompletor((Completer) i.next())) {
            // empty
        }

        console.addCompletor(new ArgumentCompleter
            (new SimpleCompleter(new String[]{"foo", "bar", "baz"})));

        assertBuffer("foo foo ", new Buffer("foo f").op(ConsoleReader.COMPLETE));
        assertBuffer("foo ba", new Buffer("foo b").op(ConsoleReader.COMPLETE));
        assertBuffer("foo ba", new Buffer("foo ba").op(ConsoleReader.COMPLETE));
        assertBuffer("foo baz ", new Buffer("foo baz").op(ConsoleReader.COMPLETE));

        // test completion in the mid range
        assertBuffer("foo baz", new Buffer("f baz").left().left().left().left().op(ConsoleReader.COMPLETE));
        assertBuffer("ba foo", new Buffer("b foo").left().left().left().left().op(ConsoleReader.COMPLETE));
        assertBuffer("foo ba baz", new Buffer("foo b baz").left().left().left().left().op(ConsoleReader.COMPLETE));
        assertBuffer("foo foo baz", new Buffer("foo f baz").left().left().left().left().op(ConsoleReader.COMPLETE));
    }
}