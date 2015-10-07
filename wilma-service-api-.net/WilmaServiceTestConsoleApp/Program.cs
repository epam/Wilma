using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using epam.wilma_service_api;

namespace WilmaServiceTestConsoleApp
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var wsConf = new WilmaServiceConfig("http://ESYJPB-SZG", 1234);
            var ws = new WilmaService(wsConf);

            ws.GetVersionInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetActualLoadInformationAsync().ContinueWith(res => { Console.WriteLine(res.Result); });
            ws.GetMessageLoggingStatusAsync().ContinueWith(res => { Console.WriteLine(res.Result); });

            ws.SetMessageLoggingStatusAsync(WilmaService.MessageLoggingControlStatus.On).ContinueWith(res => { if (res.Result) { ws.GetMessageLoggingStatusAsync().ContinueWith(res1 => { Console.WriteLine(res1.Result); }); } });

            ws.SetOperationModeAsync(WilmaService.OperationMode.WILMA).ContinueWith(res1 => { ws.GetOperationModeAsync().ContinueWith(res => { Console.WriteLine(res.Result); }); });

           // ws.ShutdownApplicationAsync().ContinueWith(res => { Console.WriteLine("Wilma shuted down: {0}", res.Result);});

            Console.ReadLine();
        }
    }
}
