package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.Alpr;
import br.gov.sc.ciasc.ragdevconverge.service.AlprService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alpr")
public class AlprController {

    private final AlprService alprService;

    public AlprController(AlprService alprService) {
        this.alprService = alprService;
    }

    @GetMapping()
    public List<Alpr> buscaPlacas(@RequestParam String userQuery) {
        return alprService.search(userQuery, 10);
    }
}
