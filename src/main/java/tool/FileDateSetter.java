package tool;

import java.time.LocalDateTime;
import java.util.Locale;
import tool.cmnclslib.cls.ClsFsDate;
import tool.cmnclslib.cls.ClsLogger;
import tool.cmnclslib.mdl.MdlConst;
import tool.cmnclslib.mdl.MdlDate;
import tool.cmnclslib.mdl.MdlFile;

/**
 * FileDateSetter アプリケーションのエントリーポイントクラスです。
 */
public final class FileDateSetter {

    private FileDateSetter() {
        // インスタンス化防止
    }

    /**
     * アプリケーションのエントリーポイントです。
     *
     * @param args コマンドライン引数の配列
     */
    public static void main(String[] args) {
        int returnCode = mainProcess(args);
        if (returnCode != MdlConst.LVL_I) {
            System.exit(returnCode);
        }
    }

    /**
     * コマンドライン引数を解析し、指定されたファイルまたはディレクトリの日時変更処理を実行します。
     *
     * @param args コマンドライン引数の配列
     * @return 処理の実行結果を示す終了コード（0: 正常終了, 10: 警告/ヘルプ表示, 20: エラー終了）
     */
    public static int mainProcess(String[] args) {
        long startTimestamp = System.nanoTime();
        LocalDateTime startTime = LocalDateTime.now();

        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        ClsFsDate dateSetter = new ClsFsDate(logger);
        ClsFind fileFinder = new ClsFind(logger, appArg, dateSetter);
        boolean isSuccess = appArg.parse(args);

        if (appArg.getVerbose() > 0) {
            logger.writeLine(MdlConst.LVL_NONE, "===<<< [" + appArg.getExeBaseName() + "] START : " + MdlDate.getFormattedDate(startTime, "yyyy/MM/dd HH:mm:ss") + ">>>===");
        }

        if (isSuccess && !appArg.isUsage()) {
            switch (MdlFile.getPathType(appArg.getPath())) {
                case MdlFile.PATH_IS_DIRECTORY:
                    appArg.setBaseDir(true);
                    break;
                case MdlFile.PATH_IS_FILE:
                    appArg.setBaseDir(false);
                    break;
                default:
                    isSuccess = false;
                    logger.writeLine(MdlConst.LVL_E, "NO SUCH A FILE OR DIRECTORY : " + appArg.getPath());
                    break;
            }

            if (isSuccess) {
                dateSetter.setVerbose(appArg.getVerbose());
                if (appArg.getDiffLevel() > 1) {
                    dateSetter.setVerbose(0);
                }
            }

            if (isSuccess) {
                try {
                    isSuccess = (fileFinder.execute() == MdlConst.LVL_I);
                } catch (Exception ex) {
                    isSuccess = false;
                    logger.writeLine(MdlConst.LVL_E, "fileFinder.Execute() : " + ex.getMessage());
                    if (appArg.isStackTrace()) {
                        logger.writeLine(MdlConst.LVL_NONE, "");
                        for (StackTraceElement elem : ex.getStackTrace()) {
                            logger.writeLine(MdlConst.LVL_NONE, elem.toString());
                        }
                        logger.writeLine(MdlConst.LVL_NONE, "");
                    }
                }
            }

            if (appArg.getVerbose() > -3) {
                if (appArg.isExec()) {
                    if (appArg.isModDir()) {
                        logger.writeLine(MdlConst.LVL_I, String.format(Locale.ROOT,
                                "[処理結果] 総フォルダ数 = %d / 対象数 = %d (更新=%d / SKIP=%d) / ERROR数 = %d / 対象外数 = %d",
                                fileFinder.getTotalCountDir(),
                                (fileFinder.getSuccessCountDirMod() + fileFinder.getSkipCountDir()),
                                fileFinder.getSuccessCountDirMod(),
                                fileFinder.getSkipCountDir(),
                                fileFinder.getErrorCountDirMod(),
                                fileFinder.getNoTargetCountDir()));
                    }
                    if (appArg.isModFile()) {
                        logger.writeLine(MdlConst.LVL_I, String.format(Locale.ROOT,
                                "[処理結果] 総ファイル数 = %d / 対象数 = %d (更新=%d / SKIP=%d) / ERROR数 = %d / 対象外数 = %d",
                                fileFinder.getTotalCountFile(),
                                (fileFinder.getSuccessCountFileMod() + fileFinder.getSkipCountFile()),
                                fileFinder.getSuccessCountFileMod(),
                                fileFinder.getSkipCountFile(),
                                fileFinder.getErrorCountFileMod(),
                                fileFinder.getNoTargetCountFile()));
                    }
                } else {
                    if (appArg.isModDir()) {
                        logger.writeLine(MdlConst.LVL_I, String.format(Locale.ROOT,
                                "[抽出結果] 総フォルダ数 = %d / 対象数 = %d / 対象外数 = %d",
                                fileFinder.getTotalCountDir(),
                                (fileFinder.getSuccessCountDirMod() + fileFinder.getSkipCountDir()),
                                fileFinder.getNoTargetCountDir()));
                    }
                    if (appArg.isModFile()) {
                        logger.writeLine(MdlConst.LVL_I, String.format(Locale.ROOT,
                                "[抽出結果] 総ファイル数 = %d / 対象数 = %d / 対象外数 = %d",
                                fileFinder.getTotalCountFile(),
                                (fileFinder.getSuccessCountFileMod() + fileFinder.getSkipCountFile()),
                                fileFinder.getNoTargetCountFile()));
                    }
                }
            }

            if (isSuccess) {
                appArg.setReturnCode(MdlConst.LVL_I);
            } else {
                appArg.setReturnCode(MdlConst.LVL_E);
                if (appArg.getVerbose() > -3) {
                    if (fileFinder.getErrorCountFileMod() > 0) {
                        logger.writeLine(MdlConst.LVL_E, fileFinder.getErrorCountFileMod() + "個のファイル日付更新に失敗しました。");
                    }
                    if (fileFinder.getErrorCountDirList() > 0) {
                        logger.writeLine(MdlConst.LVL_E, fileFinder.getErrorCountDirList() + "回サブディレクトリ一覧の取得に失敗しました。");
                    }
                    if (fileFinder.getErrorCountFileList() > 0) {
                        logger.writeLine(MdlConst.LVL_E, fileFinder.getErrorCountFileList() + "回ファイル一覧の取得に失敗しました。");
                    }
                }
            }
        } else {
            if (appArg.isUsage()) {
                appArg.setReturnCode(MdlConst.LVL_W);
                appArg.showUsage();
            } else {
                appArg.setReturnCode(MdlConst.LVL_E);
            }
        }

        if (appArg.getVerbose() > 0) {
            LocalDateTime endTime = LocalDateTime.now();
            double elapsedSeconds = (System.nanoTime() - startTimestamp) / 1_000_000_000.0;
            logger.writeLine(MdlConst.LVL_NONE, String.format(Locale.ROOT,
                    "===<<< [%s] EXIT (%d) : %s : %.3f sec>>>===",
                    appArg.getExeBaseName(), appArg.getReturnCode(), MdlDate.getFormattedDate(endTime, "yyyy/MM/dd HH:mm:ss"), elapsedSeconds));
        }

        if (appArg.isEchoRetcode()) {
            logger.writeLine(MdlConst.LVL_NONE, String.valueOf(appArg.getReturnCode()));
        }

        return appArg.getReturnCode();
    }
}
