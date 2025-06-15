Anotações


### Criação de Namespace no kubernetes
```
kubectl create ns file-flow
kubectl config set-context --current --namespace=file-flow
```

### Criar
```
kubectl apply -f k8s/fileflow
kubectl apply -f k8s/fileflow-front
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis
kubectl apply -f k8s/graylog
```

### Remover
```
kubectl delete -f k8s/fileflow
kubectl delete -f k8s/fileflow-front
kubectl delete -f k8s/postgres
kubectl delete -f k8s/redis
helm uninstall graylog -n file-flow
```

### WSL/ErrorConnect (LinuxDriver)
```
minikube -n file-flow service fileflow-front-svc --url
```

### Graylog Access
```
kubectl port-forward -n file-flow svc/graylog 9000:9000
```


choco install openssl

## Graylog DataNode: Geração de Certificados e Configuração do Secret

O DataNode do Graylog exige certificados SSL para funcionar corretamente. Como este repositório pode ser clonado e executado em diferentes ambientes, **os certificados não são versionados**. Cada usuário deve gerar os seus próprios certificados e criar um Secret no Kubernetes antes de subir o ambiente.

### 1. Gerando certificados autoassinados

Execute os comandos abaixo para gerar os certificados necessários:

```sh
# Gere o certificado HTTP
openssl req -x509 -newkey rsa:4096 -keyout http.key -out http.crt -days 365 -nodes -subj "/CN=graylog-datanode"
openssl pkcs12 -export -out http.p12 -inkey http.key -in http.crt -password pass:graylog

# Gere o certificado Transport
openssl req -x509 -newkey rsa:4096 -keyout transport.key -out transport.crt -days 365 -nodes -subj "/CN=graylog-datanode"
openssl pkcs12 -export -out transport.p12 -inkey transport.key -in transport.crt -password pass:graylog
```

### 2. Criando o Secret no Kubernetes

Com os arquivos `http.p12` e `transport.p12` gerados, crie o Secret:

```sh
kubectl create secret generic graylog-datanode-certs --from-file=http.p12 --from-file=transport.p12 -n file-flow
```

### 3. Subindo o ambiente

Depois de criar o Secret, aplique os manifests normalmente:

```sh
kubectl apply -f k8s/graylog
```

- Se precisar remover e recriar o Secret, use:
  ```sh
  kubectl delete secret graylog-datanode-certs -n file-flow
  ```


