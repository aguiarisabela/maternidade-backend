# 🤱🏻 Renascer Mulher | Projeto Social - Comunidade para Mulheres
___

- **Nº do Squad: 12**

- **Integrantes:**

1. Elzilane Cardoso Barreto
2. Hirislayne Batista Ramos dos Santos
3. Isabella Castro Silva de Aguiar
4. Fernanda Aline Ferreira Alves de Souza
5. Andiara dos Passos Sousa Rios
___
   
## Sobre o Projeto 
O projeto tem como objetivo desenvolver uma página web voltada para mulheres, 
proporcionando um espaço seguro para discussões, compartilhamento de experiências e suporte mútuo.

## Problema Identificado
Muitas mulheres encontram dificuldades em acessar um ambiente acolhedor para compartilhar desafios da gravidez, trocar 
experiências sobre a maternidade, encontrar dicas e buscar apoio. Além disso, a falta de espaços confiáveis para discutir 
temas como carreira, saúde e bem-estar dificulta a construção de redes de suporte.

## Solução Proposta:
Para atender a essa necessidade, criamos uma comunidade digital interativa, onde as usuárias podem:
- Criar e visualizar postagens sobre temas relevantes.
- Comentar e trocar experiências em cada post.
- Interagir por meio de curtidas, destacando os conteúdos mais relevantes.
- Filtrar publicações por mais curtidas e mais comentadas, facilitando o acesso às discussões mais populares.

Além disso, buscamos criar um espaço informativo com conteúdos sobre cuidados na gestação, dicas de maternidade e 
orientações práticas para o dia a dia com o bebê, ajudando as usuárias a se sentirem ouvidas, seguras e preparadas 
para os desafios dessa fase tão especial. ❤️
___

## Tecnologias Utilizadas
## 📦 Backend (`maternidade-backend`)

### **Principais Tecnologias Utilizadas**
- **Java**: Linguagem principal do backend, responsável pela lógica de negócios, regras de validação e interação com o banco de dados.
- **Spring Boot**: Framework que facilita a criação de APIs RESTful, gerenciamento de dependências, injeção de dependências e configuração automática.
- **Spring Data JPA**: Abstração para persistência e manipulação de dados no banco de dados relacional, permitindo o uso de repositórios e queries automáticas.
- **MySQL**: Banco de dados relacional utilizado para armazenar dados de usuários, postagens, comentários, curtidas e categorias.
- **Maven**: Gerenciador de dependências e automação de build.
- **WebMVC**: Usado para configuração de CORS e gerenciamento de rotas de arquivos estáticos (uploads).
- **MultipartFile**: Suporte a upload de arquivos (imagens) via endpoints REST.

### **Estrutura e Integração**
- **Modelos (Entities)**: Classes Java que representam tabelas do banco de dados, como `User`, `Post`, `Comentario` etc. Usam anotações (`@Entity`, `@Table`) para mapear os dados.
- **Repositórios**: Interfaces que estendem `JpaRepository`, permitindo operações CRUD automáticas para cada entidade.
- **Serviços**: Camada intermediária onde ficam as regras de negócio e métodos de manipulação de dados. Exemplo: `PostService`, `UserService`, `ComentarioService`.
- **Controllers**: Responsáveis por expor endpoints REST para consumo do frontend. Exemplo: `/posts`, `/users`, `/comentarios`.
- **Upload de Arquivos**: Gerenciado por `FileUploadService`, que recebe arquivos do frontend, salva em diretórios locais e retorna os caminhos relativos para uso nas respostas da API.
- **CORS**: Configurado para permitir requisições do frontend hospedado em outro domínio ou porta.

---

## 💻 Frontend (`maternidade-recode`)

### **Principais Tecnologias Utilizadas**
- **JavaScript (React)**: Biblioteca principal para construção da interface interativa. Utiliza componentes para renderização dinâmica e manipulação de estados.
- **HTML e CSS**: Estruturação e estilização das páginas, incluindo responsividade e layout amigável.
- **Bootstrap**: Framework CSS para componentes prontos e responsivos.
- **Axios**: Cliente HTTP para comunicação com o backend, responsável por enviar e receber dados via chamadas REST.
- **FormData**: Usado para manipulação de uploads de arquivos e envio de dados multipart/form-data.
- **React**: Estrutura a interface do projeto em componentes funcionais, gerencia estados com hooks e integra com a API RESTful usando Axios. Proporciona renderização reativa e melhora a experiência do usuário com navegação fluida e dinâmica.
- **Vercel**: Plataforma de deploy e hospedagem do frontend.

### **Estrutura e Integração**
- **Componentes React**: Exemplo: `ComunidadeContent.jsx` gerencia criação, edição, exclusão e listagem de postagens, além de upload de imagens.
- **Autenticação e Perfil**: O frontend armazena dados do usuário autenticado via `localStorage` e faz requisições para buscar foto de perfil e nome completo.
- **Comunicação com Backend**: Todas as operações (criar, editar, excluir posts, comentários, buscar perfil) são feitas via Axios para endpoints definidos pelo backend Spring Boot.
- **Upload de Arquivos**: O usuário pode anexar imagens às postagens, que são enviadas como multipart para o backend, armazenadas e retornadas com o caminho de acesso.

---

## Modelagem do Banco de Dados
A estrutura do banco de dados foi projetada para gerenciar a comunidade, permitindo postagens, comentários, curtidas, interações e categorização. A modelagem inclui as seguintes entidades:
- Usuário: Responsável por criar postagens e interagir na plataforma.
- Postagem: Representa um post criado por um usuário.
- Comentário: Permite que os usuários interajam com os posts.
- Curtida: Registro de interações positivas nos posts.
- Categoria: Permite classificar os posts por temas.

