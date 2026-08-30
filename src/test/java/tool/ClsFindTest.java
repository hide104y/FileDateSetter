package tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tool.cmnclslib.cls.ClsFsDate;
import tool.cmnclslib.cls.ClsLogger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClsFind 単体テスト")
class ClsFindTest {

    @TempDir
    Path testTmpDir;

    @Test
    @DisplayName("コンストラクタによるプロパティ初期化検証")
    void testConstructorInitializesPropertiesToZero() {
        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        ClsFsDate fsDate = new ClsFsDate(logger);

        ClsFind finder = new ClsFind(logger, appArg, fsDate);

        assertEquals(0L, finder.getErrorCountDirList());
        assertEquals(0L, finder.getErrorCountFileList());
        assertEquals(0L, finder.getErrorCountFileMod());
        assertEquals(0L, finder.getSuccessCountFileMod());
        assertEquals(0L, finder.getErrorCountDirMod());
        assertEquals(0L, finder.getSuccessCountDirMod());
        assertEquals(0L, finder.getSkipCountFile());
        assertEquals(0L, finder.getTotalCountFile());
        assertEquals(0L, finder.getNoTargetCountFile());
        assertEquals(0L, finder.getSkipCountDir());
        assertEquals(0L, finder.getTotalCountDir());
        assertEquals(0L, finder.getNoTargetCountDir());
    }

    @Test
    @DisplayName("単一ファイルの走査および日付処理検証")
    void testExecuteWithSingleFileShouldProcessFile() throws IOException {
        Path filePath = testTmpDir.resolve("20260101_sample.txt");
        Files.writeString(filePath, "test content");

        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        appArg.parse(new String[]{"-f", filePath.toAbsolutePath().toString(), "-name"});

        ClsFsDate fsDate = new ClsFsDate(logger);
        ClsFind finder = new ClsFind(logger, appArg, fsDate);

        int result = finder.execute();

        assertEquals(0, result);
        assertEquals(1L, finder.getTotalCountFile());
    }

    @Test
    @DisplayName("再帰ディレクトリ走査と配下ファイルの処理検証")
    void testExecuteWithDirectoryRecursiveShouldProcessFilesInDirectory() throws IOException {
        Path subDir = testTmpDir.resolve("SubDir");
        Files.createDirectories(subDir);

        Path file1 = testTmpDir.resolve("20260801_file1.txt");
        Path file2 = subDir.resolve("20260802_file2.txt");
        Files.writeString(file1, "file1");
        Files.writeString(file2, "file2");

        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        appArg.parse(new String[]{"-f", testTmpDir.toAbsolutePath().toString(), "-name"});
        appArg.setBaseDir(true);

        ClsFsDate fsDate = new ClsFsDate(logger);
        ClsFind finder = new ClsFind(logger, appArg, fsDate);

        int result = finder.execute();

        assertEquals(0, result);
        assertTrue(finder.getTotalCountFile() >= 2L);
    }

    @Test
    @DisplayName("-set オプション指定時の日付変更実行検証")
    void testExecuteWithSetOptionExecutesModification() throws IOException {
        Path file = testTmpDir.resolve("test_target.txt");
        Files.writeString(file, "dummy content");

        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        appArg.parse(new String[]{"-f", file.toAbsolutePath().toString(), "-set", "-date", "2026/08/30 12:00:00"});

        ClsFsDate fsDate = new ClsFsDate(logger);
        ClsFind finder = new ClsFind(logger, appArg, fsDate);

        int result = finder.execute();

        assertEquals(0, result);
        assertEquals(1L, finder.getTotalCountFile());
        assertTrue(finder.getSuccessCountFileMod() + finder.getSkipCountFile() >= 1L);
    }

    @Test
    @DisplayName("ディレクトリ対象更新（-dir）オプションの実行検証")
    void testExecuteWithDirOptionProcessesDirectories() throws IOException {
        Path subDir = testTmpDir.resolve("FolderA");
        Files.createDirectories(subDir);

        ClsLogger logger = new ClsLogger();
        ClsAppArg appArg = new ClsAppArg(logger);
        appArg.parse(new String[]{"-f", testTmpDir.toAbsolutePath().toString(), "-dir", "-date", "2026/08/30 12:00:00"});
        appArg.setBaseDir(true);

        ClsFsDate fsDate = new ClsFsDate(logger);
        ClsFind finder = new ClsFind(logger, appArg, fsDate);

        int result = finder.execute();

        assertEquals(0, result);
        assertTrue(finder.getTotalCountDir() >= 1L);
    }
}
