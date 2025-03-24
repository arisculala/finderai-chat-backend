#!/bin/bash

# Check if Elasticsearch is already running
if curl -s http://127.0.0.1:9200 >/dev/null; then
  echo "Elasticsearch is already running."
  exit 0
fi

# Check if the container exists
if docker ps -a --filter "name=elasticsearch" | grep elasticsearch >/dev/null; then
  echo "Elasticsearch container already exists. Restarting..."
  docker start elasticsearch
else
  echo "Starting a new Elasticsearch container..."
  docker run -p 127.0.0.1:9200:9200 -d --name elasticsearch \
    -e "discovery.type=single-node" \
    -e "xpack.security.enabled=false" \
    -e "xpack.license.self_generated.type=trial" \
    -v "elasticsearch-data:/usr/share/elasticsearch/data" \
    docker.elastic.co/elasticsearch/elasticsearch:8.15.0
fi

# Wait for Elasticsearch to become available
echo "Waiting for Elasticsearch to be ready..."
until curl -s http://127.0.0.1:9200 >/dev/null; do
  echo -n "."
  sleep 3
done

echo "Elasticsearch is up and running!"
