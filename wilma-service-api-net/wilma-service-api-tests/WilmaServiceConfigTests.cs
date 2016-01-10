/*==========================================================================
 Copyright 2016 EPAM Systems

 This file is part of Wilma.

 Wilma is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Wilma is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
 ===========================================================================*/

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
        public void HostAndPort_CreateWilmaServiceConfig_Success()
        {
            new WilmaServiceConfig("alma", 0);
        }

        [Test]
        public void HostIsNull_CreateWilmaServiceConfig_ThrowArgumentNullException()
        {
            Action ac = () => { new WilmaServiceConfig(null, 0); };
            ac.ShouldThrow<ArgumentNullException>();
        }

        [Test]
        public void HostIsEmpty_CreateWilmaServiceConfig_ThrowArgumentNullException()
        {
            Action ac = () => { new WilmaServiceConfig("", 0); };
            ac.ShouldThrow<ArgumentNullException>();
        }

        [Test]
        public void GivenHostAndPort_CreateWilmaServiceConfig_StoredHostAndPortEqual()
        {
            var host = "alma.hu";
            ushort port = 9875;
            var res = new WilmaServiceConfig(host, port);

            res.Host.ShouldBeEquivalentTo(host);
            res.Port.ShouldBeEquivalentTo(port);
        }
    }
}
