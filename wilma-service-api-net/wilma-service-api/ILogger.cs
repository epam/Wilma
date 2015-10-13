using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace epam.wilma_service_api
{
    /// <summary>
    /// ILogger interface. Implement this to support WilmaService logging.
    /// </summary>
    public interface ILogger
    {
        void Debug(string format, params object[] prs);
        void Warning(string format, params object[] prs);
        void Error(string format, params object[] prs);
        void Info(string format, params object[] prs);
    }
}
