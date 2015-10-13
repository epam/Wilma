using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Net.Http;

namespace wilma_service_api_tests
{
    internal class FakeHttpMessageHandler : HttpMessageHandler
    {
        private HttpResponseMessage response;

        public FakeHttpMessageHandler(HttpResponseMessage response)
        {
            this.response = response;
        }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            var responseTask = new TaskCompletionSource<HttpResponseMessage>();
            responseTask.SetResult(response);

            return responseTask.Task;
        }
    }
}
