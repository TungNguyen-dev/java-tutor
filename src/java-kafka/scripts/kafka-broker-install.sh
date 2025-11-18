#!/usr/bin/env bash
set -e

CONTAINER_NAME="kafka-broker"
IMAGE="apache/kafka:latest"

# Check Docker availability
if ! command -v docker &>/dev/null; then
  echo "Docker is not installed. Please install Docker Desktop for macOS."
  exit 1
fi

echo "Checking existing container..."
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "Container '${CONTAINER_NAME}' already exists. Removing it..."
  docker stop $CONTAINER_NAME >/dev/null 2>&1 || true
  docker rm $CONTAINER_NAME >/dev/null 2>&1
  echo "Removed existing container."
fi

echo "Pulling Kafka image..."
docker pull $IMAGE

echo "Starting Kafka broker..."
docker run -d \
  --name $CONTAINER_NAME \
  -p 9092:9092 \
  -p 9093:9093 \
  -e KAFKA_NODE_ID=1 \
  -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \
  -e KAFKA_NUM_PARTITIONS=3 \
  $IMAGE

echo "Kafka broker is running."
echo "Port: 9092"
echo "Container name: $CONTAINER_NAME"

echo ""
echo "Useful commands:"
echo "  docker logs -f $CONTAINER_NAME"
echo "  docker stop $CONTAINER_NAME"
echo "  docker rm $CONTAINER_NAME"
