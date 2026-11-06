package senai499.com.br.odontocare.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import senai499.com.br.odontocare.dto.ClienteCadastroDTO;
import senai499.com.br.odontocare.model.Cliente;
import senai499.com.br.odontocare.model.Usuario;
import senai499.com.br.odontocare.model.PerfilUsuario;
import senai499.com.br.odontocare.repository.ClienteRepository;
import senai499.com.br.odontocare.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void cadastrar(ClienteCadastroDTO dto) {

        String email = dto.getEmail().trim().toLowerCase();

        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        // Cria o usuário
        Usuario usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(PerfilUsuario.CLIENTE);

        usuarioRepository.save(usuario);

        // Cria o cliente associado ao usuário
        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome().trim());
        cliente.setUsuario(usuario);

        clienteRepository.save(cliente);
    }
}