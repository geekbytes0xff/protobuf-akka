#!/bin/bash

echo "deleting events in cassandra"

sudo docker stop cassandra-demo

sudo docker rm cassandra-demo

docker run -d --name cassandra-demo cassandra:latest

echo "*************** RESET DONE **************"