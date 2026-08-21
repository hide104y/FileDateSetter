using System;
using System.IO;
using CmnClsLib.Class;
using CmnClsLib.Module;

// 2026/08/08 Gemini 3.6 Flash (High) Review & Modified

namespace FileDateSetter.Class
{
    /// <summary>
    /// ファイルおよびディレクトリの日付検索・更新処理を実行するクラスです。
    /// </summary>
    /// <param name="logger">ログ出力を行う <see cref="ClsLogger"/> オブジェクト。</param>
    /// <param name="appArg">実行引数および設定情報を保持する <see cref="ClsAppArg"/> オブジェクト。</param>
    /// <param name="fsDate">ファイルシステムの日付を設定・操作する <see cref="ClsFsDate"/> オブジェクト。</param>
    /// <example>
    /// <code>
    /// var finder = new ClsFind(logger, appArg, fsDate);
    /// bool isSuccess = finder.Execute();
    /// </code>
    /// </example>
    public class ClsFind(ClsLogger logger, ClsAppArg appArg, ClsFsDate fsDate)
    {
        private readonly ClsLogger _logger = logger;
        private readonly ClsAppArg _appArg = appArg;
        private readonly ClsFsDate _fsDate = fsDate;

        /// <summary>
        /// ディレクトリ一覧取得時に発生したエラーの総件数を取得または設定します。
        /// </summary>
        public ulong ErrorCountDirList { get; set; } = 0;

        /// <summary>
        /// ファイル一覧取得時に発生したエラーの総件数を取得または設定します。
        /// </summary>
        public ulong ErrorCountFileList { get; set; } = 0;

        /// <summary>
        /// ファイル日付変更時に発生したエラーの総件数を取得または設定します。
        /// </summary>
        public ulong ErrorCountFileModification { get; set; } = 0;

        /// <summary>
        /// ファイル日付変更に成功した総件数を取得または設定します。
        /// </summary>
        public ulong SuccessCountFileModification { get; set; } = 0;

        /// <summary>
        /// ディレクトリ日付変更時に発生したエラーの総件数を取得または設定します。
        /// </summary>
        public ulong ErrorCountDirectoryModification { get; set; } = 0;

        /// <summary>
        /// ディレクトリ日付変更に成功した総件数を取得または設定します。
        /// </summary>
        public ulong SuccessCountDirectoryModification { get; set; } = 0;

        /// <summary>
        /// スキップされたファイルの総件数を取得または設定します。
        /// </summary>
        public ulong SkipCountFile { get; set; } = 0;

        /// <summary>
        /// 処理対象としてスキャンされたファイルの総件数を取得または設定します。
        /// </summary>
        public ulong TotalCountFile { get; set; } = 0;

        /// <summary>
        /// 対象外と判定されたファイルの総件数を取得または設定します。
        /// </summary>
        public ulong NoTargetCountFile { get; set; } = 0;

        /// <summary>
        /// スキップされたディレクトリの総件数を取得または設定します。
        /// </summary>
        public ulong SkipCountDir { get; set; } = 0;

        /// <summary>
        /// 処理対象としてスキャンされたディレクトリの総件数を取得または設定します。
        /// </summary>
        public ulong TotalCountDir { get; set; } = 0;

        /// <summary>
        /// 対象外と判定されたディレクトリの総件数を取得または設定します。
        /// </summary>
        public ulong NoTargetCountDir { get; set; } = 0;

        /// <summary>
        /// 設定されたパラメータに基づき、指定パスに対するファイル・ディレクトリの日付更新処理を実行します。
        /// </summary>
        /// <returns>処理中にエラーが発生しなかった場合は true、1件以上のエラーが発生した場合は false。</returns>
        /// <example>
        /// <code>
        /// var finder = new ClsFind(logger, appArg, fsDate);
        /// bool isSuccess = finder.Execute();
        /// </code>
        /// </example>
        public int Execute()
        {
            if (_appArg.IsBaseDir)
            {
                ProcessDirectoryRecursive(_appArg.Path, "", 0, 0);
            }
            else
            {
                UpdateTargetDate(_appArg.Path, MdlFile.PATH_IS_FILE);
            }

            return ((ErrorCountDirList + ErrorCountFileList + ErrorCountFileModification) == 0 ? MdlConst.LVL_I : MdlConst.LVL_E);
        }

