using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using epam.wilma_service_api;

namespace wilma_service_api_tests
{
    internal class LoggerImpl : ILogger
    {
        public void Debug(string format, params object[] prs)
        {
        }

        public void Warning(string format, params object[] prs)
        {
        }

        public void Error(string format, params object[] prs)
        {
        }

        public void Info(string format, params object[] prs)
        {
        }
    }
}
