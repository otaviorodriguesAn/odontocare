package senai499.com.br.odontocare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteAreaController {

    // Página principal do cliente
    @GetMapping
    public String index() {
        return "cliente/index";
    }

    // Página de agendamento
    @GetMapping("/agendamento")
    public String agendamento() {
        return "cliente/agendamento";
    }

    // Meus agendamentos
    @GetMapping("/meus-agendamentos")
    public String meusAgendamentos() {
        return "cliente/meus-agendamentos";
    }

    // Perfil do cliente
    @GetMapping("/perfil")
    public String perfil() {
        return "cliente/perfil";
    }
}