package io.github.alexfx1.domain.repository;

import io.github.alexfx1.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClienteRepository {

    private static final String INSERT = "insert into cliente (nome) values (?) ";
    private static final String SELECT_ALL = "select * from cliente";
    private static final String UPDATE = "update cliete set nome = ? where id = ? ";
    private static final String DELETE = "delete from cliete where id = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Cliente salvar(Cliente cliente){
        entityManager.persist(cliente);
        return cliente;
    }

    public Cliente atualizar(Cliente cliente){
        jdbcTemplate.update(UPDATE,cliente.getNome(), cliente.getId());
        return cliente;
    }

    public void deletar(Integer id){
        jdbcTemplate.update(DELETE, id);
    }

    public List<Cliente> getAllClientes(){
        return jdbcTemplate.query(SELECT_ALL, obterClienteMapper());
    }

    private RowMapper<Cliente> obterClienteMapper() {
        return new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Cliente(rs.getString("nome"));
            }
        };
    }

    public List<Cliente> buscarPorNome(String nome){
        return jdbcTemplate.query(SELECT_ALL.concat(" where nome = ? "),
                new Object[]{"%" + nome + "%"}, obterClienteMapper());
    }
}
