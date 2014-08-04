#!/bin/bash

ROOT='./../resources/'
PROTOS='./../resources/protos.proto'

PROTO_PATH="$ROOT $PROTOS"

protoc --java_out=. --proto_path=$PROTO_PATH