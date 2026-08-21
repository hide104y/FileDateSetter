using System;
using System.Text;
using System.IO;
using System.Text.RegularExpressions;
using System.Collections;
using System.Collections.Generic;
using CmnClsLib.Class;
using CmnClsLib.Module;

// 2026/08/08 Gemini 3.6 Flash (High) Review & Modified

namespace FileDateSetter.Class
{
    public class ClsAppArg
    {
        private ClsLogger _logger;
        private ClsCmmnArgs _cmmnArgs;
        private string _exeDir = "";
        private string _exeBaseName = "";
        private int _pid = 0;
        private int _verbose = 0;
        private int _returnCode = MdlConst.LVL_I;
        private bool _isUsage = false;
        private bool _isStackTrace = false;
        private string _path = "";
        private string _modifiedDateStr = "";
        private int _modeCode = 0;
        private ulong _minDepth = 0;
        private ulong _maxDepth = MdlConst.ULNG_MAX;
        private int _typeCode = MdlConst.INT_TYPE_FILE;
        private int _diffLevel = 0;
        private int _checkDate = 19700101;
        private string _showTypeStr = "f";
        private bool _isExec = false;
        private bool _isCreationTime = false;
        private bool _isLastWriteTime = false;
        private bool _isGetDateByName = false;
        private bool _isGetDateByDirName = false;
        private bool _isGetDateBySpecFName = false;
        private bool _isModFile = true;
        private bool _isModDir = false;
        private bool _isBaseDir = false;
        private bool _isRegIncBasename = false;
        private bool _isRegExcBasename = false;
        private bool _isIncHitRecursive = false;
        private bool _isExcHitRecursive = false;
        private bool _isDirFilterOr = false;
        private bool _isForce = false;
        private bool _isDiff = false;
        private bool _isDq = false;
        private bool _isUpdateCheck = false;
        private bool _isSymLink = false;
        List<string> _incFilesList = [];
        List<string> _excFilesList = [];
        List<string> _incDirsList = [];
        List<string> _excDirsList = [];
        List<string> _incSpecsList = [];
        List<string> _excSpecsList = [];
        private string _domainName = "";
        private string _username = "";
        private string _usernameWithoutDomain = "";
        private string _password = "";
        private bool _isSwitchUser = false;
        private bool _isLogon = false;
        private bool _isLogonAlwaysOk = false;
        private string _netSharePath = "";
        private string _driveName = "";
        private bool _isMount = false;
        private bool _isUmount = false;
        private List<int> _netUseOkErrNoList = [];
        private bool _isEchoRetcode = false;
        private bool _isBefore = false;
        private bool _isAfter = false;
        private DateTime _beforeTime = new DateTime(9999, 12, 31, 23, 59, 59, DateTimeKind.Utc).ToLocalTime();
        private DateTime _afterTime = new DateTime(0001, 1, 1, 0, 0, 0, DateTimeKind.Utc).ToLocalTime();

        /// <summary>
        /// <see cref="ClsAppArg"/> クラスの新しいインスタンスを初期化します。
        /// </summary>
        /// <param name="logger">ログ出力オブジェクト</param>
        /// <example>
        /// <code>
        /// var logger = new ClsLogger();
        /// var appArg = new ClsAppArg(logger);
        /// </code>
        /// </example>
        public ClsAppArg(ClsLogger logger)
        {
            _logger = logger;
            _cmmnArgs = new(_logger);
            _cmmnArgs.GetModuleInfo(System.Diagnostics.Process.GetCurrentProcess().MainModule?.FileName ?? "");
            _exeDir = _cmmnArgs.ExeDir;
            _exeBaseName = _cmmnArgs.ExeBaseName;
            _pid = _cmmnArgs.Pid;
        }

        /// <summary>実行ファイルのベース名を取得または設定します。</summary>
        public string ExeBaseName { get => _exeBaseName; set => _exeBaseName = value; }

        /// <summary>実行ファイルのディレクトリパスを取得または設定します。</summary>
        public string ExeDir { get => _exeDir; set => _exeDir = value; }

        /// <summary>ヘルプ・使用方法の表示要求フラグを取得します。</summary>
        public bool IsUsage => _isUsage;

        /// <summary>アプリケーションの終了コードを取得または設定します。</summary>
        public int ReturnCode { get => _returnCode; set => _returnCode = value; }

        /// <summary>ログ出力の冗長度レベルを取得または設定します。</summary>
        public int Verbose { get => _verbose; set => _verbose = value; }

        /// <summary>例外時のスタックトレース表示フラグを取得または設定します。</summary>
        public bool IsStackTrace { get => _isStackTrace; set => _isStackTrace = value; }

