# Gestão de Vagas - API REST com Java e Spring Boot
🛠 Pré-requisitos
Antes de começar, você precisa ter instalado:
-Java 17+

-Maven 3.8+

-PostgreSQL

Recomendação usar uma:
Uma IDE (recomendado: IntelliJ, VS Code ou Eclipse)


[![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blueviolet?logo=postgresql)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8-orange?logo=apache-maven)](https://maven.apache.org/)

> API RESTful para gestão de vagas de emprego, desenvolvida com Java 17 e Spring Boot. Projeto criado seguindo boas práticas de arquitetura e desenvolvimento backend.

---

## 🧩 Visão geral

Este projeto simula uma plataforma de gerenciamento de vagas. com exemplos práticos de:

- Padrão MVC
- Validação de dados com Bean Validation
- Utilização de DTOs
- Acesso a dados com Spring Data JPA
- Integração com banco PostgreSQL

---

## ⚙️ Funcionalidades atuais

- ✅ Administrador e funções
- ✅ Empresa e suas funções 
- ✅ Candidato e suas funções

---

## 📦 Tecnologias utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **Hibernate Validator**

---



🗂 Estrutura do projeto
gestao-vagas-java
├── controller    
├── service        
├── repository     
├── model          
└── dto        


🛠️ Em desenvolvimento
🔄 Rota /vagas/ (em breve)
Será implementado um novo endpoint para listagem de vagas por tipo (ex: CLT, PJ, estágio), permitindo filtragem via query params ou path variables.
## 🚀 Como executar o projeto
📥 Passo 1 – Clone o repositório
git clone https://github.com/ell120603/gestao-vagas-java.git
cd gestao-vagas-java


🧾Passo 2 – Configure o banco de dados PostgreSQL
Crie um banco de dados chamado gestao_vagas no seu PostgreSQL.
No arquivo src/main/resources/application.properties, configure com seus dados de acesso:
spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_vagas
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true



🚀 Passo 3 – Execute o projeto
Se estiver usando o terminal:
./mvnw spring-boot:run
