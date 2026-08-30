package tool;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import tool.cmnclslib.cls.ClsCmmnArgs;
import tool.cmnclslib.cls.ClsLogger;
import tool.cmnclslib.mdl.MdlApp;
import tool.cmnclslib.mdl.MdlArg;
import tool.cmnclslib.mdl.MdlConst;
import tool.cmnclslib.mdl.MdlDate;
import tool.cmnclslib.mdl.MdlFile;
import tool.cmnclslib.mdl.MdlUtil;

/**
 * コマンドライン引数の解析およびアプリケーション設定パラメータの管理を行うクラスです。
 */
public class ClsAppArg {

    private final ClsLogger logger;
    private final ClsCmmnArgs cmmnArgs;
    private final ClsProp baseDir;

    /**
     * {@link ClsAppArg} クラスの新しいインスタンスを初期化します。
     *
     * @param logger ログ出力オブジェクト
     */
    public ClsAppArg(ClsLogger logger) {
        this.logger = Objects.requireNonNull(logger, "logger must not be null");
        this.cmmnArgs = new ClsCmmnArgs(logger);
        this.cmmnArgs.getModuleInfo();
        this.baseDir = new ClsProp();
        this.baseDir.setExeDir(this.cmmnArgs.getExeDir());
        this.baseDir.setExeBaseName(this.cmmnArgs.getExeBaseName());
    }

    /**
     * 保持している設定データオブジェクト（{@link ClsProp}）を取得します。
     *
     * @return 設定データオブジェクト
     */
    public ClsProp getBaseDirObj() {
        return baseDir;
    }

    /**
     * 保持している設定データオブジェクト（{@link ClsProp}）を取得します。
     *
     * @return 設定データオブジェクト
     */
    public ClsProp getPropObj() {
        return baseDir;
    }

    /**
     * 実行ファイルのベース名を取得します。
     *
     * @return 実行ファイルベース名
     */
    public String getExeBaseName() {
        return baseDir.getExeBaseName();
    }

    /**
     * 実行ファイルのベース名を設定します。
     *
     * @param exeBaseName 実行ファイルベース名
     */
    public void setExeBaseName(String exeBaseName) {
        baseDir.setExeBaseName(exeBaseName);
    }

    /**
     * 実行ファイルのディレクトリパスを取得します。
     *
     * @return 実行ファイルディレクトリパス
     */
    public String getExeDir() {
        return baseDir.getExeDir();
    }

    /**
     * 実行ファイルのディレクトリパスを設定します。
     *
     * @param exeDir 実行ファイルディレクトリパス
     */
    public void setExeDir(String exeDir) {
        baseDir.setExeDir(exeDir);
    }

    /**
     * ヘルプ・使用方法の表示要求フラグを取得します。
     *
     * @return ヘルプ要求フラグ
     */
    public boolean isUsage() {
        return baseDir.isUsage();
    }

    /**
     * アプリケーションの終了コードを取得します。
     *
     * @return 終了コード
     */
    public int getReturnCode() {
        return baseDir.getReturnCode();
    }

    /**
     * アプリケーションの終了コードを設定します。
     *
     * @param returnCode 終了コード
     */
    public void setReturnCode(int returnCode) {
        baseDir.setReturnCode(returnCode);
    }

    /**
     * ログ出力の冗長度レベルを取得します。
     *
     * @return 冗長度レベル
     */
    public int getVerbose() {
        return baseDir.getVerbose();
    }

    /**
     * ログ出力の冗長度レベルを設定します。
     *
     * @param verbose 冗長度レベル
     */
    public void setVerbose(int verbose) {
        baseDir.setVerbose(verbose);
    }

    /**
     * 例外時のスタックトレース表示フラグを取得します。
     *
     * @return スタックトレース表示フラグ
     */
    public boolean isStackTrace() {
        return baseDir.isStackTrace();
    }

    /**
     * 対象パスを取得します。
     *
     * @return 対象パス
     */
    public String getPath() {
        return baseDir.getPath();
    }

    /**
     * 対象パスを設定します。
     *
     * @param path 対象パス
     */
    public void setPath(String path) {
        baseDir.setPath(path);
    }

    /**
     * 設定する変更日付文字列を取得します。
     *
     * @return 変更日付文字列
     */
    public String getModifiedDateStr() {
        return baseDir.getModifiedDateStr();
    }