        /// <summary>対象パスを取得します。</summary>
        public string Path => _path;

        /// <summary>設定する変更日付文字列を取得または設定します。</summary>
        public string ModifiedDateStr { get => _modifiedDateStr; set => _modifiedDateStr = value; }

        /// <summary>処理モードコードを取得します。</summary>
        public int ModeCode => _modeCode;

        /// <summary>変更対象種別コード（ファイル/ディレクトリ/両方）を取得または設定します。</summary>
        public int TypeCode { get => _typeCode; set => _typeCode = value; }

        /// <summary>差分表示レベルを取得または設定します。</summary>
        public int DiffLevel { get => _diffLevel; set => _diffLevel = value; }

        /// <summary>有効日付の確認しきい値（数値表現）を取得または設定します。</summary>
        public int CheckDate { get => _checkDate; set => _checkDate = value; }

        /// <summary>最小探索ディレクトリ階層深さを取得します。</summary>
        public ulong MinDepth => _minDepth;

        /// <summary>最大探索ディレクトリ階層深さを取得します。</summary>
        public ulong MaxDepth => _maxDepth;

        /// <summary>日付変更処理の実行フラグを取得します。</summary>
        public bool IsExec => _isExec;

        /// <summary>ファイル日付変更対象フラグを取得します。</summary>
        public bool IsModFile => _isModFile;

        /// <summary>ディレクトリ日付変更対象フラグを取得します。</summary>
        public bool IsModDir => _isModDir;

        /// <summary>ファイル名からの日付検索・取得フラグを取得します。</summary>
        public bool IsGetDateByName => _isGetDateByName;

        /// <summary>ディレクトリ名からの日付検索・取得フラグを取得します。</summary>
        public bool IsGetDateByDirName => _isGetDateByDirName;

        /// <summary>指定パターン名からの日付検索・取得フラグを取得します。</summary>
        public bool IsGetDateBySpecFName => _isGetDateBySpecFName;

        /// <summary>作成日時対象フラグを取得します。</summary>
        public bool IsCreationTime => _isCreationTime;

        /// <summary>更新日時対象フラグを取得します。</summary>
        public bool IsLastWriteTime => _isLastWriteTime;

        /// <summary>ベースディレクトリ対象フラグを取得または設定します。</summary>
        public bool IsBaseDir { get => _isBaseDir; set => _isBaseDir = value; }

        /// <summary>正規表現ファイル名対象判定フラグを取得または設定します。</summary>
        public bool IsRegIncBasename { get => _isRegIncBasename; set => _isRegIncBasename = value; }

        /// <summary>正規表現ファイル名除外判定フラグを取得または設定します。</summary>
        public bool IsRegExcBasename { get => _isRegExcBasename; set => _isRegExcBasename = value; }

        /// <summary>対象ヒット時の再帰適用フラグを取得または設定します。</summary>
        public bool IsIncHitRecursive { get => _isIncHitRecursive; set => _isIncHitRecursive = value; }

        /// <summary>除外ヒット時の再帰適用フラグを取得または設定します。</summary>
        public bool IsExcHitRecursive { get => _isExcHitRecursive; set => _isExcHitRecursive = value; }

        /// <summary>ディレクトリフィルタのOR条件適用フラグを取得または設定します。</summary>
        public bool IsDirFilterOr { get => _isDirFilterOr; set => _isDirFilterOr = value; }

        /// <summary>強制更新フラグを取得または設定します。</summary>
        public bool IsForce { get => _isForce; set => _isForce = value; }

        /// <summary>差分のみ表示フラグを取得または設定します。</summary>
        public bool IsDiff { get => _isDiff; set => _isDiff = value; }

        /// <summary>ダブルクォーテーション囲み出力フラグを取得または設定します。</summary>
        public bool IsDq { get => _isDq; set => _isDq = value; }

        /// <summary>更新有無確認フラグを取得または設定します。</summary>
        public bool IsUpdateCheck { get => _isUpdateCheck; set => _isUpdateCheck = value; }

        /// <summary>シンボリックリンク判定有効化フラグを取得または設定します。</summary>
        public bool IsSymLink { get => _isSymLink; set => _isSymLink = value; }

        /// <summary>対象ファイル名フィルタパターンリストを取得します。</summary>
        public List<string> IncFilesList => _incFilesList;

        /// <summary>除外ファイル名フィルタパターンリストを取得します。</summary>
        public List<string> ExcFilesList => _excFilesList;

        /// <summary>対象ディレクトリ名フィルタパターンリストを取得します。</summary>
        public List<string> IncDirsList => _incDirsList;

