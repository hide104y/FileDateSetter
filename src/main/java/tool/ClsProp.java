package tool;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import tool.cmnclslib.mdl.MdlConst;

/**
 * 日付変更対象のパスおよび処理パラメータを保持する設定データクラスです。
 */
public class ClsProp {

    private String path = "";
    private boolean isBaseDir = false;
    private int modeCode = 0;
    private int typeCode = MdlConst.INT_TYPE_FILE;
    private String showTypeStr = "f";
    private String modifiedDateStr = "";
    private int diffLevel = 0;
    private int checkDate = 19700101;
    private long minDepth = 0;
    private long maxDepth = MdlConst.LNG_MAX;
    private boolean isExec = false;
    private boolean isModFile = true;
    private boolean isModDir = false;
    private boolean isGetDateByName = false;
    private boolean isGetDateByDirName = false;
    private boolean isGetDateBySpecFName = false;
    private boolean isCreationTime = false;
    private boolean isLastWriteTime = false;
    private boolean isRegIncBasename = false;
    private boolean isRegExcBasename = false;
    private boolean isIncHitRecursive = false;
    private boolean isExcHitRecursive = false;
    private boolean isDirFilterOr = false;
    private boolean isForce = false;
    private boolean isDiff = false;
    private boolean isDq = false;
    private boolean isUpdateCheck = false;
    private boolean isSymLink = false;
    private List<String> incFilesList = new ArrayList<>();
    private List<String> excFilesList = new ArrayList<>();
    private List<String> incDirsList = new ArrayList<>();
    private List<String> excDirsList = new ArrayList<>();
    private List<String> incSpecsList = new ArrayList<>();
    private boolean isEchoRetcode = false;
    private boolean isBefore = false;
    private boolean isAfter = false;
    private LocalDateTime beforeTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    private LocalDateTime afterTime = LocalDateTime.of(1, 1, 1, 0, 0, 0);
    private int verbose = 0;
    private boolean isStackTrace = false;
    private int returnCode = MdlConst.LVL_I;
    private boolean isUsage = false;
    private String exeBaseName = "";
    private String exeDir = "";

    /**
     * {@link ClsProp} クラスの新しいインスタンスを初期化します。
     */
    public ClsProp() {
    }

    /**
     * 対象パスを取得します。
     *
     * @return 対象パス
     */
    public String getPath() {
        return path;
    }

    /**
     * 対象パスを設定します。
     *
     * @param path 対象パス
     */
    public void setPath(String path) {
        this.path = path != null ? path : "";
    }

    /**
     * ベースディレクトリ判定フラグを取得します。
     *
     * @return ディレクトリの場合は {@code true}、ファイルの場合は {@code false}
     */
    public boolean isBaseDir() {
        return isBaseDir;
    }

    /**
     * ベースディレクトリ判定フラグを設定します。
     *
     * @param baseDir ディレクトリの場合は {@code true}
     */
    public void setBaseDir(boolean baseDir) {
        isBaseDir = baseDir;
    }

    /**
     * 日時更新処理モードコードを取得します。
     * 0:全て、1:作成日、2:更新日、3:作成日・更新日、4:アクセス日、5:作成日・アクセス日、6:更新日・アクセス日
     *
     * @return 処理モードコード
     */
    public int getModeCode() {
        return modeCode;
    }

    /**
     * 日時更新処理モードコードを設定します。
     *
     * @param modeCode 処理モードコード
     */
    public void setModeCode(int modeCode) {
        this.modeCode = modeCode;
    }

    /**
     * 変更対象種別コード（ファイル/ディレクトリ/両方）を取得します。
     *
     * @return 種別コード
     */
    public int getTypeCode() {
        return typeCode;
    }

    /**
     * 変更対象種別コードを設定します。
     *
     * @param typeCode 種別コード
     */
    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 変更対象種別文字列（"f", "d", "a"）を取得します。
     *
     * @return 対象種別文字列
     */
    public String getShowTypeStr() {
        return showTypeStr;
    }

    /**
     * 変更対象種別文字列を設定します。
     *
     * @param showTypeStr 対象種別文字列
     */
    public void setShowTypeStr(String showTypeStr) {
        this.showTypeStr = showTypeStr != null ? showTypeStr : "f";
    }

