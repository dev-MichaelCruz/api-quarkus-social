# API Quarkus Social

Esta API foi desenvolvida usando a linguagem **Java**, em conjunto com a plataforma **Quarkus**.
Tem a proposta de uma rede social com seguidores, postagens de texto e relação de usuários e seguidores.

## Requisitos
* Maven para gerenciar as dependências do projeto;
* Quarkus CLI: [quarkus.io](https://quarkus.io/get-started/);
* Algum banco de dados (neste exemplo foi utilizado PostgreSQL);

## Como utilizar
* Faça o fork desse repositório;
* Clone para a sua máquina;
* Acesse o arquivo **apllication.properties** para configurar suas variáveis de ambientes para utilizar seu banco de dados local;
* Pelo terminal, na pasta `quarkus-social`, execute o comando: `quarkus dev` ou `./mvnw quarkus:dev`
	*	**Atenção:** Caso queira utilizar com banco de dados temporário, basta rodar um dos comandos acima;

## Documentação
Quando a aplicação estiver no ar, acesse pelo navegador `http://localhost:8080/q/swagger-ui` para conferir toda a documentação da API e testar os endpoints.
