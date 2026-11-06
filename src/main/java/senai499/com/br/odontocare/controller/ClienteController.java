package senai499.com.br.odontocare.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import senai499.com.br.odontocare.dto.ClienteCadastroDTO;
import senai499.com.br.odontocare.service.ClienteService;

@Controller
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Exibe o formulário
    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {

        model.addAttribute("clienteCadastro", new ClienteCadastroDTO());

        return "cadastro";
    }

    // Recebe os dados do formulário
    @PostMapping("/cadastro")
    public String cadastrarCliente(
            @Valid @ModelAttribute("clienteCadastro")
            ClienteCadastroDTO dto,
            BindingResult resultado,
            Model model) {

        // Verifica os erros de validação
        if (resultado.hasErrors()) {
            return "cadastro";
        }

        try {

            clienteService.cadastrar(dto);

            return "redirect:/login?cadastro=sucesso";

        } catch (IllegalArgumentException erro) {

            resultado.reject("erro.cadastro", erro.getMessage());

            return "cadastro";
        }
    }
}