    /**
     * 設定する変更日付文字列を取得します。
     *
     * @return 変更日付文字列
     */
    public String getModifiedDateStr() {
        return modifiedDateStr;
    }

    /**
     * 設定する変更日付文字列を設定します。
     *
     * @param modifiedDateStr 変更日付文字列
     */
    public void setModifiedDateStr(String modifiedDateStr) {
        this.modifiedDateStr = modifiedDateStr != null ? modifiedDateStr : "";
    }

    /**
     * 差分表示レベルを取得します。
     *
     * @return 差分表示レベル
     */
    public int getDiffLevel() {
        return diffLevel;
    }

    /**
     * 差分表示レベルを設定します。
     *
     * @param diffLevel 差分表示レベル
     */
    public void setDiffLevel(int diffLevel) {
        this.diffLevel = diffLevel;
    }

    /**
     * 有効日付の確認しきい値（数値表現）を取得します。
     *
     * @return 有効日付確認数値
     */
    public int getCheckDate() {
        return checkDate;
    }

    /**
     * 有効日付の確認しきい値（数値表現）を設定します。
     *
     * @param checkDate 有効日付確認数値
     */
    public void setCheckDate(int checkDate) {
        this.checkDate = checkDate;
    }

    /**
     * 最小探索ディレクトリ階層深さを取得します。
     *
     * @return 最小階層深さ
     */
    public long getMinDepth() {
        return minDepth;
    }

    /**
     * 最小探索ディレクトリ階層深さを設定します。
     *
     * @param minDepth 最小階層深さ
     */
    public void setMinDepth(long minDepth) {
        this.minDepth = minDepth;
    }

    /**
     * 最大探索ディレクトリ階層深さを取得します。
     *
     * @return 最大階層深さ
     */
    public long getMaxDepth() {
        return maxDepth;
    }

