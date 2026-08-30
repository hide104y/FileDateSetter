package tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import tool.cmnclslib.cls.ClsFsDate;
import tool.cmnclslib.cls.ClsLogger;
import tool.cmnclslib.mdl.MdlConst;
import tool.cmnclslib.mdl.MdlDate;
import tool.cmnclslib.mdl.MdlFile;

/**
 * ファイルおよびディレクトリの日付検索・更新処理を実行するクラスです。
 */
public class ClsFind {

    private final ClsLogger logger;
    private final ClsAppArg appArg;
    private final ClsFsDate fsDate;

    private long errorCountDirList = 0;
    private long errorCountFileList = 0;
    private long errorCountFileMod = 0;
    private long successCountFileMod = 0;
    private long errorCountDirMod = 0;
    private long successCountDirMod = 0;
    private long skipCountFile = 0;
    private long totalCountFile = 0;
    private long noTargetCountFile = 0;
    private long skipCountDir = 0;
    private long totalCountDir = 0;
    private long noTargetCountDir = 0;

    /**
     * {@link ClsFind} クラスの新しいインスタンスを初期化します。
     *
     * @param logger ログ出力を行う {@link ClsLogger} オブジェクト
     * @param appArg 実行引数および設定情報を保持する {@link ClsAppArg} オブジェクト
     * @param fsDate ファイルシステムの日付を設定・操作する {@link ClsFsDate} オブジェクト
     */
    public ClsFind(ClsLogger logger, ClsAppArg appArg, ClsFsDate fsDate) {
        this.logger = Objects.requireNonNull(logger, "logger must not be null");
        this.appArg = Objects.requireNonNull(appArg, "appArg must not be null");
        this.fsDate = Objects.requireNonNull(fsDate, "fsDate must not be null");
    }

    /**
     * サブディレクトリ一覧取得時のエラー件数を取得します。
     *
     * @return サブディレクトリ一覧取得エラー件数
     */
    public long getErrorCountDirList() {
        return errorCountDirList;
    }

    /**
     * サブディレクトリ一覧取得時のエラー件数を設定します。
     *
     * @param errorCountDirList サブディレクトリ一覧取得エラー件数
     */
    public void setErrorCountDirList(long errorCountDirList) {
        this.errorCountDirList = errorCountDirList;
    }

    /**
     * ファイル一覧取得時のエラー件数を取得します。
     *
     * @return ファイル一覧取得エラー件数
     */
    public long getErrorCountFileList() {
        return errorCountFileList;
    }

    /**
     * ファイル一覧取得時のエラー件数を設定します。
     *
     * @param errorCountFileList ファイル一覧取得エラー件数
     */
    public void setErrorCountFileList(long errorCountFileList) {
        this.errorCountFileList = errorCountFileList;
    }

    /**
     * ファイル日付更新失敗件数を取得します。
     *
     * @return ファイル日付更新失敗件数
     */
    public long getErrorCountFileMod() {
        return errorCountFileMod;
    }

    /**
     * ファイル日付更新失敗件数を設定します。
     *
     * @param errorCountFileMod ファイル日付更新失敗件数
     */
    public void setErrorCountFileMod(long errorCountFileMod) {
        this.errorCountFileMod = errorCountFileMod;
    }

    /**
     * ファイル日付更新成功件数を取得します。
     *
     * @return ファイル日付更新成功件数
     */
    public long getSuccessCountFileMod() {
        return successCountFileMod;
    }

    /**
     * ファイル日付更新成功件数を設定します。
     *
     * @param successCountFileMod ファイル日付更新成功件数
     */
    public void setSuccessCountFileMod(long successCountFileMod) {
        this.successCountFileMod = successCountFileMod;
    }

    /**
     * ディレクトリ日付更新失敗件数を取得します。
     *
     * @return ディレクトリ日付更新失敗件数
     */
    public long getErrorCountDirMod() {
        return errorCountDirMod;
    }

