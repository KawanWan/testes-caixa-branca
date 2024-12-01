ERROS IDENTIFICADOS NO CÓDIGO

1. Driver JDBC Obsoleto
Descrição do Problema: O código utiliza Class.forName("com.mysql.Driver.Manager").newInstance();, que está desatualizado e não compatível com versões recentes do MySQL Connector/J.
Correção: Substituir por Class.forName("com.mysql.cj.jdbc.Driver");, que é a forma recomendada para conectar ao MySQL nas versões atuais.

2. URL de Conexão Malformada
Descrição do Problema: A URL de conexão não especifica parâmetros essenciais, como o uso ou não de SSL e a porta do MySQL.
Correção: A URL deve seguir o padrão correto:
    jdbc:mysql://127.0.0.1:3306/test?user=lopes&password=123&useSSL=false

3. Falta de Fechamento de Recursos
Descrição do Problema: Recursos como Connection, Statement e ResultSet não são fechados, podendo causar vazamento de conexões e consumo excessivo de memória.
Correção: Usar try-with-resources para garantir que os recursos sejam fechados automaticamente.

4. Vulnerabilidade a Ataques de Injeção SQL
Descrição do Problema: O código concatena diretamente os valores do login e da senha na instrução SQL, tornando-o vulnerável a SQL Injection.
Exemplo de Exploração: Um atacante pode injetar código malicioso:
    login: ' OR '1'='1
    senha: qualquer_coisa
Isso permite acessar o sistema sem uma senha válida.
Correção: Usar Prepared Statements para parametrizar a consulta:
    String sql = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, login);
    ps.setString(2, senha);

5. Tratamento de Exceções Incompleto
Descrição do Problema: O código captura exceções com catch (Exception e) { }, mas não registra ou exibe os erros, dificultando a depuração.
Correção: Incluir o registro ou exibição das exceções:
    catch (Exception e) {
        e.printStackTrace();
    }

6. Falta de Validação de Parâmetros
Descrição do Problema: Os valores de login e senha são utilizados diretamente na consulta SQL sem validação prévia.
Correção: Validar os parâmetros antes de usá-los:
    if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
        throw new IllegalArgumentException("Login ou senha inválidos!");
    }

ETAPA 3

Início (1):

Início do método verificarUsuario na classe User.
Conexão com o banco (2):

Chamada ao método conectarBD() na linha Connection conn = conectarBD();.
Montagem da Query SQL (3):

Construção da string SQL no bloco:
java
Copiar código
sql += "select nome from usuarios ";
sql += "where login = " + "'" + login + "'";
sql += " and senha = " + "'" + senha + "';";
Execução do Try (4):

Início do bloco try no método verificarUsuario.
4.1 Criação do Statement (4.1): - Criação do objeto Statement com Statement st = conn.createStatement();.

4.2 Execução da Query (4.2): - Execução da query SQL com ResultSet rs = st.executeQuery(sql);.

4.3 Verificação do ResultSet (4.3): - Condição if (rs.next()) para verificar se há um resultado.

markdown
Copiar código
4.3.1 **Usuário Encontrado (4.3.1)**:
    - Bloco onde `result = true;` e `nome = rs.getString("nome");`.

    4.3.1.1 **Atualizar Variáveis (4.3.1.1)**:
        - Atribuição do nome encontrado à variável `nome`.

4.3.2 **Usuário Não Encontrado (4.3.2)**:
    - Bloco implícito quando o `if` não é verdadeiro (não há um `else` explícito, mas o retorno será `false`).
Bloco Catch (Captura Exceções) (5):

Captura de exceções com o catch (Exception e).
Retorno do Resultado (6):

Retorno do valor da variável result com return result;.

CÁLCULO DA COMPLEXIDADE CICLOMÁTICA

Nós (N):

1. Início
2. Conexão com o banco
3. Montagem da query SQL
4. Execução do bloco try
5. Criação do Statement
6. Execução da query
7. Verificação do ResultSet
8. Usuário encontrado
9. Atualizar variáveis
10. Usuário não encontrado
11. Bloco catch
12. Retorno do resultado
Total: N=12

Arestas (E):

De 1 para 2
De 2 para 3
De 3 para 4
De 4 para 5
De 5 para 6
De 6 para 7
De 7 para 8 (se o if for verdadeiro)
De 7 para 10 (se o if for falso)
De 8 para 9
De 9 para 6 (retorna ao fluxo principal)
De 4 para 11 (em caso de exceção)
De 11 para 6
De 6 para 12
Total: E=13

Componentes conectados (P): 
P=1 (uma única função sem subcomponentes independentes)

M = 13 − 12 + 2(1)

M = 3

A complexidade ciclomática do código é 3.

Um valor de complexidade ciclomática de 3 indica que existem 3 caminhos independentes no grafo, ou seja, 3 cenários de execução possíveis:
Fluxo principal onde o usuário é encontrado (if (rs.next()) verdadeiro).
Fluxo principal onde o usuário não é encontrado (if (rs.next()) falso).
Fluxo de erro (execução do bloco catch).