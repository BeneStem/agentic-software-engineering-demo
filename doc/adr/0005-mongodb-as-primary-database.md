# 5. Use MongoDB as Primary Database

## Status

Accepted

## Context

The product search system needs to store and query product data with varying attributes. Requirements include:
- Flexible schema for different product types and attributes
- Full-text search capabilities for product search
- High read performance for search queries
- Ability to handle complex aggregations for filtering
- Horizontal scalability for growing product catalog

Relational databases would require complex schemas with many joins for product variants. The semi-structured nature of product data with varying attributes makes document storage more suitable.

## Decision

We will use MongoDB 4.4+ as the primary database with:
- Document model for storing product data
- MongoDB Panache Kotlin for object mapping
- Collections: products, classifications, search_analytics, availability_cache
- Indexes on: klassifikationId, price, availability fields, text search
- Aggregation pipelines for complex filter queries
- WiredTiger storage engine for compression

## Consequences

**Positive:**
- Flexible schema evolution without migrations
- Native full-text search reduces need for separate search engine
- Excellent read performance with proper indexing
- Natural fit for product data with varying attributes
- Built-in sharding for horizontal scaling
- Aggregation framework powerful for analytics

**Negative:**
- No ACID transactions across collections
- Eventually consistent in replica set configurations
- No foreign key constraints or joins
- Query language less familiar than SQL
- Index management crucial for performance
- Larger storage footprint compared to normalized relational data

**Neutral:**
- Different backup and recovery strategies
- Monitoring and operations tools specific to MongoDB
- Schema design requires different thinking than relational