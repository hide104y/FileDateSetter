package tool;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tool.cmnclslib.mdl.MdlConst;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClsProp 単体テスト")
class ClsPropTest {

    @Test
    @DisplayName("デフォルトコンストラクタの初期値検証")
    void testDefaultValues() {
        ClsProp baseDir = new ClsProp();

        assertEquals("", baseDir.getPath());
        assertFalse(baseDir.isBaseDir());
        assertEquals(0, baseDir.getModeCode());
        assertEquals(MdlConst.INT_TYPE_FILE, baseDir.getTypeCode());
        assertEquals("f", baseDir.getShowTypeStr());
        assertEquals("", baseDir.getModifiedDateStr());
        assertEquals(0, baseDir.getDiffLevel());
        assertEquals(19700101, baseDir.getCheckDate());
        assertEquals(0, baseDir.getMinDepth());
        assertEquals(MdlConst.LNG_MAX, baseDir.getMaxDepth());
        assertFalse(baseDir.isExec());
        assertTrue(baseDir.isModFile());
        assertFalse(baseDir.isModDir());
        assertFalse(baseDir.isGetDateByName());
        assertFalse(baseDir.isGetDateByDirName());
        assertFalse(baseDir.isGetDateBySpecFName());
        assertFalse(baseDir.isCreationTime());
        assertFalse(baseDir.isLastWriteTime());
        assertFalse(baseDir.isRegIncBasename());
        assertFalse(baseDir.isRegExcBasename());
        assertFalse(baseDir.isIncHitRecursive());
        assertFalse(baseDir.isExcHitRecursive());
        assertFalse(baseDir.isDirFilterOr());
        assertFalse(baseDir.isForce());
        assertFalse(baseDir.isDiff());
        assertFalse(baseDir.isDq());
        assertFalse(baseDir.isUpdateCheck());
        assertFalse(baseDir.isSymLink());
        assertNotNull(baseDir.getIncFilesList());
        assertNotNull(baseDir.getExcFilesList());
        assertNotNull(baseDir.getIncDirsList());
        assertNotNull(baseDir.getExcDirsList());
        assertNotNull(baseDir.getIncSpecsList());
        assertFalse(baseDir.isEchoRetcode());
        assertFalse(baseDir.isBefore());
        assertFalse(baseDir.isAfter());
        assertEquals(0, baseDir.getVerbose());
        assertFalse(baseDir.isStackTrace());
        assertEquals(MdlConst.LVL_I, baseDir.getReturnCode());
        assertFalse(baseDir.isUsage());
        assertEquals("", baseDir.getExeBaseName());
        assertEquals("", baseDir.getExeDir());
    }

