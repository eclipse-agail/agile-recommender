#!/bin/bash

for version in perf-openjdk perf-zulujdk perf-zulujdk-Xint ; do
  time docker build -f Dockerfile.$version -t agileiot/recommenderandconfigurator:$version .
done
