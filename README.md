# Gestão de Vagas - Microserviço com Java e Spring Boot

🔗 **Acesso público à API Swagger:**  
[https://gestao-vagas-java-qii9.onrender.com/api/swagger-ui/index.html#/](https://gestao-vagas-java-qii9.onrender.com/api/swagger-ui/index.html#/)

---

## 🛠 Pré-requisitos

Antes de começar, você precisa ter instalado:

- Java 17+  
- Maven 3.8+  
- PostgreSQL  

💡 Recomenda-se o uso de uma IDE como: **IntelliJ**, **VS Code** ou **Eclipse**

[![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blueviolet?logo=postgresql)](https://www.postgresql.org/)  
[![Maven](https://img.shields.io/badge/Maven-3.8-orange?logo=apache-maven)](https://maven.apache.org/)

> Microserviço para gestão de vagas de emprego, desenvolvido com Java 17 e Spring Boot.  
> Projeto criado seguindo boas práticas de arquitetura e desenvolvimento backend.

---

## 🧩 Visão geral

Este projeto simula uma plataforma robusta e escalável para o gerenciamento de vagas de emprego, utilizando boas práticas de arquitetura com Java e Spring Boot.

Destaques e práticas aplicadas:

- 🧩 Arquitetura em camadas baseada no padrão MVC (Model-View-Controller), promovendo separação de responsabilidades  
- 🧠 Organização orientada a DDD (Domain-Driven Design), com foco na modelagem clara e coerente dos domínios de negócio  
- 🔐 Autenticação segura com JWT para proteção das rotas  
- ✅ Validação de dados com Bean Validation  
- 📦 Uso de DTOs (Data Transfer Objects) para comunicação entre camadas  
- 📚 Documentação interativa com Swagger  
- 🛢️ Integração com banco de dados PostgreSQL via Spring Data JPA  
- 🐳 Suporte a Docker para conteinerização e portabilidade  
- ⛔ Tratamento centralizado de erros para respostas consistentes  

---

## ⚙️ Funcionalidades atuais

- ✅ Cadastro, autenticação e gerenciamento de empresas  
- ✅ Criação, edição, listagem pública e remoção de vagas  
- ✅ Cadastro e gerenciamento de candidatos  
- ✅ Proteção de rotas com autenticação JWT  
- ✅ Documentação automática e interativa com Swagger  

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

## 🗂 Estrutura do Projeto

```
gestao-vagas-java/
├── config/         # Configurações (Banco, Swagger, etc.)
├── controllers/    # Camada de controle (REST)
├── dtos/           # Objetos de transferência de dados
├── entities/       # Entidades JPA
├── repositories/   # Interfaces do Spring Data
├── services/       # Lógica de negócio
├── exceptions/     # Tratamento de erros
└── ...
```

## 📦 Exemplos de Payloads

### 🏢 Empresas
```json
{
  "name": "Tech Solutions",
  "username": "techsolutions",
  "email": "contato@techsolutions.com",
  "password": "senha123456",
  "website": "https://techsolutions.com",
  "description": "Empresa especializada em desenvolvimento de software"
}
```
```json
{
  "name": "Innovative Tech",
  "username": "innovativetech",
  "email": "contato@innovativetech.com",
  "password": "senha7891011",
  "website": "https://innovativetech.com",
  "description": "Empresa focada em soluções inovadoras para o mercado de tecnologia"
}
```
```json
{
  "name": "Alphabet",
  "username": "alphabet",
  "email": "contato@alphabet.com",
  "password": "senha772289",
  "website": "https://alphabet.com",
  "description": "Empresa especializada em desenvolvimento de software"
}
```

### 📄 Vagas
```json
{
  "titulo": "Desenvolvedor Full Stack",
  "descricao": "Vaga para desenvolvedor full stack com experiência em React e Node.js",
  "areaAtuacao": "TI",
  "requisitos": ["React", "Node.js", "TypeScript", "PostgreSQL", "Docker"],
  "tipoContrato": "CLT",
  "localizacao": "Remoto",
  "salario": 12000.00,
  "beneficios": "VA, VR, Plano de saúde, Odonto"
}
```
```json
{
  "titulo": "Analista de Marketing Digital",
  "descricao": "Experiência com redes sociais e campanhas online",
  "areaAtuacao": "Marketing",
  "requisitos": ["Google Analytics", "Meta Ads"],
  "tipoContrato": "CLT",
  "localizacao": "São Paulo - Híbrido",
  "salario": 5000.00,
  "beneficios": "VA, VR, Saúde"
}
```
```json
{
  "titulo": "Médico Clínico Geral",
  "descricao": "Atuação em clínica médica",
  "areaAtuacao": "Medicina",
  "requisitos": ["Prontuário Eletrônico"],
  "tipoContrato": "PJ",
  "localizacao": "Rio de Janeiro",
  "salario": 15000.00,
  "beneficios": "Plano de saúde, Vale transporte"
}
```

### 👤 Candidatos
```json
{
  "name": "João Silva",
  "username": "joaosilva",
  "email": "joao.silva@example.com",
  "password": "senhaSegura123",
  "description": "Dev Java com Spring Boot"
}
```
```json
{
  "name": "Pedro Paulo",
  "username": "pedropaulo",
  "email": "pedro.paulo@example.com",
  "password": "senhaSegura1238890",
  "description": "Dev backend apaixonado por código limpo"
}
```
```json
{
  "name": "Maria Clara",
  "username": "mariaclara",
  "email": "mariaclara@example.com",
  "password": "senhaSegura1231955",
  "description": "Desenvolvedora full stack com foco em backend"
}
```

---
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

---

## 👨‍💻 Autores

Este projeto foi desenvolvido por:

- **Eliel**  
  🔹 👤 Perfil do candidato  
  🔹 💼 Cadastro e autenticação de candidato  
  🔹 💼 Cadastro e autenticação de empresa  
  🔹 📝 Listagem de vagas  
  🔹 🔧 Criação de vagas
  🧪 Testes unitários  

- **Emilie**  
  🔹 🧪 Testes unitários  

- **Francisco**  
  🔹 🧪 Testes unitários  
  🔹 👔 Exibição de perfil da empresa  
  🔹 📩 Candidatura em vaga  

- **Pietro**  
  🔹 🔁 Rotas de alteração (Candidato, Empresa)  
  🔹 👥 Listagem de candidatos da vaga  
  🔹 🔍 Pesquisa com filtros em vagas
  🔹 📚 Documentação

- **Rafael**  
  🔹 🚫 Desativação de candidato  
  🔹 📝 Alteração de dados em vagas  
  🔹 📚 Documentação  

- **Thiago**  
  🔹 🧪 Testes unitários  
  🔹 🗑️ Implementação de Soft Delete (Empresa)  
  🔹 ❌ Fechamento de vagas
