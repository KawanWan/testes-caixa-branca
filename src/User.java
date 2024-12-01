import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Classe User - Gerencia conexões com o banco de dados e verifica credenciais de usuário.
 *
 * Esta classe possui métodos para conectar a um banco de dados MySQL e verificar se um usuário existe
 * com base no login e senha fornecidos. O resultado da verificação e o nome do usuário são armazenados
 * em variáveis de instância.
 */
public class User {

    /**
     * Construtor padrão.
     * Inicializa uma nova instância da classe User.
     */
    public User() {
        // Construtor vazio
    }

    /**
     * Nome do usuário encontrado no banco de dados.
     */
    public String nome = "";

    /**
     * Resultado da verificação do usuário. True se encontrado, false caso contrário.
     */
    public boolean result = false;

    /**
     * Método para conectar ao banco de dados.
     *
     * Este método estabelece uma conexão com o banco de dados usando o Driver MySQL.
     * @return Objeto Connection representando a conexão estabelecida com o banco de dados.
     */
    public Connection conectarBD() {
        Connection conn = null;
        try {
            // Carrega o driver JDBC do MySQL
            Class.forName("com.mysql.Driver.Manager").newInstance();

            // String de conexão com o banco de dados
            String url = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";

            // Estabelece a conexão com o banco
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            // Trata exceções silenciosamente
        }
        return conn;
    }

    /**
     * Método para verificar se um usuário existe no banco de dados.
     *
     * Este método verifica se o login e a senha fornecidos correspondem a um usuário no banco de dados.
     * Caso o usuário seja encontrado, o nome é armazenado na variável "nome" e o resultado é atualizado
     * para "true".
     *
     * @param login Login do usuário a ser verificado.
     * @param senha Senha do usuário a ser verificado.
     * @return True se o usuário for encontrado, false caso contrário.
     */
    public boolean verificarUsuario(String login, String senha) {
        // Inicializa a variável para armazenar a instrução SQL
        String sql = "";

        // Conecta ao banco de dados
        Connection conn = conectarBD();

        // Monta a instrução SQL para buscar o usuário
        sql += "select nome from usuarios ";
        sql += "where login = '" + login + "'";
        sql += " and senha = '" + senha + "';";

        try {
            // Cria o objeto Statement para executar a query
            Statement st = conn.createStatement();

            // Executa a query SQL
            ResultSet rs = st.executeQuery(sql);

            // Verifica se há resultados na query
            if (rs.next()) {
                // Se o usuário for encontrado, atualiza o resultado e o nome
                result = true;
                nome = rs.getString("nome");
            }
        } catch (Exception e) {
            // Trata exceções silenciosamente
        }

        // Retorna o resultado da verificação
        return result;
    }
}
// fim da classe
