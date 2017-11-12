package br.com.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.enumUtil.StatusEnum;
import br.com.model.Ferramenta;
import br.com.repository.FerramentaRepository;
import br.com.service.FerramentaService;
	
@Controller
public class FerramentaController {

	/** Bean gerenciado do Spring
	 * é necessário injetar com a anotação
	 * @Autowired
	 *  **/
	@Autowired
	private FerramentaRepository ferramenta;
	
	@Autowired
	private FerramentaService ferramentaService;
	
	@RequestMapping("/admin")
	public String index() {
		return "admin/index";
	}
	
	@RequestMapping("/admin/ferramenta/adicionar-ferramenta")
	public ModelAndView adicionar() {
		ModelAndView mv = new ModelAndView("admin/ferramenta/adicionar-ferramenta");
		mv.addObject(new Ferramenta());
		return mv;
	}
	
	@RequestMapping("/admin/ferramenta/lista-ferramenta")
	public ModelAndView lista(@RequestParam(defaultValue = "%")String nome) {
		//Iterable<Ferramenta> lista = ferramenta.findAll();
		Iterable<Ferramenta> lista = ferramenta.findByNomeContaining(nome);
		ModelAndView mv = new ModelAndView("admin/ferramenta/lista-ferramenta");
		mv.addObject("ferramentas", lista);
		return mv;
	}
	
	@RequestMapping(value = "/admin/ferramenta/salvar", method=RequestMethod.POST)
	public String salvar(@Validated Ferramenta f, Errors erros, RedirectAttributes redirectAttributes){
		if(erros.hasErrors()){
			return "admin/ferramenta/adicionar-ferramenta";
		}
		f.setDataCadastrada(new Date());
		f.setStatus(StatusEnum.DISPONIVEL);
		ferramentaService.salvar(f);
		redirectAttributes.addFlashAttribute("mensagem", "Ferramenta cadastrada com sucesso!");
		return "redirect:adicionar-ferramenta";
	}
	
	@RequestMapping("/admin/ferramenta/editar/{id}")
    public ModelAndView edit(@PathVariable("id") Ferramenta f) {
		
		ModelAndView mv = new ModelAndView("/admin/ferramenta/adicionar-ferramenta");
         mv.addObject("ferramenta", f);
        return mv;
    }
	
	@RequestMapping(value= "/admin/ferramenta/deletar/{id}",method=RequestMethod.DELETE)
	public String excluir(@PathVariable String id, RedirectAttributes redirectAttributes) {
		Long codigo = Long.parseLong(id);
		ferramentaService.excluir(codigo);
		redirectAttributes.addFlashAttribute("mensagem", "Ferramenta exlcuída com sucesso" );
		return"redirect:/admin/ferramenta/lista-ferramenta";
	}
	
}