    /**
     * 設定する変更日付文字列を設定します。
     *
     * @param modifiedDateStr 変更日付文字列
     */
    public void setModifiedDateStr(String modifiedDateStr) {
        baseDir.setModifiedDateStr(modifiedDateStr);
    }

    /**
     * 処理モードコードを取得します。
     *
     * @return 処理モードコード
     */
    public int getModeCode() {
        return baseDir.getModeCode();
    }

    /**
     * 処理モードコードを設定します。
     *
     * @param modeCode 処理モードコード
     */
    public void setModeCode(int modeCode) {
        baseDir.setModeCode(modeCode);
    }

    /**
     * 変更対象種別コード（ファイル/ディレクトリ/両方）を取得します。
     *
     * @return 種別コード
     */
    public int getTypeCode() {
        return baseDir.getTypeCode();
    }

    /**
     * 変更対象種別コードを設定します。
     *
     * @param typeCode 種別コード
     */
    public void setTypeCode(int typeCode) {
        baseDir.setTypeCode(typeCode);
    }

    /**
     * 差分表示レベルを取得します。
     *
     * @return 差分表示レベル
     */
    public int getDiffLevel() {
        return baseDir.getDiffLevel();
    }

    /**
     * 差分表示レベルを設定します。
     *
     * @param diffLevel 差分表示レベル
     */
    public void setDiffLevel(int diffLevel) {
        baseDir.setDiffLevel(diffLevel);
    }

    /**
     * 有効日付の確認しきい値（数値表現）を取得します。
     *
     * @return 有効日付確認数値
     */
    public int getCheckDate() {
        return baseDir.getCheckDate();
    }

    /**
     * 有効日付の確認しきい値（数値表現）を設定します。
     *
     * @param checkDate 有効日付確認数値
     */
    public void setCheckDate(int checkDate) {
        baseDir.setCheckDate(checkDate);
    }

    /**
     * 最小探索ディレクトリ階層深さを取得します。
     *
     * @return 最小階層深さ
     */
    public long getMinDepth() {
        return baseDir.getMinDepth();
    }

    /**
     * 最大探索ディレクトリ階層深さを取得します。
     *
     * @return 最大階層深さ
     */
    public long getMaxDepth() {
        return baseDir.getMaxDepth();
    }

    /**
     * 日付変更処理の実行フラグを取得します。
     *
     * @return 実行フラグ
     */
    public boolean isExec() {
        return baseDir.isExec();
    }

    /**
     * ファイル日付変更対象フラグを取得します。
     *
     * @return ファイル対象フラグ
     */
    public boolean isModFile() {
        return baseDir.isModFile();
    }

    /**
     * ディレクトリ日付変更対象フラグを取得します。
     *
     * @return ディレクトリ対象フラグ
     */
    public boolean isModDir() {
        return baseDir.isModDir();
    }

    /**
     * ファイル名からの日付検索・取得フラグを取得します。
     *
     * @return ファイル名取得フラグ
     */
    public boolean isGetDateByName() {
        return baseDir.isGetDateByName();
    }

    /**
     * ディレクトリ名からの日付検索・取得フラグを取得します。
     *
     * @return ディレクトリ名取得フラグ
     */
    public boolean isGetDateByDirName() {
        return baseDir.isGetDateByDirName();
    }

    /**
     * 指定パターン名からの日付検索・取得フラグを取得します。
     *
     * @return 指定パターン取得フラグ
     */
    public boolean isGetDateBySpecFName() {
        return baseDir.isGetDateBySpecFName();
    }

    /**
     * 作成日時対象フラグを取得します。
     *
     * @return 作成日時対象フラグ
     */
    public boolean isCreationTime() {
        return baseDir.isCreationTime();
    }

    /**
     * 更新日時対象フラグを取得します。
     *
     * @return 更新日時対象フラグ
     */
    public boolean isLastWriteTime() {
        return baseDir.isLastWriteTime();
    }

    /**
     * ベースディレクトリ対象フラグを取得します。
     *
     * @return ベースディレクトリ対象フラグ
     */
    public boolean isBaseDir() {
        return baseDir.isBaseDir();
    }

    /**
     * ベースディレクトリ対象フラグを設定します。
     *
     * @param baseDir ベースディレクトリ対象フラグ
     */
    public void setBaseDir(boolean baseDir) {
        this.baseDir.setBaseDir(baseDir);
    }

    /**
     * 正規表現ファイル名対象判定フラグを取得します。
     *
     * @return 対象判定フラグ
     */
    public boolean isRegIncBasename() {
        return baseDir.isRegIncBasename();
    }

