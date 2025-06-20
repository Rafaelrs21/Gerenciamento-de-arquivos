Anotações


### Criação de Namespace no kubernetes
```bash
kubectl create ns file-flow
kubectl config set-context --current --namespace=file-flow
```

### Criar secrets

### Criar
```bash
kubectl apply -f k8s/postgres
kubectl apply -f k8s/redis
kubectl apply -f k8s/fluent-bit
helm install graylog ./k8s/graylog/ -n file-flow

kubectl apply -f k8s/fileflow
kubectl apply -f k8s/fileflow-front
```

### Remover
```bash
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

### Configurar Graylog

Acessar o container graylog e pegar a senha do root

```bash
Log do graylog com a senha, parecido com isto:
It seems you are starting Graylog for the first time. To set up a fresh install, a setup interface has

been started. You must log in to it to perform the initial configuration and continue.


Initial configuration is accessible at 0.0.0.0:9000, with username 'admin' and password 'moDDJApZpX'.

Try clicking on http://admin:moDDJApZpX@0.0.0.0:9000⁠
```

Acessar site do graylog: http://localhost:30090
Preencher o organizationName, colocar um periodo e prover certificado para o node.

Após a conclusão(aprox 3min), quando ficar verde clicar em Resume.. sera redirecionado para a tela de login.

logar com admin/admin

Ir em System -> Content Packs e instalar o pack content-pack-fe76a969-79b0-4b96-8d68-010233862bab-1.json

Com isso vai estar configurado para receber logs do fileflow.

Unico porem que não vai categorizar os logs, vai ser apenas um log.
Precisa ir em System -> Inputs e obter o Id destes inputs(codigo varia sempre), atraves do show received messages.
Usar este gl2_source_input:68554048908334446a4cda76 para configurar os filtros no Streams.





