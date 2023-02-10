﻿/*==========================================================================
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
using System.IO;
using System.Net;
using System.Net.Http;
using epam.wilma_service_api;
using epam.wilma_service_api.ServiceCommClasses;
using FluentAssertions;
using Newtonsoft.Json;
using NUnit.Framework;

namespace wilma_service_api_tests
{
    [TestFixture]
    public class WilmaServiceTests
    {
        [Test]
        public void NoILogger_WilmaServiceCreate_ThrowArgumentNullException()
        {
            Assert.Throws<ArgumentNullException>(NoILogger_WilmaServiceCreate_ThrowArgumentNullException_CallMethod);
        }

        void NoILogger_WilmaServiceCreate_ThrowArgumentNullException_CallMethod()
        {
            var wsc = new WilmaServiceConfig("host", 1);
            new WilmaService(wsc, (ILogger)null);
        }

        [Test]
        public void ConfigIsNull_WilmaServiceCreate_ThrowArgumentNullException()
        {
            Assert.Throws<ArgumentNullException>(ConfigIsNull_WilmaServiceCreate_ThrowArgumentNullException_CallMethod);
        }

        void ConfigIsNull_WilmaServiceCreate_ThrowArgumentNullException_CallMethod()
        {
            new WilmaService(null, new LoggerImpl());
        }

        [Test]
        public void ConfigLoggerOk_WilmaServiceCreate_Succes()
        {
            var host = "alma";
            ushort port = 12;
            new WilmaService(new WilmaServiceConfig(host, port), new LoggerImpl());
        }
        

        private WilmaService PrepareWilmaGet(string strContent)
        {
            var res = new HttpResponseMessage(HttpStatusCode.OK) { Content = new StringContent(strContent) };

            var messageHandler = new FakeHttpMessageHandler(res);
            var client = new HttpClient(messageHandler);

            var ws = new WilmaService(new WilmaServiceConfig("proba", 1), new LoggerImpl(), client);
            return ws;
        }

        private WilmaService PrepareWilmaPost(string strContent)
        {
            var res = new HttpResponseMessage(HttpStatusCode.OK) { Content = new StringContent(strContent) };

            var messageHandler = new FakeHttpMessageHandler(res);
            var client = new HttpClient(messageHandler);

            var ws = new WilmaService(new WilmaServiceConfig("proba", 1), new LoggerImpl(), client);
            return ws;
        }

        [Test]
        public void CreateWilmaService_CallGetVersionInformationAsync_Success()
        {
            var resStr = "version";
            var ws = PrepareWilmaGet(resStr);

            var res = ws.GetVersionInformationAsync().Result;

            res.Should().BeEquivalentTo(resStr);
        }

        [Test]
        public void CreateWilmaService_CallGetActualLoadInformationAsync_Success()
        {
            var resStr = JsonConvert.SerializeObject(new LoadInformation());
            var ws = PrepareWilmaGet(resStr);

            var res = ws.GetVersionInformationAsync().Result;

            res.Should().BeEquivalentTo(resStr);
        }

        [Test]
        public void CreateWilmaService_CallShutdownApplicationAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.ShutdownApplicationAsync().Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetMessageLoggingStatusAsync_Success()
        {
            var resStr = JsonConvert.SerializeObject(new LoggingStatus());
            var ws = PrepareWilmaGet(resStr);

            var res = ws.GetMessageLoggingStatusAsync().Result;

            Assert.IsTrue(res == WilmaService.MessageLoggingStatusEnum.Off);
        } 
        
        [Test]
        public void CreateWilmaService_CallSetMessageLoggingStatusAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.SetMessageLoggingStatusAsync(WilmaService.MessageLoggingStatusEnum.On).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetResponseVolatilityStatusAsync_Success()
        {
            var resStr = JsonConvert.SerializeObject(new ResponseVolatilityStatus());
            var ws = PrepareWilmaGet(resStr);

            var res = ws.GetResponseVolatilityStatusAsync().Result;

            Assert.IsTrue(res == WilmaService.ResponseVolatilityStatusEnum.Off);
        }

        [Test]
        public void CreateWilmaService_CallSetResponseVolatilityStatusAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.SetResponseVolatilityStatusAsync(WilmaService.ResponseVolatilityStatusEnum.On).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetOperationModeAsync_ERROR()
        {
            var ws = PrepareWilmaGet(JsonConvert.SerializeObject(new OperationMode()));

            var res = ws.GetOperationModeAsync().Result;

            Assert.IsTrue(res == WilmaService.OperationModeEnum.ERROR);
        }

        [Test]
        public void CreateWilmaService_CallSetOperationModeAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.SetOperationModeAsync(WilmaService.OperationModeEnum.STUB).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetLocalhostBlockingStatusAsync_Off()
        {
            var ws = PrepareWilmaGet(JsonConvert.SerializeObject(new LocalhostControlStatus()));

            var res = ws.GetLocalhostBlockingStatusAsync().Result;

            Assert.IsTrue(res == WilmaService.LocalhostControlStatusEnum.Off);
        }

        [Test]
        public void CreateWilmaService_CallSetLocalhostBlockingStatusAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.SetLocalhostBlockingStatusAsync(WilmaService.LocalhostControlStatusEnum.On).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetMessageMarkingStatusAsync_Off()
        {
            var ws = PrepareWilmaGet(JsonConvert.SerializeObject(new MessageMarkingStatus()));

            var res = ws.GetMessageMarkingStatusAsync().Result;

            Assert.IsTrue(res == WilmaService.MessageMarkingStatusEnum.Off);
        }

        [Test]
        public void CreateWilmaService_CallSetMessageMarkingStatusAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.SetMessageMarkingStatusAsync(WilmaService.MessageMarkingStatusEnum.On).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallGetStubConfigInformationAsync_Success()
        {
            var retObj = JsonConvert.DeserializeObject("{Prop: 'prop'}");
            var ws = PrepareWilmaGet(JsonConvert.SerializeObject(retObj));

            var res = ws.GetStubConfigInformationAsync().Result;

            res.Should().BeEquivalentTo(retObj);
        }
        
        [Test]
        public void CreateWilmaService_CallChangeStubConfigStatusAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.ChangeStubConfigStatusAsync("", WilmaService.StubConfigStatusEnum.Enabled).Result;

            Assert.IsTrue(res);
        }
        
        [Test]
        public void CreateWilmaService_CallChangeStubConfigOrderAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.ChangeStubConfigOrderAsync("", WilmaService.StubConfigOrderEnum.Up).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallDropStubConfigAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.DropStubConfigAsync("").Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallPersistActualStubConfigAsync_Success()
        {
            var ws = PrepareWilmaGet("");

            var res = ws.PersistActualStubConfigAsync().Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallUploadConditionCheckerAsync_Success()
        {
            var ws = PrepareWilmaPost("");

            var res = ws.UploadConditionCheckerAsync("", new MemoryStream()).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallUploadTemplateAsync_Success()
        {
            var ws = PrepareWilmaPost("");

            var res = ws.UploadTemplateAsync("", new MemoryStream()).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallUploadResponseFormatterAsync_Success()
        {
            var ws = PrepareWilmaPost("");

            var res = ws.UploadResponseFormatterAsync("", new MemoryStream()).Result;

            Assert.IsTrue(res);
        }

        [Test]
        public void CreateWilmaService_CallUploadStubConfigurationAsync_Success()
        {
            string stubConfigContent = "{\"wilmaStubConfiguration\": {}}";
            var ws = PrepareWilmaPost("");

            var res = ws.UploadStubConfigurationAsync("test.json", stubConfigContent).Result;

            Assert.IsTrue(res);
        }
    }
}
