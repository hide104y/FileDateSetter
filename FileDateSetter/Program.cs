using System;
using CmnClsLib.Class;
using CmnClsLib.Module;
using FileDateSetter.Class;

// 2026/08/08 Gemini 3.6 Flash (High) Review & Modified

namespace FileDateSetter
{
    public class Program
    {
        /// <summary>
        /// FileDateSetter アプリケーションのエントリーポイントです。
        /// コマンドライン引数を解析し、指定されたファイルまたはディレクトリの日時変更処理を実行します。
        /// </summary>
        /// <param name="args">コマンドライン引数の配列。</param>
        /// <returns>
        /// 処理の実行結果を示す終了コード（0: 正常終了, 1: 警告/ヘルプ表示, 2: エラー終了）。
        /// </returns>
        /// <example>
        /// 使用例:
        /// <code>
        /// // 指定ディレクトリ以下のファイル・フォルダの日時を一括更新
        /// FileDateSetter.exe -path "C:\TargetDir" -today -vv -set
        /// 
        /// // ヘルプメッセージを表示
        /// FileDateSetter.exe -h
        /// </code>
        /// </example>
        public static int Main(string[] args)
        {
            DateTime startTime = DateTime.Now;
            ClsLogger logger = new();
            ClsAppArg appArg = new(logger);
            ClsFsDate dateSetter = new(logger);
            ClsFind fileFinder = new(logger, appArg, dateSetter);
            bool isSuccess = appArg.Parse(args);

            if (appArg.Verbose > 0)
            {
                logger.WriteLine(MdlConst.LVL_NONE, $"===<<< [{appArg.ExeBaseName}] START : {MdlDate.GetFormattedDate(startTime, "yyyy/MM/dd HH:mm:ss")}>>>===");
            }

            if (isSuccess && !appArg.IsUsage)
            {
                switch (MdlFile.GetPathType(appArg.Path))
                {
                    case MdlFile.PATH_IS_DIRECTORY:
                        appArg.IsBaseDir = true;
                        break;
                    case MdlFile.PATH_IS_FILE:
                        break;
                    default:
                        isSuccess = false;
                        logger.WriteLine(MdlConst.LVL_E, $"NO SUCH A FILE OR DIRECTORY : {appArg.Path}");
                        break;
                }

                if (isSuccess)
                {
                    dateSetter.Verbose = appArg.Verbose;
                    if (appArg.DiffLevel > 1) dateSetter.Verbose = 0;
                }

                if (isSuccess)
                {
                    if (OperatingSystem.IsWindows() && (appArg.IsSwitchUser || appArg.IsLogon))
                    {
                        using ClsLogon logon = new();
                        logon.DomainName = string.IsNullOrEmpty(appArg.DomainName)
                            ? Environment.UserDomainName.ToUpper()
                            : appArg.DomainName;
                        logon.Username = appArg.UsernameWithoutDomain;
                        logon.Password = appArg.Password;
                        try
                        {
                            logon.Execute(fileFinder);
                            appArg.ReturnCode = logon.ReturnCode;
                        }
                        catch (Exception ex)
                        {
                            appArg.ReturnCode = MdlConst.LVL_E;
                            logger.WriteLine(MdlConst.LVL_NONE, $"CALL logon.Execute() -> actionController.Main(): {ex.Message}");
                            if (appArg.IsStackTrace)
                            {
                                logger.WriteLine(MdlConst.LVL_NONE, "");
                                logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                                logger.WriteLine(MdlConst.LVL_NONE, "");
                            }
                        }
                    }
                    else
                    {
                        try
                        {
                            isSuccess = (fileFinder.Execute() == MdlConst.LVL_I ? true : false);
                        }
                        catch (Exception ex)
                        {
                            isSuccess = false;
                            logger.WriteLine(MdlConst.LVL_E, $"fileFinder.Execute() : {ex.Message}");
                            if (appArg.IsStackTrace)
                            {
                                logger.WriteLine(MdlConst.LVL_NONE, "");
                                logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                                logger.WriteLine(MdlConst.LVL_NONE, "");
                            }
                        }
                    }
                }

                if (appArg.Verbose > -3)
                {
                    if (appArg.IsExec)
                    {
                        if (appArg.IsModDir)
                            logger.WriteLine(MdlConst.LVL_I, $"[処理結果] 総フォルダ数 = {fileFinder.TotalCountDir} / 対象数 = {fileFinder.SuccessCountDirectoryModification + fileFinder.SkipCountDir} (更新={fileFinder.SuccessCountDirectoryModification} / SKIP={fileFinder.SkipCountDir}) / ERROR数 = {fileFinder.ErrorCountDirectoryModification} / 対象外数 = {fileFinder.NoTargetCountDir}");
                        if (appArg.IsModFile)
                            logger.WriteLine(MdlConst.LVL_I, $"[処理結果] 総ファイル数 = {fileFinder.TotalCountFile} / 対象数 = {fileFinder.SuccessCountFileModification + fileFinder.SkipCountFile} (更新={fileFinder.SuccessCountFileModification} / SKIP={fileFinder.SkipCountFile}) / ERROR数 = {fileFinder.ErrorCountFileModification} / 対象外数 = {fileFinder.NoTargetCountFile}");
                    }
                    else
                    {
                        if (appArg.IsModDir)
                            logger.WriteLine(MdlConst.LVL_I, $"[抽出結果] 総フォルダ数 = {fileFinder.TotalCountDir} / 対象数 = {fileFinder.SuccessCountDirectoryModification + fileFinder.SkipCountDir} / 対象外数 = {fileFinder.NoTargetCountDir}");
                        if (appArg.IsModFile)
                            logger.WriteLine(MdlConst.LVL_I, $"[抽出結果] 総ファイル数 = {fileFinder.TotalCountFile} / 対象数 = {fileFinder.SuccessCountFileModification + fileFinder.SkipCountFile} / 対象外数 = {fileFinder.NoTargetCountFile}");
                    }
                }

                if (isSuccess)
                {
                    appArg.ReturnCode = MdlConst.LVL_I;
                }
                else
                {
                    appArg.ReturnCode = MdlConst.LVL_E;
                    if (appArg.Verbose > -3)
                    {
                        if (fileFinder.ErrorCountFileModification > 0)
                            logger.WriteLine(MdlConst.LVL_E, $"{fileFinder.ErrorCountFileModification}個のファイル日付更新に失敗しました。");
                        if (fileFinder.ErrorCountDirList > 0)
                            logger.WriteLine(MdlConst.LVL_E, $"{fileFinder.ErrorCountDirList}回サブディレクトリ一覧の取得に失敗しました。");
                        if (fileFinder.ErrorCountFileList > 0)
                            logger.WriteLine(MdlConst.LVL_E, $"{fileFinder.ErrorCountFileList}回ファイル一覧の取得に失敗しました。");
                    }
                }
            }
            else
            {
                if (appArg.IsUsage)
                {
                    appArg.ReturnCode = MdlConst.LVL_W;
                    appArg.ShowUsage();
                }
                else
                {
                    appArg.ReturnCode = MdlConst.LVL_E;
                }
            }

            if (appArg.Verbose > 0)
            {
                DateTime endTime = DateTime.Now;
                double elapsedSeconds = (endTime - startTime).TotalSeconds;
                logger.WriteLine(MdlConst.LVL_NONE, $"===<<< [{appArg.ExeBaseName}] EXIT ({appArg.ReturnCode}) : {MdlDate.GetFormattedDate(endTime, "yyyy/MM/dd HH:mm:ss")} : {elapsedSeconds:F3} sec>>>===");
            }

            if (appArg.IsEchoRetcode)
            {
                logger.WriteLine(MdlConst.LVL_NONE, appArg.ReturnCode.ToString());
            }

            return appArg.ReturnCode;
        }

    }
}
