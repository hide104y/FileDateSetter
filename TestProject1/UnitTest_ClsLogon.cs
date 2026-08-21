using System;
using System.Runtime.Versioning;
using FileDateSetter.Class;
using Xunit;

namespace TestProject1
{
    [SupportedOSPlatform("windows")]
    public class UnitTest_ClsLogon
    {
        [Fact]
        public void InitialValues_ShouldBeDefault()
        {
            using var logon = new ClsLogon();

            Assert.Equal(0, logon.ReturnCode);
            Assert.Equal(0, logon.Verbose);
            Assert.Equal(string.Empty, logon.DomainName);
            Assert.Equal(string.Empty, logon.Username);
            Assert.Equal(string.Empty, logon.Password);
            Assert.Equal(string.Empty, logon.Message);
        }

        [Fact]
        public void Properties_SetAndGet_ShouldWorkCorrectly()
        {
            using var logon = new ClsLogon();

            logon.Verbose = 10;
            logon.DomainName = "MYDOMAIN";
            logon.Username = "AdminUser";
            logon.Password = "SecretPass123";

            Assert.Equal(10, logon.Verbose);
            Assert.Equal("MYDOMAIN", logon.DomainName);
            Assert.Equal("AdminUser", logon.Username);
            Assert.Equal("SecretPass123", logon.Password);
        }

        [Fact]
        public void Dispose_MultipleCalls_ShouldNotThrow()
        {
            var logon = new ClsLogon();
            logon.Dispose();

            var exception = Record.Exception(() => logon.Dispose());
            Assert.Null(exception);
        }

        [Fact]
        public void EnumValues_LogonSessionType_ShouldMatchExpected()
        {
            Assert.Equal(2, (int)ClsLogon.LogonSessionType.Interactive);
            Assert.Equal(3, (int)ClsLogon.LogonSessionType.Network);
            Assert.Equal(4, (int)ClsLogon.LogonSessionType.Batch);
            Assert.Equal(5, (int)ClsLogon.LogonSessionType.Service);
            Assert.Equal(8, (int)ClsLogon.LogonSessionType.NetworkCleartext);
            Assert.Equal(9, (int)ClsLogon.LogonSessionType.NewCredentials);
        }

        [Fact]
        public void EnumValues_LogonProvider_ShouldMatchExpected()
        {
            Assert.Equal(0, (int)ClsLogon.LogonProvider.Default);
            Assert.Equal(1, (int)ClsLogon.LogonProvider.WinNT35);
            Assert.Equal(2, (int)ClsLogon.LogonProvider.WinNT40);
            Assert.Equal(3, (int)ClsLogon.LogonProvider.WinNT50);
        }
    }
}