    /**
     * ディレクトリ日付更新失敗件数を設定します。
     *
     * @param errorCountDirMod ディレクトリ日付更新失敗件数
     */
    public void setErrorCountDirMod(long errorCountDirMod) {
        this.errorCountDirMod = errorCountDirMod;
    }

    /**
     * ディレクトリ日付更新成功件数を取得します。
     *
     * @return ディレクトリ日付更新成功件数
     */
    public long getSuccessCountDirMod() {
        return successCountDirMod;
    }

    /**
     * ディレクトリ日付更新成功件数を設定します。
     *
     * @param successCountDirMod ディレクトリ日付更新成功件数
     */
    public void setSuccessCountDirMod(long successCountDirMod) {
        this.successCountDirMod = successCountDirMod;
    }

    /**
     * 変更不要（更新スキップ）となったファイル件数を取得します。
     *
     * @return ファイルスキップ件数
     */
    public long getSkipCountFile() {
        return skipCountFile;
    }

    /**
     * 変更不要（更新スキップ）となったファイル件数を設定します。
     *
     * @param skipCountFile ファイルスキップ件数
     */
    public void setSkipCountFile(long skipCountFile) {
        this.skipCountFile = skipCountFile;
    }

    /**
     * 走査対象となった総ファイル件数を取得します。
     *
     * @return 総ファイル件数
     */
    public long getTotalCountFile() {
        return totalCountFile;
    }

    /**
     * 走査対象となった総ファイル件数を設定します。
     *
     * @param totalCountFile 総ファイル件数
     */
    public void setTotalCountFile(long totalCountFile) {
        this.totalCountFile = totalCountFile;
    }

    /**
     * 日付取得対象外となったファイル件数を取得します。
     *
     * @return 対象外ファイル件数
     */
    public long getNoTargetCountFile() {
        return noTargetCountFile;
    }

    /**
     * 日付取得対象外となったファイル件数を設定します。
     *
     * @param noTargetCountFile 対象外ファイル件数
     */
    public void setNoTargetCountFile(long noTargetCountFile) {
        this.noTargetCountFile = noTargetCountFile;
    }

    /**
     * 変更不要（更新スキップ）となったディレクトリ件数を取得します。
     *
     * @return ディレクトリスキップ件数
     */
    public long getSkipCountDir() {
        return skipCountDir;
    }

    /**
     * 変更不要（更新スキップ）となったディレクトリ件数を設定します。
     *
     * @param skipCountDir ディレクトリスキップ件数
     */
    public void setSkipCountDir(long skipCountDir) {
        this.skipCountDir = skipCountDir;
    }

    /**
     * 走査対象となった総ディレクトリ件数を取得します。
     *
     * @return 総ディレクトリ件数
     */
    public long getTotalCountDir() {
        return totalCountDir;
    }

    /**
     * 走査対象となった総ディレクトリ件数を設定します。
     *
     * @param totalCountDir 総ディレクトリ件数
     */
    public void setTotalCountDir(long totalCountDir) {
        this.totalCountDir = totalCountDir;
    }

    /**
     * 日付取得対象外となったディレクトリ件数を取得します。
     *
     * @return 対象外ディレクトリ件数
     */
    public long getNoTargetCountDir() {
        return noTargetCountDir;
    }

    /**
     * 日付取得対象外となったディレクトリ件数を設定します。
     *
     * @param noTargetCountDir 対象外ディレクトリ件数
     */
    public void setNoTargetCountDir(long noTargetCountDir) {
        this.noTargetCountDir = noTargetCountDir;
    }

