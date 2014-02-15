/*
 * Copyright (c) 2009,2014 Kostas Symeonidis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cylog.rdo.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A wrapper for Apache Commons Logging which automatically detects the constructing class' name
 * and initializes itself properly.
 *
 * @author Kostas Symeonidis
 */
public class ClassLogger implements Log {

    // ---- Constants ---------------------------------------------------------

    public static final String MUTEX = "mutex";

    // ---- Static ------------------------------------------------------------

    private static Log log = null;

    // ---- Constructors ------------------------------------------------------

    public ClassLogger() {
        StackTraceElement trace[] = new Throwable().getStackTrace();
        String className = trace[1].getClassName();

        synchronized (MUTEX) {
            log = LogFactory.getLog(className);
        }
    }

    // ---- Interface implementation ------------------------------------------

    public boolean isDebugEnabled() {
        return true; // return true always
    }

    public boolean isErrorEnabled() {
        return true; // return true always
    }

    public boolean isFatalEnabled() {
        return true; // return true always
    }

    public boolean isInfoEnabled() {
        return true; // return true always
    }

    public boolean isTraceEnabled() {
        return true; // return true always
    }

    public boolean isWarnEnabled() {
        return true; // return true always
    }

    public void trace(Object message) {
        log.trace(message);
    }

    public void trace(Object message, Throwable t) {
        log.trace(message, t);
    }

    public void debug(Object message) {
        log.debug(message);
        System.out.println("DEBUG: " + message);  // TODO: remove
    }

    public void debug(Object message, Throwable t) {
        log.debug(message, t);
    }

    public void info(Object message) {
        log.info(message);
        System.out.println("INFO : " + message);  // TODO: remove
    }

    public void info(Object message, Throwable t) {
        log.info(message, t);
    }

    public void warn(Object message) {
        log.warn(message);
        System.out.println("WARN : " + message);  // TODO: remove
    }

    public void warn(Object message, Throwable t) {
        log.warn(message, t);
    }

    public void error(Object message) {
        log.error(message);
    }

    public void error(Object message, Throwable t) {
        log.error(message, t);
    }

    public void fatal(Object message) {
        log.fatal(message);
    }

    public void fatal(Object message, Throwable t) {
        log.fatal(message, t);
    }

    // ---- Formatting methods ------------------------------------------------

    public void trace(String fmt, Object... args) {
        trace(String.format(fmt, args));
    }

    public void trace(Throwable t, String fmt, Object... args) {
        trace(String.format(fmt, args), t);
    }

    public void debug(String fmt, Object... args) {
        debug(String.format(fmt, args));
    }

    public void debug(Throwable t, String fmt, Object... args) {
        debug(String.format(fmt, args), t);
    }

    public void info(String fmt, Object... args) {
        info(String.format(fmt, args));
    }

    public void info(Throwable t, String fmt, Object... args) {
        info(String.format(fmt, args), t);
    }

    public void warn(String fmt, Object... args) {
        warn(String.format(fmt, args));
    }

    public void warn(Throwable t, String fmt, Object... args) {
        warn(String.format(fmt, args), t);
    }

    public void error(String fmt, Object... args) {
        error(String.format(fmt, args));
    }

    public void error(Throwable t, String fmt, Object... args) {
        error(String.format(fmt, args), t);
    }

    public void fatal(String fmt, Object... args) {
        fatal(String.format(fmt, args));
    }

    public void fatal(Throwable t, String fmt, Object... args) {
        fatal(String.format(fmt, args), t);
    }
}
