#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

TEST_LOG=$SCRIPT_DIR/test_logs/system_test_$CLIENT_OS.log
LOG_LEVEL=logging.INFO

echo "SCRIPT_DIR = ${SCRIPT_DIR}"
echo "TEST_LOG = ${TEST_LOG}"
echo "LOG_LEVEL = ${LOG_LEVEL}"

export SERVER_SHORT=$SERVER_HOSTNAME

export SERVER_HOSTNAME CLIENT_HOSTNAME ENABLED_COLLECTORS LOG_LEVEL TPM_VERSION TEST_LOG CLIENT_OS

# prepare log file directory
#rm -rf ./test_logs
#mkdir ./test_logs
rm -rf $SCRIPT_DIR/test_logs
mkdir -p $SCRIPT_DIR/test_logs

# run systems tests
echo "Running systems tests on ${SERVER_HOSTNAME} and ${CLIENT_HOSTNAME}"
# run the test script file and provide the config file
TEST_OUTPUT=$SCRIPT_DIR/test_logs/test_output$$.txt
echo "Testing system tests logging..." | tee $TEST_LOG
python $SCRIPT_DIR/system_test.py 2>&1 | tee $TEST_OUTPUT
SYSTEM_TEST_EXIT_CODE=$PIPESTATUS

# check result
if [[ $SYSTEM_TEST_EXIT_CODE == 0 ]]
then
    echo "SUCCESS: System test passed"
    exit 0
fi

echo "ERROR: System tests failed"
exit 1
