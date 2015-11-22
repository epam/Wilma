#!/bin/bash

# kill test-client
wilma_tc_pid=`ps -aef|grep java|grep wilma-test-client.jar|awk '{print $2}'`
echo ${wilma_tc_pid}
if [ -n "$wilma_tc_pid" ]; then
    kill -9 ${wilma_tc_pid}
fi

# kill wilma
wilma_pid=`ps -aef|grep java|grep wilma.jar|awk '{print $2}'`
echo ${wilma_pid}
if [ -n "$wilma_pid" ]; then
    kill -9 ${wilma_pid}
fi

# kill wilma-ms
wilma_ms_pid=`ps -aef|grep java|grep wilma-message-search.jar|awk '{print $2}'`
echo ${wilma_ms_pid}
if [ -n "$wilma_ms_pid" ]; then
    kill -9 ${wilma_ms_pid}
fi

# kill test-server
wilma_ts_pid=`ps -aef|grep java|grep wilma-test-server.jar|awk '{print $2}'`
echo ${wilma_ts_pid}
if [ -n "$wilma_ts_pid" ]; then
    kill -9 ${wilma_ts_pid}
fi
