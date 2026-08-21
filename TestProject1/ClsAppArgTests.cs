using System;
using System.IO;
using FileDateSetter.Class;
using CmnClsLib.Class;
using Xunit;

namespace FileDateSetterClsAppArgTests
{
    public class ClsAppArgTests
    {
        private ClsAppArg CreateSut()
        {
            var logger = new ClsLogger();
            return new ClsAppArg(logger);
        }

        [Fact]
        public void Parse_ValidPathAndBasicArgs_ReturnsTrueAndSetsProperties()
        {
            // Arrange
            var sut = CreateSut();
            string tempDir = Path.Combine(Path.GetTempPath(), Path.GetRandomFileName());
            Directory.CreateDirectory(tempDir);

            try
            {
                string[] args = ["-path", tempDir, "-mode", "1", "-set", "-type", "f"];

                // Act
                bool result = sut.Parse(args);

                // Assert
                Assert.True(result);
                Assert.Equal(tempDir, sut.Path);
                Assert.Equal(1, sut.ModeCode);
                Assert.True(sut.IsExec);
                Assert.True(sut.IsModFile);
                Assert.False(sut.IsModDir);
            }
            finally
            {
                if (Directory.Exists(tempDir))
                {
                    Directory.Delete(tempDir, true);
                }
            }
        }

        [Fact]
        public void Parse_MissingPath_ReturnsFalse()
        {
            // Arrange
            var sut = CreateSut();
            string[] args = ["-mode", "2"];

            // Act
            bool result = sut.Parse(args);

            // Assert
            Assert.False(result);
        }

        [Fact]
        public void Parse_MinDepthGreaterThanMaxDepth_ReturnsFalse()
        {
            // Arrange
            var sut = CreateSut();
            string tempDir = Path.Combine(Path.GetTempPath(), Path.GetRandomFileName());
            Directory.CreateDirectory(tempDir);

            try
            {
                string[] args = ["-path", tempDir, "-min", "5", "-max", "2"];

                // Act
                bool result = sut.Parse(args);

                // Assert
                Assert.False(result);
            }
            finally
            {
                if (Directory.Exists(tempDir))
                {
                    Directory.Delete(tempDir, true);
                }
            }
        }

        [Fact]
        public void Parse_TodayOption_SetsTodayFormattedDate()
        {
            // Arrange
            var sut = CreateSut();
            string tempDir = Path.Combine(Path.GetTempPath(), Path.GetRandomFileName());
            Directory.CreateDirectory(tempDir);

            try
            {
                string[] args = ["-path", tempDir, "-today"];

                // Act
                bool result = sut.Parse(args);

                // Assert
                Assert.True(result);
                Assert.Equal(DateTime.Now.ToString("yyyy/MM/dd"), sut.ModifiedDateStr);
            }
            finally
            {
                if (Directory.Exists(tempDir))
                {
                    Directory.Delete(tempDir, true);
                }
            }
        }
    }
}
