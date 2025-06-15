# Deploy Commands - FileFlow with Graylog Integration

## 🚀 Quick Start Summary

For experienced users, the essential commands are:

```bash
# 1. Create namespace
kubectl create namespace file-flow

# 2. Deploy infrastructure  
kubectl apply -f k8s/postgres && kubectl apply -f k8s/redis && kubectl apply -f k8s/fluent-bit
helm install graylog ./k8s/graylog/ -n file-flow


kubectl apply -f k8s/fileflow && kubectl apply -f k8s/fileflow-front

kubectl port-forward -n file-flow svc/fileflow-front-svc 3000:80
# Frontend: http://localhost:3000
# Graylog: http://localhost:30090 (admin/admin)
```

## Prerequisites

- kubectl installed and configured
- helm installed
- Kubernetes cluster running 


### 1. Create Namespace

```bash
kubectl create namespace file-flow
```

### 2. Apply Configuration Files

```bash
# PostgreSQL configuration
kubectl apply -f ./k8s/postgres/postgres-config.yaml
```

### 3. Deploy Databases

```bash
# PostgreSQL
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis
helm install graylog ./k8s/graylog/ -n file-flow
```

### 4. Deploy Log Collector (Fluent Bit) - Basic

```bash
# Deploy Fluent Bit basic configuration
kubectl apply -f k8s/fluent-bit-rbac.yaml
kubectl apply -f k8s/fluent-bit-configmap.yaml  
kubectl apply -f k8s/fluent-bit-daemonset.yaml
```

### 5. Upgrade to Enhanced Fluent Bit (Recommended)

```bash
# Remove basic configuration
kubectl delete configmap fluent-bit-config -n kube-system

# Apply enhanced configuration with automatic tagging
kubectl apply -f k8s/fluent-bit-enhanced-configmap.yaml

# Update DaemonSet to use enhanced configuration
kubectl patch daemonset fluent-bit -n kube-system -p '{"spec":{"template":{"spec":{"volumes":[{"name":"fluent-bit-config","configMap":{"name":"fluent-bit-config-enhanced"}}]}}}}'

# Verify Fluent Bit is running with new configuration
kubectl get pods -n kube-system | findstr fluent-bit
kubectl logs -n kube-system daemonset/fluent-bit --tail=10
```

### 6. Deploy Applications

```bash
# FileFlow Backend
kubectl apply -f k8s/fileflow
kubectl apply -f k8s/fileflow-front
```

### 7. Access Applications

```bash
# Open Frontend (in new terminal/background)
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
netstat -an | findstr :3000   # Check if frontend port is open
netstat -an | findstr :30090  # Check if Graylog port is open

# Check deployments and DaemonSet
kubectl get deployments -n file-flow
kubectl get daemonset -n kube-system fluent-bit

# View application logs
kubectl logs -n file-flow deployment/graylog --tail=20
kubectl logs -n file-flow deployment/fileflow-deployment --tail=20

# Check Fluent Bit configuration and logs
kubectl get configmap fluent-bit-config-enhanced -n kube-system
kubectl logs -n kube-system daemonset/fluent-bit --tail=20
kubectl describe daemonset fluent-bit -n kube-system

# Test log collection in Graylog
# Go to: http://localhost:30090 (admin/admin)
# Search: k8s_container_name:fileflow
```

## Access Information

- **Graylog Web UI**: http://localhost:30090
- **Username**: admin
- **Password**: admin

## Required Graylog Configuration

### 1. Configure Input (System > Inputs)

**GELF UDP Input**
- Port: 12201
- Bind address: 0.0.0.0  
- For: All pod logs collected by Fluent Bit
- **Note**: This single input will receive ALL logs from your cluster with rich Kubernetes metadata

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

### 3. Test Filters

Use these filters in Graylog search to test:

```bash
# Backend Java logs
k8s_container_name:fileflow

# Frontend HTTP logs  
k8s_container_name:fileflow-front

# All application logs
k8s_namespace_name:file-flow

# Only errors
k8s_namespace_name:file-flow AND (level:error OR message:*error*)

# Hibernate queries
message:*Hibernate*

# HTTP requests
message:*HTTP*
```

