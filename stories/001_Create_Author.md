## História 1
**Como usuário**

**Gostaria** de cadastrar um **Autor**

Para que, posteriormente, possa atribuir-lhe livros e então vendê-los.

### Requisitos Funcionais

**RF 1:** O usuário deverá informar os seguintes campos para o cadastro de um autor:
- Nome do autor
    - Campo de texto, não deve ser **nulo** nem ultrapassar **255** caracteres;
- Email
    - Campo de texto, não podendo ultrapassar 255 caracteres;
    - Deverá ser um email válido.
- Descrição
    - Campo de texto obrigatório, contendo no máximo 400 caracteres.

**RF 2:** Caso algum campo obrigatório não esteja preenchido ou esteja com um formato inválido,
o sistema deverá retornar um erro informando quais campos contêm erros para que o usuário possa corrigi-los.

**RF 3:** O email deverá ser de uso único. Caso haja algum autor com o mesmo email, o sistema deverá rejeitar a
criação do autor.

### Requisitos de Qualidade

**RNF 1:** O servidor deverá receber as requisições em JSON, através do header _Content-Type_ igual a _application/json_.

**RNF 2:** Sendo corretamente validados, os dados deverão ser persistidos em um banco de dados relacional.

**RNF 3:** Ao cadastrar o autor, o servidor deverá retornar status 201 com o ID do novo autor no header _Location_. O valor
desse header deverá apontar para a URI com a representação do autor criado.

**RNF 4:** Erros de inputs deverão ser retornados em JSON sob o status **400 Bad Request**, conforme exemplo abaixo:
```json
{
  "errors": [
    {
      "field": "email",
      "error": "Descrição do erro",
      "code": "Código do erro"
    }
  ],
  "occurredAt": "2024-10-09T03:00:00"
}
```
**RNF 5:** Os campos de data deverão ser persistidos em [ISO-8601](https://www.iso.org/iso-8601-date-and-time-format.html).

**RNF 6:** Em casos de erros no servidor, o sistema deverá retornar o status 500 com um mensagem genérica.

**RNF 7:** Todo input recebido pelo servidor deverá ser logado adequadamente pelo servidor. Dados sensíveis deverão ser transformados criptografados.

**RNF 8:** Os testes de integração deverão persistir os dados em uma instância do banco PostgresSQL por meio da lib [TestContainers](https://java.testcontainers.org/).

**RNF 9:** Alterações no **schema** do banco de dados deverão ser versionados por meio do flyway, seguindo o padrão: _**VX_nome_migration.sql**_. Onde X representa um dígito entre 0 e 9.

**RNF 10:** Para evitar inconsistências devido a _race conditions_, deverá ser implementada uma chave de unicidade no email do autor.

**RNF 11:** Caso haja algum erro de conflito no servidor, o sistema deverá retornar o status **409 Conflict**.
