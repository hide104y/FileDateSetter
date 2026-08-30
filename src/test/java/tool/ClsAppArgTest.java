package tool;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tool.cmnclslib.cls.ClsLogger;
import tool.cmnclslib.mdl.MdlConst;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClsAppArg 単体テスト")
class ClsAppArgTest {

    @TempDir
    Path tempDir;

    private ClsAppArg createSut() {
        ClsLogger logger = new ClsLogger();
        return new ClsAppArg(logger);
    }

    @Test
    @DisplayName("コンストラクタのnull引数に対する例外検証")
    void testConstructorWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> new ClsAppArg(null));
    }

    @Test
    @DisplayName("有効なパスと基本引数のパース成功検証")
    void testParseValidPathAndBasicArgs() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{"-path", tempDirPath, "-mode", "1", "-set", "-type", "f"};

        boolean result = sut.parse(args);

        assertTrue(result);
        assertEquals(tempDirPath, sut.getPath());
        assertEquals(1, sut.getModeCode());
        assertTrue(sut.isExec());
        assertTrue(sut.isModFile());
        assertFalse(sut.isModDir());
    }

    @Test
    @DisplayName("parseにnullを渡した時の例外検証")
    void testParseWithNullThrowsException() {
        ClsAppArg sut = createSut();
        assertThrows(NullPointerException.class, () -> sut.parse(null));
    }

    @Test
    @DisplayName("パス未指定時のパース失敗検証")
    void testParseMissingPathReturnsFalse() {
        ClsAppArg sut = createSut();
        String[] args = new String[]{"-mode", "2"};

        boolean result = sut.parse(args);

        assertFalse(result);
    }

    @Test
    @DisplayName("minDepth > maxDepth 時のパース失敗検証")
    void testParseMinDepthGreaterThanMaxDepthReturnsFalse() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{"-path", tempDirPath, "-min", "5", "-max", "2"};

        boolean result = sut.parse(args);

        assertFalse(result);
    }

    @Test
    @DisplayName("-today オプションによる日付設定検証")
    void testParseTodayOptionSetsTodayFormattedDate() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{"-path", tempDirPath, "-today"};

        boolean result = sut.parse(args);

        assertTrue(result);
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        assertEquals(expectedDate, sut.getModifiedDateStr());
    }

    @Test
    @DisplayName("-tomorrow, -yesterday オプションによる日付設定検証")
    void testParseTomorrowAndYesterdayOptions() {
        ClsAppArg sut1 = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        boolean res1 = sut1.parse(new String[]{"-path", tempDirPath, "-tomorrow"});
        assertTrue(res1);
        String expectedTomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        assertEquals(expectedTomorrow, sut1.getModifiedDateStr());

        ClsAppArg sut2 = createSut();
        boolean res2 = sut2.parse(new String[]{"-path", tempDirPath, "-yesterday"});
        assertTrue(res2);
        String expectedYesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        assertEquals(expectedYesterday, sut2.getModifiedDateStr());
    }

    @Test
    @DisplayName("-date, -now, -term オプションによる日付設定検証")
    void testParseDateNowAndTermOptions() {
        ClsAppArg sutDate = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        assertTrue(sutDate.parse(new String[]{"-path", tempDirPath, "-date", "2026/08/30 15:30:00"}));
        assertEquals("2026/08/30 15:30:00", sutDate.getModifiedDateStr());

        ClsAppArg sutNow = createSut();
        assertTrue(sNow(sutNow, tempDirPath));

        ClsAppArg sutTerm = createSut();
        assertTrue(sutTerm.parse(new String[]{"-path", tempDirPath, "-term", "2"}));
        String expectedTerm = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        assertEquals(expectedTerm, sutTerm.getModifiedDateStr());
    }

    private boolean sNow(ClsAppArg sutNow, String tempDirPath) {
        return sutNow.parse(new String[]{"-path", tempDirPath, "-now"});
    }

    @Test
    @DisplayName("-creationtime, -lastwritetime オプションの検証")
    void testParseTimeOptions() {
        ClsAppArg sut1 = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        assertTrue(sut1.parse(new String[]{"-path", tempDirPath, "-creationtime"}));
        assertTrue(sut1.isCreationTime());
        assertFalse(sut1.isLastWriteTime());

        ClsAppArg sut2 = createSut();
        assertTrue(sut2.parse(new String[]{"-path", tempDirPath, "-lastwritetime"}));
        assertFalse(sut2.isCreationTime());
        assertTrue(sut2.isLastWriteTime());
    }

    @Test
    @DisplayName("-dir, -dironly, -type オプションによる種別設定検証")
    void testParseDirAndDirOnlyOptions() {
        ClsAppArg sutDir = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        boolean resDir = sutDir.parse(new String[]{"-path", tempDirPath, "-dir"});
        assertTrue(resDir);
        assertTrue(sutDir.isModDir());
        assertTrue(sutDir.isModFile());

        ClsAppArg sutDirOnly = createSut();
        boolean resDirOnly = sutDirOnly.parse(new String[]{"-path", tempDirPath, "-dironly"});
        assertTrue(resDirOnly);
        assertTrue(sutDirOnly.isModDir());
        assertFalse(sutDirOnly.isModFile());

        ClsAppArg sutTypeD = createSut();
        assertTrue(sutTypeD.parse(new String[]{"-path", tempDirPath, "-type", "d"}));
        assertTrue(sutTypeD.isModDir());
        assertFalse(sutTypeD.isModFile());

        ClsAppArg sutTypeA = createSut();
        assertTrue(sutTypeA.parse(new String[]{"-path", tempDirPath, "-type", "a"}));
        assertTrue(sutTypeA.isModDir());
        assertTrue(sutTypeA.isModFile());
    }

    @Test
    @DisplayName("各種フラグオプション（-dq, -check, -sym, -echo-retcd）の検証")
    void testParseFlagOptions() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{
            "-path", tempDirPath,
            "-dq",
            "-check",
            "-sym",
            "-echo-retcd",
            "-j",
            "-check-date", "20250101"
        };

        boolean result = sut.parse(args);

        assertTrue(result);
        assertTrue(sut.isDq());
        assertTrue(sut.isUpdateCheck());
        assertTrue(sut.isSymLink());
        assertTrue(sut.isEchoRetcode());
        assertEquals(20250101, sut.getCheckDate());
    }

    @Test
    @DisplayName("各種フィルタオプションのパース検証")
    void testParseFilterOptions() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{
            "-path", tempDirPath,
            "-if", "\\.txt$,\\.log$",
            "-id", "^tmp$",
            "-xf", "\\.bak$",
            "-xd", "^obj$",
            "-idorxd",
            "-no-id-rec",
            "-no-xd-rec",
            "-spec", "data_.*\\.csv"
        };

        boolean result = sut.parse(args);

        assertTrue(result);
        assertEquals(2, sut.getIncFilesList().size());
        assertEquals(1, sut.getIncDirsList().size());
        assertEquals(1, sut.getExcFilesList().size());
        assertEquals(1, sut.getExcDirsList().size());
        assertTrue(sut.isDirFilterOr());
        assertFalse(sut.isIncHitRecursive());
        assertFalse(sut.isExcHitRecursive());
        assertTrue(sut.isGetDateBySpecFName());
        assertEquals(1, sut.getIncSpecsList().size());
    }

    @Test
    @DisplayName("-before, -after 日時閾値オプションのパース検証")
    void testParseDateTimeThresholdOptions() {
        ClsAppArg sut = createSut();
        String tempDirPath = tempDir.toAbsolutePath().toString();
        String[] args = new String[]{
            "-path", tempDirPath,
            "-before", "20260830",
            "-after", "yesterday"
        };

        boolean result = sut.parse(args);

        assertTrue(result);
        assertTrue(sut.isBefore());
        assertTrue(sut.isAfter());
        assertNotNull(sut.getBeforeTime());
        assertNotNull(sut.getAfterTime());
    }

    @Test
    @DisplayName("委譲ゲッター・セッターの動作検証")
    void testDelegatingGettersAndSetters() {
        ClsAppArg sut = createSut();
        assertNotNull(sut.getBaseDirObj());

        sut.setExeBaseName("TestExe");
        assertEquals("TestExe", sut.getExeBaseName());

        sut.setExeDir("/opt/bin");
        assertEquals("/opt/bin", sut.getExeDir());

        sut.setReturnCode(MdlConst.LVL_W);
        assertEquals(MdlConst.LVL_W, sut.getReturnCode());

        sut.setVerbose(2);
        assertEquals(2, sut.getVerbose());

        sut.setPath("/var/log");
        assertEquals("/var/log", sut.getPath());

        sut.setModifiedDateStr("2026/01/01");
        assertEquals("2026/01/01", sut.getModifiedDateStr());

        sut.setModeCode(3);
        assertEquals(3, sut.getModeCode());

        sut.setTypeCode(MdlConst.INT_TYPE_DIRECTORY);
        assertEquals(MdlConst.INT_TYPE_DIRECTORY, sut.getTypeCode());

        sut.setDiffLevel(1);
        assertEquals(1, sut.getDiffLevel());

        sut.setCheckDate(20230101);
        assertEquals(20230101, sut.getCheckDate());

        sut.setBaseDir(true);
        assertTrue(sut.isBaseDir());
    }

    @Test
    @DisplayName("ヘルプオプションのパースおよび showUsage 実行検証")
    void testHelpOptionAndShowUsage() {
        ClsAppArg sut = createSut();
        String[] args = new String[]{"-h"};

        boolean result = sut.parse(args);

        assertTrue(result);
        assertTrue(sut.isUsage());
        assertDoesNotThrow(sut::showUsage);
    }
}
