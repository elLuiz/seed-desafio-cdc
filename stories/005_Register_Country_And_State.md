## História 5: Cadastro de países
**Como usuário** do sistema

**Gostaria** de cadastrar países e estados no sistema

**Para que** os meus futuros consumidores possam informar seus países e estados ao efetuar uma compra.

## Requisitos Funcionais
**RF 01:** Ao cadastrar um país, o usuário deve enviar as seguintes informações:
- **Nome do país**, campo de texto obrigatório e único, com no máximo 120 caracteres.

**RF 02:** Ao cadastrar um estado, o usuário deve informar o seguinte:
- **Nome do Estado:**, campo de texto obrigatório e único, com no máximo 120 caracteres;
- **País de Origem:**, nome do país de origem, não podendo ultrapassar 120 caracteres.

## Requisitos Não Funcionais
**RNF 01:** O cadastro, tanto de país quanto de estado, devem ser enviados por meio do método HTTP `POST`.

**RNF 02:** Violações de regras devem ser retornados sob o status `400 Bad Request` com o payload informando quais regras foram feridas.

**RNF 03:** Os dados devem ser enviados em `application/json`.

**RNF 04:** Ao criar um recurso, o servidor deve retornar o status `201 Created` com o _header_ `Location` apontando para URI
de visualização do recurso.

**RNF 05:** As tabelas e seus respectivos relacionamentos devem ser criados por meio de _migrations_.

**RNF 06:** Os estados de um país podem ser registrados em lote (_batch_).