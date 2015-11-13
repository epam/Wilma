/*==========================================================================
 Copyright 2015 EPAM Systems

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
using System.Configuration;
using System.IO;
using System.Threading.Tasks;
using epam.wilma_service_api;

namespace WilmaServiceTestConsoleApp
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var host = ConfigurationManager.AppSettings["host"];
            var port = Convert.ToUInt16(ConfigurationManager.AppSettings["port"]);

            var wsConf = new WilmaServiceConfig(host, port);
            var ws = new WilmaService(wsConf, new Logger());

            ws.GetVersionInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetActualLoadInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetMessageLoggingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.SetMessageLoggingStatusAsync(WilmaService.MessageLoggingStatusEnum.On).ContinueWith(res => { if (res.Result) { ws.GetMessageLoggingStatusAsync().ContinueWith(res1 => { Console.WriteLine(res1.Result); }); } });

            ws.SetOperationModeAsync(WilmaService.OperationModeEnum.WILMA).ContinueWith(res1 => { ws.GetOperationModeAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

            ws.SetLocalhostBlockingStatusAsync(WilmaService.LocalhostControlStatusEnum.On).ContinueWith(res1 => { ws.GetLocalhostBlockingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

            ws.SetMessageMarkingStatusAsync(WilmaService.MessageMarkingStatusEnum.On).ContinueWith(res1 => { ws.GetMessageMarkingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

            ws.GetStubConfigInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.ChangeStubConfigStatusAsync("EPAMNEWS", WilmaService.StubConfigStatusEnum.Enabled).ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.ChangeStubConfigOrderAsync("EPAMNEWS", WilmaService.StubConfigOrderEnum.Up).ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.DropStubConfigAsync("EPAMNEWS").ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.PersistActualStubConfigAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            Upload(@"trial", ws.UploadConditionCheckerAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            Upload(@"trial", ws.UploadTemplateAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            Upload(@"trial", ws.UploadTemplateFormatterAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            Upload(@"trial", ws.UploadStubConfigurationAsync).ContinueWith(res => { Console.WriteLine(res.Result); });

            // ws.ShutdownApplicationAsync().ContinueWith(res => { Console.WriteLine("Wilma shuted down: {0}", res.Result);});

            Console.ReadLine();
        }

        private static async Task<bool> Upload(string text, Func<string, Stream, Task<bool>> func)
        {
            using (var ms = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(text)))
            {
                var res = await func("", ms);
                return res;
            }
        }


    }
}