    /**
     * 設定されたパラメータに基づき、指定パスに対するファイル・ディレクトリの日付更新処理を実行します。
     *
     * @return 処理中にエラーが発生しなかった場合は {@link MdlConst#LVL_I}、1件以上のエラーが発生した場合は {@link MdlConst#LVL_E}。
     */
    public int execute() {
        if (appArg.isBaseDir()) {
            processDirectoryRecursive(appArg.getPath(), "", 0, 0);
        } else {
            updateTargetDate(appArg.getPath(), MdlFile.PATH_IS_FILE);
        }

        return ((errorCountDirList + errorCountFileList + errorCountFileMod + errorCountDirMod) == 0
                ? MdlConst.LVL_I
                : MdlConst.LVL_E);
    }

    /**
     * 指定されたディレクトリを再帰的に走査し、フィルタ条件に従って日付変更またはサブディレクトリの処理を実行します。
     *
     * @param currentPath 走査対象の絶対パス
     * @param relativePath ルートからの相対パス
     * @param currentDepth 現在のディレクトリ階層の深さ
     * @param previousEffective 親ディレクトリから引き継いだ有効判定フラグ
     * @return 正常に走査処理が完了した場合は {@code true}、途中でエラーが発生した場合は {@code false}
     */
    private boolean processDirectoryRecursive(String currentPath, String relativePath, long currentDepth, int previousEffective) {
        if (currentDepth > appArg.getMaxDepth()) {
            return true;
        }

        boolean result = true;
        boolean isSymLink = false;
        int currentEffective = previousEffective;

        if (currentDepth >= appArg.getMinDepth()) {
            try {
                if (appArg.isSymLink()) {
                    isSymLink = MdlFile.isSymlink(currentPath);
                }

                if (appArg.getVerbose() > 6) {
                    logger.writeLine(MdlConst.LVL_NONE, "■■■[recursive()][ParentDir][" + currentDepth + "] PATH=" + relativePath + " ■■■");
                    logger.writeLine(MdlConst.LVL_NONE, "isSymLink      = " + isSymLink);
                    logger.writeLine(MdlConst.LVL_NONE, "previousEffective     = " + previousEffective);
                    logger.writeLine(MdlConst.LVL_NONE, "BlnIsIncHitRecursive = " + appArg.isIncHitRecursive());
                    logger.writeLine(MdlConst.LVL_NONE, "BlnIsExcHitRecursive = " + appArg.isExcHitRecursive());
                    logger.writeLine(MdlConst.LVL_NONE, "BlnIsDirFilterOr     = " + appArg.isDirFilterOr());
                }

                int filterCheck = MdlFile.evaluatePathFilterCode(relativePath, appArg.isRegIncBasename(), appArg.isRegExcBasename(),
                        appArg.getIncDirsList(), appArg.getExcDirsList(), appArg.isDirFilterOr(), appArg.getVerbose());
                currentEffective = MdlFile.combineFilterFlags(currentEffective, filterCheck, appArg.isDirFilterOr(),
                        appArg.isIncHitRecursive(), appArg.isExcHitRecursive());

                if (appArg.getVerbose() > 6) {
                    logger.writeLine(MdlConst.LVL_NONE, "filterCheck      = " + filterCheck);
                    logger.writeLine(MdlConst.LVL_NONE, "currentEffective  = " + currentEffective);
                }

                if (currentDepth > 0 && currentEffective > 1 && appArg.isExcHitRecursive()) {
                    return true;
                }

                if (currentEffective == 1) {
                    if (appArg.isModDir()) {
                        updateTargetDate(currentPath, MdlFile.PATH_IS_DIRECTORY);
                    }

                    if (!isSymLink) {
                        if (appArg.isGetDateBySpecFName()) {
                            appArg.setModifiedDateStr("");
                            List<String> files = listTopDirectoryFiles(currentPath);
                            for (String targetFilePath : files) {
                                if (MdlFile.isPathFilterMatched(targetFilePath, true, true, appArg.getIncSpecsList(), appArg.getExcFilesList())) {
                                    String dateStr = MdlDate.extractDateFromPath(targetFilePath, true, appArg.getCheckDate());
                                    if (dateStr != null && !dateStr.isEmpty()) {
                                        appArg.setModifiedDateStr(dateStr);
                                        if (appArg.isModDir()) {
                                            updateTargetDate(currentPath, MdlFile.PATH_IS_DIRECTORY);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        if (appArg.isModFile()) {
                            processFilesInDirectory(currentPath);
                        }
                        if (appArg.isGetDateBySpecFName()) {
                            appArg.setModifiedDateStr("");
                        }
                    }
                }

                if (isSymLink) {
                    return result;
                }
            } catch (Exception ex) {
                logger.writeLine(MdlConst.LVL_NONE, "EXCEPTION : ClsFind.recursive() 1 : " + ex.getMessage() + " : " + relativePath);
                if (appArg.isStackTrace()) {
                    logger.writeLine(MdlConst.LVL_NONE, "");
                    for (StackTraceElement elem : ex.getStackTrace()) {
                        logger.writeLine(MdlConst.LVL_NONE, elem.toString());
                    }
                    logger.writeLine(MdlConst.LVL_NONE, "");
                }
            }
        }

        return processSubDirectories(currentPath, relativePath, currentDepth, currentEffective) && result;
    }

    /**
     * 指定されたディレクトリ内の直下サブディレクトリを列挙し、再帰呼び出しを行います。
     *
     * @param currentPath 走査対象の絶対パス
     * @param relativePath 親から引き継いだ相対パス
     * @param currentDepth 現在の深さ
     * @param currentEffective 有効フラグ
     * @return すべてのサブディレクトリの処理が成功した場合は {@code true}、一部でエラーが発生した場合は {@code false}
     */
    private boolean processSubDirectories(String currentPath, String relativePath, long currentDepth, int currentEffective) {
        boolean result = true;
        try {
            List<String> subDirectories = listTopDirectorySubDirs(currentPath);
            for (String directoryPath : subDirectories) {
                try {
                    String subDirectoryName = Paths.get(directoryPath).getFileName().toString();
                    String nextRelativePath = relativePath.isEmpty()
                            ? subDirectoryName
                            : relativePath + File.separator + subDirectoryName;

                    if (!processDirectoryRecursive(directoryPath, nextRelativePath, currentDepth + 1, currentEffective)) {
                        result = false;
                    }
                } catch (Exception ex) {
                    logger.writeLine(MdlConst.LVL_NONE, "EXCEPTION : ClsFind.recursive() 2 : " + ex.getMessage() + " : " + relativePath);
                    if (appArg.isStackTrace()) {
                        logger.writeLine(MdlConst.LVL_NONE, "");
                        for (StackTraceElement elem : ex.getStackTrace()) {
                            logger.writeLine(MdlConst.LVL_NONE, elem.toString());
                        }
                        logger.writeLine(MdlConst.LVL_NONE, "");
                    }
                }
            }
        } catch (Exception ex) {
            errorCountDirList++;
            logger.writeLine(MdlConst.LVL_E, "EXCEPTION : GetDirectories : " + currentPath + " : " + ex.getMessage());
            if (appArg.isStackTrace()) {
                logger.writeLine(MdlConst.LVL_NONE, "");
                for (StackTraceElement elem : ex.getStackTrace()) {
                    logger.writeLine(MdlConst.LVL_NONE, elem.toString());
                }
                logger.writeLine(MdlConst.LVL_NONE, "");
            }
        }
        return result;
    }

    /**
     * 指定されたディレクトリ直下に存在するファイルを列挙し、ファイルフィルタに適合した対象の日付更新処理を呼び出します。
     *
     * @param currentPath 処理対象ファイルの存在するディレクトリパス
     */
    private void processFilesInDirectory(String currentPath) {
        try {
            List<String> files = listTopDirectoryFiles(currentPath);
            for (String filePath : files) {
                if (MdlFile.isPathFilterMatched(filePath, true, true, appArg.getIncFilesList(), appArg.getExcFilesList())) {
                    if (MdlFile.isValidFileDateTime(filePath, appArg.isBefore(), appArg.getBeforeTime(), appArg.isAfter(), appArg.getAfterTime())) {
                        if (appArg.getVerbose() > 6) {
                            logger.writeLine(MdlConst.LVL_NONE, "[H I T] " + filePath);
                        }
                        updateTargetDate(filePath, MdlFile.PATH_IS_FILE);
                    } else {
                        if (appArg.getVerbose() > 6) {
                            logger.writeLine(MdlConst.LVL_NONE, "[NOHIT] " + filePath);
                        }
                    }
                } else {
                    if (appArg.getVerbose() > 6) {
                        logger.writeLine(MdlConst.LVL_NONE, "[NOHIT] " + filePath);
                    }
                }
            }
        } catch (Exception ex) {
            errorCountFileList++;
            logger.writeLine(MdlConst.LVL_E, "EXCEPTION : " + currentPath + " : " + ex.getMessage());
            if (appArg.isStackTrace()) {
                logger.writeLine(MdlConst.LVL_NONE, "");
                for (StackTraceElement elem : ex.getStackTrace()) {
                    logger.writeLine(MdlConst.LVL_NONE, elem.toString());
                }
                logger.writeLine(MdlConst.LVL_NONE, "");
            }
        }
    }

    /**
     * 指定ディレクトリ直下のファイル一覧を取得します。
     *
     * @param dirPath ディレクトリパス
     * @return ファイル絶対パスのリスト
     */
    private List<String> listTopDirectoryFiles(String dirPath) {
        List<String> resultList = new ArrayList<>();
        Path path = Paths.get(dirPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return resultList;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    resultList.add(entry.toAbsolutePath().toString());
                }
            }
        } catch (IOException | SecurityException e) {
            errorCountFileList++;
        }
        resultList.sort(null);
        return resultList;
    }

    /**
     * 指定ディレクトリ直下のサブディレクトリ一覧を取得します。
     *
     * @param dirPath ディレクトリパス
     * @return サブディレクトリ絶対パスのリスト
     */
    private List<String> listTopDirectorySubDirs(String dirPath) {
        List<String> resultList = new ArrayList<>();
        Path path = Paths.get(dirPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return resultList;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    resultList.add(entry.toAbsolutePath().toString());
                }
            }
        } catch (IOException | SecurityException e) {
            errorCountDirList++;
        }
        resultList.sort(null);
        return resultList;
    }

    /**
     * 指定されたパスの作成日時を取得します。
     *
     * @param path 対象パス
     * @return 作成日時。取得できない場合は {@code null}
     */
    private LocalDateTime getCreationTime(String path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
            return LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
        } catch (IOException | SecurityException e) {
            return null;
        }
    }

