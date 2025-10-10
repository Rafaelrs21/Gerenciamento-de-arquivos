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
kubectl apply -f k8s/sonar
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
kubectl delete -f k8s/sonar
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

### Configurar Sonarqube

Pegar a URL de acesso do Sonarqube

```bash
minikube -n file-flow service sonarqube-svc --url
```

Acessar o Sonarqube na url retornada pelo comando acima
Realizar login com as credenciais
```bash
login: admin
password: admin
```
Após realizar login, será necessário alterar a senha do usuário admin.

Após a alteração de senha, será necessário criar um token de autenticação para o Sonarqube.
Acessar o perfil, no canto superior direito, clicar em "My Account" -> "Security" e criar um token de autenticação.
Após criar o token, copiar o token gerado, pois não será possível visualizá-lo novamente.

Rodar o verify do maven, na raiz do projeto
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=file-flow \
  -Dsonar.projectName="Fileflow" \
  -Dsonar.host.url=<URL_DO_SONARQUBE> \
  -Dsonar.login=<TOKEN_GERADO>
```
O projeto será buildado, testes serão executados, é necessário que o comando ocorra sem erros e o Sonarqube será atualizado com as informações do projeto.
Apósa execução do comando, acessar o Sonarqube na url retornada pelo comando acima e verificar se o projeto foi adicionado corretamente.
Pode demorar alguns minutos para que o Sonarqube processe as informações do projeto e exiba os resultados.
Será retornada uma URL para visualizar o dashboard criado do projeto no Sonarqube, exemplo:
```
[INFO] 08:10:42.430 ANALYSIS SUCCESSFUL, you can find the results at: http://127.0.0.1:40111/dashboard?id=file-flow
```