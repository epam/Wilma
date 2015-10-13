using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace epam.wilma_service_api.StubClasses
{
    public class Condition
    {
        public ConditionTypes ConditionType { get; set; }

        public enum ConditionTypes
        {
            AND,
            OR,
            NOT,
            SIMPLE
        }
    }
}