    /**
     * 正規表現ファイル名除外判定フラグを取得します。
     *
     * @return 除外判定フラグ
     */
    public boolean isRegExcBasename() {
        return baseDir.isRegExcBasename();
    }

    /**
     * 対象ヒット時の再帰適用フラグを取得します。
     *
     * @return 再帰適用フラグ
     */
    public boolean isIncHitRecursive() {
        return baseDir.isIncHitRecursive();
    }

    /**
     * 除外ヒット時の再帰適用フラグを取得します。
     *
     * @return 再帰適用フラグ
     */
    public boolean isExcHitRecursive() {
        return baseDir.isExcHitRecursive();
    }

    /**
     * ディレクトリフィルタのOR条件適用フラグを取得します。
     *
     * @return OR条件適用フラグ
     */
    public boolean isDirFilterOr() {
        return baseDir.isDirFilterOr();
    }

    /**
     * 強制更新フラグを取得します。
     *
     * @return 強制更新フラグ
     */
    public boolean isForce() {
        return baseDir.isForce();
    }

    /**
     * 差分のみ表示フラグを取得します。
     *
     * @return 差分のみ表示フラグ
     */
    public boolean isDiff() {
        return baseDir.isDiff();
    }

    /**
     * ダブルクォーテーション囲み出力フラグを取得します。
     *
     * @return 囲み出力フラグ
     */
    public boolean isDq() {
        return baseDir.isDq();
    }

    /**
     * 更新有無確認フラグを取得します。
     *
     * @return 更新有無確認フラグ
     */
    public boolean isUpdateCheck() {
        return baseDir.isUpdateCheck();
    }

    /**
     * シンボリックリンク判定有効化フラグを取得します。
     *
     * @return シンボリックリンク有効化フラグ
     */
    public boolean isSymLink() {
        return baseDir.isSymLink();
    }

    /**
     * 対象ファイル名フィルタパターンリストを取得します。
     *
     * @return フィルタパターンリスト
     */
    public List<String> getIncFilesList() {
        return baseDir.getIncFilesList();
    }

    /**
     * 除外ファイル名フィルタパターンリストを取得します。
     *
     * @return 除外パターンリスト
     */
    public List<String> getExcFilesList() {
        return baseDir.getExcFilesList();
    }

    /**
     * 対象ディレクトリ名フィルタパターンリストを取得します。
     *
     * @return フィルタパターンリスト
     */
    public List<String> getIncDirsList() {
        return baseDir.getIncDirsList();
    }

    /**
     * 除外ディレクトリ名フィルタパターンリストを取得します。
     *
     * @return 除外パターンリスト
     */
    public List<String> getExcDirsList() {
        return baseDir.getExcDirsList();
    }

    /**
     * 指定ファイル名フィルタパターンリストを取得します。
     *
     * @return 指定パターンリスト
     */
    public List<String> getIncSpecsList() {
        return baseDir.getIncSpecsList();
    }

    /**
     * 終了コード表示フラグを取得します。
     *
     * @return 終了コード表示フラグ
     */
    public boolean isEchoRetcode() {
        return baseDir.isEchoRetcode();
    }

    /**
     * 以前の日付閾値判定フラグを取得します。
     *
     * @return 判定フラグ
     */
    public boolean isBefore() {
        return baseDir.isBefore();
    }

    /**
     * 以後の日付閾値判定フラグを取得します。
     *
     * @return 判定フラグ
     */
    public boolean isAfter() {
        return baseDir.isAfter();
    }

    /**
     * 以前の日付閾値を取得します。
     *
     * @return 以前の日付閾値
     */
    public LocalDateTime getBeforeTime() {
        return baseDir.getBeforeTime();
    }

    /**
     * 以後の日付閾値を取得します。
     *
     * @return 以後の日付閾値
     */
    public LocalDateTime getAfterTime() {
        return baseDir.getAfterTime();
    }

    /**
     * コマンドライン引数を解析し、各種設定プロパティに値を設定します。
     *
     * @param args コマンドライン引数の配列
     * @return 引数の解析および検証に成功した場合は {@code true}。それ以外の場合は {@code false}。
     */
    public boolean parse(String[] args) {
        Objects.requireNonNull(args, "args must not be null");

        Map<String, String> namedArgs = MdlArg.getNamedArgs(args);
        cmmnArgs.setNamedArgs(namedArgs);
        boolean isOk = cmmnArgs.getCommonArgs();

        parseGeneralOptions(namedArgs);
        isOk = parsePathOption(namedArgs, isOk);
        isOk = parseModeAndDepth(namedArgs, isOk);
        isOk = parseDateOptions(namedArgs, isOk);
        parseFilterOptions();
        parseDateTimeThresholds(namedArgs);

        namedArgs.clear();
        return isOk;
    }

