#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export CLIENT_OS=centos7
export CLIENT_HOSTNAME=hirs-client-$CLIENT_OS-tpm2

export SERVER_OS=$CLIENT_OS
export SERVER_HOSTNAME=hirs-appraiser-$SERVER_OS

export ENABLED_COLLECTORS=
export TPM_VERSION=2.0

#echo "CLIENT_OS = ${CLIENT_OS}"
#echo "CLIENT_HOSTNAME = ${CLIENT_HOSTNAME}"
#echo "SERVER_OS = ${SERVER_OS}"
#echo "SERVER_HOSTNAME  = ${SERVER_HOSTNAME}"
#echo "ENABLED_COLLECTORS = ${ENABLED_COLLECTORS}"
#echo "TPM_VERSION = ${TPM_VERSION}"

$SCRIPT_DIR/systems-test.core.sh