        /// <summary>
        /// 指定されたディレクトリを再帰的に走査し、フィルタ条件に従って日付変更またはサブディレクトリの処理を実行します。
        /// </summary>
        /// <param name="currentPath">走査対象の絶対パス。</param>
        /// <param name="relativePath">ルートからの相対パス。</param>
        /// <param name="currentDepth">現在のディレクトリ階層の深さ。</param>
        /// <param name="previousEffective">親ディレクトリから引き継いだ有効判定フラグ。</param>
        /// <returns>正常に走査処理が完了した場合は true、途中でエラーが発生した場合は false。</returns>
        /// <example>
        /// <code>
        /// bool success = ProcessDirectoryRecursive(@"C:\TargetDir", "", 0, 0);
        /// </code>
        /// </example>
        private bool ProcessDirectoryRecursive(string currentPath, string relativePath, ulong currentDepth, int previousEffective)
        {
            if (currentDepth > _appArg.MaxDepth)
            {
                return true;
            }

            bool result = true;
            bool isSymLink = false;
            int currentEffective = previousEffective;

            if (currentDepth >= _appArg.MinDepth)
            {
                try
                {
                    if (_appArg.IsSymLink)
                    {
                        isSymLink = MdlFile.IsSymlink(currentPath);
                    }

                    if (_appArg.Verbose > 6)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"■■■[recursive()][ParentDir][{currentDepth}] PATH={relativePath} ■■■");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"isSymLink      = {isSymLink}");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"previousEffective     = {previousEffective}");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"BlnIsIncHitRecursive = {_appArg.IsIncHitRecursive}");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"BlnIsExcHitRecursive = {_appArg.IsExcHitRecursive}");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"BlnIsDirFilterOr     = {_appArg.IsDirFilterOr}");
                    }

                    int filterCheck = MdlFile.EvaluatePathFilterCode(relativePath, _appArg.IsRegIncBasename, _appArg.IsRegExcBasename, _appArg.IncDirsList, _appArg.ExcDirsList, _appArg.IsDirFilterOr, _appArg.Verbose);
                    currentEffective = MdlFile.CombineFilterFlags(currentEffective, filterCheck, _appArg.IsDirFilterOr, _appArg.IsIncHitRecursive, _appArg.IsExcHitRecursive);

                    if (_appArg.Verbose > 6)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"filterCheck      = {filterCheck}");
                        _logger.WriteLine(MdlConst.LVL_NONE, $"currentEffective  = {currentEffective}");
                    }

                    if (currentDepth > 0 && currentEffective > 1 && _appArg.IsExcHitRecursive)
                    {
                        return true;
                    }

                    if (currentEffective == 1)
                    {
                        if (_appArg.IsModDir)
                        {
                            UpdateTargetDate(currentPath, MdlFile.PATH_IS_DIRECTORY);
                        }

                        if (!isSymLink)
                        {
                            if (_appArg.IsGetDateBySpecFName)
                            {
                                _appArg.ModifiedDateStr = "";
                                foreach (string targetFilePath in Directory.EnumerateFiles(currentPath, "*", SearchOption.TopDirectoryOnly))
                                {
                                    if (MdlFile.IsPathFilterMatched(targetFilePath, true, true, _appArg.IncSpecsList, _appArg.ExcSpecsList))
                                    {
                                        string dateStr = MdlDate.ExtractDateFromPath(targetFilePath, true, _appArg.CheckDate);
                                        if (!string.IsNullOrEmpty(dateStr))
                                        {
                                            _appArg.ModifiedDateStr = dateStr;
                                            if (_appArg.IsModDir) UpdateTargetDate(currentPath, MdlFile.PATH_IS_DIRECTORY);
                                            break;
                                        }
                                    }
                                }
                            }

                            if (_appArg.IsModFile) ProcessFilesInDirectory(currentPath);
                            if (_appArg.IsGetDateBySpecFName) _appArg.ModifiedDateStr = "";
                        }
                    }

                    if (isSymLink) return result;
                }
                catch (Exception ex)
                {
                    _logger.WriteLine(MdlConst.LVL_NONE, $"EXCEPTION : ClsFind.recursive() 1 : {ex.Message} : {relativePath}");
                    if (_appArg.IsStackTrace)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, "");
                        _logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                        _logger.WriteLine(MdlConst.LVL_NONE, "");
                    }
                }
            }

            return ProcessSubDirectories(currentPath, relativePath, currentDepth, currentEffective) && result;
        }

        /// <summary>
        /// 指定されたディレクトリ内の直下サブディレクトリを列挙し、再帰呼び出しを行います。
        /// </summary>
        /// <param name="currentPath">走査対象の絶対パス。</param>
        /// <param name="relativePath">親から引き継いだ相対パス。</param>
        /// <param name="currentDepth">現在の深さ。</param>
        /// <param name="currentEffective">有効フラグ。</param>
        /// <returns>すべてのサブディレクトリの処理が成功した場合は true、一部でエラーが発生した場合は false。</returns>
        /// <example>
        /// <code>
        /// bool isOk = ProcessSubDirectories(@"C:\TargetDir", "SubFolder", 1, 1);
        /// </code>
        /// </example>
        private bool ProcessSubDirectories(string currentPath, string relativePath, ulong currentDepth, int currentEffective)
        {
            bool result = true;
            try
            {
                foreach (string directoryPath in Directory.EnumerateDirectories(currentPath, "*", SearchOption.TopDirectoryOnly))
                {
                    try
                    {
                        string subDirectoryName = Path.GetFileName(directoryPath);
                        string nextRelativePath = string.IsNullOrEmpty(relativePath)
                            ? subDirectoryName
                            : Path.Combine(relativePath, subDirectoryName);

                        if (!ProcessDirectoryRecursive(directoryPath, nextRelativePath, currentDepth + 1, currentEffective))
                        {
                            result = false;
                        }
                    }
                    catch (Exception ex)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"EXCEPTION : ClsFind.recursive() 2 : {ex.Message} : {relativePath}");
                        if (_appArg.IsStackTrace)
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, "");
                            _logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                            _logger.WriteLine(MdlConst.LVL_NONE, "");
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                ErrorCountDirList++;
                _logger.WriteLine(MdlConst.LVL_E, $"EXCEPTION : GetDirectories : {currentPath} : {ex.Message}");
                if (_appArg.IsStackTrace)
                {
                    _logger.WriteLine(MdlConst.LVL_NONE, "");
                    _logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                    _logger.WriteLine(MdlConst.LVL_NONE, "");
                }
            }
            return result;
        }

        /// <summary>
        /// 指定されたディレクトリ直下に存在するファイルを列挙し、ファイルフィルタに適合した対象の日付更新処理を呼び出します。
        /// </summary>
        /// <param name="currentPath">処理対象ファイルの存在するディレクトリパス。</param>
        /// <example>
        /// <code>
        /// ProcessFilesInDirectory(@"C:\TargetDir");
        /// </code>
        /// </example>
        private void ProcessFilesInDirectory(string currentPath)
        {
            try
            {
                foreach (string filePath in Directory.EnumerateFiles(currentPath, "*", SearchOption.TopDirectoryOnly))
                {
                    if (MdlFile.IsPathFilterMatched(filePath, true, true, _appArg.IncFilesList, _appArg.ExcFilesList))
                    {
                        if (MdlFile.IsValidFileDateTime(filePath, _appArg.IsBefore, _appArg.BeforeTime, _appArg.IsAfter, _appArg.AfterTime))
                        {
                            if (_appArg.Verbose > 6) _logger.WriteLine(MdlConst.LVL_NONE, $"[H I T] {filePath}");
                            UpdateTargetDate(filePath, MdlFile.PATH_IS_FILE);
                        }
                        else
                        {
                            if (_appArg.Verbose > 6) _logger.WriteLine(MdlConst.LVL_NONE, $"[NOHIT] {filePath}");
                        }
                    }
                    else
                    {
                        if (_appArg.Verbose > 6) _logger.WriteLine(MdlConst.LVL_NONE, $"[NOHIT] {filePath}");
                    }
                }
            }
            catch (Exception ex)
            {
                ErrorCountFileList++;
                _logger.WriteLine(MdlConst.LVL_E, $"EXCEPTION : {currentPath} : {ex.Message}");
                if (_appArg.IsStackTrace)
                {
                    _logger.WriteLine(MdlConst.LVL_NONE, "");
                    _logger.WriteLine(MdlConst.LVL_NONE, ex.StackTrace ?? "");
                    _logger.WriteLine(MdlConst.LVL_NONE, "");
                }
            }
        }

        /// <summary>
        /// 指定されたパス（ファイルまたはディレクトリ）の日付解析および更新処理を行います。
        /// </summary>
        /// <param name="targetPath">対象のファイルまたはディレクトリの絶対パス。</param>
        /// <param name="pathType">パス種別（<see cref="MdlFile.PATH_IS_DIRECTORY"/> または <see cref="MdlFile.PATH_IS_FILE"/>）。</param>
        /// <example>
        /// <code>
        /// UpdateTargetDate(@"C:\TargetDir\file.txt", MdlFile.PATH_IS_FILE);
        /// </code>
        /// </example>
        private void UpdateTargetDate(string targetPath, int pathType)
        {
            bool isSuccess = false;
            string modifiedDate = "";
            int returnCode = 0;
            string pathTypeStr = pathType == MdlFile.PATH_IS_DIRECTORY ? "D" : "F";
            string displayPath = _appArg.IsDq ? $"\"{targetPath}\"" : targetPath;
            string targetLastWriteTime = "";

            if (pathType == MdlFile.PATH_IS_DIRECTORY)
            {
                TotalCountDir++;
            }
            else
            {
                TotalCountFile++;
            }

            // ファイル情報取得
            if (_appArg.Verbose > 2)
            {
                targetLastWriteTime = "                   ";
                try
                {
                    var objPathFFr = new FileInfo(targetPath);
                    targetLastWriteTime = MdlDate.GetFormattedDate(objPathFFr.LastWriteTime, "yyyy/MM/dd HH:mm:ss");
                }
                catch { }
            }

            // 更新日取得
            if (_appArg.IsGetDateByName)
            {
                modifiedDate = MdlDate.ExtractDateFromPath(targetPath, true, _appArg.CheckDate);
                if (string.IsNullOrEmpty(modifiedDate) && _appArg.IsGetDateByDirName)
                {
                    modifiedDate = MdlDate.ExtractDateFromStringReverse(Path.GetDirectoryName(targetPath) ?? "", true, _appArg.CheckDate);
                }
            }
            else
            {
                if (_appArg.IsGetDateByDirName)
                {
                    modifiedDate = MdlDate.ExtractDateFromStringReverse(Path.GetDirectoryName(targetPath) ?? "", true, _appArg.CheckDate);
                }
                else if (_appArg.IsCreationTime)
                {
                    modifiedDate = MdlDate.GetFormattedDate(File.GetCreationTime(targetPath), "yyyy/MM/dd HH:mm:ss");
                }
                else if (_appArg.IsLastWriteTime)
                {
                    modifiedDate = MdlDate.GetFormattedDate(File.GetLastWriteTime(targetPath), "yyyy/MM/dd HH:mm:ss");
                }
                else
                {
                    modifiedDate = _appArg.ModifiedDateStr;
                }
            }

            // 更新対象の場合（更新日が取得できた場合）
            if (!string.IsNullOrEmpty(modifiedDate))
            {
                // 実行フラグがONの場合
                if (_appArg.IsExec)
                {
                    // 更新
                    returnCode = _fsDate.SetDateCore(targetPath, modifiedDate, _appArg.ModeCode, pathType, false, _appArg.IsForce, true);
                    // 更新成功
                    if (returnCode > -1)
                    {
                        isSuccess = true;
                        bool isShow = !(returnCode == 0 && _appArg.IsDiff);
                        string status = returnCode == 0 ? "---" : "UPD";
                        if (isShow)
                        {
                            if (_appArg.Verbose > 2)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{targetLastWriteTime}=>{modifiedDate}][{returnCode:000}] {displayPath}");
                            }
                            else if (_appArg.Verbose == 2)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{modifiedDate}][{returnCode:000}] {displayPath}");
                            }
                            else if (_appArg.Verbose == 1)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{returnCode:000}] {displayPath}");
                            }
                            else if (_appArg.Verbose == 0)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}] {displayPath}");
                            }
                            else if (_appArg.Verbose == -1)
                            {
                                string shortStatus = returnCode == 0 ? "-" : "U";
                                _logger.WriteLine(MdlConst.LVL_NONE, $"{shortStatus} {pathTypeStr} {displayPath}");
                            }
                            else
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, displayPath);
                            }
                        }
                    }
                    // 更新失敗
                    else
                    {
                        if (_appArg.Verbose >= -1)
                        {
                            if (_appArg.Verbose > 2)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[ERR][{pathTypeStr}][{targetLastWriteTime}=>{modifiedDate}][---] {displayPath}");
                            }
                            else if (_appArg.Verbose == 2)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[ERR][{pathTypeStr}][{modifiedDate}][---] {displayPath}");
                            }
                            else if (_appArg.Verbose == 1)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[ERR][{pathTypeStr}][---] {displayPath}");
                            }
                            else if (_appArg.Verbose == 0)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"[ERR][{pathTypeStr}] {displayPath}");
                            }
                            else if (_appArg.Verbose == -1)
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, $"E {pathTypeStr} {displayPath}");
                            }
                            else
                            {
                                _logger.WriteLine(MdlConst.LVL_NONE, displayPath);
                            }
                        }
                    }
                }
                // -listが指定されている場合
                else
                {
                    isSuccess = true;
                    bool isShow = true;
                    string result = "---";
                    string status = "-U-";
                    if (_appArg.IsUpdateCheck)
                    {
                        returnCode = _fsDate.SetDateCore(targetPath, modifiedDate, _appArg.ModeCode, pathType, false, _appArg.IsForce, false);
                        result = returnCode.ToString("000");
                        if (returnCode == 0 && _appArg.IsDiff) isShow = false;
                        status = returnCode == 0 ? "---" : "-U-";
                    }
                    if (isShow)
                    {
                        if (_appArg.Verbose > 2)
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{targetLastWriteTime}=>{modifiedDate}][{result}] {displayPath}");
                        }
                        else if (_appArg.Verbose == 2)
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{modifiedDate}][{result}] {displayPath}");
                        }
                        else if (_appArg.Verbose == 1)
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}][{result}] {displayPath}");
                        }
                        else if (_appArg.Verbose == 0)
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, $"[{status}][{pathTypeStr}] {displayPath}");
                        }
                        else if (_appArg.Verbose == -1)
                        {
                            string shortStatus = returnCode == 0 ? "-" : "U";
                            _logger.WriteLine(MdlConst.LVL_NONE, $"{shortStatus} {pathTypeStr} {displayPath}");
                        }
                        else
                        {
                            _logger.WriteLine(MdlConst.LVL_NONE, displayPath);
                        }
                    }
                }

                // カウンタインクリメント
                if (isSuccess)
                {
                    if (returnCode == 0)
                    {
                        if (pathType == MdlFile.PATH_IS_DIRECTORY) SkipCountDir++;
                        else SkipCountFile++;
                    }
                    else
                    {
                        if (pathType == MdlFile.PATH_IS_DIRECTORY) SuccessCountDirectoryModification++;
                        else SuccessCountFileModification++;
                    }
                }
                else
                {
                    if (pathType == MdlFile.PATH_IS_DIRECTORY) ErrorCountDirectoryModification++;
                    else ErrorCountFileModification++;
                }
            }
            // 更新対象外の場合（日が取得できなかった場合）
            else
            {
                if (!_appArg.IsDiff)
                {
                    if (_appArg.Verbose > 2)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"[XXX][{pathTypeStr}][{targetLastWriteTime}=>----/--/--][---] {displayPath}");
                    }
                    else if (_appArg.Verbose == 2)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"[XXX][{pathTypeStr}][----/--/--][---] {displayPath}");
                    }
                    else if (_appArg.Verbose == 1)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"[XXX][{pathTypeStr}][---] {displayPath}");
                    }
                    else if (_appArg.Verbose == 0)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"[XXX][{pathTypeStr}] {displayPath}");
                    }
                    else if (_appArg.Verbose == -1)
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, $"  {pathTypeStr} {displayPath}");
                    }
                    else
                    {
                        _logger.WriteLine(MdlConst.LVL_NONE, displayPath);
                    }
                }
                if (pathType == MdlFile.PATH_IS_DIRECTORY) NoTargetCountDir++;
                else NoTargetCountFile++;
            }
        }
    }
}
