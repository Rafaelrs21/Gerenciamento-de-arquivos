Anotações


### Criação de Namespace no kubernetes
```
kubectl create ns file-flow
kubectl config set-context --current --namespace=file-flow
```

### Criar
```
kubectl apply -f k8s/fileflow
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis
```

### Remover
```
kubectl delete -f k8s/fileflow
kubectl delete -f k8s/postgres
kubectl delete -f k8s/redis
```

### WSL/ErrorConnect (LinuxDriver)
```
minikube -n file-flow service fileflow-svc --url
```