        /// <summary>除外ディレクトリ名フィルタパターンリストを取得します。</summary>
        public List<string> ExcDirsList => _excDirsList;

        /// <summary>指定ファイル名フィルタパターンリストを取得します。</summary>
        public List<string> IncSpecsList => _incSpecsList;

        /// <summary>除外指定ファイル名フィルタパターンリストを取得します。</summary>
        public List<string> ExcSpecsList => _excSpecsList;

        /// <summary>認証用ドメイン名を取得または設定します。</summary>
        public string DomainName { get => _domainName; set => _domainName = value; }

        /// <summary>認証用ユーザー名を取得または設定します。</summary>
        public string Username { get => _username; set => _username = value; }

        /// <summary>ドメイン抜きユーザー名を取得または設定します。</summary>
        public string UsernameWithoutDomain { get => _usernameWithoutDomain; set => _usernameWithoutDomain = value; }

        /// <summary>認証用パスワードを取得または設定します。</summary>
        public string Password { get => _password; set => _password = value; }

        /// <summary>ユーザー切り替え認証実行フラグを取得または設定します。</summary>
        public bool IsSwitchUser { get => _isSwitchUser; set => _isSwitchUser = value; }

        /// <summary>ログオンフラグを取得または設定します。</summary>
        public bool IsLogon { get => _isLogon; set => _isLogon = value; }

        /// <summary>認証エラー無視フラグを取得または設定します。</summary>
        public bool IsLogonAlwaysOk { get => _isLogonAlwaysOk; set => _isLogonAlwaysOk = value; }

        /// <summary>ネットワーク共有パスを取得または設定します。</summary>
        public string NetSharePath { get => _netSharePath; set => _netSharePath = value; }

        /// <summary>ドライブ名を取得または設定します。</summary>
        public string DriveName { get => _driveName; set => _driveName = value; }

        /// <summary>マウント実行フラグを取得または設定します。</summary>
        public bool IsMount { get => _isMount; set => _isMount = value; }

        /// <summary>アンマウント実行フラグを取得または設定します。</summary>
        public bool IsUmount { get => _isUmount; set => _isUmount = value; }

        /// <summary>net use正常扱いエラー番号リストを取得または設定します。</summary>
        public List<int> NetUseOkErrNoList { get => _netUseOkErrNoList; set => _netUseOkErrNoList = value; }

        /// <summary>終了コード表示フラグを取得または設定します。</summary>
        public bool IsEchoRetcode { get => _isEchoRetcode; set => _isEchoRetcode = value; }
        /// <summary>以前の日付閾値フラグを取得または設定します。</summary>
        public bool IsBefore { get => _isBefore; set => _isBefore = value; }
        /// <summary>以前の日付閾値フラグを取得または設定します。</summary>
        public bool IsAfter { get => _isAfter; set => _isAfter = value; }
        /// <summary>以前の日付閾値を取得または設定します。</summary>
        public DateTime BeforeTime { get => _beforeTime; set => _beforeTime = value; }
        /// <summary>以後の日付閾値を取得または設定します。</summary>
        public DateTime AfterTime { get => _afterTime; set => _afterTime = value; }

