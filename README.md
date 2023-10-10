# MyBot - Chatbot Application

MyBot is a Java-based chatbot application that allows users to interact with a chatbot and receive responses based on predefined responses and an external API.

![MyBot Logo](src/main/java/myBot/logo.png)

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Database Setup](#database-setup)
- [Usage](#usage)
- [API Integration](#api-integration)
- [Contributing](#contributing)
- [License](#license)

## Introduction

MyBot is a simple chatbot application built using Java Swing for the user interface and integrated with a MySQL database for storing chat messages. It can respond to user messages with predefined responses from the database or by querying an external API.

## Features

- User-friendly chat interface.
- Database integration for storing chat history.
- Integration with an external API (OpenAI GPT-3.5 Turbo) for dynamic responses.
- Customizable chatbot behavior through the external API.

## Getting Started

To get started with MyBot, follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone <repository-url>
   
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myBotQueries", "root", "Root1234");

String apiKey = "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";
