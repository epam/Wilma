using System;
using epam.wilma_service_api;

namespace WilmaServiceNugetTestConsoleApp
{
    public class Logger : ILogger
    {
        public void Debug(string format, params object[] prs)
        {
            Console.WriteLine(format, prs);
        }

        public void Warning(string format, params object[] prs)
        {
            Console.WriteLine(format, prs);
        }

        public void Error(string format, params object[] prs)
        {
            Console.WriteLine(format, prs);
        }

        public void Info(string format, params object[] prs)
        {
            Console.WriteLine(format, prs);
        }
    }
}
