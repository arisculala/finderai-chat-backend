# FinderAI-Chat-Backend

FinderAI Chat Backend is a robust AI-powered service that supports multi-bot, multi-user conversations with vector-based search, chat history management, and AI-driven responses. It integrates multiple AI providers, stores chat messages in PostgreSQL, and indexes messages in Elasticsearch for analytics and insights.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- Multi-AI Provider Support
  - FinderAI integrates with multiple AI models for text embeddings and chat responses:
    - OpenAI, DeepSeek, HuggingFace
- Chat Service
  - Users can interact with AI-powered bots using the POST /chat endpoint.
  - Supports multi-bot and multi-user conversations.
  - Stores chat history in PostgreSQL.
  - Indexes messages in Elasticsearch for analytics and insights.
- Text Embedding & Vector Search
  - Convert text into numerical vector embeddings.
  - Save embeddings in PostgreSQL using pgvector.
  - Perform semantic search using vector-based similarity.
- Analytics & Metrics
  - Uses Elasticsearch for chat analytics, trends, and user interactions.
  - Can be visualized with tools like Elasticvue.

## Prerequisites

Ensure you have the following installed:

- Java 21+
- Maven
- Docker
- PostgreSQL
- Elasticsearch (for analytics)
- Elasticvue (GUI for elasticsearch / Kabana)

## Setup and Installation

**1. Clone the Repository**

```bash
git clone https://github.com/arisculala/finderai-chat-backend.git
cd finderai-chat-backend
```

**2. Setup the Database**

- Ensure PostgreSQL is installed and create a database:

```bash
CREATE DATABASE finderai_chat_backend_db;
```

- Run the schema migration (integration using Liquibase)

**3. Configure Environment Variables**
Set up `appliction.yml`: (update configuration, based on your settings)

```bash
cd finderai-chat-backend/src/main/resources (or directory location)
vi application.yml
```

## Running the Application

### Run Elasticsearch

- Elasticsearch is required for chat analytics.
- Set script permissions:

```bash
cd finderai-chat-backend/scripts
chmod +x run_elasticsearch.sh
chmod +x stop_elasticsearch.sh
```

- Run Elasticsearch:

```bash
./run_elasticsearch.sh
```

- Stop Elasticsearch:

```bash
./stop_elasticsearch.sh

```

### Elasticsearch Viewer

- You can user elasticvue for your elasticsearch GUI viewer (https://elasticvue.com/installation)

### Run finderai-chat-backend

```bash
cd finderai-chat-backend
mvn clean install
mvn spring-boot:run
```

### Access Swagger

- API Documentation: http://localhost:8081/swagger-ui/index.html#/

## API Documentation

### 📌 Chat API

- **_/api/v1/chat_** (Send a chat message)

  - Send a message to a bot and receive a response
  - Request: `POST /api/v1/chat`

  ```bash
  {
    "sender": "USER",
    "botId": "bot123",
    "userId": "user123",
    "message": "cool is summer",
    "metadata": {
        "author": "John Doe 1",
        "category": "Technology",
        "timestamp": "2025-03-19T12:00:00Z"
    },
    "limit": 1
  }
  ```

  - Response:

  ```bash
  {
    "id": 294768334321225728,
    "botId": "bot123",
    "userId": "user123",
    "message": "Oops! Looks like my AI brain had a hiccup.",
    "sender": "BOT",
    "timestamp": "2025-03-24T17:44:07.433535",
    "metadata": {
        "matches": [
            {
                "provider": "fallback",
                "model": "fallback",
                "text": "Oops! Looks like my AI brain had a hiccup.",
                "metadata": null
            }
        ],
        "source": "finderai"
    }
  }
  ```

### 📌 Chat User History API

- **_/api/v1/chat/users/{userId}/history_** (Get User Chat History)

  - Retrieve all chat history for a specific user, across all bots.
  - Request: `GET /api/v1/chat/users/{userId}/history?page=0&size=10`
  - Response:

  ```bash
  {
    "page": 0,
    "size": 20,
    "totalPages": 1,
    "totalMessages": 2,
    "messages": [
        {
            "id": 294364903915917312,
            "botId": "bot123",
            "userId": "user123",
            "message": "Notifications are a key component of monitors that keep your team informed of issues and support troubleshooting. When creating your monitor, add to the Configure notifications and automations section.",
            "sender": "USER",
            "timestamp": "2025-03-23T15:01:02.129231",
            "metadata": {
                "author": "John Doe 1",
                "category": "Technology",
                "timestamp": "2025-03-19T12:00:00Z"
            }
        },
        {
            "id": 294365062255087616,
            "botId": "bot123",
            "userId": "user123",
            "message": "how are you today?",
            "sender": "USER",
            "timestamp": "2025-03-23T15:01:39.880014",
            "metadata": {
                "author": "John Doe 1",
                "category": "Technology",
                "timestamp": "2025-03-19T12:00:00Z"
            }
        }
      ]
  }
  ```

### 📌 Chat Bot/User History API

- **_/api/v1/bots/{botId}/users/{userId}/history_** (Get Bot/User Chat History)

  - Retrieve chat history for a specific user and bot.
  - Request: `GET /api/v1/bots/{botId}/users/{userId}/history?page=0&size=10`
  - Response:

  ```bash
  {
    "page": 0,
    "size": 20,
    "totalPages": 1,
    "totalMessages": 2,
    "messages": [
        {
            "id": 294364903915917312,
            "botId": "bot123",
            "userId": "user123",
            "message": "Notifications are a key component of monitors that keep your team informed of issues and support troubleshooting. When creating your monitor, add to the Configure notifications and automations section.",
            "sender": "USER",
            "timestamp": "2025-03-23T15:01:02.129231",
            "metadata": {
                "author": "John Doe 1",
                "category": "Technology",
                "timestamp": "2025-03-19T12:00:00Z"
            }
        },
        {
            "id": 294365062255087616,
            "botId": "bot123",
            "userId": "user123",
            "message": "how are you today?",
            "sender": "USER",
            "timestamp": "2025-03-23T15:01:39.880014",
            "metadata": {
                "author": "John Doe 1",
                "category": "Technology",
                "timestamp": "2025-03-19T12:00:00Z"
            }
        }
      ]
  }
  ```

## Future Enhancements

- Multi-turn AI Conversations – More natural, contextual bot responses.
- Integration with More AI Providers – Expand support for additional LLMs.
- Chatbot Customization – Allow users to configure bot personalities.
- Advanced Analytics – Monitor chat trends, sentiment analysis.

## Contributing

To contribute:

- Fork the Repository
- Crete a feature branch: `git checkout -b feat:new-feature`
- Commit your changes: `git commit -m 'added new feature'`
- Push to the branch: `git push origin feat/new-feature`
- Create a pull Request

## License

This project is licensed under the **MIT License**.

## Contact

- 📧 Email: arisculala@gmail.com
- 🐙 GitHub: [arisculala](https://github.com/arisculala "Visit MyGithub")
- Enjoy using **FinderAI-Chat-Backend**! 🚀 If you have any questions, feel free to reach out. 😊
