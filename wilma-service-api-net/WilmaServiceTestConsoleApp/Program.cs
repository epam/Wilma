using System;
using System.IO;
using System.Threading.Tasks;
using epam.wilma_service_api;

namespace WilmaServiceTestConsoleApp
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var wsConf = new WilmaServiceConfig("EPHUBUDW2039T1.budapest.epam.com", 1234);
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

            UploadFile(@"c:\wilmaaa\proba.json", ws.UploadConditionCheckerAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            UploadFile(@"c:\wilmaaa\proba.json", ws.UploadTemplateAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            UploadFile(@"c:\wilmaaa\proba.json", ws.UploadTemplateFormatterAsync).ContinueWith(res => { Console.WriteLine(res.Result); });
            UploadFile(@"c:\wilmaaa\proba.json", ws.UploadStubConfigurationAsync).ContinueWith(res => { Console.WriteLine(res.Result); });

            // ws.ShutdownApplicationAsync().ContinueWith(res => { Console.WriteLine("Wilma shuted down: {0}", res.Result);});

            Console.ReadLine();
        }

        private static async Task<bool> UploadFile(string filePath, Func<string, Stream, Task<bool>> func)
        {
            using (var fs = File.OpenRead(filePath))
            {
                var res = await func(Path.GetFileName(filePath), fs);
                return res;
            }
        }
    }
}
