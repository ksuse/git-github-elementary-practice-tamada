package com.github.clock;

import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider{
    @Override
    public String[] getVersion() {
        return new String[] { "clock 1.0.0-SNAPSHOT" };
    }
}
