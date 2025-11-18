#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="kafka-broker"
WORKDIR="/opt/kafka/bin"
BOOTSTRAP="localhost:9092"
TOPIC_NAME="${1:-test-topic}"

# Colors for readability
GREEN="\033[0;32m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # no color

log() { echo -e "${GREEN}[INFO]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() {
  echo -e "${RED}[ERROR]${NC} $1"
  exit 1
}

check_docker() {
  if ! command -v docker &>/dev/null; then
    error "Docker is not installed or not in PATH."
  fi
}

check_container_running() {
  if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    error "Kafka container '${CONTAINER_NAME}' is not running."
  fi
}

wait_for_kafka() {
  log "Waiting for Kafka to be available on ${BOOTSTRAP} ..."
  for i in {1..20}; do
    if (echo >/dev/tcp/localhost/9092) &>/dev/null; then
      log "Kafka is ready."
      return
    fi
    warn "Retrying in 1 second... ($i/20)"
    sleep 1
  done
  error "Kafka did not become ready in time."
}

create_topic() {
  log "Creating topic '${TOPIC_NAME}' (idempotent)..."
  docker exec -it "$CONTAINER_NAME" bash -c "
    cd $WORKDIR &&
    sh kafka-topics.sh --create \
      --if-not-exists \
      --topic ${TOPIC_NAME} \
      --bootstrap-server ${BOOTSTRAP}
  "
}

list_topics() {
  log "Existing topics:"
  docker exec -it "$CONTAINER_NAME" bash -c "
    cd $WORKDIR &&
    sh kafka-topics.sh --list --bootstrap-server ${BOOTSTRAP}
  "
}

produce_messages() {
  log "Producing a single message 'Hello, World' to topic '${TOPIC_NAME}'."
  docker exec -i "$CONTAINER_NAME" bash -c "
    cd \"$WORKDIR\" &&
    printf 'Hello, World\n' | sh kafka-console-producer.sh \
      --topic ${TOPIC_NAME} \
      --bootstrap-server ${BOOTSTRAP}
  "
}

consume_messages() {
  log "Consuming messages from topic '${TOPIC_NAME}' with a 30s timeout."
  docker exec -i "$CONTAINER_NAME" bash -c "
    cd \"$WORKDIR\" &&
    timeout 30s sh kafka-console-consumer.sh \
      --topic ${TOPIC_NAME} \
      --from-beginning \
      --bootstrap-server ${BOOTSTRAP}
      --property print.key=true \
      --property print.timestamp=true \
      --property print.offset=true \
      --property print.partition=true
  "
}

# ----------------------------
# Main Execution Flow
# ----------------------------

check_docker
check_container_running
wait_for_kafka

create_topic
list_topics

produce_messages
consume_messages
