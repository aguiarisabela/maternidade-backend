
package org.maternidade.maternidade_recode.service;

import java.util.List;

import org.maternidade.maternidade_recode.exception.ResourceNotFoundException;
import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.repository.ComentarioRepository;
import org.springframework.stereotype.Service;

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
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));
    }

    public List<Comentario> findByPostId(Long postId) {
        return comentarioRepository.findByPostId(postId);
    }

    public Comentario save(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    public void delete(Long id) {
        comentarioRepository.deleteById(id);
    }
}