    /**
     * 全般オプション（冗長度、強制、差分、スタックトレース、出力モード等）を解析して設定します。
     *
     * @param namedArgs コマンドライン引数マップ
     */
    private void parseGeneralOptions(Map<String, String> namedArgs) {
        baseDir.setUsage(cmmnArgs.isUsage());
        baseDir.setVerbose(cmmnArgs.getVerbose());
        baseDir.setStackTrace(cmmnArgs.isStackTrace());
        baseDir.setForce(cmmnArgs.isForce());
        baseDir.setDiff(cmmnArgs.isDiff());
        baseDir.setDiffLevel(cmmnArgs.getDiffLevel());

        if (MdlArg.containsKey(namedArgs, "j")) {
            logger.setValueByKey(ClsLogger.IS_STDERR, "true");
        }
        if (MdlArg.containsKey(namedArgs, "dq")) {
            baseDir.setDq(true);
        }
        if (MdlArg.containsKey(namedArgs, "check")) {
            baseDir.setUpdateCheck(true);
        }
        if (MdlArg.containsKey(namedArgs, "sym")) {
            baseDir.setSymLink(true);
        }
        if (MdlArg.containsKey(namedArgs, "echo-retcd")) {
            baseDir.setEchoRetcode(true);
        }
    }

    /**
     * 対象パス指定オプション（-path, -f）を解析して設定します。
     *
     * @param namedArgs コマンドライン引数マップ
     * @param currentOk これまでの解析成否状態
     * @return パスが有効に取得・設定できた場合は {@code true}、無効な場合は {@code false}
     */
    private boolean parsePathOption(Map<String, String> namedArgs, boolean currentOk) {
        if (!currentOk) {
            return false;
        }
        String pathVal = "";
        if (MdlArg.containsKey(namedArgs, "f")) {
            pathVal = cmmnArgs.getPathParam("f", MdlFile.PATH_AUTO_DETECT, false);
        }
        if (MdlArg.containsKey(namedArgs, "path")) {
            pathVal = cmmnArgs.getPathParam("path", MdlFile.PATH_AUTO_DETECT, false);
        }
        baseDir.setPath(pathVal);

        if (baseDir.getPath().isEmpty() && !baseDir.isUsage()) {
            logger.writeLine(MdlConst.LVL_E, "INVALID ARGUMENT : -path|-f " + baseDir.getPath());
            return false;
        }
        return true;
    }

