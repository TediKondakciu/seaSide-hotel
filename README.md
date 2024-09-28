# Hotel Booking Web App

A simple hotel booking web application built using Java and Spring Boot, connected to a MySQL database. The application is containerized using Docker and deployed on a Kubernetes cluster with Minikube. It features Helm for Kubernetes configuration and uses a custom domain name (seaside-hotel.com) configured through an Ingress service.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)

## Features

- Java and Spring Boot-based hotel booking web application.
- Connected to a MySQL database for storing booking and hotel data.
- Dockerized with a `Dockerfile` and `docker-compose.yaml` for easy deployment.
- Kubernetes deployment managed with Helm.
- Secure storage of database credentials using a secrets file.
- Custom domain name setup via Ingress service.
- Deployed locally on Minikube for development and testing.
- **User Authentication and Authorization**: Implemented login functionality with role-based access control.
- **OpenAPI Documentation**: API documentation available at [http://localhost:9192/swagger-ui/index.html](http://localhost:9192/swagger-ui/index.html) (accessible when the server is running).
- **Unit Testing**: Comprehensive unit tests for all repository and service layers to ensure functionality and reliability.

## Prerequisites

- [Java 17+](https://openjdk.java.net/install/index.html)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySQL](https://dev.mysql.com/downloads/)
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Helm](https://helm.sh/docs/intro/install/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [Kubectl](https://kubernetes.io/docs/tasks/tools/)
