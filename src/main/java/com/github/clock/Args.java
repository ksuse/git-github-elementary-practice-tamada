package com.github.clock;

import java.time.ZoneId;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name="clock", versionProvider=VersionProvider.class, description="サンプルプロジェクト（アナログ時計）")
public class Args {
    @Option(names= {"-t", "--timezone"}, description="タイムゾーンを指定する．デフォルトはシステムデフォルト．", paramLabel="ZONEID")
    private String timeZone;

    @Option(names={ "-h", "--help"}, description="ヘルプメッセージを表示する")
    private boolean showHelp = false;

    @Option(names={ "-H", "--more-help"}, description="ヘルプメッセージを表示する（詳細表示）．")
    private boolean showMoreHelp = false;

    @Option(names = {"-v", "--version"}, description="バージョンを表示して終了する")
    private boolean showVersion = false;

    @Option(names={"-d", "--debug"}, description="デバッグモード")
    private boolean debugMode = false;

    @Option(names={"--long-hand-color", "-l"}, description="長針の色を指定する")
    private String longHandColor = "#ff0000";

    public boolean isRunningMode(){
        return !isShowVersion() && !isShowHelp();
    }

    public boolean isShowHelp(){
        return showHelp || showMoreHelp;
    }

    public String getLongHandColor() {
        return longHandColor;
    }

    public boolean isShowVersion(){
        return showVersion;
    }

    public boolean isDebugMode(){
        return debugMode;
    }

    public ZoneId getZoneId(){
        ZoneId id = ZoneId.systemDefault();
        if(timeZone != null)
            id = ZoneId.of(timeZone);
        return id;
    }

    public void showHelp(CommandLine commandline){
        commandline.usage(System.out);
        if(showMoreHelp){
            System.out.println("Available TimeZones");
            ZoneId.getAvailableZoneIds().stream()
                .forEach(id -> System.out.printf("\t%s%n", id));
        }
    }
}