    /**
     * 処理モード、探索深度（min/max）、実行/一覧フラグ、対象種別（ファイル/ディレクトリ）を解析して設定します。
     *
     * @param namedArgs コマンドライン引数マップ
     * @param currentOk これまでの解析成否状態
     * @return 解析に問題がなければ {@code true}、不正な深度指定等の場合は {@code false}
     */
    private boolean parseModeAndDepth(Map<String, String> namedArgs, boolean currentOk) {
        boolean isOk = currentOk;

        if (MdlArg.containsKey(namedArgs, "mode")) {
            String tempStr = MdlArg.getValue(namedArgs, "mode");
            if (tempStr != null && !tempStr.isBlank()) {
                int tempInt = MdlUtil.parseInt(tempStr, MdlConst.INT_NULL);
                if (tempInt != MdlConst.INT_NULL) {
                    baseDir.setModeCode(tempInt);
                }
            }
        }

        if (MdlArg.containsKey(namedArgs, "check-date")) {
            String tempStr = MdlArg.getValue(namedArgs, "check-date");
            if (tempStr != null && !tempStr.isBlank()) {
                int tempInt = MdlUtil.parseInt(tempStr, MdlConst.INT_NULL);
                if (tempInt != MdlConst.INT_NULL) {
                    baseDir.setCheckDate(tempInt);
                }
            }
        }

        if (MdlArg.containsKey(namedArgs, "min")) {
            String tempStr = MdlArg.getValue(namedArgs, "min");
            if (tempStr != null && !tempStr.isBlank()) {
                int tempInt = MdlUtil.parseInt(tempStr, MdlConst.INT_NULL);
                if (tempInt != MdlConst.INT_NULL) {
                    baseDir.setMinDepth(tempInt);
                }
            }
        }

        if (MdlArg.containsKey(namedArgs, "max")) {
            String tempStr = MdlArg.getValue(namedArgs, "max");
            if (tempStr != null && !tempStr.isBlank()) {
                int tempInt = MdlUtil.parseInt(tempStr, MdlConst.INT_NULL);
                if (tempInt != MdlConst.INT_NULL) {
                    baseDir.setMaxDepth(tempInt);
                }
            }
        }

        if (baseDir.getMinDepth() > baseDir.getMaxDepth()) {
            isOk = false;
            logger.writeLine(MdlConst.LVL_E, "INVALID ARGUMENT : -min " + baseDir.getMinDepth() + " > -max : " + baseDir.getMaxDepth());
        }

        if (MdlArg.containsKey(namedArgs, "set") || MdlArg.containsKey(namedArgs, "exec")) {
            baseDir.setExec(true);
        }
        if (MdlArg.containsKey(namedArgs, "list")) {
            baseDir.setExec(false);
        }

        if (MdlArg.containsKey(namedArgs, "dir")) {
            baseDir.setTypeCode(MdlConst.INT_TYPE_ALL);
            baseDir.setShowTypeStr("a");
            baseDir.setModDir(true);
            baseDir.setModFile(true);
        }

        if (MdlArg.containsKey(namedArgs, "dironly")) {
            baseDir.setTypeCode(MdlConst.INT_TYPE_DIRECTORY);
            baseDir.setShowTypeStr("d");
            baseDir.setModDir(true);
            baseDir.setModFile(false);
        }

        String typeStr = MdlArg.getValue(namedArgs, "type");
        if (typeStr != null && !typeStr.isBlank()) {
            switch (typeStr.strip()) {
                case "f":
                    baseDir.setTypeCode(MdlConst.INT_TYPE_FILE);
                    baseDir.setShowTypeStr("f");
                    baseDir.setModDir(false);
                    baseDir.setModFile(true);
                    break;
                case "d":
                    baseDir.setTypeCode(MdlConst.INT_TYPE_DIRECTORY);
                    baseDir.setShowTypeStr("d");
                    baseDir.setModDir(true);
                    baseDir.setModFile(false);
                    break;
                default:
                    baseDir.setTypeCode(MdlConst.INT_TYPE_ALL);
                    baseDir.setShowTypeStr("a");
                    baseDir.setModDir(true);
                    baseDir.setModFile(true);
                    break;
            }
        }

        return isOk;
    }

