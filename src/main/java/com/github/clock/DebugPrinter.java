package com.github.clock;

import java.util.function.Supplier;

public interface DebugPrinter {
    void println(Supplier<String> messageSupplier);

    public static DebugPrinter of(boolean debugMode) {
        if(debugMode)
            return new DebugPrinterImpl();
        return new NullPrinter();
    }
}

class DebugPrinterImpl implements DebugPrinter {
    public void println(Supplier<String> messageSupplier) {
        System.out.println(messageSupplier.get());
    }
}

class NullPrinter implements DebugPrinter {
    public void println(Supplier<String> messageSupplier) {
        // do nothing.
    }
}

