#!/bin/bash

docker run -ti --rm \
  --link nosql_redis_1:redis \
  --net=nosql_default \
  redis redis-cli -h redis -p 6379

