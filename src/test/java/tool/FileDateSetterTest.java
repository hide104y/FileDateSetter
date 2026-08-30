package tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tool.cmnclslib.mdl.MdlConst;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileDateSetter メインプロセス単体テスト")
class FileDateSetterTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("ヘルプ引数指定時は警告コード（LVL_W）を返却する")
    void testMainProcessWithHelpArgumentReturnsWarningExitCode() {
        String[] args = new String[]{"-?"};

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_W, exitCode);
    }

    @Test
    @DisplayName("不正引数指定時はエラーコード（LVL_E）を返却する")
    void testMainProcessWithInvalidArgumentReturnsErrorExitCode() {
        String[] args = new String[]{"--invalid-argument-test"};

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_E, exitCode);
    }

    @Test
    @DisplayName("引数なし実行時はエラーコード（LVL_E）を返却する")
    void testMainProcessWithNoArgsReturnsErrorExitCode() {
        String[] args = new String[0];

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_E, exitCode);
    }

    @Test
    @DisplayName("存在しないパス指定時はエラーコード（LVL_E）を返却する")
    void testMainProcessWithNonExistentPathReturnsErrorExitCode() {
        Path nonExistentPath = tempDir.resolve("nonExistentSubFolder");
        String[] args = new String[]{"-path", nonExistentPath.toAbsolutePath().toString()};

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_E, exitCode);
    }

    @Test
    @DisplayName("有効なディレクトリパス指定時は正常コード（LVL_I）を返却する")
    void testMainProcessWithValidDirectoryPathReturnsSuccessExitCode() {
        String[] args = new String[]{"-path", tempDir.toAbsolutePath().toString(), "-v", "0"};

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_I, exitCode);
    }

    @Test
    @DisplayName("有効なファイルパス指定および実行オプション指定時は正常コード（LVL_I）を返却する")
    void testMainProcessWithValidFileAndSetOptionReturnsSuccessExitCode() throws IOException {
        Path testFile = tempDir.resolve("sample.txt");
        Files.writeString(testFile, "sample data");
        String[] args = new String[]{"-path", testFile.toAbsolutePath().toString(), "-set", "-date", "2026/08/30 10:00:00", "-v", "1"};

        int exitCode = FileDateSetter.mainProcess(args);

        assertEquals(MdlConst.LVL_I, exitCode);
    }
}
