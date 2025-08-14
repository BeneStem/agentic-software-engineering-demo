# Deployment Diagram - Finden Product Search System

## Overview

The Deployment diagram shows how the Finden system is deployed to the Google Cloud Platform (GCP) using Kubernetes. The infrastructure is managed using Terraform and follows cloud-native best practices for scalability and reliability.

## Deployment Diagram - Production Environment

```mermaid
flowchart TD
    %% Styling
    classDef browser fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef cdn fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef loadbalancer fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef node fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px
    classDef pod fill:#ffebee,stroke:#b71c1c,stroke-width:2px
    classDef database fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    classDef messaging fill:#e0f2f1,stroke:#004d40,stroke-width:2px
    classDef monitoring fill:#f5f5f5,stroke:#424242,stroke-width:1px
    
    %% External Users
    Users[Users<br/>Web Browsers]:::browser
    
    %% CDN Layer
    CDN[Google Cloud CDN<br/><i>Static Assets<br/>Global Distribution</i>]:::cdn
    
    %% Load Balancer
    subgraph GCP[Google Cloud Platform - europe-west3]
        GLB[Google Load Balancer<br/><i>HTTPS Termination<br/>Path-based Routing</i>]:::loadbalancer
        
        %% Kubernetes Cluster
        subgraph GKE[GKE Cluster - Production]
            %% Namespaces
            subgraph ns-backend[Namespace: finden-backend]
                %% Backend Nodes
                subgraph node1[Node Pool: Backend (n2-standard-2)]
                    subgraph backend-pod1[Pod: Backend-1]
                        backend1[Quarkus Backend<br/><i>Container<br/>512Mi-1536Mi RAM</i>]:::pod
                    end
                    subgraph backend-pod2[Pod: Backend-2]
                        backend2[Quarkus Backend<br/><i>Container<br/>512Mi-1536Mi RAM</i>]:::pod
                    end
                    subgraph backend-pod3[Pod: Backend-3]
                        backend3[Quarkus Backend<br/><i>Container<br/>512Mi-1536Mi RAM</i>]:::pod
                    end
                end
                
                BackendSvc[Service: Backend<br/><i>ClusterIP<br/>Port 8081</i>]:::loadbalancer
            end
            
            subgraph ns-frontend[Namespace: finden-frontend]
                %% Frontend Nodes
                subgraph node2[Node Pool: Frontend (n2-standard-2)]
                    subgraph frontend-pod1[Pod: Frontend-1]
                        ssr1[SSR Server<br/><i>Node.js Container<br/>256Mi-512Mi RAM</i>]:::pod
                    end
                    subgraph frontend-pod2[Pod: Frontend-2]
                        ssr2[SSR Server<br/><i>Node.js Container<br/>256Mi-512Mi RAM</i>]:::pod
                    end
                end
                
                FrontendSvc[Service: Frontend<br/><i>NodePort<br/>Port 3000</i>]:::loadbalancer
            end
            
            %% Ingress
            Ingress[Ingress Controller<br/><i>nginx<br/>Path Routing</i>]:::loadbalancer
        end
        
        %% Managed Services
        subgraph ManagedServices[Managed Services]
            MongoDB[MongoDB Atlas<br/><i>M30 Cluster<br/>3 Replicas<br/>europe-west3</i>]:::database
            
            subgraph Kafka[Confluent Cloud]
                KafkaCluster[Kafka Cluster<br/><i>Multi-zone<br/>Schema Registry</i>]:::messaging
            end
            
            subgraph Monitoring[Operations]
                Prometheus[Prometheus<br/><i>Metrics Collection</i>]:::monitoring
                Stackdriver[Google Cloud Monitoring<br/><i>Logs & Traces</i>]:::monitoring
                Grafana[Grafana<br/><i>Dashboards</i>]:::monitoring
            end
        end
        
        %% Container Registry
        GCR[Google Container Registry<br/><i>Docker Images<br/>finden-backend:latest<br/>finden-frontend:latest</i>]:::database
    end
    
    %% Connections
    Users -->|HTTPS| CDN
    Users -->|API Calls| GLB
    CDN -->|Cache Miss| GLB
    
    GLB -->|Path: /api/*| Ingress
    GLB -->|Path: /*| Ingress
    
    Ingress -->|/api/*| BackendSvc
    Ingress -->|/*| FrontendSvc
    
    BackendSvc --> backend1
    BackendSvc --> backend2
    BackendSvc --> backend3
    
    FrontendSvc --> ssr1
    FrontendSvc --> ssr2
    
    backend1 --> MongoDB
    backend2 --> MongoDB
    backend3 --> MongoDB
    
    backend1 --> KafkaCluster
    backend2 --> KafkaCluster
    backend3 --> KafkaCluster
    
    backend1 -.->|Metrics| Prometheus
    backend2 -.->|Metrics| Prometheus
    backend3 -.->|Metrics| Prometheus
    
    ssr1 -.->|Logs| Stackdriver
    ssr2 -.->|Logs| Stackdriver
    
    Prometheus -->|Visualize| Grafana
    
    GKE -->|Pull Images| GCR
```