___

## 🔗 Integração entre Frontend e Backend

- O **frontend React** consome a **API RESTful** exposta pelo backend Spring Boot. Exemplos:
  - **Listagem de posts**: `GET http://localhost:8080/posts`
  - **Criação de posts com imagem**: `POST http://localhost:8080/posts` com multipart/form-data
  - **Busca de foto de perfil**: `GET http://localhost:8080/users/user-photo/{userId}`
  - **Comentários e curtidas**: Endpoints dedicados para cada ação, refletindo no banco de dados relacional.

- **Persistência**: Todas as interações realizadas no frontend (posts, comentários, likes, uploads) são refletidas no banco de dados MySQL através da API do backend.
- **Segurança e Consistência**: O backend controla as regras de negócio, validações e integrações com o banco, garantindo a integridade dos dados.
- **Arquitetura em Camadas**: Cada repositório tem responsabilidade clara e definida (frontend para interface e experiência do usuário, backend para lógica, regras e persistência).

---

**Relacionamentos:**

1️⃣ Usuário → Postagem
- Um usuário pode criar várias postagens ou nenhuma → (0,N)
- Cada postagem pertence a um único usuário → (1,1)
  
2️⃣ Usuário → Comentário
- Um usuário pode fazer vários comentários ou nenhum → (0,N)
- Cada comentário pertence a um único usuário → (1,1)

3️⃣ Usuário → Curtida
- Um usuário pode curtir várias postagens ou nenhuma → (0,N)
- Cada curtida pertence a um único usuário → (1,1)

4️⃣ Postagem → Comentário
- Uma postagem pode ter vários comentários ou nenhum → (0,N)
- Cada comentário pertence a uma única postagem → (1,1)

5️⃣ Postagem → Curtida
- Uma postagem pode receber várias curtidas ou nenhuma → (0,N)
- Cada curtida pertence a uma única postagem (só existe uma unica vez, por postagem) → (1,1)

6️⃣ Categoria → Postagem (Opcional)
- Uma categoria pode conter várias postagens ou nenhuma → (0,N)
- Cada postagem pode pertencer a uma única categoria → (1,1)

**Modelo Conceitual:**
![image](https://github.com/user-attachments/assets/75a6a188-8c02-4c89-adf2-dc16df4ba2e0)

**Modelo Lógico:**
![image](https://github.com/user-attachments/assets/d9900ced-ab45-4864-b196-7b78b792b9d1)

___

## Questionário

**1 - Considerando o desafio escolhido, qual é o problema a ser resolvido e que será contemplado com o projeto final?**  

*A falta de informação e apoio para mulheres que enfrentam os desafios da maternidade,
especialmente durante a gravidez e o puerpério. Muitas mães não têm acesso fácil a
recursos confiáveis e comunidades de suporte que possam ajudá-las a lidar com as
mudanças físicas, emocionais e práticas que acompanham a maternidade. Sendo assim, 
buscou-se criar um espaço digital seguro e acessível para que mulheres possam compartilhar 
experiências e obter apoio mútuo.*

**2 - Qual o público-alvo? A solução poderá ser aplicada a todos, sem restrição de idade ou grau de escolaridade, por exemplo?**  

*O público-alvo são mulheres grávidas e mães de recém-nascidos, independentemente da
idade ou grau de escolaridade, principalmente aquelas que buscam informações e apoio durante a maternidade. 
Além disso, pais e outros cuidadores também podem se beneficiar do conteúdo, tornando-o inclusivo e acessível 
para todos que desempenham um papel ativo na criação de um bebê.*

**3 - O problema foi escolhido com base em quais dados oficiais? Como vocês identificaram que esse realmente é um problema para o público-alvo? Indique as referências usadas, justificando a sua escolha.**

*O problema foi identificado com base em dados de instituições de saúde e pesquisas
acadêmicas que mostram a importância do apoio durante a gravidez. Por exemplo,
estudos indicam que o acesso a informações e suporte emocional pode reduzir o estresse
e melhorar a saúde mental das mães. Referências usadas podem incluir organizações
como a Organização Mundial da Saúde (OMS) e estudos publicados em revistas
científicas sobre saúde materna.*

*(Apoio familiar) https://auhebaby.com.br/relacionamentos-na-gravidez/importanciaapoio-familiar-gravidez/*

*(As dificuldades da maternidade e o apoio familiar sob o olhar da mãe adolescente)
https://pesquisa.bvsalud.org/portal/resource/pt/lil-735636*


**4 - Como esse problema afeta o público-alvo?**

*A falta de informação e apoio pode levar ao aumento do estresse, ansiedade e depressão 
entre as mães, impactando negativamente sua saúde e bem-estar. Além disso, a falta de 
conhecimento sobre cuidados com a saúde e o bebê pode resultar em dificuldades para 
lidar com as necessidades do recém-nascido, afetando a qualidade de vida da família 
como um todo.*

____

#### Link de Acesso ao Site: *https://renascer-mulher.vercel.app/*

___

## Observações Finais

- O projeto pode ser facilmente escalado, pois a separação frontend/backend permite trocar tecnologias de cada lado sem afetar o outro.
- O modelo de integração via API REST permite integração futura com aplicativos mobile ou outros sistemas.
- Upload de imagens e manipulação de arquivos já está preparado para produção, otimizando a experiência do usuário.
