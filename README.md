# アナログ時計

## 依存ライブラリ

* [picocli](https://github.com/remkop/picocli)
    * コマンドラインオプション解析ライブラリ

## 使用方法

```
Usage: clock [-dhHv] [-t=TIMEZONE]
Sample project for analog clock
  -d, --debug               debug mode.
  -h, --help                print this message.
  -H, --more-help           print detail help message.
  -t, --timezone=TIMEZONE   Specifies time zone.  Default is local time.
  -v, --version             show version and quit.
```

## タイムゾーン

`-t` オプションで時計のタイムゾーンを指定できます．
利用可能なタイムゾーンは，`--more-help` オプション(`-H`)で閲覧できます．



