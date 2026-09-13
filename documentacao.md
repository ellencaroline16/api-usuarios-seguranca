# O Auth 2.0:

    Na API que eu desenvolvi, a autenticação é feita na direto: o usuário manda email e senha pro próprio sistema e recebe um token JWT. Isso funciona bem quando é o próprio dono da conta que está usando. Mas e se uma outra aplicação (de outra empresa, por exemplo) precisasse acessar os dados dos usuários sem que eles tivessem que dar a senha pra essa aplicação terceira? Nesse caso, entraria o OAuth 2.0.

    A ideia central do OAuth é que o usuário nunca entrega a senha pra aplicação parceira. Quem recebe a senha continua sendo só o meu sistema. O que acontece é o seguinte: a aplicação parceira redireciona o usuário pra uma tela de login do meu sistema (não da aplicação parceira dela). O usuário faz login normal ali, e depois aparece uma tela perguntando se ele autoriza aquela aplicação a acessar os dados dele. Se ele aceitar, meu sistema gera um código e manda de volta pra aplicação parceira.

    Com esse código, a aplicação parceira troca ele por um token de acesso, comunicando direto com o meu servidor (sem passar pelo navegador do usuário de novo). Esse token é o que ela vai usar dali pra frente pra acessar os endpoints protegidos da API — bem parecido com o JWT que eu já uso, só que esse token representa "a aplicação X, que foi autorizada pelo usuário Y", e não o próprio usuário logando.

    As vantagens : a senha do usuário nunca sai do meu sistema, dá pra revogar o acesso de uma aplicação parceira a qualquer momento sem precisar mexer na senha de ninguém, e dá pra limitar o que cada aplicação pode fazer (por exemplo, só ler dados, sem poder excluir usuário).

    No projeto eu não cheguei a implementar isso, pois ia bem além do escopo da atividade em questão — mas se um dia a API precisasse ser usada por sistemas de fora, esse seria o próximo passo depois do JWT.


# Análise de Segurança:

    Pensando nos riscos do projeto, o primeiro que me veio à cabeça foi o de guardar senha em texto puro no banco. Se alguém tivesse acesso ao banco de dados (ou se ele vazasse), todo mundo ficaria exposto de uma vez. Por isso usei bcrypt pra gerar o hash da senha antes de salvar — cheguei a conferir isso direto no H2 Console, e a senha aparece como um hash gigante começando com $2a$10$, então nem eu, olhando o banco, consigo saber qual é a senha original.

    Outro risco é alguém roubar o token JWT (por exemplo, interceptando a comunicação) e usar ele pra se passar pelo usuário. Como mitigação, configurei o token pra expirar em 1 hora, então mesmo que alguém pegue o token, ele só serve por um tempo limitado. O ideal em produção seria também usar HTTPS, pra dificultar que o token seja capturado no meio do caminho — isso eu não implementei porque estou rodando local, mas é importante deixar registrado.

    Também pensei no risco de alguém sem permissão acessar endpoints que não deveria, tipo um usuário comum conseguindo ver dados de todo mundo. Pra isso usei o RBAC com @PreAuthorize, restringindo cada endpoint pelo perfil do usuário — testei isso tanto pelo Postman quanto pela interface, criando um usuário Admin e um Cliente e comparando o que cada um conseguia fazer.

    Um risco que eu não tinha pensado de cara, mas percebi enquanto criava o endpoint de atualização, foi o de um usuário conseguir trocar o próprio perfil sozinho (por exemplo, um Cliente virando Admin só editando o próprio cadastro). Pra evitar isso, o campo de perfil nem aparece no DTO de atualização — só dá pra mudar nome e email por ali.

    Por fim, também bloqueei cadastro de email duplicado, checando antes de salvar se já existe alguém com aquele email, tanto no cadastro quanto na edição.

    Uma limitação que vale mencionar: como usei o banco H2 em memória, todo dado se perde quando o servidor reinicia. Isso não chega a ser uma falha de segurança do sistema em si, mas é algo que precisaria mudar (pra um banco de verdade, tipo PostgreSQL) se fosse pra produção.