    /**
     * 最大探索ディレクトリ階層深さを設定します。
     *
     * @param maxDepth 最大階層深さ
     */
    public void setMaxDepth(long maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * 日付変更処理の実行フラグを取得します。
     *
     * @return 実行する場合は {@code true}
     */
    public boolean isExec() {
        return isExec;
    }

    /**
     * 日付変更処理の実行フラグを設定します。
     *
     * @param exec 実行フラグ
     */
    public void setExec(boolean exec) {
        isExec = exec;
    }

    /**
     * ファイル日付変更対象フラグを取得します。
     *
     * @return ファイルを対象とする場合は {@code true}
     */
    public boolean isModFile() {
        return isModFile;
    }

    /**
     * ファイル日付変更対象フラグを設定します。
     *
     * @param modFile ファイル対象フラグ
     */
    public void setModFile(boolean modFile) {
        isModFile = modFile;
    }

    /**
     * ディレクトリ日付変更対象フラグを取得します。
     *
     * @return ディレクトリを対象とする場合は {@code true}
     */
    public boolean isModDir() {
        return isModDir;
    }

    /**
     * ディレクトリ日付変更対象フラグを設定します。
     *
     * @param modDir ディレクトリ対象フラグ
     */
    public void setModDir(boolean modDir) {
        isModDir = modDir;
    }

    /**
     * ファイル名からの日付検索・取得フラグを取得します。
     *
     * @return ファイル名から取得する場合は {@code true}
     */
    public boolean isGetDateByName() {
        return isGetDateByName;
    }

    /**
     * ファイル名からの日付検索・取得フラグを設定します。
     *
     * @param getDateByName ファイル名取得フラグ
     */
    public void setGetDateByName(boolean getDateByName) {
        isGetDateByName = getDateByName;
    }

    /**
     * ディレクトリ名からの日付検索・取得フラグを取得します。
     *
     * @return ディレクトリ名から取得する場合は {@code true}
     */
    public boolean isGetDateByDirName() {
        return isGetDateByDirName;
    }

    /**
     * ディレクトリ名からの日付検索・取得フラグを設定します。
     *
     * @param getDateByDirName ディレクトリ名取得フラグ
     */
    public void setGetDateByDirName(boolean getDateByDirName) {
        isGetDateByDirName = getDateByDirName;
    }

    /**
     * 指定パターン名からの日付検索・取得フラグを取得します。
     *
     * @return 指定パターンから取得する場合は {@code true}
     */
    public boolean isGetDateBySpecFName() {
        return isGetDateBySpecFName;
    }

    /**
     * 指定パターン名からの日付検索・取得フラグを設定します。
     *
     * @param getDateBySpecFName 指定パターン取得フラグ
     */
    public void setGetDateBySpecFName(boolean getDateBySpecFName) {
        isGetDateBySpecFName = getDateBySpecFName;
    }

    /**
     * 作成日時対象フラグを取得します。
     *
     * @return 作成日時を対象とする場合は {@code true}
     */
    public boolean isCreationTime() {
        return isCreationTime;
    }

    /**
     * 作成日時対象フラグを設定します。
     *
     * @param creationTime 作成日時対象フラグ
     */
    public void setCreationTime(boolean creationTime) {
        isCreationTime = creationTime;
    }

    /**
     * 更新日時対象フラグを取得します。
     *
     * @return 更新日時を対象とする場合は {@code true}
     */
    public boolean isLastWriteTime() {
        return isLastWriteTime;
    }

    /**
     * 更新日時対象フラグを設定します。
     *
     * @param lastWriteTime 更新日時対象フラグ
     */
    public void setLastWriteTime(boolean lastWriteTime) {
        isLastWriteTime = lastWriteTime;
    }

    /**
     * 正規表現ファイル名対象判定フラグを取得します。
     *
     * @return 対象判定に正規表現を使用する場合は {@code true}
     */
    public boolean isRegIncBasename() {
        return isRegIncBasename;
    }

    /**
     * 正規表現ファイル名対象判定フラグを設定します。
     *
     * @param regIncBasename 対象判定フラグ
     */
    public void setRegIncBasename(boolean regIncBasename) {
        isRegIncBasename = regIncBasename;
    }

    /**
     * 正規表現ファイル名除外判定フラグを取得します。
     *
     * @return 除外判定に正規表現を使用する場合は {@code true}
     */
    public boolean isRegExcBasename() {
        return isRegExcBasename;
    }

    /**
     * 正規表現ファイル名除外判定フラグを設定します。
     *
     * @param regExcBasename 除外判定フラグ
     */
    public void setRegExcBasename(boolean regExcBasename) {
        isRegExcBasename = regExcBasename;
    }

    /**
     * 対象ヒット時の再帰適用フラグを取得します。
     *
     * @return 再帰適用する場合は {@code true}
     */
    public boolean isIncHitRecursive() {
        return isIncHitRecursive;
    }

    /**
     * 対象ヒット時の再帰適用フラグを設定します。
     *
     * @param incHitRecursive 再帰適用フラグ
     */
    public void setIncHitRecursive(boolean incHitRecursive) {
        isIncHitRecursive = incHitRecursive;
    }

    /**
     * 除外ヒット時の再帰適用フラグを取得します。
     *
     * @return 再帰適用する場合は {@code true}
     */
    public boolean isExcHitRecursive() {
        return isExcHitRecursive;
    }

    /**
     * 除外ヒット時の再帰適用フラグを設定します。
     *
     * @param excHitRecursive 再帰適用フラグ
     */
    public void setExcHitRecursive(boolean excHitRecursive) {
        isExcHitRecursive = excHitRecursive;
    }

    /**
     * ディレクトリフィルタのOR条件適用フラグを取得します。
     *
     * @return OR条件を適用する場合は {@code true}
     */
    public boolean isDirFilterOr() {
        return isDirFilterOr;
    }

    /**
     * ディレクトリフィルタのOR条件適用フラグを設定します。
     *
     * @param dirFilterOr OR条件適用フラグ
     */
    public void setDirFilterOr(boolean dirFilterOr) {
        isDirFilterOr = dirFilterOr;
    }

    /**
     * 強制更新フラグを取得します。
     *
     * @return 強制更新する場合は {@code true}
     */
    public boolean isForce() {
        return isForce;
    }

    /**
     * 強制更新フラグを設定します。
     *
     * @param force 強制更新フラグ
     */
    public void setForce(boolean force) {
        isForce = force;
    }

    /**
     * 差分のみ表示フラグを取得します。
     *
     * @return 差分のみ表示する場合は {@code true}
     */
    public boolean isDiff() {
        return isDiff;
    }

    /**
     * 差分のみ表示フラグを設定します。
     *
     * @param diff 差分のみ表示フラグ
     */
    public void setDiff(boolean diff) {
        isDiff = diff;
    }

    /**
     * ダブルクォーテーション囲み出力フラグを取得します。
     *
     * @return 囲み出力を行う場合は {@code true}
     */
    public boolean isDq() {
        return isDq;
    }

    /**
     * ダブルクォーテーション囲み出力フラグを設定します。
     *
     * @param dq 囲み出力フラグ
     */
    public void setDq(boolean dq) {
        isDq = dq;
    }

    /**
     * 更新有無確認フラグを取得します。
     *
     * @return 確認を行う場合は {@code true}
     */
    public boolean isUpdateCheck() {
        return isUpdateCheck;
    }

    /**
     * 更新有無確認フラグを設定します。
     *
     * @param updateCheck 確認フラグ
     */
    public void setUpdateCheck(boolean updateCheck) {
        isUpdateCheck = updateCheck;
    }

    /**
     * シンボリックリンク判定有効化フラグを取得します。
     *
     * @return 有効化する場合は {@code true}
     */
    public boolean isSymLink() {
        return isSymLink;
    }

    /**
     * シンボリックリンク判定有効化フラグを設定します。
     *
     * @param symLink 有効化フラグ
     */
    public void setSymLink(boolean symLink) {
        isSymLink = symLink;
    }

    /**
     * 対象ファイル名フィルタパターンリストを取得します。
     *
     * @return フィルタパターンリスト
     */
    public List<String> getIncFilesList() {
        return incFilesList;
    }

    /**
     * 対象ファイル名フィルタパターンリストを設定します。
     *
     * @param incFilesList フィルタパターンリスト
     */
    public void setIncFilesList(List<String> incFilesList) {
        this.incFilesList = incFilesList != null ? new ArrayList<>(incFilesList) : new ArrayList<>();
    }

    /**
     * 除外ファイル名フィルタパターンリストを取得します。
     *
     * @return 除外パターンリスト
     */
    public List<String> getExcFilesList() {
        return excFilesList;
    }

    /**
     * 除外ファイル名フィルタパターンリストを設定します。
     *
     * @param excFilesList 除外パターンリスト
     */
    public void setExcFilesList(List<String> excFilesList) {
        this.excFilesList = excFilesList != null ? new ArrayList<>(excFilesList) : new ArrayList<>();
    }

    /**
     * 対象ディレクトリ名フィルタパターンリストを取得します。
     *
     * @return フィルタパターンリスト
     */
    public List<String> getIncDirsList() {
        return incDirsList;
    }

    /**
     * 対象ディレクトリ名フィルタパターンリストを設定します。
     *
     * @param incDirsList フィルタパターンリスト
     */
    public void setIncDirsList(List<String> incDirsList) {
        this.incDirsList = incDirsList != null ? new ArrayList<>(incDirsList) : new ArrayList<>();
    }

    /**
     * 除外ディレクトリ名フィルタパターンリストを取得します。
     *
     * @return 除外パターンリスト
     */
    public List<String> getExcDirsList() {
        return excDirsList;
    }

    /**
     * 除外ディレクトリ名フィルタパターンリストを設定します。
     *
     * @param excDirsList 除外パターンリスト
     */
    public void setExcDirsList(List<String> excDirsList) {
        this.excDirsList = excDirsList != null ? new ArrayList<>(excDirsList) : new ArrayList<>();
    }

    /**
     * 指定ファイル名フィルタパターンリストを取得します。
     *
     * @return 指定パターンリスト
     */
    public List<String> getIncSpecsList() {
        return incSpecsList;
    }

    /**
     * 指定ファイル名フィルタパターンリストを設定します。
     *
     * @param incSpecsList 指定パターンリスト
     */
    public void setIncSpecsList(List<String> incSpecsList) {
        this.incSpecsList = incSpecsList != null ? new ArrayList<>(incSpecsList) : new ArrayList<>();
    }

    /**
     * 終了コード表示フラグを取得します。
     *
     * @return 表示する場合は {@code true}
     */
    public boolean isEchoRetcode() {
        return isEchoRetcode;
    }

    /**
     * 終了コード表示フラグを設定します。
     *
     * @param echoRetcode 表示フラグ
     */
    public void setEchoRetcode(boolean echoRetcode) {
        this.isEchoRetcode = echoRetcode;
    }

    /**
     * 以前の日付閾値判定フラグを取得します。
     *
     * @return 判定する場合は {@code true}
     */
    public boolean isBefore() {
        return isBefore;
    }

    /**
     * 以前の日付閾値判定フラグを設定します。
     *
     * @param before 判定フラグ
     */
    public void setBefore(boolean before) {
        isBefore = before;
    }

    /**
     * 以後の日付閾値判定フラグを取得します。
     *
     * @return 判定する場合は {@code true}
     */
    public boolean isAfter() {
        return isAfter;
    }

    /**
     * 以後の日付閾値判定フラグを設定します。
     *
     * @param after 判定フラグ
     */
    public void setAfter(boolean after) {
        isAfter = after;
    }

    /**
     * 以前の日付閾値を取得します。
     *
     * @return 以前の日付閾値
     */
    public LocalDateTime getBeforeTime() {
        return beforeTime;
    }

    /**
     * 以前の日付閾値を設定します。
     *
     * @param beforeTime 以前の日付閾値
     */
    public void setBeforeTime(LocalDateTime beforeTime) {
        this.beforeTime = beforeTime;
    }

    /**
     * 以後の日付閾値を取得します。
     *
     * @return 以後の日付閾値
     */
    public LocalDateTime getAfterTime() {
        return afterTime;
    }

    /**
     * 以後の日付閾値を設定します。
     *
     * @param afterTime 以後の日付閾値
     */
    public void setAfterTime(LocalDateTime afterTime) {
        this.afterTime = afterTime;
    }

    /**
     * ログ冗長度（Verbose）を取得します。
     *
     * @return 冗長度レベル
     */
    public int getVerbose() {
        return verbose;
    }

    /**
     * ログ冗長度（Verbose）を設定します。
     *
     * @param verbose 冗長度レベル
     */
    public void setVerbose(int verbose) {
        this.verbose = verbose;
    }

    /**
     * スタックトレース表示フラグを取得します。
     *
     * @return 表示する場合は {@code true}
     */
    public boolean isStackTrace() {
        return isStackTrace;
    }

    /**
     * スタックトレース表示フラグを設定します。
     *
     * @param stackTrace 表示フラグ
     */
    public void setStackTrace(boolean stackTrace) {
        isStackTrace = stackTrace;
    }

    /**
     * アプリケーションの終了コードを取得します。
     *
     * @return 終了コード
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * アプリケーションの終了コードを設定します。
     *
     * @param returnCode 終了コード
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * ヘルプ・使用方法の表示要求フラグを取得します。
     *
     * @return 表示要求フラグ
     */
    public boolean isUsage() {
        return isUsage;
    }

    /**
     * ヘルプ・使用方法の表示要求フラグを設定します。
     *
     * @param usage 表示要求フラグ
     */
    public void setUsage(boolean usage) {
        isUsage = usage;
    }

    /**
     * 実行ファイルのベース名を取得します。
     *
     * @return 実行ファイルベース名
     */
    public String getExeBaseName() {
        return exeBaseName;
    }

    /**
     * 実行ファイルのベース名を設定します。
     *
     * @param exeBaseName 実行ファイルベース名
     */
    public void setExeBaseName(String exeBaseName) {
        this.exeBaseName = exeBaseName != null ? exeBaseName : "";
    }

    /**
     * 実行ファイルのディレクトリパスを取得します。
     *
     * @return 実行ファイルディレクトリパス
     */
    public String getExeDir() {
        return exeDir;
    }

    /**
     * 実行ファイルのディレクトリパスを設定します。
     *
     * @param exeDir 実行ファイルディレクトリパス
     */
    public void setExeDir(String exeDir) {
        this.exeDir = exeDir != null ? exeDir : "";
    }
}
