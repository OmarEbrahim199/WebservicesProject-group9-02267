#!/bin/bash
set -e
docker image prune -f
docker-compose up -d rabbitmq
sleep 10
docker-compose up -d report-service-dtu token_service_dtu dtupay_server account-service-dtu payment-service-dtu