    @Test
    @DisplayName("ゲッター・セッターの動作検証")
    void testGettersAndSetters() {
        ClsProp baseDir = new ClsProp();

        baseDir.setPath("/tmp/test");
        assertEquals("/tmp/test", baseDir.getPath());

        baseDir.setBaseDir(true);
        assertTrue(baseDir.isBaseDir());

        baseDir.setModeCode(3);
        assertEquals(3, baseDir.getModeCode());

        baseDir.setTypeCode(MdlConst.INT_TYPE_DIRECTORY);
        assertEquals(MdlConst.INT_TYPE_DIRECTORY, baseDir.getTypeCode());

        baseDir.setShowTypeStr("d");
        assertEquals("d", baseDir.getShowTypeStr());

        baseDir.setModifiedDateStr("2026/08/30 12:00:00");
        assertEquals("2026/08/30 12:00:00", baseDir.getModifiedDateStr());

        baseDir.setDiffLevel(2);
        assertEquals(2, baseDir.getDiffLevel());

        baseDir.setCheckDate(20200101);
        assertEquals(20200101, baseDir.getCheckDate());

        baseDir.setMinDepth(1);
        assertEquals(1, baseDir.getMinDepth());

        baseDir.setMaxDepth(5);
        assertEquals(5, baseDir.getMaxDepth());

        baseDir.setExec(true);
        assertTrue(baseDir.isExec());

        baseDir.setModFile(false);
        assertFalse(baseDir.isModFile());

        baseDir.setModDir(true);
        assertTrue(baseDir.isModDir());

        baseDir.setGetDateByName(true);
        assertTrue(baseDir.isGetDateByName());

        baseDir.setGetDateByDirName(true);
        assertTrue(baseDir.isGetDateByDirName());

        baseDir.setGetDateBySpecFName(true);
        assertTrue(baseDir.isGetDateBySpecFName());

        baseDir.setCreationTime(true);
        assertTrue(baseDir.isCreationTime());

        baseDir.setLastWriteTime(true);
        assertTrue(baseDir.isLastWriteTime());

        baseDir.setRegIncBasename(true);
        assertTrue(baseDir.isRegIncBasename());

        baseDir.setRegExcBasename(true);
        assertTrue(baseDir.isRegExcBasename());

        baseDir.setIncHitRecursive(true);
        assertTrue(baseDir.isIncHitRecursive());

        baseDir.setExcHitRecursive(true);
        assertTrue(baseDir.isExcHitRecursive());

        baseDir.setDirFilterOr(true);
        assertTrue(baseDir.isDirFilterOr());

        baseDir.setForce(true);
        assertTrue(baseDir.isForce());

        baseDir.setDiff(true);
        assertTrue(baseDir.isDiff());

        baseDir.setDq(true);
        assertTrue(baseDir.isDq());

        baseDir.setUpdateCheck(true);
        assertTrue(baseDir.isUpdateCheck());

        baseDir.setSymLink(true);
        assertTrue(baseDir.isSymLink());

        baseDir.setIncFilesList(List.of("*.txt"));
        assertEquals(List.of("*.txt"), baseDir.getIncFilesList());

        baseDir.setExcFilesList(List.of("*.tmp"));
        assertEquals(List.of("*.tmp"), baseDir.getExcFilesList());

        baseDir.setIncDirsList(List.of("log"));
        assertEquals(List.of("log"), baseDir.getIncDirsList());

        baseDir.setExcDirsList(List.of("temp"));
        assertEquals(List.of("temp"), baseDir.getExcDirsList());

        baseDir.setIncSpecsList(List.of("spec.*"));
        assertEquals(List.of("spec.*"), baseDir.getIncSpecsList());

        baseDir.setEchoRetcode(true);
        assertTrue(baseDir.isEchoRetcode());

        baseDir.setBefore(true);
        assertTrue(baseDir.isBefore());

        baseDir.setAfter(true);
        assertTrue(baseDir.isAfter());

        LocalDateTime now = LocalDateTime.now();
        baseDir.setBeforeTime(now);
        assertEquals(now, baseDir.getBeforeTime());

        baseDir.setAfterTime(now);
        assertEquals(now, baseDir.getAfterTime());

        baseDir.setVerbose(3);
        assertEquals(3, baseDir.getVerbose());

        baseDir.setStackTrace(true);
        assertTrue(baseDir.isStackTrace());

        baseDir.setReturnCode(MdlConst.LVL_W);
        assertEquals(MdlConst.LVL_W, baseDir.getReturnCode());

        baseDir.setUsage(true);
        assertTrue(baseDir.isUsage());

        baseDir.setExeBaseName("FileDateSetter");
        assertEquals("FileDateSetter", baseDir.getExeBaseName());

        baseDir.setExeDir("/bin");
        assertEquals("/bin", baseDir.getExeDir());
    }

    @Test
    @DisplayName("リストセッターにおける防御的コピーの動作検証")
    void testDefensiveCopyInListSetters() {
        ClsProp baseDir = new ClsProp();
        List<String> mutableList = new ArrayList<>(List.of("a.txt"));

        baseDir.setIncFilesList(mutableList);
        mutableList.add("b.txt");

        assertEquals(1, baseDir.getIncFilesList().size());
        assertEquals("a.txt", baseDir.getIncFilesList().get(0));
    }
}
