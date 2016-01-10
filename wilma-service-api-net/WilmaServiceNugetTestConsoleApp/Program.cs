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
using System.Configuration;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using epam.wilma_service_api;

namespace WilmaServiceNugetTestConsoleApp
{
    class Program
    {
        static void Main(string[] args)
        {
            var host = ConfigurationManager.AppSettings["host"];
            var port = Convert.ToUInt16(ConfigurationManager.AppSettings["port"]);

            var wsConf = new WilmaServiceConfig(host, port);
            var ws = new WilmaService(wsConf, new Logger());

            ws.GetVersionInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetActualLoadInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetMessageLoggingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.SetMessageLoggingStatusAsync(WilmaService.MessageLoggingControlStatus.On).ContinueWith(res => { if (res.Result) { ws.GetMessageLoggingStatusAsync().ContinueWith(res1 => { Console.WriteLine(res1.Result); }); } });

            ws.SetOperationModeAsync(WilmaService.OperationModes.WILMA).ContinueWith(res1 => { ws.GetOperationModeAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

            ws.SetLocalhostBlockingStatusAsync(WilmaService.LocalhostControlStatuses.On).ContinueWith(res1 => { ws.GetLocalhostBlockingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

            ws.GetStubConfigInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.ChangeStubConfigStatusAsync("EPAMNEWS", WilmaService.StubConfigStatus.Enabled).ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.ChangeStubConfigOrderAsync("EPAMNEWS", WilmaService.StubConfigOrder.Up).ContinueWith(res => { Console.WriteLine(res.Result); });
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
            using (var ms = new MemoryStream(Encoding.UTF8.GetBytes(text)))
            {
                var res = await func("", ms);
                return res;
            }
        }
    }
}
