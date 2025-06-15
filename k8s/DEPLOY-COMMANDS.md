# Deploy Commands - FileFlow with Intelligent Logging

## 🚀 Quick Start Summary

For experienced users, the essential commands are:

```bash
# 1. Create namespace
kubectl create namespace file-flow

# 2. Deploy infrastructure  
kubectl apply -f k8s/postgres && kubectl apply -f k8s/redis
helm install graylog ./k8s/graylog/ -n file-flow

# 3. Deploy intelligent logging
kubectl apply -f k8s/fluent-bit

# 4. Deploy applications
kubectl apply -f k8s/fileflow && kubectl apply -f k8s/fileflow-front

# 5. Access applications
kubectl port-forward -n file-flow svc/fileflow-front-svc 3000:80
# Frontend: http://localhost:3000
# Graylog: http://localhost:30090 (admin/admin)
```

## Requisitos

- kubectl installed and configured
- helm installed
- Kubernetes cluster running

## Step-by-Step Deployment

### 1. Create Namespace

```bash
kubectl create namespace file-flow
```

### 2. Apply PostgreSQL Configuration

```bash
kubectl apply -f k8s/postgres/postgres-config.yaml
```

### 3. Deploy Infrastructure

```bash
# Deploy PostgreSQL and Redis
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis

# Deploy Graylog with Helm
helm install graylog ./k8s/graylog/ -n file-flow
```

### 4. Deploy Intelligent Log Collector (Fluent Bit)

```bash
# Deploy Fluent Bit with automatic tagging and intelligent processing
kubectl apply -f k8s/fluent-bit
```

### 5. Deploy Applications

```bash
# Deploy FileFlow Backend and Frontend
kubectl apply -f k8s/fileflow
kubectl apply -f k8s/fileflow-front
```

### 6. Access Applications

```bash
# Open Frontend (run in background or new terminal)
kubectl port-forward -n file-flow svc/fileflow-front-svc 3000:80

# Access URLs:
# Frontend: http://localhost:3000
# Graylog: http://localhost:30090 (admin/admin)
```

## Verification Commands

```bash
# Check all pods status
kubectl get pods -n file-flow
kubectl get pods -n kube-system | findstr fluent-bit

# Check services and port-forwards
kubectl get svc -n file-flow
netstat -an | findstr :3000   # Frontend port
netstat -an | findstr :30090  # Graylog port

# Check deployments and DaemonSet
kubectl get deployments -n file-flow
kubectl get daemonset -n kube-system fluent-bit

# View application logs
kubectl logs -n file-flow deployment/graylog --tail=20
kubectl logs -n file-flow deployment/fileflow-deployment --tail=20

# Check Fluent Bit status
kubectl get configmap fluent-bit-config -n kube-system
kubectl logs -n kube-system daemonset/fluent-bit --tail=20

# Test log collection in Graylog
# Go to: http://localhost:30090 (admin/admin)
# Search: k8s_container_name:fileflow
```

## Graylog Configuration

### 1. Configure Input (System > Inputs)

**GELF UDP Input**
- Port: 12201
- Bind address: 0.0.0.0  
- For: All pod logs collected by Fluent Bit
- **Note**: This single input receives ALL logs with rich Kubernetes metadata

### 2. Create Organized Streams (System > Streams)

Create these streams for automatic log organization:

**📱 Backend Java Stream:**
- Title: `📱 FileFlow Backend (Java/Hibernate)`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `k8s_container_name` equals `fileflow`
- ✅ Remove matches from "All messages" stream

**🌐 Frontend HTTP Stream:**
- Title: `🌐 FileFlow Frontend (Nginx/HTTP)`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `k8s_container_name` equals `fileflow-front`
- ✅ Remove matches from "All messages" stream

**🗄️ Database Stream:**
- Title: `🗄️ Database (PostgreSQL)`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `k8s_container_name` equals `postgres`
- ✅ Remove matches from "All messages" stream

**🔴 Cache Stream:**
- Title: `🔴 Cache (Redis)`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `k8s_container_name` equals `redis`
- ✅ Remove matches from "All messages" stream

**❌ Errors Stream:**
- Title: `❌ Errors - FileFlow Application`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `message` matches regex `(?i)(error|exception|failed|warn|warning)`
- ❌ **DO NOT** remove matches from "All messages" stream

**👤 User Activity Stream:**
- Title: `👤 User Activity (Login/Files)`
- Rules:
  - `k8s_namespace_name` equals `file-flow`
  - `message` matches regex `(?i)(username|login|files|upload|download)`
- ❌ **DO NOT** remove matches from "All messages" stream

### 3. Available Filter Fields

The Fluent Bit configuration automatically adds these fields for filtering:

