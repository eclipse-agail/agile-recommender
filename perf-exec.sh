#!/bin/bash

for version in perf-openjdk perf-zulujdk perf-zulujdk-Xint ; do
  time docker run -it build agileiot/recommenderandconfigurator:$version
done