## Infrastructure Components

### Google Cloud Platform

#### Regions and Zones
- **Primary Region**: europe-west3 (Frankfurt)
- **Zones**: Multi-zone deployment (a, b, c)
- **Disaster Recovery**: Cross-region backups to europe-west1

#### Networking
- **VPC**: Custom VPC with private subnets
- **Firewall Rules**: Restrictive ingress, allow egress
- **Cloud NAT**: For outbound internet access
- **Private Google Access**: For GCP services

### Kubernetes Infrastructure

#### GKE Cluster Configuration
```yaml
Cluster:
  name: finden-production-cluster
  version: 1.27.x (Regular Channel)
  location: europe-west3
  node_locations: [europe-west3-a, europe-west3-b, europe-west3-c]
  networking:
    cluster_ipv4_cidr: 10.0.0.0/14
    services_ipv4_cidr: 10.4.0.0/19
  security:
    workload_identity: enabled
    binary_authorization: enabled
    private_cluster: true
```

#### Node Pools

##### Backend Node Pool
```yaml
backend-pool:
  machine_type: n2-standard-2
  disk_size: 100GB
  disk_type: pd-standard
  min_nodes: 2
  max_nodes: 10
  autoscaling: enabled
  auto_repair: true
  auto_upgrade: true
  preemptible: false
```

##### Frontend Node Pool
```yaml
frontend-pool:
  machine_type: n2-standard-2
  disk_size: 50GB
  disk_type: pd-standard
  min_nodes: 1
  max_nodes: 5
  autoscaling: enabled
  auto_repair: true
  auto_upgrade: true
  preemptible: true  # Cost optimization
```

### Container Deployments

#### Backend Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: finden-backend-deployment
  namespace: finden-backend
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    spec:
      containers:
      - name: finden-backend
        image: eu.gcr.io/project/finden-backend:latest
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1536Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /q/health/live
            port: 8081
          initialDelaySeconds: 40
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8081
          initialDelaySeconds: 40
```

#### Frontend Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: finden-frontend-deployment
  namespace: finden-frontend
spec:
  replicas: 2
  strategy:
    type: RollingUpdate
  template:
    spec:
      containers:
      - name: finden-frontend
        image: eu.gcr.io/project/finden-frontend:latest
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "200m"
        env:
        - name: NODE_ENV
          value: "production"
        - name: API_URL
          value: "http://finden-backend-service:8081"
```

### External Services

#### MongoDB Atlas
- **Cluster Tier**: M30 (Production)
- **Replicas**: 3-node replica set
- **Region**: GCP europe-west3
- **Backup**: Continuous backups with point-in-time recovery
- **Encryption**: Encryption at rest and in transit
- **Access**: VPC peering with GKE cluster

