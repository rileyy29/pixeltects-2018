package com.pixeltects.core.utils.messages;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ConsoleFilter implements Filter {
    public boolean isLoggable(LogRecord log) {
        if (log.getMessage().contains("Summoned new") || log.getMessage().contains("Displaying particle"))
            return false;
        return true;
    }
}
