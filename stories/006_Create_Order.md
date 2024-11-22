## História 6: Finalização de compra
Criar um fluxo para permitir a efetivação de uma compra no sistema. 

## Requisitos Funcionais

### RF 1: Registro de um pedido
#### Restrições
Ao finalizar um pedido, o usuário deve informar os seguintes campos:
- **Email:**
  - Campo de texto obrigatório;
  - Deve ser um email válido, obedecendo a especificação;
- **Nome:**
  - Campo de texto obrigatório;
  - Não deve exceder 120 caracteres;
- **Sobrenome:**
  - Campo de texto obrigatório;
  - Não deve exceder 120 caracteres;
- **Documento:**
  - Campo de texto obrigatório;
  - Deve conter apenas caracteres numéricos;
  - Não deve exceder 14 caracteres;
  - Deve ser formatado ao usuário;
    - Se for CPF, deve seguir o formato: `xxx.xxx.xxx-xx`
    - Se for CNPJ, deve seguir o formato: `xx.xxx.xxx/xxxx-xx`
- **Endereço:**
  - Campo de texto obrigatório;
  - Não deve exceder 200 caracteres;
- **Complemento:**
  - Campo de texto obrigatório;
  - Não deve exceder 200 caracteres;
- **Cidade**:
  - Campo de texto obrigatório;
  - Não deve exceder 150 caracteres;
- **País**
  - Campo de seleção única obrigatório;
- **Estado:**
  - Campo de seleção única obrigatório;
  - A seleção deve ser habilitada se o país selecionado tiver ao menos um Estado;
  - Deve ser obrigatório se e somente se o país de origem tiver ao menos um Estado.
- **CEP:**
  - Campo de texto obrigatório;
  - Não deve exceder 20 caracteres;
- **Telefone:**
  - Campo de texto obrigatório;
  - Não deve ultrapassar 9 caracteres;
  - Ao ser exibido ao usuário, deve seguir o formato: `xxxxx-xxxx`.
#### Resultado esperado
Caso alguma pré-condição seja ferida, o sistema deve informar ao usuário sobre o ocorrido.

### RF 2: Registro de pedidos
#### Restrições
Junto aos dados da compra, o cliente deve informar quais são os produtos que ele deseja comprar. A fim da finalizar uma compra, o usuário deve enviar as seguintes informações:
- Livro selecionado:
  - O livro deve estar registrado no sistema;
  - O livro já deve estar publicado no sistema.
- Quantidade:
  - A quantidade deve ser maior do que 0;
  - A quantidade não deve ultrapassar 20.
- Total:
  - O total deve ser igual a soma da relação entre livros e quantidades.
  - 
#### Resultado esperado
A compra deve ser registrada com o status **REALIZADA**.

Caso alguma dessas regras sejam violadas, o sistema deve informar o usuário sobre o ocorrido.
### RF 3: Aplicação de cupom
Ao finalizar uma compra, o usuário pode aplicar um cupom de desconto. Uma vez aplicado, o desconto será aplicado sobre o valor final da compra.
#### Restrições
- O código do cupom deve existir;
- O cupom não deve estar expirado;
- O usuário pode aplicar apenas **um** cupom a sua compra.

#### Resultado esperado
O cupom deve ser atrelado a compra e o desconto dele deve ser aplicado ao valor final da compra.

Caso alguma restrição seja ferida, o usuário deve ser informado sobre o erro ocorrido.

## Requisitos Não Funcionais

### RNF 1: Envio de requisições
Todas as requisições devem ser enviadas, por meio do método `POST`, em `application/json`.

### RNF 2: Validações
Caso alguma regra de negócio seja ferida, o servidor deve informar, por meio de um JSON, os erros ocorridos sob o status `400 Bad Request`.

### RNF 3: Sucesso ao finalizar a compra
Caso a requisição de compra seja válida, o servidor deve retornar o status `201 Created` com o seguinte payload, em `application/json`, de resposta:
```json
{
  "address": {
    "address": "",
    "complement": "",
    "city": "",
    "zipCode": "",
    "country": "",
    "state": ""
  }
}
```
Além disso, o servidor deve enviar no header `Location` a URI para verificar o status de uma compra.

### RNF 4: Persistência
Os dados de uma compra devem ser persistidos em um banco de dados relacional.

### RNF 5: Comparação de datas
As datas devem ser comparadas utilizando a timezone (fuso horário) `UTC`.