## História 2: Criação de categorias
**Como usuário** 

**Gostaria** de realizar o cadastro de uma categoria no sistema

**Para que** possa relacioná-la a outras entidades.

### Requisitos Funcionais

**RF 1:** O usuário deverá informar um nome de categoria único para cadastrá-la no sistema.

**RF 2:** Caso a categoria não seja única, o sistema deverá alertar o usuário sobre a falha 
no cadastro.

**RF 3:** Caso o nome não seja informado ou ultrapasse 120 caracteres, o sistema deverá impedir o cadastro da categoria.

### Requisitos Não Funcionais
**RNF 1:** Os dados de criação deverão ser recebidos através de um endpoint POST, com content type igual a _application/json_.

**RNF 2:** Erros de input devem ser retornados sob o status `400 Bad Request`.

**RNF 3:** Erros de _constraint_ devem ser retornados sob o status `409 Conflict`.