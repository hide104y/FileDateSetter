using System;
using System.IO;
using CmnClsLib.Module;
using FileDateSetter;
using Xunit;

namespace FileDateSetter.Tests
{
    public class FileDateSetterProgramTests
    {
        [Fact]
        public void Main_WithHelpArgument_ReturnsWarningExitCode()
        {
            // Arrange
            string[] args = ["-?"];

            // Act
            int exitCode = Program.Main(args);

            // Assert
            Assert.Equal(MdlConst.LVL_W, exitCode);
        }

        [Fact]
        public void Main_WithInvalidArgument_ReturnsErrorExitCode()
        {
            // Arrange
            string[] args = ["--invalid-argument-test"];

            // Act
            int exitCode = Program.Main(args);

            // Assert
            Assert.Equal(MdlConst.LVL_E, exitCode);
        }

        [Fact]
        public void Main_WithNoArgs_ReturnsErrorExitCode()
        {
            // Arrange
            string[] args = Array.Empty<string>();

            // Act
            int exitCode = Program.Main(args);

            // Assert
            Assert.Equal(MdlConst.LVL_E, exitCode);
        }

        [Fact]
        public void Main_WithNonExistentPath_ReturnsErrorExitCode()
        {
            // Arrange
            string nonExistentPath = Path.Combine(System.IO.Path.GetTempPath(), @"UnitTest", @"FileDateSetter", @"Program-nonExistentPath");
            string[] args = ["-path", nonExistentPath];

            // Act
            int exitCode = Program.Main(args);

            // Assert
            Assert.Equal(MdlConst.LVL_E, exitCode);
        }

        [Fact]
        public void Main_WithValidDirectoryPath_ReturnsSuccessExitCode()
        {
            // Arrange
            string tempDir = Path.Combine(System.IO.Path.GetTempPath(), @"UnitTest", @"FileDateSetter", @"Program");
            Directory.CreateDirectory(tempDir);
            try
            {
                string[] args = ["-path", tempDir, "/v:0"];

                // Act
                int exitCode = Program.Main(args);

                // Assert
                Assert.Equal(MdlConst.LVL_I, exitCode);
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