    /**
     * 日付指定および日付取得方法オプションを解析して設定します。
     *
     * @param namedArgs コマンドライン引数マップ
     * @param currentOk これまでの解析成否状態
     * @return 解析に問題がなければ {@code true}、不正な日付指定の場合は {@code false}
     */
    private boolean parseDateOptions(Map<String, String> namedArgs, boolean currentOk) {
        boolean isOk = currentOk;

        if (MdlArg.containsKey(namedArgs, "name")) {
            baseDir.setGetDateByName(true);
            baseDir.setGetDateByDirName(false);
        }

        if (MdlArg.containsKey(namedArgs, "dirname")) {
            baseDir.setGetDateByName(true);
            baseDir.setGetDateByDirName(true);
        }

        if (MdlArg.containsKey(namedArgs, "dirnameonly")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(true);
        }

        if (MdlArg.containsKey(namedArgs, "pathname")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            baseDir.setModifiedDateStr(MdlDate.extractDateFromPath(baseDir.getPath()));
            if (baseDir.getModifiedDateStr().isEmpty()) {
                logger.writeLine(MdlConst.LVL_E, "-pathname IS NOT AVAILABLE : " + baseDir.getPath());
                isOk = false;
            }
        }

        if (MdlArg.containsKey(namedArgs, "spec")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            baseDir.setGetDateBySpecFName(true);
            String specStr = MdlArg.getValue(namedArgs, "spec");
            if (specStr != null && !specStr.isBlank()) {
                baseDir.setIncSpecsList(MdlUtil.parseCsvToList(baseDir.getIncSpecsList(), specStr));
            }
        }

        String dateStr = MdlArg.getValue(namedArgs, "date");
        if (dateStr != null && !dateStr.isBlank()) {
            baseDir.setModifiedDateStr(MdlDate.validateAndFormatDate(dateStr, true));
            if (baseDir.getModifiedDateStr().isEmpty()) {
                logger.writeLine(MdlConst.LVL_E, "PLEASE SPECIFY THE ARGUMENT : -date " + dateStr);
                isOk = false;
            }
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
        }

        if (MdlArg.containsKey(namedArgs, "now")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            String nowStr = MdlArg.getValue(namedArgs, "now");
            int tempInt = MdlUtil.parseInt(nowStr, MdlConst.INT_NULL);
            if (tempInt != MdlConst.INT_NULL) {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(tempInt), "yyyy/MM/dd HH:mm:ss"));
            } else {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss"));
            }
        }

        if (MdlArg.containsKey(namedArgs, "today")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            String todayStr = MdlArg.getValue(namedArgs, "today");
            int tempInt = MdlUtil.parseInt(todayStr, MdlConst.INT_NULL);
            if (tempInt != MdlConst.INT_NULL) {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(tempInt), "yyyy/MM/dd"));
            } else {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd"));
            }
        }

        if (MdlArg.containsKey(namedArgs, "tomorrow") || MdlArg.containsKey(namedArgs, "nextday")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(1), "yyyy/MM/dd"));
        }

        if (MdlArg.containsKey(namedArgs, "yesterday") || MdlArg.containsKey(namedArgs, "prevday")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(-1), "yyyy/MM/dd"));
        }

        if (MdlArg.containsKey(namedArgs, "term")) {
            baseDir.setGetDateByName(false);
            baseDir.setGetDateByDirName(false);
            String termStr = MdlArg.getValue(namedArgs, "term");
            int tempInt = MdlUtil.parseInt(termStr, MdlConst.INT_NULL);
            if (tempInt != MdlConst.INT_NULL) {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(tempInt), "yyyy/MM/dd"));
            } else {
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd"));
            }
        }

        if (MdlArg.containsKey(namedArgs, "creationtime")) {
            baseDir.setCreationTime(true);
            baseDir.setLastWriteTime(false);
        }

        if (MdlArg.containsKey(namedArgs, "lastwritetime")) {
            baseDir.setCreationTime(false);
            baseDir.setLastWriteTime(true);
        }

        if (baseDir.getModifiedDateStr().isEmpty()) {
            if (!baseDir.isGetDateByName() && !baseDir.isGetDateByDirName() && !baseDir.isGetDateBySpecFName()) {
                baseDir.setGetDateByName(false);
                baseDir.setGetDateByDirName(false);
                baseDir.setModifiedDateStr(MdlDate.getFormattedDate(LocalDateTime.now().plusDays(1), "yyyy/MM/dd"));
            }
        }

        return isOk;
    }

    /**
     * ファイル・ディレクトリの包含・除外フィルタオプションを設定します。
     */
    private void parseFilterOptions() {
        cmmnArgs.getFilterLists();
        baseDir.setIncFilesList(cmmnArgs.getIncFilesList());
        baseDir.setIncDirsList(cmmnArgs.getIncDirsList());
        baseDir.setExcFilesList(cmmnArgs.getExcFilesList());
        baseDir.setExcDirsList(cmmnArgs.getExcDirsList());
        baseDir.setRegIncBasename(cmmnArgs.isRegIncBasename());
        baseDir.setRegExcBasename(cmmnArgs.isRegExcBasename());
        baseDir.setDirFilterOr(cmmnArgs.isDirFilterOr());
        baseDir.setIncHitRecursive(cmmnArgs.isIncHitRecursive());
        baseDir.setExcHitRecursive(cmmnArgs.isExcHitRecursive());
    }

    /**
     * 日時閾値（-before, -after）オプションを解析して設定します。
     *
     * @param namedArgs コマンドライン引数マップ
     */
    private void parseDateTimeThresholds(Map<String, String> namedArgs) {
        if (MdlArg.containsKey(namedArgs, "before")) {
            String paramValue = MdlArg.getValue(namedArgs, "before");
            LocalDateTime dt = parseThresholdDateTime(paramValue, 19700101.0);
            if (dt != null) {
                baseDir.setBeforeTime(dt);
                baseDir.setBefore(true);
            }
        }

        if (MdlArg.containsKey(namedArgs, "after")) {
            String paramValue = MdlArg.getValue(namedArgs, "after");
            LocalDateTime dt = parseThresholdDateTime(paramValue, 10101.0);
            if (dt != null) {
                baseDir.setAfterTime(dt);
                baseDir.setAfter(true);
            }
        }
    }

    /**
     * 指定された日時文字列または相対日数パラメータを解析し、{@link LocalDateTime} に変換します。
     *
     * @param paramValue 解析対象の文字列（"now", "today", "yesterday", "tomorrow", 数値日数, yyyyMMdd等）
     * @param relativeDaysThreshold 相対日数判定の閾値
     * @return 解析された {@link LocalDateTime}。解析できない場合は {@code null}
     */
    private LocalDateTime parseThresholdDateTime(String paramValue, double relativeDaysThreshold) {
        if (paramValue == null || paramValue.isBlank()) {
            return null;
        }
        switch (paramValue.strip()) {
            case "now":
                return LocalDateTime.now();
            case "today":
                return LocalDate.now().atStartOfDay();
            case "lastday":
            case "yesterday":
                return LocalDate.now().minusDays(1).atStartOfDay();
            case "tomorrow":
            case "nextday":
                return LocalDate.now().plusDays(1).atStartOfDay();
            default:
                double parsedDbl = MdlUtil.parseDouble(paramValue, MdlConst.DBL_NULL);
                if (parsedDbl != MdlConst.DBL_NULL) {
                    if (parsedDbl < relativeDaysThreshold) {
                        long days = (long) parsedDbl;
                        return LocalDate.now().plusDays(days).atStartOfDay();
                    } else {
                        return MdlDate.parseDateTime(paramValue);
                    }
                }
                return null;
        }
    }

    /**
     * アプリケーションの使用方法（ヘルプメッセージ）をログに出力します。
     */
    public void showUsage() {
        String exeExt = MdlApp.isWindows() ? ".exe" : "";
        logger.writeLine(MdlConst.LVL_NONE, "");
        logger.writeLine(MdlConst.LVL_NONE, "Usage : " + baseDir.getExeBaseName() + exeExt + " -path <path> [Option] [Option]...");
        logger.writeLine(MdlConst.LVL_NONE, "");
        logger.writeLine(MdlConst.LVL_NONE, "Basic Option        : ");
        logger.writeLine(MdlConst.LVL_NONE, "   -path|-f path    : 日付更新対象  （現在値=" + baseDir.getPath() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -mode value      : 0:全て、1:作成日、2:更新日、3:作成日・更新日、4:アクセス日、5:作成日・アクセス日、6:更新日・アクセス日 （現在値=" + baseDir.getModeCode() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -set|-exec       : 日付更新実行  （現在値=" + baseDir.isExec() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "日付指定            :               （現在値=" + baseDir.getModifiedDateStr() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -date value      : 設定日付の指定（指定日時   例：" + MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd") + " 00:00:00）");
        logger.writeLine(MdlConst.LVL_NONE, "   -now   [-days]   : 設定日付の指定（現在の日時 例：" + MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd HH:mm:ss") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -today [-days]   : 設定日付の指定（本日の日付 例：" + MdlDate.getFormattedDate(LocalDateTime.now(), "yyyy/MM/dd") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -tomorrow        : 設定日付の指定（明日の日付 例：" + MdlDate.getFormattedDate(LocalDateTime.now().plusDays(1), "yyyy/MM/dd") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -yesterday       : 設定日付の指定（昨日の日付 例：" + MdlDate.getFormattedDate(LocalDateTime.now().plusDays(-1), "yyyy/MM/dd") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "日付取得方法指定    : ");
        logger.writeLine(MdlConst.LVL_NONE, "   -creationtime    : 設定日付の指定（作成日を取得）                        （現在値=" + baseDir.isCreationTime() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -lastwritetime   : 設定日付の指定（更新日を取得）                        （現在値=" + baseDir.isLastWriteTime() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -pathname        : 設定日付の指定（-path名の先頭から日付を検索・取得）");
        logger.writeLine(MdlConst.LVL_NONE, "   -name            : 設定日付の指定（ファイル名の先頭から日付を検索・取得）（現在値=" + ((baseDir.isGetDateByName() && !baseDir.isGetDateByDirName()) ? "true" : "false") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -dirname         : 設定日付の指定（ファイル名の先頭から->DIR名の後ろからの順番で日付を検索・取得）（現在値=" + ((baseDir.isGetDateByName() && baseDir.isGetDateByDirName()) ? "true" : "false") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -dirnameonly     : 設定日付の指定（DIR名の後ろから設定日付を検索・取得） （現在値=" + ((!baseDir.isGetDateByName() && baseDir.isGetDateByDirName()) ? "true" : "false") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -spec 正規表現   : 設定日付の指定（指示ファイル名から設定日付を取得）    （現在値=" + (baseDir.isGetDateBySpecFName() ? "[" + String.join("|", baseDir.getIncSpecsList()) + "]" : "false") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -check-date n    : 有効日付確認日                                        （現在値=" + baseDir.getCheckDate() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "日付更新対象        : ");
        logger.writeLine(MdlConst.LVL_NONE, "   -type f|d|a      : 変更対象(f:file | d:dir | a:all)（現在値=" + baseDir.getShowTypeStr() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -sym             : シンボリックリンク判定有効化     （現在値=" + baseDir.isSymLink() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "Advanced Option     : ");
        logger.writeLine(MdlConst.LVL_NONE, "   -force           : 既に設定済時の強制日付更新フラグ（現在値=" + baseDir.isForce() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -min  value      : 最小ディレクトリ階層            （現在値=" + baseDir.getMinDepth() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -max  value      : 最大ディレクトリ階層            （現在値=" + baseDir.getMaxDepth() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -if 正規表現     : 絞り込みファイル名(カンマ区切り) (例：\\.log$,\\.dat$）（現在値=[" + String.join("|", baseDir.getIncFilesList()) + "])");
        logger.writeLine(MdlConst.LVL_NONE, "   -id 正規表現     : 絞り込みディレクトリ名(カンマ区切り）                （現在値=[" + String.join("|", baseDir.getIncDirsList()) + "])");
        logger.writeLine(MdlConst.LVL_NONE, "   -xf 正規表現     : 除外ファイル名(カンマ区切り) (例：\\.exe$,\\.dll$）    （現在値=[" + String.join("|", baseDir.getExcFilesList()) + "])");
        logger.writeLine(MdlConst.LVL_NONE, "   -xd 正規表現     : 除外ディレクトリ名(カンマ区切り）                    （現在値=[" + String.join("|", baseDir.getExcDirsList()) + "])");
        logger.writeLine(MdlConst.LVL_NONE, "   -idorxd          : -id or -xdフラグ               （現在値=" + baseDir.isDirFilterOr() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -no-id-rec       : -id結果の階層下への非適用フラグ（現在値=" + !baseDir.isIncHitRecursive() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -no-xd-rec       : -xd結果の階層下への非適用フラグ（現在値=" + !baseDir.isExcHitRecursive() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -before yyyyMMdd : 更新日付が指定日以前            （現在値=" + (baseDir.isBefore() ? MdlDate.getFormattedDate(baseDir.getBeforeTime(), "yyyyMMdd") + "：" + MdlDate.getFormattedDate(baseDir.getBeforeTime(), "yyyy/MM/dd HH:mm:ss") : "") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -after  yyyyMMdd : 更新日付が指定日以降            （現在値=" + (baseDir.isAfter() ? MdlDate.getFormattedDate(baseDir.getAfterTime(), "yyyyMMdd") + "：" + MdlDate.getFormattedDate(baseDir.getAfterTime(), "yyyy/MM/dd HH:mm:ss") : "") + "）");
        logger.writeLine(MdlConst.LVL_NONE, "Other Option        : ");
        logger.writeLine(MdlConst.LVL_NONE, "   -v|-vv|-brief    : 冗長表示|簡素表示         （現在値=" + baseDir.getVerbose() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -check           : -list -v時の更新有無確認  （現在値=" + baseDir.isUpdateCheck() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -diff            : 更新のみ表示              （現在値=" + baseDir.isDiff() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -stacktrace      : 例外時STACKTRACE表示フラグ（現在値=" + baseDir.isStackTrace() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -echo-retcd      : 終了コード表示フラグ      （現在値=" + baseDir.isEchoRetcode() + "）");
        logger.writeLine(MdlConst.LVL_NONE, "   -console mode    : メッセージ表示 off|stdout|stderr");
        logger.writeLine(MdlConst.LVL_NONE, "   -ldir path       : ログ出力先ディレクトリパス（日付付ファイル名で出力）");
        logger.writeLine(MdlConst.LVL_NONE, "   -log  path       : ログ出力ファイルパス      （-ldirより優先）");
        logger.writeLine(MdlConst.LVL_NONE, "");
        logger.writeLine(MdlConst.LVL_NONE, "Return Code : " + MdlConst.LVL_I + ":SUCCESS / " + MdlConst.LVL_W + ":WARN / " + MdlConst.LVL_E + ":ERROR");
        logger.writeLine(MdlConst.LVL_NONE, "");
    }


}
