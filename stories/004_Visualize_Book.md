## História 4: Exibição de um livro
**Como usuário** do sistema,

**Gostaria** de visualizar as informações de um livro registrado

**Para que** possa saber mais detalhes sobre ele além do título.

### Requisitos Funcionais

**RF 01:** Ao buscar por um determinado livro, o sistema deve exibir as seguintes informações:
- Título;
- Nome do autor;
- Descrição do autor;
- Preço;
- Resumo;
- Sumário;
- Número de páginas;
- ISBN;
- Data de publicação.

**RF 02:** Caso o livro não exista, o sistema deve retornar uma mensagem de erro ao usuário informando que o livro não existe.

**RF 03:** A data de publicação deve ser retornada no seguinte formato: MM/yyyy, onde `MM` representa o mês e `yyyy`, o ano.

**RF 04:** O preço deve ser formatado em duas casas decimais.

### Requisitos Não Funcionais

**RNF 01:** A requisição deve ocorrer por meio do método HTTP GET.

**RNF 02:** O servidor deve retornar o conteúdo do livro em `application/json` sob o status HTTP `200 OK`.

**RNF 03:** Caso o livro não exista, o servidor deve retornar o payload de erro sob o status HTTP `404 Not Found`.