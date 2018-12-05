package br.com.cursoapirest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursoapirest.dtos.EmpresaDto;
import br.com.cursoapirest.dtos.LancamentoDto;
import br.com.cursoapirest.entities.Empresa;
import br.com.cursoapirest.entities.Lancamento;
import br.com.cursoapirest.response.Response;
import br.com.cursoapirest.services.EmpresaService;


@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

	@Autowired
	private EmpresaService empresaService;

	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	public EmpresaController() {
	}

	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<EmpresaDto>>
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

		if (!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterEmpresaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}
	
	/*
	 * Busca todas as empresas cadastradas
	 * 
	 */
	@GetMapping(value = "/todos")
	@Description("Retorna todas as empresas cadastradas com paginação e ordenação/nEx. Paginação: pag=1/nEx. ordenação ord=cnpj/n Ex. dir=DESC")
	public ResponseEntity<Response<Page<EmpresaDto>>> buscarTodos(
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		
		log.info("Buscando todas as empresas cadastradas");
		
		Response<Page<EmpresaDto>> response = new Response<Page<EmpresaDto>>();
		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		
		Page<Empresa> empresas = this.empresaService.buscarTodos(pageRequest);
		Page<EmpresaDto> empresaDto = empresas.map(empresa -> this.converterEmpresaDto(empresa));


		if (empresas==null || empresas.getSize()<0) {
			log.info("Nenhuma empresa encontrada");
			response.getErrors().add("Nenhuma empresa encontrada.");
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(empresaDto);
		return ResponseEntity.ok(response);
	}

	

	/**
	 * Popula um DTO com os dados de uma empresa.
	 * 
	 * @param empresa
	 * @return EmpresaDto
	 */
	private EmpresaDto converterEmpresaDto(Empresa empresa) {
		EmpresaDto empresaDto = new EmpresaDto();
		empresaDto.setId(empresa.getId());
		empresaDto.setCnpj(empresa.getCnpj());
		empresaDto.setRazaoSocial(empresa.getRazaoSocial());

		return empresaDto;
	}
	
	
	/**
	 * Popula uma lista de DTO´s com todas as empresas cadastradas no banco de dados
	 * 
	 * @param empresa
	 * @return EmpresaDto
	 */
	private List<EmpresaDto> converterListEmpresaDto(List<Empresa> empresas) {
		
		List<EmpresaDto> empresaDtoList = new ArrayList<EmpresaDto>();
		
		for (Empresa empresa : empresas) {
			EmpresaDto empresaDto = new EmpresaDto();
			empresaDto.setId(empresa.getId());
			empresaDto.setCnpj(empresa.getCnpj());
			empresaDto.setRazaoSocial(empresa.getRazaoSocial());
			empresaDtoList.add(empresaDto);
		}
		
		return empresaDtoList;
	}

}
