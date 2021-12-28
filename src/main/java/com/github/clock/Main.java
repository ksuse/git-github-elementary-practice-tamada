package com.github.clock;

import com.github.clock.swing.ClockViewer;
import picocli.CommandLine;

public class Main{
    public Main(String[] arguments) {
        Args args = parseOptions(arguments);
        if(args.isRunningMode())
            showClock(args);
    }

    private void showClock(Args args) {
        Observers observers = new Observers();
        Clock clock = new Clock(args.getZoneId(), observers);
        ClockViewer viewer = new ClockViewer(clock, args, observers, DebugPrinter.of(args.isDebugMode()));
        viewer.showClock();
    }

    private Args parseOptions(String[] arguments) {
        Args args = new Args();
        CommandLine commandline = new CommandLine(args);
        commandline.parseArgs(arguments);

        if(args.isShowVersion())
            commandline.printVersionHelp(System.out);
        if(args.isShowHelp())
            args.showHelp(commandline);
        return args;
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}