package com.demo.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

        private final com.demo.web.repository.MenuRepository menuRepository;

        public CatalogController(com.demo.web.repository.MenuRepository menuRepository) {
                this.menuRepository = menuRepository;
        }

        @GetMapping
        public String catalog(Model model) {
                model.addAttribute("menuItems", menuRepository.findByType("MENU"));
                model.addAttribute("decorItems", menuRepository.findByType("DECOR"));
                return "catalog";
        }
}
