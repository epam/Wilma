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
using epam.wilma_service_api;

namespace WilmaServiceTestConsoleApp
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
