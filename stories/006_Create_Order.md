## História 6: Finalização de compra
Criar um fluxo para permitir a efetivação de uma compra no sistema. 

## Requisitos Funcionais

### RF 1: Registro de um pedido
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
