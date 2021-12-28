package com.github.clock;

import com.github.clock.swing.ClockViewer;
import picocli.CommandLine;

public class Main{
    public Main(String[] arguments) {
        Args args = parseOptions(arguments);

        if(args.isRunningMode()){
            Clock clock = new Clock(args.getZoneId());
            ClockViewer viewer = new ClockViewer(clock, args);
            viewer.setDebugMode(args.isDebugMode());
            viewer.showClock();
        }
    }

    private Args parseOptions(String[] arguments) {
        Args args = new Args();
        CommandLine commandline = new CommandLine(args);
        commandline.parseArgs(arguments);

        if(args.isShowVersion()){
            commandline.printVersionHelp(System.out);
        }
        if(args.isShowHelp()){
            args.showHelp(commandline);
        }
        return args;
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}