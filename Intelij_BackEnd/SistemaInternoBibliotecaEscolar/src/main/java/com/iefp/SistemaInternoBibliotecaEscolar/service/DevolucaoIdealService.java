package com.iefp.SistemaInternoBibliotecaEscolar.service;

import com.iefp.SistemaInternoBibliotecaEscolar.model.DevolucaoIdeal;
import com.iefp.SistemaInternoBibliotecaEscolar.repository.DevolucaoIdealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevolucaoIdealService {
    private final DevolucaoIdealRepository devolucaoIdealRepository;

    public DevolucaoIdealService(DevolucaoIdealRepository devolucaoIdealRepository) {
        this.devolucaoIdealRepository = devolucaoIdealRepository;
    }

    public List<DevolucaoIdeal> listarTodasDevolucoesIdeais() {
        return devolucaoIdealRepository.findAll();
    }

    public Optional<DevolucaoIdeal> buscarPorId(Integer id) {
        return devolucaoIdealRepository.findById(id);
    }

    public Optional<DevolucaoIdeal> buscarPorEmprestimo(Integer idEmprestimo) {
        return devolucaoIdealRepository.findAll().stream().filter(d -> d.getEmprestimo().getIdEmprestimo().equals(idEmprestimo)).findFirst();

    }
}
