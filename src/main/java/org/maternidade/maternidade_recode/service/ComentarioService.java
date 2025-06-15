package org.maternidade.maternidade_recode.service;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    public List<Comentario> findAll() {
        return comentarioRepository.findAll();
    }

    public Comentario findById(Long id) {
        return comentarioRepository.findById(id).orElseThrow();
    }

    public Comentario save(Comentario comment) {
        return comentarioRepository.save(comment);
    }

    public void delete(Long id) {
        comentarioRepository.deleteById(id);
    }
}
