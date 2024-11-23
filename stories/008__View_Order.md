## História: Visualizar Pedido
Como usuário 

Gostaria de visualizar os detalhes de uma compra

Para que possa confirmar se os detalhes, itens e o valor da compra estão de acordo com o esperado.

## Requisitos Funcionais

### RF 1: Visualização de compra
Ao visualizar uma compra, o sistema deve exibir as seguintes informações ao usuário:
- Nome do usuário, a partir da concatenação do primeiro nome e sobrenome;
- Documento do usuário (CPF ou CNPJ);
  - O documento deve ser formatado de acordo com o tipo do documento.
- Endereço de entrega;
- Número de telefone do usuário;
- Status da compra;
- Livros pedidos e o total de cada livro;
- Total da compra;
- Informações do cupom aplicado à compra, caso tenha sido utilizado algum cupom;
- Total da compra com o desconto, se foi aplicado algum cupom de desconto.

## Requisitos Não Funcionais
### RNF 1: Endpoint de visualização
O servidor deve fornecer um endpoint do tipo `GET` para visualizar as informações da compra, conforme RF 1. O endpoint deve ter as seguintes restrições:
- Deve receber, obrigatoriamente, o ID da compra como parâmetro de URI;
- Os detalhes da compra deve ser acessível através da URI: `api/v1/order/{id}`;
- Deve retornar os detalhes da compra em `application/json`.

#### RNF 1.1: Códigos HTTP
Os seguintes status devem ser retornados pelo servidor:
- `200 OK`, caso a compra tenha sido encontrada no banco de dados;
- `400 Bad Request`, caso o ID da compra não tenha sido especificado na URL;
- `404 Not Found`, caso a compra não tenha sido encontrada no banco de dados;
- `500 Internal Server Error`, caso tenha sido encontrado algum erro inesperado no servidor.

#### RNF 1.2: Corpo de resposta
O corpo de resposta deve conter a seguinte formatação:

```json
{
  "customerName": "PRIMEIRO NOME SOBRENOME",
  "document": "(222.222.222-00|22.222.222/2222-22)",
  "phoneNumber": "(00) 00000-0000",
  "status": "PENDING",
  "address": {
    "street": "R. Ali",
    "city": "Teste",
    "complement": "",
    "country": "",
    "state": ""
  },
  "items": [
    {
      "bookId": 1,
      "quantity": 2,
      "price": 20.00
    }
  ],
  "appliedCoupon": {
    "id": 10,
    "code": "CODE20",
    "discount": 20
  },
  "orderTotal": 40.00,
  "orderTotalWithDiscount": 32.00
}
```

Os valores decimais devem conter 2 casas decimais.

### RNF 2: DTOs
Os dados da compra devem ser trafegados por meio de _Data Transfer Objects_ (DTO). Dessa forma, não há riscos de expor informações sensíveis aos consumidores da API.



