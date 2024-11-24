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