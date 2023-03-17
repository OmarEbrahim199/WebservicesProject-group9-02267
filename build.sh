#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd messaging-utilities
./build.sh
popd

# Build DTUFacade
pushd DTUFacade
./build.sh
popd

# Build the Account Service
pushd AccountService
./build.sh
popd

# Build the Payment Service
pushd PaymentService
./build.sh
popd

# Build the Report Service
pushd ReportService
./build.sh
popd

# Build the Token Service
pushd TokenService
./build.sh
popd