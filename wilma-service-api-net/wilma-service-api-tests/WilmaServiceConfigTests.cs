using System;
using epam.wilma_service_api;
using FluentAssertions;
using NUnit.Framework;

namespace wilma_service_api_tests
{
    [TestFixture]
    public class WilmaServiceConfigTests
    {
        [Test]
        public void HostAndPort_CreateWilamServiceConfig_Success()
        {
            var res = new WilmaServiceConfig("alma", 0);
            res.Should().NotBe(null);
        }

        [Test]
        public void HostIsNull_CreateWilamServiceConfig_ThrowArgumentNullException()
        {
            Action ac = () => { new WilmaServiceConfig(null, 0); };
            ac.ShouldThrow<ArgumentNullException>();
        }

        [Test]
        public void HostIsEmpty_CreateWilamServiceConfig_ThrowArgumentNullException()
        {
            Action ac = () => { new WilmaServiceConfig("", 0); };
            ac.ShouldThrow<ArgumentNullException>();
        }

        [Test]
        public void GivenHostAndPort_CreateWilamServiceConfig_StoredHostAndPortEqual()
        {
            var host = "alma.hu";
            uint port = 98765;
            var res = new WilmaServiceConfig(host, port);

            res.Host.ShouldBeEquivalentTo(host);
            res.Port.ShouldBeEquivalentTo(port);
        }
    }
}