## Troubleshooting

### Application Issues

**Pods in Pending state:**
```bash
kubectl describe pod <pod-name> -n file-flow
```

**Service not accessible:**
```bash
kubectl get endpoints -n file-flow
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
kubectl get configmap fluent-bit-config-enhanced -n kube-system
```

**Logs not appearing in Graylog streams:**
```bash
# Test basic search in Graylog
# Search: k8s_namespace_name:file-flow

# Check if Graylog input is receiving logs
# Go to: System > Inputs > GELF UDP (should show msg/s > 0)

# Verify stream rules are correct
# Go to: System > Streams > [Your Stream] > Manage Rules
```

**Fluent Bit configuration issues:**
```bash
# Reset to basic configuration
kubectl delete configmap fluent-bit-config-enhanced -n kube-system
kubectl apply -f k8s/fluent-bit-configmap.yaml
kubectl patch daemonset fluent-bit -n kube-system -p '{"spec":{"template":{"spec":{"volumes":[{"name":"fluent-bit-config","configMap":{"name":"fluent-bit-config"}}]}}}}'

# Re-apply enhanced configuration
kubectl apply -f k8s/fluent-bit-enhanced-configmap.yaml
kubectl patch daemonset fluent-bit -n kube-system -p '{"spec":{"template":{"spec":{"volumes":[{"name":"fluent-bit-config","configMap":{"name":"fluent-bit-config-enhanced"}}]}}}}'
```

### Cleanup (if needed)

```bash
# Remove applications
kubectl delete namespace file-flow

# Remove Graylog Helm release
helm uninstall graylog -n file-flow

# Remove Fluent Bit (optional)
kubectl delete daemonset fluent-bit -n kube-system
kubectl delete configmap fluent-bit-config-enhanced -n kube-system
kubectl delete configmap fluent-bit-config -n kube-system
kubectl delete clusterrole fluent-bit
kubectl delete clusterrolebinding fluent-bit
kubectl delete serviceaccount fluent-bit -n kube-system

# Stop port-forwards
# Find and kill port-forward processes
netstat -an | findstr :3000
netstat -an | findstr :30090
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

### Logging Infrastructure
- **Fluent Bit Enhanced**: DaemonSet running on all nodes with intelligent log processing
- **Automatic Tagging**: Logs enriched with app_component, operation_type, log_level, etc.
- **Stream Organization**: Automatic separation by Backend, Frontend, Database, Cache, Errors, User Activity
- **GELF UDP Input**: Single input (port 12201) receiving all logs with rich metadata

### Log Flow & Processing
1. **Collection**: All pod logs → Fluent Bit (reads from `/var/log/containers/`)
2. **Enhancement**: Fluent Bit adds Kubernetes metadata + custom tags
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

## 📁 Related Files

### Fluent Bit Configuration Files
- `fluent-bit-rbac.yaml` - Permissions for Fluent Bit
- `fluent-bit-configmap.yaml` - Basic Fluent Bit configuration  
- `fluent-bit-enhanced-configmap.yaml` - **Enhanced configuration with automatic tagging**
- `fluent-bit-daemonset.yaml` - DaemonSet deployment

### Documentation Files
- `AUTOMATIC-SETUP-GUIDE.md` - Detailed guide for automatic log organization
- `GRAYLOG-FILTERS-GUIDE.md` - Comprehensive filtering and search guide
- `FLUENT-BIT-README.md` - Technical details about Fluent Bit implementation

### Configuration Files
- `graylog-streams-config.json` - Stream configurations for import (if needed)

## 🎯 Next Steps

After deployment:

1. **Test the setup** using verification commands above
2. **Configure Graylog streams** for automatic log organization
3. **Set up dashboards** in Graylog for monitoring
4. **Configure alerts** for proactive monitoring
5. **Explore log filters** using the GRAYLOG-FILTERS-GUIDE.md

For detailed configuration and advanced features, see:
- `AUTOMATIC-SETUP-GUIDE.md` - Complete automation guide
- `GRAYLOG-FILTERS-GUIDE.md` - Advanced filtering techniques 