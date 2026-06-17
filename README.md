# 🚀 Lead Recall AI

Lead Recall AI is an intelligent lead recovery and opportunity matching platform built for dealerships and inventory-driven businesses.

It transforms customer conversations into actionable sales opportunities by using AI to detect purchase intent, structure lead data, and automatically match interested buyers with newly available inventory.

---

## 💡 What it does

Lead Recall AI continuously analyzes conversations from messaging channels such as WhatsApp, extracts meaningful intent using AI, and connects potential buyers to relevant products the moment they become available.

When a new vehicle enters stock, the system automatically:

- Identifies previously interested customers  
- Matches them with compatible inventory  
- Creates sales opportunities  
- Triggers automated engagement actions (notifications, CRM updates, follow-ups)  

---

## ⚙️ Core Features

- 🧠 AI-powered purchase intent detection  
- 🎯 Lead qualification and scoring system  
- 📦 Real-time inventory monitoring & normalization  
- 🔗 Intelligent lead-to-stock matching engine  
- 📈 Automated opportunity generation  
- 💬 WhatsApp integration (Evolution API / Meta API ready)  
- 🔄 CRM & ERP integration support  
- ⚡ Event-driven architecture  
- 🌐 Multi-channel support (WhatsApp, Instagram, Web Chat, etc.)  

---

## 🧪 Example: AI in action

### 📩 Incoming message


"Quero um Corolla usado até 80 mil"


### 🧠 AI extraction result

```json
{
  "intent": "BUY_CAR",
  "vehicle": "Toyota Corolla",
  "budget": 80000,
  "confidence": 0.90
}
```

### 📊 Lead created in system
```json
{
  "phone": "5511999999999",
  "intent": "BUY_CAR",
  "vehicleInterest": "Toyota Corolla",
  "budget": 80000,
  "score": 80
}
```

## 🚗 Example: Matching engine

When a new vehicle arrives:

### 🆕 Inventory update
Toyota Corolla XEi 2022 - R$ 79.900

### 🔍 System match result
Match found:
- Lead: 5511999999999
- Interest: Toyota Corolla
- Budget compatibility: YES
- Score: 80

### 🤖 Automated action
📲 WhatsApp message sent:

"Olá! Temos um Toyota Corolla XEi 2022 disponível por R$ 79.900.
Ainda tem interesse?"

# 🏗️ Architecture Overview

Lead Recall AI is built on a scalable event-driven architecture designed for real-time processing and extensibility.

Core Components
📩 Messaging Providers
Handles incoming messages from external channels (WhatsApp, Instagram, Web Chat)
🔌 Adapter Layer
Normalizes different messaging formats into a unified event structure
⚙️ Event Processing Core
Orchestrates system events and business workflows
🧠 AI Intelligence Module
Extracts intent, sentiment, and purchase signals from conversations
🚗 Inventory Engine
Tracks stock updates and normalizes product data
🔍 Matching Engine
Connects leads with relevant inventory based on behavior and intent
🤖 Automation Layer
Executes actions such as notifications, CRM updates, and follow-ups
📊 Dashboard & Analytics
Provides insights into leads, opportunities, and conversion performance
🎯 Goal

Transform historical and real-time customer conversations into high-value sales opportunities, automatically reconnecting interested buyers with newly available inventory.

# ⚡Vision

Turn every conversation into a potential sale — even months after it happened.

# 🧠 Lead Recall AI

“Every lead remembered. Every opportunity recovered.”

# 📌 Tech highlights
Java 21 + Spring Boot
Spring Data JPA
MySQL
Event-driven architecture
REST + Messaging integration
AI-powered lead extraction (Groq / OpenAI compatible)

# 🔥 Why it matters

Most leads are lost not because they said “no”…
but because no system remembered them.

Lead Recall AI fixes that.

It turns forgotten conversations into active revenue opportunities.