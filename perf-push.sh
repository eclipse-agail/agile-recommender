#!/bin/bash

for version in perf-openjdk perf-zulujdk perf-zulujdk-Xint ; do
  time docker push agileiot/recommenderandconfigurator:$version
done
