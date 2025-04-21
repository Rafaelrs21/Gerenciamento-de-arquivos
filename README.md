# 📄 Gerenciamento de Documentos

## 📌 Sobre o Projeto
O **Gerenciamento de Documentos** é um sistema desenvolvido para facilitar a organização, armazenamento e controle de documentos de forma segura e eficiente. Ele permite o gerenciamento centralizado de arquivos..

---

## 🚀 Funcionalidades
- Upload, download e gerenciamento de documentos
- Controle de acesso e permissões

---

## 👥 Colaboradores
- [Iasmim Silveira](https://github.com/iasmimsilveira)
- [Rafael Santiago](https://github.com/Rafaelrs21)
- [Breno Lobato](https://github.com/kick250)
- [Lucio Caetano](https://github.com/caetanoinf)
- [Gabriel Fonseca](https://github.com/gabrielborel)
---

# Como Rodar o Projeto

Este projeto utiliza **Docker Compose** para o banco de dados e **Maven Wrapper** para rodar a aplicação Spring Boot.

## Pré-requisitos
Antes de começar, certifique-se de ter instalado:
- [Docker e Docker Compose](https://docs.docker.com/get-docker/)
- [Java 17+](https://adoptium.net/)

## Configuração inicial
Antes de rodar o projeto, crie o arquivo de configuração necessário:
```sh
cp src/main/resources/application.yml.example src/main/resources/application.yml
```
Dev:
```sh
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml
```
Test:
```sh
cp src/main/resources/application-test.yml.example src/main/resources/application-test.yml
```

Isso criará o arquivo de configuração base para a aplicação.

## Passos para subir o projeto

1. **Subir uma network**
   ```sh
   docker network create file-flow-network
   ```
   Isso iniciará a network necessária para comunicar o BD e a APP.

2. **Subir o banco de dados**
   ```sh
   docker-compose -f docker-compose.dev-database.yml up -d
   ```
   Isso iniciará o banco de dados em segundo plano.

3. **Subir o fileflow project**
   ```sh
   docker-compose -f docker-compose.app.yml up --build
   ```
   Isso iniciará o projeto em segundo plano.

4. **Rodar a aplicação Spring Boot**
   ```sh
   ./mvnw spring-boot:run
   ```
   Isso compilará e iniciará o servidor
   (Se estiver utilizando em memória).

6. **Acessar a aplicação**
   Normalmente, o projeto estará disponível em:
   ```
   http://localhost:8080
   ```

## Como parar os serviços
Para desligar o banco de dados, execute:
```sh
docker-compose -f docker-compose.dev-database.yml down
```
Isso encerrará os containers do banco de dados.

Para desligar o fileflow , execute:
```sh
docker-compose -f docker-compose.app.yml down
```
Isso encerrará os containers do app.

Para desligar a network, execute:
```sh
docker network rm file-flow-network
```
Isso encerrará a network do projeto.

Se precisar interromper a aplicação Spring Boot, basta pressionar `CTRL + C` no terminal onde ela está rodando.

## Criando uma tabela no banco de dados com Flyway
Este projeto utiliza o **Flyway** para versionamento do banco de dados. Para criar uma nova tabela, siga os passos abaixo:

1. **Criar um novo arquivo de migração** na pasta `src/main/resources/db/migration/`.
   O nome do arquivo deve seguir o padrão:
   ```
   V{versão}__{descricao}.sql
   ```
   Exemplo:
   ```
   V1__criar_tabela_usuarios.sql
   ```

2. **Escrever a SQL de criação da tabela** dentro do arquivo:
   ```sql
   CREATE TABLE usuarios (
       id SERIAL PRIMARY KEY,
       nome VARCHAR(100) NOT NULL,
       email VARCHAR(100) UNIQUE NOT NULL,
       criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

3. **Rodar a aplicação novamente** para que o Flyway aplique a migração automaticamente:
   ```sh
   ./mvnw spring-boot:run
   ```

Se a migração for bem-sucedida, a tabela será criada no banco de dados automaticamente.

---
Se tiver dúvidas, entre em contato com a equipe! 🚀
