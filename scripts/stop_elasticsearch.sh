#!/bin/bash

# check if Elasticsearch is running
if docker ps --filter "name=elasticsearch" --filter "status=running" | grep elasticsearch >/dev/null; then
  echo "Stopping Elasticsearch..."
  docker stop elasticsearch
  echo "Elasticsearch stopped."
else
  echo "Elasticsearch is not running."
fi

# optionally, remove the container (uncomment if needed)
# docker rm elasticsearch