    /**
     * 指定されたパスの更新日時を取得します。
     *
     * @param path 対象パス
     * @return 更新日時。取得できない場合は {@code null}
     */
    private LocalDateTime getLastWriteTime(String path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
            return LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
        } catch (IOException | SecurityException e) {
            return null;
        }
    }

    /**
     * 指定されたパス（ファイルまたはディレクトリ）の日付解析および更新処理を行います。
     *
     * @param targetPath 対象のファイルまたはディレクトリの絶対パス
     * @param pathType パス種別（{@link MdlFile#PATH_IS_DIRECTORY} または {@link MdlFile#PATH_IS_FILE}）
     */
    private void updateTargetDate(String targetPath, int pathType) {
        boolean isSuccess = false;
        String modifiedDate = "";
        int returnCode = 0;
        String pathTypeStr = (pathType == MdlFile.PATH_IS_DIRECTORY ? "D" : "F");
        String displayPath = appArg.isDq() ? "\"" + targetPath + "\"" : targetPath;
        String targetLastWriteTime = "                   ";

        if (pathType == MdlFile.PATH_IS_DIRECTORY) {
            totalCountDir++;
        } else {
            totalCountFile++;
        }

        // ファイル情報取得
        if (appArg.getVerbose() > 2) {
            try {
                LocalDateTime lwt = getLastWriteTime(targetPath);
                if (lwt != null) {
                    targetLastWriteTime = MdlDate.getFormattedDate(lwt, "yyyy/MM/dd HH:mm:ss");
                }
            } catch (Exception ignored) {
            }
        }

        // 更新日取得
        if (appArg.isGetDateByName()) {
            modifiedDate = MdlDate.extractDateFromPath(targetPath, true, appArg.getCheckDate());
            if ((modifiedDate == null || modifiedDate.isEmpty()) && appArg.isGetDateByDirName()) {
                String parentDir = MdlFile.getDirectoryPath(targetPath);
                modifiedDate = MdlDate.extractDateFromStringReverse(parentDir, true, appArg.getCheckDate());
            }
        } else {
            if (appArg.isGetDateByDirName()) {
                String parentDir = MdlFile.getDirectoryPath(targetPath);
                modifiedDate = MdlDate.extractDateFromStringReverse(parentDir, true, appArg.getCheckDate());
            } else if (appArg.isCreationTime()) {
                LocalDateTime ct = getCreationTime(targetPath);
                if (ct != null) {
                    modifiedDate = MdlDate.getFormattedDate(ct, "yyyy/MM/dd HH:mm:ss");
                }
            } else if (appArg.isLastWriteTime()) {
                LocalDateTime lwt = getLastWriteTime(targetPath);
                if (lwt != null) {
                    modifiedDate = MdlDate.getFormattedDate(lwt, "yyyy/MM/dd HH:mm:ss");
                }
            } else {
                modifiedDate = appArg.getModifiedDateStr();
            }
        }

        // 更新対象の場合（更新日が取得できた場合）
        if (modifiedDate != null && !modifiedDate.isEmpty()) {
            // 実行フラグがONの場合
            if (appArg.isExec()) {
                returnCode = fsDate.setDateCore(targetPath, modifiedDate, appArg.getModeCode(), pathType, false, appArg.isForce(), true);
                if (returnCode > -1) {
                    isSuccess = true;
                    boolean isShow = !(returnCode == 0 && appArg.isDiff());
                    String status = (returnCode == 0 ? "---" : "UPD");
                    if (isShow) {
                        if (appArg.getVerbose() > 2) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%s=>%s][%03d] %s", status, pathTypeStr, targetLastWriteTime, modifiedDate, returnCode, displayPath));
                        } else if (appArg.getVerbose() == 2) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%s][%03d] %s", status, pathTypeStr, modifiedDate, returnCode, displayPath));
                        } else if (appArg.getVerbose() == 1) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%03d] %s", status, pathTypeStr, returnCode, displayPath));
                        } else if (appArg.getVerbose() == 0) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s] %s", status, pathTypeStr, displayPath));
                        } else if (appArg.getVerbose() == -1) {
                            String shortStatus = (returnCode == 0 ? "-" : "U");
                            logger.writeLine(MdlConst.LVL_NONE, shortStatus + " " + pathTypeStr + " " + displayPath);
                        } else {
                            logger.writeLine(MdlConst.LVL_NONE, displayPath);
                        }
                    }
                } else {
                    if (appArg.getVerbose() >= -1) {
                        if (appArg.getVerbose() > 2) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[ERR][%s][%s=>%s][---] %s", pathTypeStr, targetLastWriteTime, modifiedDate, displayPath));
                        } else if (appArg.getVerbose() == 2) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[ERR][%s][%s][---] %s", pathTypeStr, modifiedDate, displayPath));
                        } else if (appArg.getVerbose() == 1) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[ERR][%s][---] %s", pathTypeStr, displayPath));
                        } else if (appArg.getVerbose() == 0) {
                            logger.writeLine(MdlConst.LVL_NONE, String.format("[ERR][%s] %s", pathTypeStr, displayPath));
                        } else if (appArg.getVerbose() == -1) {
                            logger.writeLine(MdlConst.LVL_NONE, "E " + pathTypeStr + " " + displayPath);
                        } else {
                            logger.writeLine(MdlConst.LVL_NONE, displayPath);
                        }
                    }
                }
            } else {
                // -listが指定されている場合
                isSuccess = true;
                boolean isShow = true;
                String result = "---";
                String status = "-U-";
                if (appArg.isUpdateCheck()) {
                    returnCode = fsDate.setDateCore(targetPath, modifiedDate, appArg.getModeCode(), pathType, false, appArg.isForce(), false);
                    result = String.format("%03d", returnCode);
                    if (returnCode == 0 && appArg.isDiff()) {
                        isShow = false;
                    }
                    status = (returnCode == 0 ? "---" : "-U-");
                }
                if (isShow) {
                    if (appArg.getVerbose() > 2) {
                        logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%s=>%s][%s] %s", status, pathTypeStr, targetLastWriteTime, modifiedDate, result, displayPath));
                    } else if (appArg.getVerbose() == 2) {
                        logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%s][%s] %s", status, pathTypeStr, modifiedDate, result, displayPath));
                    } else if (appArg.getVerbose() == 1) {
                        logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s][%s] %s", status, pathTypeStr, result, displayPath));
                    } else if (appArg.getVerbose() == 0) {
                        logger.writeLine(MdlConst.LVL_NONE, String.format("[%s][%s] %s", status, pathTypeStr, displayPath));
                    } else if (appArg.getVerbose() == -1) {
                        String shortStatus = (returnCode == 0 ? "-" : "U");
                        logger.writeLine(MdlConst.LVL_NONE, shortStatus + " " + pathTypeStr + " " + displayPath);
                    } else {
                        logger.writeLine(MdlConst.LVL_NONE, displayPath);
                    }
                }
            }

            // カウンタインクリメント
            if (isSuccess) {
                if (returnCode == 0) {
                    if (pathType == MdlFile.PATH_IS_DIRECTORY) {
                        skipCountDir++;
                    } else {
                        skipCountFile++;
                    }
                } else {
                    if (pathType == MdlFile.PATH_IS_DIRECTORY) {
                        successCountDirMod++;
                    } else {
                        successCountFileMod++;
                    }
                }
            } else {
                if (pathType == MdlFile.PATH_IS_DIRECTORY) {
                    errorCountDirMod++;
                } else {
                    errorCountFileMod++;
                }
            }
        } else {
            // 更新対象外の場合（日付が取得できなかった場合）
            if (!appArg.isDiff()) {
                if (appArg.getVerbose() > 2) {
                    logger.writeLine(MdlConst.LVL_NONE, String.format("[XXX][%s][%s=>----/--/--][---] %s", pathTypeStr, targetLastWriteTime, displayPath));
                } else if (appArg.getVerbose() == 2) {
                    logger.writeLine(MdlConst.LVL_NONE, String.format("[XXX][%s][----/--/--][---] %s", pathTypeStr, displayPath));
                } else if (appArg.getVerbose() == 1) {
                    logger.writeLine(MdlConst.LVL_NONE, String.format("[XXX][%s][---] %s", pathTypeStr, displayPath));
                } else if (appArg.getVerbose() == 0) {
                    logger.writeLine(MdlConst.LVL_NONE, String.format("[XXX][%s] %s", pathTypeStr, displayPath));
                } else if (appArg.getVerbose() == -1) {
                    logger.writeLine(MdlConst.LVL_NONE, "  " + pathTypeStr + " " + displayPath);
                } else {
                    logger.writeLine(MdlConst.LVL_NONE, displayPath);
                }
            }
            if (pathType == MdlFile.PATH_IS_DIRECTORY) {
                noTargetCountDir++;
            } else {
                noTargetCountFile++;
            }
        }
    }
}