```bash
# Component filters
app_component:backend
app_component:frontend
app_component:database
app_component:cache

# Operation type filters
operation_type:database
operation_type:http_request
operation_type:file_operation
operation_type:user_action

# Business action filters
business_action:file_management
business_action:authentication

# Technical filters
framework:hibernate
log_level:error
log_level:warning
log_level:info

# Kubernetes filters
k8s_container_name:fileflow
k8s_namespace_name:file-flow
```

## Troubleshooting

### Application Issues

**Pods in Pending state:**
```bash
kubectl describe pod <pod-name> -n file-flow
```

**Frontend not accessible on localhost:3000:**
```bash
# Check if port-forward is running
netstat -an | findstr :3000

# Restart port-forward
kubectl port-forward -n file-flow svc/fileflow-front-svc 3000:80
```

**Graylog not starting:**
```bash
kubectl logs -n file-flow deployment/graylog --tail=50
kubectl logs -n file-flow deployment/graylog-datanode --tail=50
```

### Fluent Bit Issues

**Fluent Bit not collecting logs:**
```bash
# Check if Fluent Bit pod is running
kubectl get pods -n kube-system | findstr fluent-bit

# Check Fluent Bit logs for errors
kubectl logs -n kube-system daemonset/fluent-bit --tail=50

# Verify configuration
kubectl get configmap fluent-bit-config -n kube-system
```

**Logs not appearing in Graylog:**
```bash
# Test basic search in Graylog
# Search: k8s_namespace_name:file-flow

# Check if Graylog input is receiving logs
# Go to: System > Inputs > GELF UDP (should show msg/s > 0)

# Verify stream rules are correct
# Go to: System > Streams > [Your Stream] > Manage Rules
```

### Cleanup (if needed)

```bash
# Remove applications
kubectl delete namespace file-flow

# Remove Graylog Helm release
helm uninstall graylog -n file-flow

# Remove Fluent Bit (optional)
kubectl delete -f k8s/fluent-bit

# Stop port-forwards
# Press Ctrl+C in terminals running port-forward
```

## Architecture Summary

### Application Components
- **FileFlow Backend**: 4 replicas (Java Spring Boot with Hibernate)
- **FileFlow Frontend**: 2 replicas with Nginx containers  
- **PostgreSQL**: 1 StatefulSet for data persistence
- **Redis**: 1 replica for caching
- **Graylog**: Central log aggregation with DataNode for indexing
- **MongoDB**: Graylog metadata storage

### Intelligent Logging Infrastructure
- **Fluent Bit**: DaemonSet with intelligent log processing and automatic tagging
- **Automatic Tagging**: Logs enriched with app_component, operation_type, log_level, etc.
- **Stream Organization**: Automatic separation by Backend, Frontend, Database, Cache, Errors, User Activity
- **GELF UDP Input**: Single input (port 12201) receiving all logs with rich metadata

### Log Flow & Processing
1. **Collection**: All pod logs → Fluent Bit (reads from `/var/log/containers/`)
2. **Enhancement**: Fluent Bit adds Kubernetes metadata + intelligent tags
3. **Transport**: GELF UDP → Graylog (port 12201)
4. **Organization**: Graylog streams automatically route logs by rules
5. **Analysis**: Organized dashboards, searches, and alerts

### Access Points
- **Frontend**: http://localhost:3000 (FileFlow application)
- **Graylog**: http://localhost:30090 (admin/admin - Log monitoring)

### Key Advantages
✅ **Zero Code Changes**: Applications unchanged, logs collected automatically  
✅ **Rich Metadata**: Every log includes pod, container, namespace information  
✅ **Automatic Organization**: Logs separated into relevant streams  
✅ **Intelligent Tagging**: operation_type, log_level, framework detection  
✅ **Real-time Monitoring**: Live log streaming and analysis  
✅ **Scalable Architecture**: DaemonSet scales with cluster nodes

## 📁 Configuration Files

### Fluent Bit Directory (`k8s/fluent-bit/`)
- `fluent-bit-rbac.yaml` - Permissions for Fluent Bit
- `fluent-bit-configmap.yaml` - **Intelligent configuration with automatic tagging**
- `fluent-bit-daemonset.yaml` - DaemonSet deployment

### Other Configuration
- `graylog-streams-config.json` - Stream configurations for import (optional)

## 🎯 Next Steps

After deployment:

1. **Test the setup** using verification commands above
2. **Configure Graylog streams** for automatic log organization
3. **Set up dashboards** in Graylog for monitoring
4. **Configure alerts** for proactive monitoring
5. **Explore intelligent filtering** using the available filter fields

**🚀 Result**: Complete FileFlow application with intelligent, organized logging system! 