        /// <summary>
        /// コマンドライン引数を解析し、各種設定プロパティに値を設定します。
        /// </summary>
        /// <param name="args">コマンドライン引数の配列</param>
        /// <returns>引数の解析および検証に成功した場合は <c>true</c>。それ以外の場合は <c>false</c>。</returns>
        /// <example>
        /// <code>
        /// var appArg = new ClsAppArg(logger);
        /// bool isOk = appArg.Parse(args);
        /// if (isOk)
        /// {
        ///     Console.WriteLine($"Path: {appArg.Path}");
        /// }
        /// </code>
        /// </example>
        public bool Parse(string[] args)
        {
            Dictionary<string, string> namedArgs = MdlArg.GetNamedArgs(args);
            _cmmnArgs.NamedArgs = namedArgs;
            bool isOk = _cmmnArgs.GetCommonArgs();
            string paramValue = "";

            // -----------------------------------------------------------------
            // ClsCmmnParams引数取得：ETC
            // -----------------------------------------------------------------
            _isUsage = _cmmnArgs.IsUsage;
            _verbose = _cmmnArgs.Verbose;
            _isStackTrace = _cmmnArgs.IsStackTrace;
            _isForce = _cmmnArgs.IsForce;
            _isDiff = _cmmnArgs.IsDiff;
            _diffLevel = _cmmnArgs.DiffLevel;

            // -----------------------------------------------------------------
            // ClsCmmnParams引数取得：認証情報
            // -----------------------------------------------------------------
            isOk = _cmmnArgs.GetArgsForAuth();
            _domainName = _cmmnArgs.DomainName;
            _username = _cmmnArgs.Username;
            _usernameWithoutDomain = _cmmnArgs.UsernameWithoutDomain;
            _password = _cmmnArgs.Password;
            _isLogonAlwaysOk = _cmmnArgs.IsLogonAlwaysOk;
            _isSwitchUser = _cmmnArgs.IsSwitchUser;
            _isLogon = _cmmnArgs.IsLogon;
            if (_isLogon) _isSwitchUser = true;

            // ジョブモードフラグ
            if (MdlArg.ContainsKey(namedArgs, "j"))
            {
                _logger.SetValueByKey(ClsLogger.IS_STDERR, "true");
            }

            // パスの取得
            if (isOk)
            {
                if (MdlArg.ContainsKey(namedArgs, "drive"))
                {
                    if (MdlArg.ContainsKey(namedArgs, "f")) _path = MdlArg.GetValue(namedArgs, "f") ?? "";
                    if (MdlArg.ContainsKey(namedArgs, "path")) _path = MdlArg.GetValue(namedArgs, "path") ?? "";
                }
                else
                {
                    if (MdlArg.ContainsKey(namedArgs, "f")) _path = _cmmnArgs.GetPathParam("f", MdlFile.PATH_AUTO_DETECT, false);
                    if (MdlArg.ContainsKey(namedArgs, "path")) _path = _cmmnArgs.GetPathParam("path", MdlFile.PATH_AUTO_DETECT, false);
                }
                if (string.IsNullOrEmpty(_path))
                {
                    _logger.WriteLine(MdlConst.LVL_E, "INVALID ARGUMENT : -path|-f " + _path);
                    isOk = false;
                }
            }

            // 日付変更対象の取得
            if (MdlArg.ContainsKey(namedArgs, "mode"))
            {
                string tempStr = MdlArg.GetValue(namedArgs, "mode");
                if (!string.IsNullOrEmpty(tempStr))
                {
                    int tempInt = MdlUtil.ParseInt(tempStr, MdlConst.INT_NULL);
                    if (tempInt != MdlConst.INT_NULL) _modeCode = tempInt;
                }
            }

            // 有効確認日付
            if (MdlArg.ContainsKey(namedArgs, "check-date"))
            {
                string tempStr = MdlArg.GetValue(namedArgs, "check-date");
                if (!string.IsNullOrEmpty(tempStr))
                {
                    int tempInt = MdlUtil.ParseInt(tempStr, MdlConst.INT_NULL);
                    if (tempInt != MdlConst.INT_NULL) _checkDate = tempInt;
                }
            }

            // 削除対象ディレクトリ階層(MIN)
            if (MdlArg.ContainsKey(namedArgs, "min"))
            {
                string tempStr = MdlArg.GetValue(namedArgs, "min");
                if (!string.IsNullOrEmpty(tempStr))
                {
                    int tempInt = MdlUtil.ParseInt(tempStr, MdlConst.INT_NULL);
                    if (tempInt != MdlConst.INT_NULL) _minDepth = (ulong)tempInt;
                }
            }

            // 削除対象ディレクトリ階層(MAX)
            if (MdlArg.ContainsKey(namedArgs, "max"))
            {
                string tempStr = MdlArg.GetValue(namedArgs, "max");
                if (!string.IsNullOrEmpty(tempStr))
                {
                    int tempInt = MdlUtil.ParseInt(tempStr, MdlConst.INT_NULL);
                    if (tempInt != MdlConst.INT_NULL) _maxDepth = (ulong)tempInt;
                }
            }

            if (_minDepth > _maxDepth)
            {
                isOk = false;
                _logger.WriteLine(MdlConst.LVL_E, "INVALID ARGUMENT : -min " + _minDepth + " > -max : " + _maxDepth);
            }

            // -set|-exec
            if (MdlArg.ContainsKey(namedArgs, "set") || MdlArg.ContainsKey(namedArgs, "exec"))
            {
                _isExec = true;
            }

            // listフラグ
            if (MdlArg.ContainsKey(namedArgs, "list"))
            {
                _isExec = false;
            }

            // ディレクトリ更新フラグ
            if (MdlArg.ContainsKey(namedArgs, "dir"))
            {
                _typeCode = MdlConst.INT_TYPE_ALL;
                _showTypeStr = "a";
                _isModDir = true;
                _isModFile = true;
            }

            // ディレクトリのみ更新フラグ
            if (MdlArg.ContainsKey(namedArgs, "dironly"))
            {
                _typeCode = MdlConst.INT_TYPE_DIRECTORY;
                _showTypeStr = "d";
                _isModDir = true;
                _isModFile = false;
            }

            // 変更対象
            string typeStr = MdlArg.GetValue(namedArgs, "type") ?? "";
            if (!string.IsNullOrEmpty(typeStr))
            {
                (_typeCode, _showTypeStr, _isModDir, _isModFile) = typeStr switch
                {
                    "f" => (MdlConst.INT_TYPE_FILE, "f", false, true),
                    "d" => (MdlConst.INT_TYPE_DIRECTORY, "d", true, false),
                    _ => (MdlConst.INT_TYPE_DIRECTORY, "a", true, true)
                };
            }

            // 日付指定
            if (MdlArg.ContainsKey(namedArgs, "name"))
            {
                _isGetDateByName = true;
                _isGetDateByDirName = false;
            }

            if (MdlArg.ContainsKey(namedArgs, "dirname"))
            {
                _isGetDateByName = true;
                _isGetDateByDirName = true;
            }

            if (MdlArg.ContainsKey(namedArgs, "dirnameonly"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = true;
            }

            if (MdlArg.ContainsKey(namedArgs, "pathname"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                _modifiedDateStr = MdlDate.ExtractDateFromPath(_path);
                if (string.IsNullOrEmpty(_modifiedDateStr))
                {
                    _logger.WriteLine(MdlConst.LVL_E, "-pathname IS NOT AVAILABLE : " + _path);
                    isOk = false;
                }
            }

            // 指定正規表現でヒットしたファイル名に存在する日付文字列をフォルダ内のファイル日付に設定
            if (MdlArg.ContainsKey(namedArgs, "spec"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                _isGetDateBySpecFName = true;
                string specStr = MdlArg.GetValue(namedArgs, "spec") ?? "";
                if (!string.IsNullOrEmpty(specStr))
                {
                    _incSpecsList = MdlUtil.ParseCsvToList(_incSpecsList, specStr);
                }
            }

            // 日付指定
            string dateStr = MdlArg.GetValue(namedArgs, "date") ?? "";
            if (!string.IsNullOrEmpty(dateStr))
            {
                _modifiedDateStr = MdlDate.ValidateAndFormatDate(dateStr, true);
                if (string.IsNullOrEmpty(_modifiedDateStr))
                {
                    _logger.WriteLine(MdlConst.LVL_E, "PLEASE SPECIFY THE ARGUMENT : -date " + dateStr);
                    isOk = false;
                }
                _isGetDateByName = false;
                _isGetDateByDirName = false;
            }

            if (MdlArg.ContainsKey(namedArgs, "now"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                string nowStr = MdlArg.GetValue(namedArgs, "now");
                int tempInt = MdlUtil.ParseInt(nowStr, MdlConst.INT_NULL);
                _modifiedDateStr = tempInt != MdlConst.INT_NULL
                    ? MdlDate.GetFormattedDate(DateTime.Now.AddDays(tempInt), "yyyy/MM/dd HH:mm:ss")
                    : MdlDate.GetFormattedDate("yyyy/MM/dd HH:mm:ss");
            }

            if (MdlArg.ContainsKey(namedArgs, "today"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                string todayStr = MdlArg.GetValue(namedArgs, "today");
                int tempInt = MdlUtil.ParseInt(todayStr, MdlConst.INT_NULL);
                _modifiedDateStr = tempInt != MdlConst.INT_NULL
                    ? MdlDate.GetFormattedDate(DateTime.Now.AddDays(tempInt), "yyyy/MM/dd")
                    : MdlDate.GetFormattedDate("yyyy/MM/dd");
            }

            if (MdlArg.ContainsKey(namedArgs, "tomorrow") || MdlArg.ContainsKey(namedArgs, "nextday"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                _modifiedDateStr = MdlDate.GetFormattedDate(DateTime.Now.AddDays(1), "yyyy/MM/dd");
            }

            if (MdlArg.ContainsKey(namedArgs, "yesterday") || MdlArg.ContainsKey(namedArgs, "prevday"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                _modifiedDateStr = MdlDate.GetFormattedDate(DateTime.Now.AddDays(-1), "yyyy/MM/dd");
            }

            if (MdlArg.ContainsKey(namedArgs, "term"))
            {
                _isGetDateByName = false;
                _isGetDateByDirName = false;
                string termStr = MdlArg.GetValue(namedArgs, "term");
                int tempInt = MdlUtil.ParseInt(termStr, MdlConst.INT_NULL);
                _modifiedDateStr = tempInt != MdlConst.INT_NULL
                    ? MdlDate.GetFormattedDate(DateTime.Now.AddDays(tempInt), "yyyy/MM/dd")
                    : MdlDate.GetFormattedDate("yyyy/MM/dd");
            }

            if (MdlArg.ContainsKey(namedArgs, "creationtime"))
            {
                _isCreationTime = true;
                _isLastWriteTime = false;
            }

            if (MdlArg.ContainsKey(namedArgs, "lastwritetime"))
            {
                _isCreationTime = false;
                _isLastWriteTime = true;
            }

            // 日付未指定の場合
            if (string.IsNullOrEmpty(_modifiedDateStr))
            {
                if (!_isGetDateByName && !_isGetDateByDirName && !_isGetDateBySpecFName)
                {
                    _isGetDateByName = false;
                    _isGetDateByDirName = false;
                    _modifiedDateStr = MdlDate.GetFormattedDate(DateTime.Now.AddDays(1), "yyyy/MM/dd");
                }
            }

            if (MdlArg.ContainsKey(namedArgs, "dq"))
            {
                _isDq = true;
            }

            if (MdlArg.ContainsKey(namedArgs, "check"))
            {
                _isUpdateCheck = true;
            }

            if (MdlArg.ContainsKey(namedArgs, "sym"))
            {
                _isSymLink = true;
            }

            if (MdlArg.ContainsKey(namedArgs, "echo-retcd"))
            {
                _isEchoRetcode = true;
            }

            // Net Use
            _cmmnArgs.GetNetUseArgs();
            _netSharePath = _cmmnArgs.NetSharePath;
            _driveName = _cmmnArgs.DriveName;
            _isMount = _cmmnArgs.IsMount;
            _isUmount = _cmmnArgs.IsUmount;
            _netUseOkErrNoList = _cmmnArgs.NetUseOkErrNoList;

            // フィルタ設定
            _cmmnArgs.GetFilterLists();
            _incFilesList = _cmmnArgs.IncFilesList;
            _incDirsList = _cmmnArgs.IncDirsList;
            _excFilesList = _cmmnArgs.ExcFilesList;
            _excDirsList = _cmmnArgs.ExcDirsList;
            _isRegIncBasename = _cmmnArgs.IsRegIncBasename;
            _isRegExcBasename = _cmmnArgs.IsRegExcBasename;
            _isDirFilterOr = _cmmnArgs.IsDirFilterOr;
            _isIncHitRecursive = _cmmnArgs.IsIncHitRecursive;
            _isExcHitRecursive = _cmmnArgs.IsExcHitRecursive;

            // -before yyyyMMdd   ：更新日付が指定日以前
            foreach (string key in new string[] { "before" })
            {
                if (MdlArg.ContainsKey(namedArgs, key))
                {
                    paramValue = MdlArg.GetValue(namedArgs, key);
                    if (!String.IsNullOrEmpty(paramValue))
                    {
                        switch (paramValue)
                        {
                            case "now":
                                _beforeTime = DateTime.Now;
                                _isBefore = true;
                                break;
                            case "today":
                                _beforeTime = DateTime.Today;
                                _isBefore = true;
                                break;
                            case "lastday":
                            case "yesterday":
                                _beforeTime = DateTime.Today.AddDays(-1.0);
                                _isBefore = true;
                                break;
                            case "tomorrow":
                            case "nextday":
                                _beforeTime = DateTime.Today.AddDays(1.0);
                                _isBefore = true;
                                break;
                            default:
                                double parsedDbl = MdlUtil.ParseDouble(paramValue, MdlConst.DBL_NULL);
                                if (parsedDbl != MdlConst.DBL_NULL)
                                {
                                    if (parsedDbl < 19700101.0)
                                    {
                                        if (parsedDbl < 0.0)
                                        {
                                            _beforeTime = DateTime.Today.AddDays(parsedDbl);
                                            _isBefore = true;
                                        }
                                        else
                                        {
                                            _beforeTime = DateTime.Today.AddDays(parsedDbl);
                                            _isBefore = true;
                                        }
                                    }
                                    else
                                    {
                                        if (MdlDate.TryParseDateTime(paramValue, out DateTime dtTmp))
                                        {
                                            _beforeTime = dtTmp;
                                            _isBefore = true;
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }

            // -after  yyyyMMdd   ：更新日付が指定日以降
            foreach (string key in new string[] { "after" })
            {
                if (MdlArg.ContainsKey(namedArgs, key))
                {
                    paramValue = MdlArg.GetValue(namedArgs, key);
                    if (!String.IsNullOrEmpty(paramValue))
                    {
                        switch (paramValue)
                        {
                            case "now":
                                _afterTime = DateTime.Now;
                                _isAfter = true;
                                break;
                            case "today":
                                _afterTime = DateTime.Today;
                                _isAfter = true;
                                break;
                            case "lastday":
                            case "yesterday":
                                _afterTime = DateTime.Today.AddDays(-1.0);
                                _isAfter = true;
                                break;
                            case "tomorrow":
                            case "nextday":
                                _afterTime = DateTime.Today.AddDays(1.0);
                                _isAfter = true;
                                break;
                            default:
                                double parsedDbl = MdlUtil.ParseDouble(paramValue, MdlConst.DBL_NULL);
                                if (parsedDbl != MdlConst.DBL_NULL)
                                {
                                    if (parsedDbl < 10101.0)
                                    {
                                        if (parsedDbl < 0.0)
                                        {
                                            _afterTime = DateTime.Today.AddDays(parsedDbl);
                                            _isAfter = true;
                                        }
                                        else
                                        {
                                            _afterTime = DateTime.Today.AddDays(parsedDbl);
                                            _isAfter = true;
                                        }
                                    }
                                    else
                                    {
                                        if (MdlDate.TryParseDateTime(paramValue, out DateTime dtTmp))
                                        {
                                            _afterTime = dtTmp;
                                            _isAfter = true;
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }

            namedArgs.Clear();

            return isOk;
        }

        /// <summary>
        /// アプリケーションの使用方法（ヘルプメッセージ）をログに出力します。
        /// </summary>
        /// <example>
        /// <code>
        /// var appArg = new ClsAppArg(logger);
        /// appArg.ShowUsage();
        /// </code>
        /// </example>
        public void ShowUsage()
        {
            _logger.WriteLine(MdlConst.LVL_NONE, "");
            _logger.WriteLine(MdlConst.LVL_NONE, "Usage : " + _exeBaseName + ".exe -path <path> [Option] [Option]...");
            _logger.WriteLine(MdlConst.LVL_NONE, "");
            _logger.WriteLine(MdlConst.LVL_NONE, "Basic Option        : ");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -path|-f path    : 日付更新対象  （現在値=" + _path + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -mode value      : 0:全て、1:作成日、2:更新日、3:作成日・更新日、4:アクセス日、5:作成日・アクセス日、6:更新日・アクセス日 （現在値=" + _modeCode + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -set|-exec       : 日付更新実行  （現在値=" + _isExec + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "日付指定            :               （現在値=" + _modifiedDateStr + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -date value      : 設定日付の指定（指定日時   例：" + MdlDate.GetFormattedDate("yyyy/MM/dd") + " 00:00:00）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -now   [-days]   : 設定日付の指定（現在の日時 例：" + MdlDate.GetFormattedDate(DateTime.Now, "yyyy/MM/dd HH:mm:ss") +"）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -today [-days]   : 設定日付の指定（本日の日付 例：" + MdlDate.GetFormattedDate(DateTime.Now, "yyyy/MM/dd") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -tomorrow        : 設定日付の指定（明日の日付 例：" + MdlDate.GetFormattedDate(DateTime.Now.AddDays(1), "yyyy/MM/dd") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -yesterday       : 設定日付の指定（昨日の日付 例：" + MdlDate.GetFormattedDate(DateTime.Now.AddDays(-1), "yyyy/MM/dd") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "日付取得方法指定    : ");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -creationtime    : 設定日付の指定（作成日を取得）                        （現在値=" + _isCreationTime + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -lastwritetime   : 設定日付の指定（更新日を取得）                        （現在値=" + _isLastWriteTime + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -pathname        : 設定日付の指定（-path名の先頭から日付を検索・取得）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -name            : 設定日付の指定（ファイル名の先頭から日付を検索・取得）（現在値=" + ((_isGetDateByName && !_isGetDateByDirName) ? "True" : "False") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -dirname         : 設定日付の指定（ファイル名の先頭から->DIR名の後ろからの順番で日付を検索・取得）（現在値=" + ((_isGetDateByName && _isGetDateByDirName) ? "True" : "False") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -dirnameonly     : 設定日付の指定（DIR名の後ろから設定日付を検索・取得） （現在値=" + ((!_isGetDateByName && _isGetDateByDirName) ? "True" : "False") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -spec 正規表現   : 設定日付の指定（指示ファイル名から設定日付を取得）    （現在値=" + (_isGetDateBySpecFName ? "[" + string.Join("|", _incSpecsList.ToArray()) + "]" : "False") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -check-date n    : 有効日付確認日                                        （現在値=" + _checkDate + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "日付更新対象        : ");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -type f|d|a      : 変更対象(f:file | d:dir | a:all)（現在値=" + _showTypeStr + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -sym             : シンボリックリンク判定有効化     （現在値=" + _isSymLink + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "Advanced Option     : ");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -force           : 既に設定済時の強制日付更新フラグ（現在値=" + _isForce + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -min  value      : 最小ディレクトリ階層            （現在値=" + _minDepth + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -max  value      : 最大ディレクトリ階層            （現在値=" + _maxDepth + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -if 正規表現     : 絞り込みファイル名(カンマ区切り) (例：\\.log$,\\.dat$）（現在値=[" + string.Join("|", _incFilesList.ToArray()) + "])");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -id 正規表現     : 絞り込みディレクトリ名(カンマ区切り）                （現在値=[" + string.Join("|", _incDirsList.ToArray()) + "])");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -xf 正規表現     : 除外ファイル名(カンマ区切り) (例：\\.exe$,\\.dll$）    （現在値=[" + string.Join("|", _excFilesList.ToArray()) + "])");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -xd 正規表現     : 除外ディレクトリ名(カンマ区切り）                    （現在値=[" + string.Join("|", _excDirsList.ToArray()) + "])");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -idorxd          : -id or -xdフラグ               （現在値=" + _isDirFilterOr + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -no-id-rec       : -id結果の階層下への非適用フラグ（現在値=" + !_isIncHitRecursive + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -no-xd-rec       : -xd結果の階層下への非適用フラグ（現在値=" + !_isExcHitRecursive + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -before yyyyMMdd : 更新日付が指定日以前            （現在値=" + (_isBefore ? MdlDate.GetFormattedDate(_beforeTime, "yyyyMMdd") + "：" + MdlDate.GetFormattedDate(_beforeTime, "yyyy/MM/dd HH:mm:ss") : "") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -after  yyyyMMdd : 更新日付が指定日以降            （現在値=" + (_isAfter ? MdlDate.GetFormattedDate(_afterTime, "yyyyMMdd") + "：" + MdlDate.GetFormattedDate(_afterTime, "yyyy/MM/dd HH:mm:ss") : "") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "Swich User Option   ：");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -su              ：偽装認証実行フラグ        （現在値=" + _isSwitchUser + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -def path        ：アカウント設定ファイルパス（現在値=" + _cmmnArgs.AuthDefFilePath + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -u username      ：ドメイン名\\ユーザー名     （現在値=" + _username + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -p password      ：パスワード                （現在値=" + _password + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -ignore-fail     ：認証エラー無視フラグ      （現在値=" + _isLogonAlwaysOk + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "Net Use Option ：");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -mount path      ：ネットワーク共有パス        （現在値=" + _netSharePath + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -drive [A-Z]     ：マウントドライブ文字        （現在値=" + _driveName + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -mount-ok-no i   ：net useの戻り値で正常と見なすエラー番号リスト(,|/区切り)");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -no-mount        ：NET USE 非接続フラグ        （現在値=" + (_isMount ? "False" : "True") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -no-umount       ：NET USE 非切断フラグ        （現在値=" + (_isUmount ? "False" : "True") + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "Other Option        ：");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -v|-vv|-brief    ：冗長表示|簡素表示         （現在値=" + _verbose + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -check           ：-list -v時の更新有無確認  （現在値=" + _isUpdateCheck + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -diff            ：更新のみ表示              （現在値=" + _isDiff + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -stacktrace      ：例外時STACKTRACE表示フラグ（現在値=" + _isStackTrace + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -echo-retcd      ：終了コード表示フラグ      （現在値=" + _isEchoRetcode + "）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -console mode    ：メッセージ表示 off|stdout|stderr");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -ldir path       ：ログ出力先ディレクトリパス（日付付ファイル名で出力）");
            _logger.WriteLine(MdlConst.LVL_NONE, "   -log  path       ：ログ出力ファイルパス      （-ldirより優先）");
            _logger.WriteLine(MdlConst.LVL_NONE, "注意                ：");
            _logger.WriteLine(MdlConst.LVL_NONE, "  ※ディレクトリをエクスプローラー等で開いていると更新に失敗する");
            _logger.WriteLine(MdlConst.LVL_NONE, "");
            _logger.WriteLine(MdlConst.LVL_NONE, "Return Code : " + MdlConst.LVL_I + ":SUCCESS / " + MdlConst.LVL_W + ":WARN / " + MdlConst.LVL_E + ":ERROR");
            _logger.WriteLine(MdlConst.LVL_NONE, "");
        }

    }
}
