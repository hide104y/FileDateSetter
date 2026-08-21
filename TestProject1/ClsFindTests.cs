using System;
using System.IO;
using CmnClsLib.Class;
using FileDateSetter.Class;
using Xunit;

namespace FileDateSetter.Tests
{
    public class ClsFindTests : IDisposable
    {
        private readonly string _testTmpDir;

        public ClsFindTests()
        {
            // 注意事項に従い、作業領域は「tmp」配下に作成する
            _testTmpDir = Path.Combine(System.IO.Path.GetTempPath(), @"UnitTest", @"FileDateSetter", @"ClsFind", Guid.NewGuid().ToString("N"));
            Directory.CreateDirectory(_testTmpDir);
        }

        public void Dispose()
        {
            if (Directory.Exists(_testTmpDir))
            {
                try
                {
                    Directory.Delete(_testTmpDir, true);
                }
                catch
                {
                    // テスト後の後処理のエラーは無視
                }
            }
        }

        [Fact]
        public void Constructor_ShouldInitializePropertiesToZero()
        {
            var logger = new ClsLogger();
            var appArg = new ClsAppArg(logger);
            var fsDate = new ClsFsDate(logger);

            var finder = new ClsFind(logger, appArg, fsDate);

            Assert.Equal(0u, finder.ErrorCountDirList);
            Assert.Equal(0u, finder.ErrorCountFileList);
            Assert.Equal(0u, finder.ErrorCountFileModification);
            Assert.Equal(0u, finder.SuccessCountFileModification);
            Assert.Equal(0u, finder.ErrorCountDirectoryModification);
            Assert.Equal(0u, finder.SuccessCountDirectoryModification);
            Assert.Equal(0u, finder.SkipCountFile);
            Assert.Equal(0u, finder.TotalCountFile);
            Assert.Equal(0u, finder.NoTargetCountFile);
            Assert.Equal(0u, finder.SkipCountDir);
            Assert.Equal(0u, finder.TotalCountDir);
            Assert.Equal(0u, finder.NoTargetCountDir);
        }

        [Fact]
        public void Execute_WithSingleFile_ShouldProcessFile()
        {
            string filePath = Path.Combine(_testTmpDir, "20260101_sample.txt");
            File.WriteAllText(filePath, "test content");

            var logger = new ClsLogger();
            var appArg = new ClsAppArg(logger);
            appArg.Parse(["-f", filePath, "-name"]);

            var fsDate = new ClsFsDate(logger);
            var finder = new ClsFind(logger, appArg, fsDate);

            int result = finder.Execute();

            Assert.Equal(0, result);
            Assert.Equal(1u, finder.TotalCountFile);
        }

        [Fact]
        public void Execute_WithDirectoryRecursive_ShouldProcessFilesInDirectory()
        {
            string subDir = Path.Combine(_testTmpDir, "SubDir");
            Directory.CreateDirectory(subDir);

            string file1 = Path.Combine(_testTmpDir, "20260801_file1.txt");
            string file2 = Path.Combine(subDir, "20260802_file2.txt");
            File.WriteAllText(file1, "file1");
            File.WriteAllText(file2, "file2");

            var logger = new ClsLogger();
            var appArg = new ClsAppArg(logger);
            appArg.Parse(["-f", _testTmpDir, "-name"]);
            appArg.IsBaseDir = true;

            var fsDate = new ClsFsDate(logger);
            var finder = new ClsFind(logger, appArg, fsDate);

            int result = finder.Execute();

            Assert.Equal(0, result);
            Assert.True(finder.TotalCountFile >= 2u);
        }
    }
}