#### Confluent Cloud (Kafka)
- **Cluster Type**: Dedicated cluster
- **Availability**: Multi-zone deployment
- **Topics**:
  - `product.events` (Partitions: 6, Replication: 3)
  - `pricing.events` (Partitions: 3, Replication: 3)
  - `availability.events` (Partitions: 3, Replication: 3)
  - `search.analytics` (Partitions: 6, Replication: 2)
- **Schema Registry**: Confluent Schema Registry with Avro

#### Google Cloud CDN
- **Cache Policies**:
  - Static assets: 1 year cache
  - API responses: No cache
  - HTML: 5 minute cache
- **Compression**: Automatic gzip/brotli
- **Global Points of Presence**: 100+ locations

### Monitoring and Operations

#### Prometheus
- **Metrics Collection**: 15-second intervals
- **Retention**: 15 days
- **Scraped Endpoints**:
  - `/q/metrics` (Backend)
  - `/metrics` (Frontend)
- **Custom Metrics**:
  - Search latency
  - Filter performance
  - Cache hit rates

#### Google Cloud Monitoring (Stackdriver)
- **Log Aggregation**: Structured JSON logs
- **Log Retention**: 30 days (default), 365 days (audit)
- **Alert Policies**:
  - High error rate (>1%)
  - High latency (P95 > 500ms)
  - Pod restarts
  - Disk usage

#### Grafana Dashboards
- **System Dashboard**: CPU, memory, network
- **Application Dashboard**: Request rates, latencies, errors
- **Business Dashboard**: Search volume, popular products, filter usage

## Deployment Environments

### Development Environment
- **Cluster**: Single-node Minikube or Docker Desktop
- **Database**: Local MongoDB container
- **Kafka**: Local Kafka container
- **Configuration**: `.env` files

### Staging Environment
- **Cluster**: GKE with reduced resources
- **Database**: MongoDB Atlas M10 tier
- **Kafka**: Confluent Cloud Basic cluster
- **Purpose**: Integration testing, UAT

### Production Environment
- **Cluster**: Full GKE setup as described above
- **Database**: MongoDB Atlas M30+ tier
- **Kafka**: Confluent Cloud Dedicated cluster
- **SLA**: 99.9% availability target

## CI/CD Pipeline

### GitLab CI Stages
1. **Build**
   - Compile Kotlin/Quarkus backend
   - Build Vue.js frontend
   - Run unit tests

2. **Test**
   - Integration tests with TestContainers
   - E2E tests with Playwright
   - Security scanning

3. **Package**
   - Build Docker images
   - Push to Google Container Registry
   - Tag with version

4. **Deploy**
   - Update Kubernetes manifests
   - Apply with kubectl
   - Verify deployment health

### Deployment Strategy
- **Method**: Rolling deployment with zero downtime
- **Rollback**: Automatic on health check failure
- **Canary Releases**: 10% traffic to new version initially
- **Blue-Green**: Available for major releases

## Security Considerations

### Network Security
- **Private GKE Cluster**: No public IPs on nodes
- **Cloud Armor**: DDoS protection and WAF
- **SSL/TLS**: Enforced for all external communication
- **Network Policies**: Restrict pod-to-pod communication

### Access Control
- **Workload Identity**: GKE to GCP service authentication
- **RBAC**: Kubernetes role-based access control
- **Service Accounts**: Least privilege principle
- **Secrets Management**: Google Secret Manager

### Compliance
- **GDPR**: No personal data storage
- **Data Residency**: EU region only
- **Audit Logging**: All administrative actions logged
- **Vulnerability Scanning**: Regular image scanning

## Scaling Strategy

### Horizontal Pod Autoscaling
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: backend-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: finden-backend-deployment
  minReplicas: 3
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

### Cluster Autoscaling
- **Node Pool Autoscaling**: Based on pending pods
- **Scale Up**: When pods cannot be scheduled
- **Scale Down**: When nodes underutilized for 10 minutes

---

*The Deployment diagram shows the production infrastructure. For architectural decisions, see [Decisions](./decisions.md).*